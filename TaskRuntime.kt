package com.ctrl.life

import kotlin.math.max

object TaskRuntime {
 fun start(t:CtrlTask,now:Long=System.currentTimeMillis())=t.copy(status=Status.ACTIVE,startedAt=t.startedAt?:now,pausedAt=null)
 fun pause(t:CtrlTask,now:Long=System.currentTimeMillis())=t.copy(status=Status.PAUSED,pausedAt=now)
 fun finish(t:CtrlTask,store:CtrlStore,now:Long=System.currentTimeMillis()):CtrlTask{
  val elapsed=max(1,((now-(t.startedAt?:now))/60000L).toInt())
  store.recordDuration(t.recurrenceKey?:t.title,elapsed,t.minutes)
  return t.copy(status=Status.COMPLETED,completedAt=now,actualMinutes=elapsed)
 }
}
