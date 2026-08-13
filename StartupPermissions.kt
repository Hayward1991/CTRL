package com.ctrl.life

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build

object StartupPermissions{
 fun notifications(activity:Activity){
  if(Build.VERSION.SDK_INT>=33&&activity.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED){
   activity.requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS),7002)
  }
 }
}
