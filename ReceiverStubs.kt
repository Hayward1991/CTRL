package com.ctrl.life

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CtrlAlarmReceiver:BroadcastReceiver(){
 override fun onReceive(context:Context,intent:Intent){
  val taskId=intent.getStringExtra("task_id")?:return
  val task=CtrlStore(context).loadTasks().firstOrNull{it.id==taskId}?:return
  if(task.isDone()||task.status==Status.SKIPPED)return
  when(intent.action){
   AlarmScheduler.SNITCH -> if(task.cleaning&&task.snitchEligible)CtrlNotifications.task(context,"Cleaning check: ${task.title} is still waiting.",true)
   AlarmScheduler.TASK -> {
    val offset=intent.getIntExtra("offset",0)
    val text=if(offset<0) "${task.title} starts in ${-offset} minutes" else "Time for ${task.title}"
    CtrlNotifications.task(context,text,task.priority==Priority.MUST)
   }
  }
 }
}
