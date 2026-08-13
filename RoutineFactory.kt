package com.ctrl.life

import java.time.*
import java.time.format.DateTimeFormatter

object RoutineFactory{
 fun ensureToday(existing:List<CtrlTask>,store:CtrlStore,schoolDay:Boolean):List<CtrlTask>{
  val day=LocalDate.now();val d=day.toString();val out=existing.toMutableList()
  fun has(k:String)=out.any{it.date==d&&it.recurrenceKey==k&&it.status!=Status.CANCELLED}
  fun add(k:String,title:String,area:Area,min:Int,start:String,clean:Boolean=false){
   if(!has(k))out+=CtrlTask(title=title,area=area,priority=Priority.MUST,date=d,start=start,minutes=store.learned(k,min),mandatoryToday=true,cleaning=clean,snitchEligible=clean,recurrenceKey=k,locationKind=LocationKind.HOME)
  }
  add("washing_on","Put on washing",Area.HOME,5,if(schoolDay)"07:10" else "08:45",true)
  add("dishwasher_am","Dishwasher",Area.HOME,5,if(schoolDay)"09:00" else "09:05",true)
  add("wipe_sides_am","Wipe sides",Area.HOME,5,if(schoolDay)"09:10" else "09:15",true)
  add("tidy_front","Tidy front room",Area.HOME,10,"10:30",true)
  add("hoover_sofa","Hoover sofa",Area.HOME,5,"10:45",true)
  val epoch=day.toEpochDay()
  if(epoch%3L==0L)add("whole_hoover","Whole house hoover",Area.HOME,25,"11:00",true) else add("hoover_down","Hoover downstairs",Area.HOME,10,"11:00",true)
  if(epoch%3L==0L)add("stove","Clean stove top",Area.HOME,7,"18:30",true)
  if(day.dayOfWeek==DayOfWeek.SATURDAY){add("windowsills","Clean windowsills",Area.HOME,15,"11:30",true);add("under_appliances","Clean under appliances",Area.HOME,15,"11:50",true)}
  add("dishwasher_pm","Dishwasher",Area.HOME,5,"18:20",true);add("wipe_sides_pm","Wipe sides",Area.HOME,5,"18:25",true)
  add("morning_care","Shower + teeth + skincare",Area.SELF_CARE,13,if(schoolDay)"06:35" else "08:00")
  add("breakfast","Breakfast",Area.SELF_CARE,15,if(schoolDay)"07:25" else "08:30")
  add("lunch","Lunch",Area.SELF_CARE,20,"12:30");add("cook_dinner","Cook dinner",Area.FAMILY,30,"17:00");add("dinner","Dinner",Area.FAMILY,45,"17:30")
  add("evening_care","Evening teeth + skincare",Area.SELF_CARE,7,"21:45")
  if(epoch%3L==0L)add("shave","Shave",Area.SELF_CARE,7,"21:35")
  if(day.dayOfWeek==DayOfWeek.SUNDAY)add("bath","Bath",Area.SELF_CARE,75,"20:15")
  return out
 }

 fun unlockDependencies(tasks:List<CtrlTask>):List<CtrlTask>{
  val out=tasks.toMutableList();tasks.filter{it.recurrenceKey=="washing_on"&&it.status==Status.COMPLETED}.forEach{w->
   val key="hang_${w.date}";if(out.none{it.recurrenceKey==key}){
    val ms=(w.completedAt?:System.currentTimeMillis())+50*60_000L;val dt=Instant.ofEpochMilli(ms).atZone(ZoneId.systemDefault()).toLocalDateTime()
    out+=CtrlTask(title="Hang washing out",area=Area.HOME,priority=Priority.MUST,date=w.date,start=dt.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),minutes=10,mandatoryToday=true,cleaning=true,snitchEligible=true,recurrenceKey=key,locationKind=LocationKind.HOME,notBefore=dt.toString())
   }
  };return out
 }
}
