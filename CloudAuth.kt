package com.ctrl.life

import android.content.Context
import org.json.JSONObject

object CloudAuth{
 data class Session(val token:String,val userId:String)
 fun session(c:Context):Session?{
  val p=c.getSharedPreferences("ctrl_cloud",Context.MODE_PRIVATE)
  val t=p.getString("token",null);val u=p.getString("user",null)
  if(t!=null&&u!=null)return Session(t,u)
  val h=mapOf("apikey" to CloudConfig.KEY,"Authorization" to "Bearer ${CloudConfig.KEY}")
  val (code,text)=HttpUtil.request("${CloudConfig.URL}/auth/v1/signup","POST","{}",h)
  if(code !in 200..299)return null
  val o=JSONObject(text);val token=o.optString("access_token");val user=o.optJSONObject("user")?.optString("id").orEmpty()
  if(token.isBlank()||user.isBlank())return null
  p.edit().putString("token",token).putString("user",user).apply();return Session(token,user)
 }
 fun headers(s:Session)=mapOf("apikey" to CloudConfig.KEY,"Authorization" to "Bearer ${s.token}")
}
