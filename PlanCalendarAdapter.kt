package com.ctrl.life

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable fun PlanScreenV1(tasks:List<CtrlTask>,pad:PaddingValues){
 val context=LocalContext.current
 PlanScreenV1(tasks,CalendarBridge.read(context),pad)
}
