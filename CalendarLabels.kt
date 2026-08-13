package com.ctrl.life

fun CalendarBlock.sourceLabel():String{
 val n=calendarName.lowercase()
 return when{
  "howbout" in n -> "HOWBOUT"
  "work" in n || "commvault" in n || "outlook" in n || "exchange" in n -> "WORK"
  else -> calendarName.uppercase()
 }
}
