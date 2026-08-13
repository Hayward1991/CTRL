package com.ctrl.life

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.max

object TaskRuntime {
 fun start(t:CtrlTask,now:Long=System.currentTimeMillis())=t.copy(status=Status.ACTIVE,startedAt=now,pausedAt=null)
 fun pause(t:CtrlTask,now:Long=System.currentTimeMillis()):CtrlTask{
  val activeSeconds=if(t.startedAt==null)0L else max(0L,(now-t.startedAt)/1000L)
  return t.copy(status=Status.PAUSED,startedAt=null,pausedAt=now,accumulatedSeconds=t.accumulatedSeconds+activeSeconds)
 }
 fun elapsedSeconds(t:CtrlTask,now:Long=System.currentTimeMillis()):Long{
  val activeSeconds=if(t.status==Status.ACTIVE&&t.startedAt!=null)max(0L,(now-t.startedAt)/1000L) else 0L
  return t.accumulatedSeconds+activeSeconds
 }
 fun postpone(t:CtrlTask,now:Long=System.currentTimeMillis()):CtrlTask{
  val used=elapsedSeconds(t,now)
  val remainingSeconds=max(60L,t.minutes*60L-used)
  val remainingMinutes=max(1,((remainingSeconds+59L)/60L).toInt())
  val next=Instant.ofEpochMilli(now).atZone(ZoneId.systemDefault()).toLocalDateTime().plusMinutes(5)
  return t.copy(status=Status.DELAYED,date=next.toLocalDate().toString(),start=next.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),minutes=remainingMinutes,startedAt=null,pausedAt=null,accumulatedSeconds=0)
 }
 fun finish(t:CtrlTask,store:CtrlStore,now:Long=System.currentTimeMillis()):CtrlTask{
  val totalSeconds=elapsedSeconds(t,now)
  val elapsed=max(1,((totalSeconds+59L)/60L).toInt())
  store.recordDuration(t.recurrenceKey?:t.title,elapsed,t.minutes)
  return t.copy(status=Status.COMPLETED,startedAt=null,pausedAt=null,accumulatedSeconds=totalSeconds,completedAt=now,actualMinutes=elapsed)
 }
}
