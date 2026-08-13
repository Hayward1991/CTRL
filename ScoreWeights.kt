package com.ctrl.life

object ScoreWeights {
 val values=mapOf(
  Area.FAMILY to .25,
  Area.SELF_CARE to .20,
  Area.HEALTH to .15,
  Area.HOME to .15,
  Area.WORK to .15,
  Area.PERSONAL to .10
 )
}

object PriorityOrder {
 fun rank(t:CtrlTask):Int {
  if(t.fixed)return 1000
  if(t.protected)return 950
  if(t.hardDeadline)return 900
  if(t.mandatoryToday)return 800
  if(t.area==Area.HEALTH&&t.priority==Priority.MUST)return 700
  return when(t.priority){Priority.MUST->600;Priority.TARGET->400;Priority.SOMEDAY->100}
 }
}
