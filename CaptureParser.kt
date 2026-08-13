package com.ctrl.life

import java.time.*

object CaptureParser{
 data class Parsed(val title:String,val area:Area,val priority:Priority,val cleaning:Boolean,val hard:Boolean,val date:LocalDate,val time:LocalTime?)
 fun parse(raw:String):Parsed{
  val s=raw.trim();val l=s.lowercase()
  val cleaning=Regex("""\b(clean|hoover|vacuum|dishwasher|washing|wipe|tidy|stove|windowsill)\b""").containsMatchIn(l)
  val area=when{
   cleaning->Area.HOME
   Regex("""\b(work|partner|customer|pipeline|quote|meeting)\b""").containsMatchIn(l)->Area.WORK
   Regex("""\b(gym|boxing|walk|training|health)\b""").containsMatchIn(l)->Area.HEALTH
   Regex("""\b(kirsty|harrison|kids|family|school)\b""").containsMatchIn(l)->Area.FAMILY
   Regex("""\b(shower|shave|skin|haircut|bath|teeth)\b""").containsMatchIn(l)->Area.SELF_CARE
   else->Area.LIFE_ADMIN
  }
  val pri=when{l.contains("must")||l.contains("urgent")->Priority.MUST;l.contains("someday")||l.contains("when i can")->Priority.SOMEDAY;else->Priority.TARGET}
  val hard=Regex("""\bby\s+(today|tomorrow|monday|tuesday|wednesday|thursday|friday|saturday|sunday)\b""").containsMatchIn(l)
  val date=if(l.contains("tomorrow"))LocalDate.now().plusDays(1) else LocalDate.now()
  val m=Regex("""\b(?:at\s*)?(\d{1,2})(?::(\d{2}))?\s*(am|pm)\b""").find(l)
  val time=m?.let{var h=it.groupValues[1].toInt();val mm=it.groupValues[2].ifBlank{"0"}.toInt();val ap=it.groupValues[3];if(ap=="pm"&&h<12)h+=12;if(ap=="am"&&h==12)h=0;LocalTime.of(h,mm)}
  val title=s.replace(Regex("""(?i)^(hey\s+control[, ]*|remind me to |add )"""),"").trim().ifBlank{"New task"}
  return Parsed(title,area,pri,cleaning,hard,date,time)
 }
}
