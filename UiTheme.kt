package com.ctrl.life

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val CtrlBg=Color(0xFFFAF9F6)
val CtrlCard=Color.White
val CtrlInk=Color(0xFF171717)
val CtrlMuted=Color(0xFF74726D)
val CtrlLine=Color(0xFFE4E0D8)
val CtrlGold=Color(0xFFCDA74C)
val CtrlSoft=Color(0xFFF2EAD7)
val CtrlDanger=Color(0xFFC62828)

@Composable fun CtrlTheme(content:@Composable()->Unit){
 MaterialTheme(colorScheme=lightColorScheme(primary=CtrlInk,background=CtrlBg,surface=CtrlCard,secondary=CtrlGold,error=CtrlDanger),content=content)
}
