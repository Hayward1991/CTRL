package com.ctrl.life

import java.net.HttpURLConnection
import java.net.URL

object HttpUtil{
 fun request(url:String,method:String="GET",body:String?=null,headers:Map<String,String> = emptyMap()):Pair<Int,String>{
  val c=(URL(url).openConnection() as HttpURLConnection);c.requestMethod=method;c.connectTimeout=8000;c.readTimeout=8000
  headers.forEach{(k,v)->c.setRequestProperty(k,v)}
  if(body!=null){c.doOutput=true;c.setRequestProperty("Content-Type","application/json");c.outputStream.use{it.write(body.toByteArray())}}
  val code=c.responseCode;val stream=if(code in 200..299)c.inputStream else c.errorStream
  val text=stream?.bufferedReader()?.use{it.readText()}.orEmpty();c.disconnect();return code to text
 }
}
