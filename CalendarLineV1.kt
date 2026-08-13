package com.ctrl.life

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.DateTimeFormatter

@Composable fun CalendarLineV1(c:CalendarBlock){
 val start=c.startZdt().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
 val end=c.endZdt().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
 Row(Modifier.fillMaxWidth()){
  Text(start,Modifier.width(56.dp),fontSize=14.sp,color=CtrlMuted)
  Surface(Modifier.weight(1f),shape=RoundedCornerShape(18.dp),color=CtrlSoft,border=BorderStroke(1.dp,CtrlGold.copy(alpha=.45f))){
   Column(Modifier.padding(16.dp)){
    Text(c.title,fontSize=17.sp,fontWeight=FontWeight.SemiBold,color=CtrlInk)
    Text("${c.sourceLabel()} · $start–$end",fontSize=12.sp,color=CtrlMuted)
   }
  }
 }
}
