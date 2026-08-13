package com.ctrl.life

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object CloudTasks{
 private fun dbArea(a:Area)=when(a){Area.LIFE_ADMIN->"personal";else->a.name.lowercase()}
 private fun dbStatus(s:Status)=when(s){Status.COMPLETED->"completed";Status.SKIPPED->"skipped";Status.CANCELLED->"cancelled";Status.ACTIVE->"active";Status.QUEUED->"queued";else->"scheduled"}
 fun push(c:Context,s:CloudAuth.Session,tasks:List<CtrlTask>){
  val a=JSONArray();tasks.forEach{t->a.put(JSONObject().apply{
   put("id",t.id);put("user_id",s.userId);put("title",t.title);put("notes",t.notes);put("life_area",dbArea(t.area));put("priority",t.priority.name.lowercase());put("status",dbStatus(t.status));put("estimated_minutes",t.minutes);put("deadline",t.deadline?:JSONObject.NULL);put("source",t.source);put("deadline_is_hard",t.hardDeadline);put("location_type",t.locationKind.name.lowercase());put("location_label",t.locationLabel?:JSONObject.NULL);put("mandatory_today",t.mandatoryToday);put("task_kind",if(t.cleaning)"cleaning" else "general");put("snitch_eligible",t.snitchEligible);put("is_protected",t.protected);put("is_flexible",!t.fixed&&!t.protected);put("accumulated_active_seconds",t.accumulatedSeconds)
  })}
  val h=CloudAuth.headers(s)+mapOf("Prefer" to "resolution=merge-duplicates,return=minimal")
  HttpUtil.request("${CloudConfig.URL}/rest/v1/tasks?on_conflict=id","POST",a.toString(),h)
 }
 fun pull(c:Context,s:CloudAuth.Session):List<JSONObject>{
  val h=CloudAuth.headers(s);val (_,text)=HttpUtil.request("${CloudConfig.URL}/rest/v1/tasks?select=*&order=created_at.desc&limit=200","GET",null,h)
  return runCatching{val a=JSONArray(text);List(a.length()){i->a.getJSONObject(i)}}.getOrElse{emptyList()}
 }
}
