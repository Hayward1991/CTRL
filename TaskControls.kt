package com.ctrl.life

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable fun ActiveTaskV1(t:CtrlTask,tasks:List<CtrlTask>,onTasks:(List<CtrlTask>)->Unit,store:CtrlStore){
 fun replace(next:CtrlTask){onTasks(tasks.map{if(it.id==t.id)next else it})}
 Surface(color=CtrlCard,shape=RoundedCornerShape(26.dp),border=BorderStroke(1.dp,if(t.fixed||t.protected)CtrlGold else CtrlLine),shadowElevation=3.dp){
  Column(Modifier.fillMaxWidth().padding(24.dp),horizontalAlignment=Alignment.CenterHorizontally){
   Text("${CtrlRules.scoreArea(t.area).name.replace('_',' ')} · ${t.priority.name}",fontSize=12.sp,color=CtrlMuted)
   Spacer(Modifier.height(8.dp));Text(t.title,fontSize=27.sp,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center,color=CtrlInk)
   Spacer(Modifier.height(8.dp));Text("${t.start} · ${t.minutes} min",fontSize=17.sp,color=CtrlMuted)
   Spacer(Modifier.height(18.dp));TaskButtonsV1(t.status,onStart={replace(t.copy(status=Status.ACTIVE))},onPause={replace(t.copy(status=Status.PAUSED))},onFinish={replace(t.copy(status=Status.COMPLETED,completedAt=System.currentTimeMillis()))})
  }
 }
}
