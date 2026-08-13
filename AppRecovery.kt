package com.ctrl.life

import android.content.Context

object AppRecovery {
 fun restore(context:Context){
  val store=CtrlStore(context)
  val tasks=store.loadTasks()
  AlarmScheduler.scheduleAll(context,tasks)
 }
}
