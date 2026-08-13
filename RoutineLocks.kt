package com.ctrl.life

object RoutineLocks{
 private val keys=setOf("morning_care","breakfast","lunch","cook_dinner","dinner","evening_care","shave","bath")
 fun apply(tasks:List<CtrlTask>)=tasks.map{t->if(t.area==Area.FAMILY||t.recurrenceKey in keys)t.copy(protected=true) else t}
}
