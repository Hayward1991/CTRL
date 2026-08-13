package com.ctrl.life

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable fun VoiceControls(){
 val context=LocalContext.current
 var enabled by remember{mutableStateOf(CtrlStore(context).getBool("hey_control"))}
 fun start(){ContextCompat.startForegroundService(context,Intent(context,VoiceServiceShell::class.java));enabled=true;CtrlStore(context).setBool("hey_control",true)}
 val permission=rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){if(it)start()}
 Button(onClick={
  if(enabled){context.stopService(Intent(context,VoiceServiceShell::class.java));enabled=false;CtrlStore(context).setBool("hey_control",false)}
  else if(ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED)start()
  else permission.launch(Manifest.permission.RECORD_AUDIO)
 }){Text(if(enabled)"Stop Hey Control" else "Start Hey Control")}
}
