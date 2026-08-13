package com.ctrl.life

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import kotlin.math.roundToInt

class CtrlStore(context:Context){
 private val p=context.getSharedPreferences("ctrl_v1",Context.MODE_PRIVATE)
 fun loadTasks():MutableList<CtrlTask>{
  val raw=p.getString("tasks",null)?:return mutableListOf()
  return runCatching{val a=JSONArray(raw);MutableList(a.length()){i->TaskJson.from(a.getJSONObject(i))}}.getOrElse{mutableListOf()}
 }
 fun saveTasks(tasks:List<CtrlTask>){val a=JSONArray();tasks.forEach{a.put(TaskJson.to(it))};p.edit().putString("tasks",a.toString()).apply()}
 fun getMode()=p.getString("mode","Normal")?:"Normal"
 fun setMode(v:String){p.edit().putString("mode",v).apply()}
 fun getBool(k:String,d:Boolean=false)=p.getBoolean(k,d)
 fun setBool(k:String,v:Boolean){p.edit().putBoolean(k,v).apply()}
 fun getString(k:String,d:String="")=p.getString(k,d)?:d
 fun setString(k:String,v:String){p.edit().putString(k,v).apply()}
 fun deviceId():String=p.getString("device_id",null)?:UUID.randomUUID().toString().also{p.edit().putString("device_id",it).apply()}
 fun learned(key:String,fallback:Int)=p.getInt("learn_${key.lowercase()}",fallback)
 fun recordDuration(key:String,actual:Int,previous:Int){
  val k="learn_${key.lowercase()}";val old=p.getInt(k,previous)
  p.edit().putInt(k,((old*.65)+(actual*.35)).roundToInt().coerceAtLeast(1)).apply()
 }
}

object TaskJson{
 fun to(t:CtrlTask)=JSONObject().apply{
  put("id",t.id);put("title",t.title);put("notes",t.notes);put("area",t.area.name);put("priority",t.priority.name)
  put("date",t.date);put("start",t.start);put("minutes",t.minutes);put("status",t.status.name);put("fixed",t.fixed);put("protected",t.protected)
  put("mandatory",t.mandatoryToday);put("cleaning",t.cleaning);put("snitch",t.snitchEligible);put("recurrence",t.recurrenceKey)
  put("deadline",t.deadline);put("hard",t.hardDeadline);put("locKind",t.locationKind.name);put("loc",t.locationLabel);put("depends",t.dependsOn);put("notBefore",t.notBefore)
  put("source",t.source);put("startedAt",t.startedAt);put("pausedAt",t.pausedAt);put("activeSecs",t.accumulatedSeconds);put("completedAt",t.completedAt);put("actual",t.actualMinutes)
 }
 fun from(o:JSONObject)=TaskJsonRead.read(o)
}
