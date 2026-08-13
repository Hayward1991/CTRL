package com.ctrl.life

import java.time.*
import java.util.UUID

enum class Tab { TODAY, PLAN, LIFE }
enum class Area { WORK, FAMILY, HOME, HEALTH, SELF_CARE, PERSONAL, LIFE_ADMIN }
enum class Priority { MUST, TARGET, SOMEDAY }
enum class Status { QUEUED, SCHEDULED, ACTIVE, PAUSED, COMPLETED, SKIPPED, CANCELLED, DELAYED }
enum class LocationKind { HOME, WORK, ANYWHERE, SPECIFIC }

data class CtrlTask(
 val id:String=UUID.randomUUID().toString(), val title:String, val notes:String="",
 val area:Area=Area.PERSONAL, val priority:Priority=Priority.TARGET,
 val date:String=LocalDate.now().toString(), val start:String="09:00", val minutes:Int=15,
 val status:Status=Status.QUEUED, val fixed:Boolean=false, val protected:Boolean=false,
 val mandatoryToday:Boolean=false, val cleaning:Boolean=false, val snitchEligible:Boolean=false,
 val recurrenceKey:String?=null, val deadline:String?=null, val hardDeadline:Boolean=false,
 val locationKind:LocationKind=LocationKind.ANYWHERE, val locationLabel:String?=null,
 val dependsOn:String?=null, val notBefore:String?=null, val source:String="manual",
 val startedAt:Long?=null, val pausedAt:Long?=null, val accumulatedSeconds:Long=0,
 val completedAt:Long?=null, val actualMinutes:Int?=null
){
 fun localStart()=LocalDateTime.of(LocalDate.parse(date),LocalTime.parse(start))
 fun end()=localStart().plusMinutes(minutes.toLong())
 fun isDone()=status==Status.COMPLETED||status==Status.CANCELLED
}

data class CalendarBlock(val id:String,val title:String,val startMillis:Long,val endMillis:Long,val calendarName:String,val location:String?,val allDay:Boolean){
 fun startZdt()=Instant.ofEpochMilli(startMillis).atZone(ZoneId.systemDefault())
 fun endZdt()=Instant.ofEpochMilli(endMillis).atZone(ZoneId.systemDefault())
}

data class PlanResult(val tasks:List<CtrlTask>,val overCapacityMinutes:Int)
