package com.ctrl.life

import android.content.Context
import org.json.JSONObject

object CloudDevice{
 fun register(c:Context,s:CloudAuth.Session){
  val store=CtrlStore(c);val body=JSONObject().apply{
   put("user_id",s.userId);put("device_key",store.deviceId());put("is_primary",true);put("platform","android")
  }.toString()
  val h=CloudAuth.headers(s)+mapOf("Prefer" to "resolution=merge-duplicates,return=minimal")
  HttpUtil.request("${CloudConfig.URL}/rest/v1/device_registry?on_conflict=user_id,device_key","POST",body,h)
 }
}
