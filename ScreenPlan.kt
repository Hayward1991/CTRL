package com.ctrl.life

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable fun PlanScreenV1(tasks:List<CtrlTask>,pad:PaddingValues){
 var day by remember{mutableStateOf(LocalDate.now())};val days=(0..13).map{LocalDate.now().plusDays(it.toLong())}
 Column(Modifier.fillMaxSize().padding(pad).padding(top=24.dp)){Column(Modifier.padding(horizontal=20.dp)){CtrlHeader();Spacer(Modifier.height(18.dp));Text("14 DAY PLAN",color=CtrlMuted,fontWeight=FontWeight.Bold)}
  LazyRow(contentPadding=PaddingValues(20.dp,12.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)){items(days){d->val selected=d==day;Surface(shape=RoundedCornerShape(16.dp),color=if(selected)CtrlInk else CtrlCard,border=BorderStroke(1.dp,if(selected)CtrlInk else CtrlLine),modifier=Modifier.clickable{day=d}){Column(Modifier.padding(14.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(d.format(DateTimeFormatter.ofPattern("EEE")).uppercase(),color=if(selected)CtrlCard else CtrlMuted);Text(d.dayOfMonth.toString(),fontWeight=FontWeight.Bold,color=if(selected)CtrlCard else CtrlInk)}}}}
  LazyColumn(contentPadding=PaddingValues(20.dp,4.dp,20.dp,100.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){val list=tasks.filter{it.date==day.toString()}.sortedBy{it.start};if(list.isEmpty())item{Text("No CTRL tasks planned.",color=CtrlMuted)}else items(list,key={it.id}){TaskLineV1(it)}}
 }
}
