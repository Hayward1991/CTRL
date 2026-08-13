package com.ctrl.life

import java.time.*
import java.time.format.DateTimeFormatter

object Planner{
 private fun blocked(tasks:List<CtrlTask>,calendar:List<CalendarBlock>,date:LocalDate):MutableList<Pair<LocalDateTime,LocalDateTime>>{
  val b=calendar.filter{it.startZdt().toLocalDate()==date||it.endZdt().toLocalDate()==date}.map{it.startZdt().toLocalDateTime() to it.endZdt().toLocalDateTime()}.toMutableList()
  tasks.filter{it.date==date.toString()&&(it.fixed||it.protected)}.forEach{b+=it.localStart() to it.end()};b.sortBy{it.first};return b
 }
 fun planDay(tasks:List<CtrlTask>,calendar:List<CalendarBlock>,date:LocalDate=LocalDate.now()):PlanResult{
  val b=blocked(tasks,calendar,date);val out=tasks.toMutableList();var cursor=date.atTime(6,0);val end=date.atTime(23,0);var over=0
  fun hit(s:LocalDateTime,e:LocalDateTime)=b.firstOrNull{s<it.second&&e>it.first}
  fun slot(from:LocalDateTime,min:Int):LocalDateTime?{var s=from;while(s.plusMinutes(min.toLong())<=end){val h=hit(s,s.plusMinutes(min.toLong()));if(h==null)return s;s=h.second.plusMinutes(CtrlRules.BUFFER_MIN.toLong())};return null}
  val movable=tasks.filter{it.date==date.toString()&&!it.isDone()&&!it.fixed&&!it.protected}.sortedWith(compareByDescending<CtrlTask>{PriorityOrder.rank(it)}.thenBy{it.deadline?:"9999"})
  movable.forEach{t->
   val desired=maxOf(cursor,t.localStart());val s=slot(desired,t.minutes)
   if(s==null){if(t.priority!=Priority.SOMEDAY)over+=t.minutes}else{
    val i=out.indexOfFirst{it.id==t.id};if(i>=0)out[i]=t.copy(start=s.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),status=if(t.status==Status.QUEUED)Status.SCHEDULED else t.status)
    cursor=s.plusMinutes(t.minutes.toLong()+CtrlRules.BUFFER_MIN)
   }
  }
  return PlanResult(out,over)
 }
}
