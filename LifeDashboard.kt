package com.ctrl.life

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable fun LifeScreenV1(tasks:List<CtrlTask>,pad:PaddingValues){
 val scores=ScoreEngine.weekly(tasks);val overall=ScoreEngine.overall(scores)
 LazyColumn(Modifier.fillMaxSize().padding(pad),contentPadding=PaddingValues(20.dp,24.dp,20.dp,100.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
  item{CtrlHeader();Spacer(Modifier.height(18.dp));Text("LIFE",color=CtrlMuted,fontWeight=FontWeight.Bold)}
  item{Surface(color=CtrlInk,shape=RoundedCornerShape(28.dp)){Column(Modifier.fillMaxWidth().padding(28.dp),horizontalAlignment=Alignment.CenterHorizontally){Text("OVERALL",color=CtrlCard.copy(alpha=.7f));Text("$overall",color=CtrlCard,fontSize=58.sp,fontWeight=FontWeight.Black);Text("this week",color=CtrlCard.copy(alpha=.7f))}}}
  items(listOf(Area.FAMILY,Area.SELF_CARE,Area.HEALTH,Area.HOME,Area.WORK,Area.PERSONAL)){a->Surface(color=CtrlCard,shape=RoundedCornerShape(20.dp),border=BorderStroke(1.dp,CtrlLine)){Row(Modifier.fillMaxWidth().padding(18.dp),verticalAlignment=Alignment.CenterVertically){Text(a.name.replace('_',' '),Modifier.weight(1f),fontWeight=FontWeight.Bold);Text("${scores[a]?:100}",fontSize=24.sp,fontWeight=FontWeight.Black)}}}
  item{Surface(color=CtrlCard,shape=RoundedCornerShape(20.dp),border=BorderStroke(1.dp,CtrlLine)){Column(Modifier.fillMaxWidth().padding(18.dp)){Text("HEY CONTROL",fontWeight=FontWeight.Bold);Spacer(Modifier.height(8.dp));Text("Voice capture runs only after you start it.",color=CtrlMuted);Spacer(Modifier.height(12.dp));VoiceControls()}}}
  item{UpdateControls()}
 }
}
