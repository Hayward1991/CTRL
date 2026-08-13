package com.ctrl.life

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder

class HeyControlService:Service(){
 override fun onBind(intent:Intent?):IBinder?=null
 override fun onCreate(){
  super.onCreate()
  CtrlNotifications.create(this)
  val notification=Notification.Builder(this,CtrlNotifications.LISTEN)
   .setContentTitle("CTRL · Listening")
   .setContentText("Say Hey Control")
   .setSmallIcon(android.R.drawable.ic_btn_speak_now)
   .setOngoing(true)
   .build()
  startForeground(4100,notification)
 }
 override fun onStartCommand(intent:Intent?,flags:Int,startId:Int):Int=START_STICKY
}
