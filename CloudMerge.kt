package com.ctrl.life

import org.json.JSONObject
import java.time.*
import java.time.format.DateTimeFormatter

object CloudMerge{
 fun merge(local:List<CtrlTask>,rows:List<JSONObject>):List<CtrlTask>{
  val out=local.associateBy{it.id}.toMutableMap()
  rows.forEach{o->
   val id=o.optString("id");if(id.isBlank()||out.containsKey(id))return@forEach
   val area=when(o.optString("life_area")){"work"->Area.WORK;"family"->Area.FAMILY;"health"->Area.HEALTH;"home"->Area.HOME;"self_care"->Area.SELF_CARE;else->Area.LIFE_ADMIN}
   val pri=runCatching{Priority.valueOf(o.optString("priority","target").uppercase())}.getOrDefault(Priority.TARGET)
   val deadline=o.optString("deadline").takeIf{it.isNotBlank()&&it!="null"}
   val dt=deadline?.let{runCatching{Instant.parse(it).atZone(ZoneId.systemDefault()).toLocalDateTime()}.getOrNull()}
   out[id]=CtrlTask(id=id,title=o.optString("title","CTRL task"),notes=o.optString("notes",""),area=area,priority=pri,date=(dt?.toLocalDate()?:LocalDate.now()).toString(),start=(dt?.toLocalTime()?:LocalTime.now().plusMinutes(5)).format(DateTimeFormatter.ofPattern("HH:mm")),minutes=o.optInt("estimated_minutes",15),mandatoryToday=o.optBoolean("mandatory_today"),cleaning=o.optString("task_kind")=="cleaning",snitchEligible=o.optBoolean("snitch_eligible"),hardDeadline=o.optBoolean("deadline_is_hard"),deadline=deadline,source=o.optString("source","cloud"))
  }
  return out.values.toList()
 }
}
