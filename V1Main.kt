package com.ctrl.life

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){
  super.onCreate(savedInstanceState)
  CtrlNotifications.create(this)
  if(checkSelfPermission(Manifest.permission.READ_CALENDAR)!=PackageManager.PERMISSION_GRANTED){
   requestPermissions(arrayOf(Manifest.permission.READ_CALENDAR),7001)
  }
  BiometricGate.unlock(this){setContent{CtrlTheme{CtrlApp()}}}
 }
}
