package com.ctrl.life

object CtrlRules {
 const val BUFFER_MIN=5
 const val SNITCH_DELAY_MIN=15
 const val WARNING_MIN=5
 const val DELAYED_AFTER_MIN=5
 const val WASH_CYCLE_MIN=50
 const val BREAKFAST_MIN=15
 const val LUNCH_MIN=20
 const val DINNER_MIN=45
 const val DINNER_COOK_MIN=30
 const val SHOWER_TEETH_MIN=8
 const val SKINCARE_MIN=5
 const val SHAVE_MIN=7
 const val BATH_MIN=75
 const val DRESS_MIN=12
 const val TRAINING_MIN=60
 const val TRAINING_WEEKLY_TARGET=4
 const val ARRIVAL_BUFFER_MIN=10
 const val GET_READY_MIN=15
 fun scoreArea(a:Area)=if(a==Area.LIFE_ADMIN)Area.PERSONAL else a
}
