package com.ctrl.life

import android.content.Context
import org.json.JSONArray

object UpdateChecker{
 data class Release(val versionCode:Int,val versionName:String,val apkUrl:String?,val notes:String?,val mandatory:Boolean)
 fun latest(context:Context,currentVersion:Int):Release?{
  val session=CloudAuth.session(context)?:return null
  val (_,body)=HttpUtil.request("${CloudConfig.URL}/rest/v1/app_releases?select=*&order=version_code.desc&limit=1","GET",null,CloudAuth.headers(session))
  val row=runCatching{JSONArray(body).optJSONObject(0)}.getOrNull()?:return null
  val code=row.optInt("version_code")
  if(code<=currentVersion)return null
  return Release(code,row.optString("version_name"),row.optString("apk_url").takeIf{it.isNotBlank()&&it!="null"},row.optString("release_notes").takeIf{it.isNotBlank()&&it!="null"},row.optBoolean("mandatory"))
 }
}
