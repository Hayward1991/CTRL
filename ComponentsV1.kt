package com.ctrl.life

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable fun CtrlBottom(tab:Tab,onTab:(Tab)->Unit){
 Surface(shadowElevation=8.dp,color=CtrlBg){Row(Modifier.fillMaxWidth().navigationBarsPadding().height(64.dp),horizontalArrangement=Arrangement.SpaceEvenly,verticalAlignment=Alignment.CenterVertically){Tab.entries.forEach{t->Text(t.name,Modifier.clickable{onTab(t)}.padding(18.dp),color=if(t==tab)CtrlInk else CtrlMuted,fontSize=12.sp,fontWeight=if(t==tab)FontWeight.Bold else FontWeight.Medium,letterSpacing=1.2.sp)}}}
}

@Composable fun CtrlHeader(){
 val d=LocalDate.now();Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("CTRL",fontSize=30.sp,fontWeight=FontWeight.Black,letterSpacing=5.sp,color=CtrlInk);Text(d.format(DateTimeFormatter.ofPattern("EEEE d MMMM")),fontSize=17.sp,color=CtrlMuted)};Surface(shape=CircleShape,color=CtrlCard,border=BorderStroke(1.dp,CtrlLine),modifier=Modifier.size(48.dp)){Box(contentAlignment=Alignment.Center){Text("JH",fontWeight=FontWeight.Bold)}}}
}

@Composable fun TaskLineV1(t:CtrlTask){
 Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){Text(t.start,Modifier.width(56.dp),fontSize=14.sp,color=CtrlMuted);Surface(Modifier.weight(1f),shape=RoundedCornerShape(18.dp),color=CtrlCard,border=BorderStroke(1.dp,if(t.fixed||t.protected)CtrlGold.copy(alpha=.6f) else CtrlLine)){Column(Modifier.padding(16.dp)){Text(t.title,fontSize=17.sp,fontWeight=FontWeight.SemiBold,color=CtrlInk);Text("${CtrlRules.scoreArea(t.area).name.replace('_',' ')} · ${t.minutes} min · ${t.status.name}",fontSize=12.sp,color=CtrlMuted)}}}
}

@Composable fun CaptureV1(onDismiss:()->Unit,onAdd:(String)->Unit){
 var text by remember{mutableStateOf("")};AlertDialog(onDismissRequest=onDismiss,title={Text("Tell Control")},text={OutlinedTextField(text,{text=it},label={Text("What needs doing?")},minLines=2)},confirmButton={Button({if(text.isNotBlank())onAdd(text.trim())},enabled=text.isNotBlank()){Text("ADD")}},dismissButton={TextButton(onDismiss){Text("Cancel")}})
}
