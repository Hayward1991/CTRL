package com.ctrl.life

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable fun TaskButtonsV1(status:Status,onStart:()->Unit,onPause:()->Unit,onPostpone:()->Unit,onFinish:()->Unit){
 Row{
  if(status==Status.ACTIVE){
   OutlinedButton(onClick=onPause){Text("PAUSE")}
   Spacer(Modifier.width(8.dp));OutlinedButton(onClick=onPostpone){Text("POSTPONE")}
  } else Button(onClick=onStart){Text(if(status==Status.PAUSED)"RESUME" else "START")}
  Spacer(Modifier.width(8.dp));Button(onClick=onFinish){Text("FINISH")}
 }
}
