package com.ctrl.life

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable fun NotificationControls(){
 val context=LocalContext.current
 var granted by remember{mutableStateOf(Build.VERSION.SDK_INT<33||ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED)}
 val launcher=rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){granted=it}
 if(granted)Text("Reminders enabled",color=CtrlMuted)
 else Button(onClick={launcher.launch(Manifest.permission.POST_NOTIFICATIONS)}){Text("Enable reminders")}
}
