package com.ctrl.life

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.max

@Composable fun ActiveTaskV1(t:CtrlTask,tasks:List<CtrlTask>,onTasks:(List<CtrlTask>)->Unit,store:CtrlStore){
 var now by remember(t.id,t.status,t.startedAt,t.accumulatedSeconds){mutableLongStateOf(System.currentTimeMillis())}
 LaunchedEffect(t.id,t.status,t.startedAt,t.accumulatedSeconds){
  now=System.currentTimeMillis()
  while(t.status==Status.ACTIVE){delay(1000);now=System.currentTimeMillis()}
 }
 fun replace(next:CtrlTask){onTasks(tasks.map{if(it.id==t.id)next else it})}
 val remaining=max(0L,t.minutes*60L-TaskRuntime.elapsedSeconds(t,now))
 val clock=String.format("%02d:%02d",remaining/60,remaining%60)
 Surface(color=CtrlCard,shape=RoundedCornerShape(26.dp),border=BorderStroke(1.dp,if(t.fixed||t.protected)CtrlGold else CtrlLine),shadowElevation=3.dp){
  Column(Modifier.fillMaxWidth().padding(24.dp),horizontalAlignment=Alignment.CenterHorizontally){
   Text("${CtrlRules.scoreArea(t.area).name.replace('_',' ')} · ${t.priority.name}",fontSize=12.sp,color=CtrlMuted)
   Spacer(Modifier.height(8.dp));Text(t.title,fontSize=27.sp,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center,color=CtrlInk)
   Spacer(Modifier.height(8.dp));Text("${t.start} · ${t.minutes} min",fontSize=17.sp,color=CtrlMuted)
   if(t.status==Status.ACTIVE||t.status==Status.PAUSED){Spacer(Modifier.height(10.dp));Text(clock,fontSize=34.sp,fontWeight=FontWeight.Black,color=CtrlInk)}
   Spacer(Modifier.height(18.dp));TaskButtonsV1(t.status,onStart={replace(TaskRuntime.start(t))},onPause={replace(TaskRuntime.pause(t))},onFinish={replace(TaskRuntime.finish(t,store))})
  }
 }
}
