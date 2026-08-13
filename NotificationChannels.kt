package com.ctrl.life

import android.app.*
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

object CtrlNotifications{
 const val NORMAL="ctrl_normal";const val IMPORTANT="ctrl_important";const val SNITCH="ctrl_snitch";const val LISTEN="ctrl_listen"
 fun create(c:Context){
  if(android.os.Build.VERSION.SDK_INT<26)return
  val n=c.getSystemService(NotificationManager::class.java)
  n.createNotificationChannel(NotificationChannel(NORMAL,"CTRL tasks",NotificationManager.IMPORTANCE_DEFAULT))
  n.createNotificationChannel(NotificationChannel(IMPORTANT,"CTRL important",NotificationManager.IMPORTANCE_HIGH))
  n.createNotificationChannel(NotificationChannel(SNITCH,"CTRL Snitch Mode",NotificationManager.IMPORTANCE_HIGH))
  n.createNotificationChannel(NotificationChannel(LISTEN,"CTRL listening",NotificationManager.IMPORTANCE_LOW))
 }
 fun task(c:Context,text:String,important:Boolean=false){
  val ch=if(important)IMPORTANT else NORMAL
  val n=NotificationCompat.Builder(c,ch).setSmallIcon(android.R.drawable.ic_popup_reminder).setContentTitle("CTRL").setContentText(text).setAutoCancel(true).build()
  ContextCompat.getSystemService(c,NotificationManager::class.java)?.notify(text.hashCode(),n)
 }
}
