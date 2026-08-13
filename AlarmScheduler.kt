package com.ctrl.life

import android.app.*
import android.content.*
import android.os.Build
import java.time.ZoneId

object AlarmScheduler{
 const val TASK="com.ctrl.life.TASK"
 const val SNITCH="com.ctrl.life.SNITCH"
 fun scheduleAll(c:Context,tasks:List<CtrlTask>){
  tasks.filter{!it.isDone()}.forEach{t->
   schedule(c,t,-CtrlRules.WARNING_MIN,TASK)
   schedule(c,t,0,TASK)
   if(t.cleaning&&t.snitchEligible)schedule(c,t,CtrlRules.SNITCH_DELAY_MIN,SNITCH)
  }
 }
 private fun schedule(c:Context,t:CtrlTask,offset:Int,action:String){
  val at=runCatching{t.localStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()+offset*60_000L}.getOrNull()?:return
  if(at<=System.currentTimeMillis())return
  val i=Intent(c,CtrlAlarmReceiver::class.java).setAction(action).putExtra("task_id",t.id).putExtra("offset",offset)
  val pi=PendingIntent.getBroadcast(c,(t.id+offset).hashCode(),i,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
  val am=c.getSystemService(AlarmManager::class.java)
  if(Build.VERSION.SDK_INT>=31&&!am.canScheduleExactAlarms())am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,at,pi) else am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,at,pi)
 }
}
