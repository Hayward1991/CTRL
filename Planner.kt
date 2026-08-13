package com.ctrl.life

import java.time.*
import java.time.format.DateTimeFormatter

object Planner{
 private val anchored=setOf("morning_care","breakfast","lunch","cook_dinner","dinner","evening_care","shave","bath")
 private fun isProtected(t:CtrlTask)=t.fixed||t.protected||t.area==Area.FAMILY||t.recurrenceKey in anchored
 private fun anchor(t:CtrlTask):CtrlTask=when(t.recurrenceKey){
  "morning_care"->t.copy(start=if(t.start<"10:00")t.start else "08:00",protected=true)
  "breakfast"->t.copy(start=if(t.start<"10:00")t.start else "08:30",protected=true)
  "lunch"->t.copy(start="12:30",protected=true)
  "cook_dinner"->t.copy(start="17:00",protected=true,area=Area.FAMILY)
  "dinner"->t.copy(start="17:30",protected=true,area=Area.FAMILY)
  "evening_care"->t.copy(start="21:45",protected=true)
  "shave"->t.copy(start="21:35",protected=true)
  "bath"->t.copy(start="20:15",protected=true)
  else->t
 }
 private fun blocked(tasks:List<CtrlTask>,calendar:List<CalendarBlock>,date:LocalDate):MutableList<Pair<LocalDateTime,LocalDateTime>>{
  val b=calendar.filter{it.startZdt().toLocalDate()==date||it.endZdt().toLocalDate()==date}.map{it.startZdt().toLocalDateTime() to it.endZdt().toLocalDateTime()}.toMutableList()
  tasks.filter{it.date==date.toString()&&isProtected(it)&&!it.isDone()}.forEach{b+=it.localStart() to it.end()}
  b.sortBy{it.first};return b
 }
 fun planDay(tasks:List<CtrlTask>,calendar:List<CalendarBlock>,date:LocalDate=LocalDate.now()):PlanResult{
  val normalized=tasks.map(::anchor);val b=blocked(normalized,calendar,date);val out=normalized.toMutableList();var cursor=date.atTime(6,0);val bedtime=date.atTime(22,30);var over=0
  fun hit(s:LocalDateTime,e:LocalDateTime)=b.firstOrNull{s<it.second&&e>it.first}
  fun slot(from:LocalDateTime,min:Int,latest:LocalDateTime):LocalDateTime?{var s=from;while(s.plusMinutes(min.toLong())<=latest){val h=hit(s,s.plusMinutes(min.toLong()));if(h==null)return s;s=h.second.plusMinutes(CtrlRules.BUFFER_MIN.toLong())};return null}
  val movable=normalized.filter{it.date==date.toString()&&!it.isDone()&&!isProtected(it)}.sortedWith(compareByDescending<CtrlTask>{PriorityOrder.rank(it)}.thenBy{it.deadline?:"9999"})
  movable.forEach{t->
   val desired=maxOf(cursor,t.localStart());val latest=if(t.area==Area.WORK)date.atTime(17,30) else bedtime;val s=slot(desired,t.minutes,latest)
   if(s==null){val i=out.indexOfFirst{it.id==t.id};if(i>=0&&t.status==Status.DELAYED)out[i]=t.copy(date=date.plusDays(1).toString(),start="06:00",status=Status.QUEUED) else if(t.priority!=Priority.SOMEDAY)over+=t.minutes}
   else{val i=out.indexOfFirst{it.id==t.id};if(i>=0)out[i]=t.copy(start=s.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),status=if(t.status==Status.QUEUED||t.status==Status.DELAYED)Status.SCHEDULED else t.status);cursor=s.plusMinutes(t.minutes.toLong()+CtrlRules.BUFFER_MIN)}
  }
  return PlanResult(out,over)
 }
}
