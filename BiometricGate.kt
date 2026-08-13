package com.ctrl.life

import android.app.Activity
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal

object BiometricGate{
 fun unlock(activity:Activity,onUnlocked:()->Unit){
  val prompt=BiometricPrompt.Builder(activity)
   .setTitle("Unlock CTRL")
   .setSubtitle("Confirm it’s you")
   .setNegativeButton("Cancel",activity.mainExecutor){_,_->activity.finish()}
   .build()
  prompt.authenticate(CancellationSignal(),activity.mainExecutor,object:BiometricPrompt.AuthenticationCallback(){
   override fun onAuthenticationSucceeded(result:BiometricPrompt.AuthenticationResult?){onUnlocked()}
  })
 }
}
