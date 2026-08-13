package com.ctrl.life

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import java.time.LocalDate
import java.time.ZoneId

object CalendarBridge{
 fun read(context:Context,from:LocalDate=LocalDate.now(),days:Int=14):List<CalendarBlock>{
  if(context.checkSelfPermission(Manifest.permission.READ_CALENDAR)!=PackageManager.PERMISSION_GRANTED)return emptyList()
  val zone=ZoneId.systemDefault();val begin=from.atStartOfDay(zone).toInstant().toEpochMilli();val end=from.plusDays(days.toLong()).atStartOfDay(zone).toInstant().toEpochMilli()
  val uri=CalendarContract.Instances.CONTENT_URI.buildUpon().apply{appendPath(begin.toString());appendPath(end.toString())}.build()
  val projection=arrayOf(CalendarContract.Instances.EVENT_ID,CalendarContract.Instances.TITLE,CalendarContract.Instances.BEGIN,CalendarContract.Instances.END,CalendarContract.Instances.CALENDAR_DISPLAY_NAME,CalendarContract.Instances.EVENT_LOCATION,CalendarContract.Instances.ALL_DAY)
  val out=mutableListOf<CalendarBlock>()
  context.contentResolver.query(uri,projection,null,null,"${CalendarContract.Instances.BEGIN} ASC")?.use{c->
   while(c.moveToNext())out+=CalendarBlock(c.getLong(0).toString(),c.getString(1)?:"Calendar",c.getLong(2),c.getLong(3),c.getString(4)?:"Calendar",c.getString(5),c.getInt(6)==1)
  }
  return out
 }
}
