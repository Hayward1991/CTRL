package com.ctrl.life

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate

@Composable fun TodayScreenV1(tasks:List<CtrlTask>,over:Int,message:String,pad:PaddingValues,onTasks:(List<CtrlTask>)->Unit,store:CtrlStore){
 val list=tasks.filter{it.date==LocalDate.now().toString()&&!it.isDone()}.sortedBy{it.start}
 val active=list.firstOrNull{it.status==Status.ACTIVE||it.status==Status.PAUSED}?:list.firstOrNull()
 LazyColumn(Modifier.fillMaxSize().padding(pad),contentPadding=PaddingValues(20.dp,24.dp,20.dp,110.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
  item{CtrlHeader()}
  if(over>0)item{Surface(color=MaterialTheme.colorScheme.errorContainer,shape=RoundedCornerShape(16.dp)){Text("OVER CAPACITY · $over min",Modifier.fillMaxWidth().padding(14.dp),textAlign=TextAlign.Center,fontWeight=FontWeight.Bold)}}
  item{Surface(color=CtrlSoft,shape=RoundedCornerShape(16.dp)){Text(message,Modifier.fillMaxWidth().padding(14.dp),textAlign=TextAlign.Center,color=CtrlInk)}}
  item{Text("NOW",fontSize=12.sp,letterSpacing=2.sp,color=CtrlMuted,fontWeight=FontWeight.Bold)}
  if(active!=null)item{ActiveTaskV1(active,tasks,onTasks,store)} else item{Surface(color=CtrlCard,shape=RoundedCornerShape(24.dp),border=BorderStroke(1.dp,CtrlLine)){Text("Nothing needs you right now.",Modifier.fillMaxWidth().padding(30.dp),textAlign=TextAlign.Center)}}
  item{Text("TODAY",fontSize=12.sp,letterSpacing=2.sp,color=CtrlMuted,fontWeight=FontWeight.Bold)}
  items(list,key={it.id}){TaskLineV1(it)}
 }
}
