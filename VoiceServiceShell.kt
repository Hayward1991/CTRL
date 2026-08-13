package com.ctrl.life

import android.app.Service
import android.content.Intent
import android.os.IBinder

class VoiceServiceShell:Service(){
 override fun onBind(intent:Intent?):IBinder?=null
}
