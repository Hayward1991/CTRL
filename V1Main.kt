package com.ctrl.life

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){
  super.onCreate(savedInstanceState)
  CtrlNotifications.create(this)
  setContent{CtrlTheme{CtrlApp()}}
 }
}
