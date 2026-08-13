package com.ctrl.life

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class VoiceServiceShell:Service(){
 override fun onBind(intent:Intent?):IBinder?=null
 override fun onCreate(){
  super.onCreate()
  CtrlNotifications.create(this)
  val notification=NotificationCompat.Builder(this,CtrlNotifications.LISTEN)
   .setSmallIcon(android.R.drawable.ic_btn_speak_now)
   .setContentTitle("CTRL · Listening")
   .setContentText("Hey Control is active")
   .setOngoing(true)
   .build()
  startForeground(4101,notification)
 }
}
