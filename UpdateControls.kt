package com.ctrl.life

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable fun UpdateControls(){
 val context=LocalContext.current
 var release by remember{mutableStateOf<UpdateChecker.Release?>(null)}
 LaunchedEffect(Unit){release=withContext(Dispatchers.IO){runCatching{UpdateChecker.latest(context,2)}.getOrNull()}}
 val r=release?:return
 Card{ListItem(headlineContent={Text("CTRL ${r.versionName} available")},supportingContent={Text(r.notes?:"A newer CTRL build is ready.")},trailingContent={
  if(r.apkUrl!=null)Button(onClick={context.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(r.apkUrl)))} ){Text("Update")}
 })}
}
