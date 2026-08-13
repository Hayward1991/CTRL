package com.ctrl.life

import java.time.LocalDate
import kotlin.math.roundToInt

object ScoreEngine{
 fun weekly(tasks:List<CtrlTask>,today:LocalDate=LocalDate.now()):Map<Area,Int>{
  val monday=today.minusDays((today.dayOfWeek.value-1).toLong());val sunday=monday.plusDays(6);val out=mutableMapOf<Area,Int>()
  ScoreWeights.values.keys.forEach{area->
   val r=tasks.filter{t->val d=runCatching{LocalDate.parse(t.date)}.getOrNull();d!=null&&!d.isBefore(monday)&&!d.isAfter(sunday)&&CtrlRules.scoreArea(t.area)==area&&t.priority!=Priority.SOMEDAY}
   if(r.isEmpty())out[area]=100 else{
    val earned=r.sumOf{t->when(t.status){Status.COMPLETED->if(t.priority==Priority.MUST)3 else 2;Status.CANCELLED->2;else->0}}
    val possible=r.sumOf{t->if(t.priority==Priority.MUST)3 else 2}.coerceAtLeast(1)
    out[area]=((earned.toDouble()/possible)*100).roundToInt().coerceIn(0,100)
   }
  };return out
 }
 fun overall(scores:Map<Area,Int>)=ScoreWeights.values.entries.sumOf{(a,w)->(scores[a]?:100)*w}.roundToInt()
}
