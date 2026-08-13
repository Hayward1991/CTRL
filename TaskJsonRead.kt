package com.ctrl.life

import org.json.JSONObject
import java.time.LocalDate
import java.util.UUID

object TaskJsonRead{
 private fun JSONObject.s(k:String)=if(has(k)&&!isNull(k))optString(k) else null
 private fun JSONObject.l(k:String)=if(has(k)&&!isNull(k))optLong(k) else null
 fun read(o:JSONObject)=CtrlTask(
  id=o.optString("id",UUID.randomUUID().toString()),title=o.optString("title","Task"),notes=o.optString("notes",""),
  area=runCatching{Area.valueOf(o.optString("area","PERSONAL"))}.getOrDefault(Area.PERSONAL),
  priority=runCatching{Priority.valueOf(o.optString("priority","TARGET"))}.getOrDefault(Priority.TARGET),
  date=o.optString("date",LocalDate.now().toString()),start=o.optString("start","09:00"),minutes=o.optInt("minutes",15),
  status=runCatching{Status.valueOf(o.optString("status","QUEUED"))}.getOrDefault(Status.QUEUED),
  fixed=o.optBoolean("fixed"),protected=o.optBoolean("protected"),mandatoryToday=o.optBoolean("mandatory"),cleaning=o.optBoolean("cleaning"),snitchEligible=o.optBoolean("snitch"),
  recurrenceKey=o.s("recurrence"),deadline=o.s("deadline"),hardDeadline=o.optBoolean("hard"),
  locationKind=runCatching{LocationKind.valueOf(o.optString("locKind","ANYWHERE"))}.getOrDefault(LocationKind.ANYWHERE),locationLabel=o.s("loc"),
  dependsOn=o.s("depends"),notBefore=o.s("notBefore"),source=o.optString("source","manual"),startedAt=o.l("startedAt"),pausedAt=o.l("pausedAt"),
  accumulatedSeconds=o.optLong("activeSecs",0),completedAt=o.l("completedAt"),actualMinutes=if(o.has("actual")&&!o.isNull("actual"))o.optInt("actual") else null
 )
}
