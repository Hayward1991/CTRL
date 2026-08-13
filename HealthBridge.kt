package com.ctrl.life

import android.content.Context
import androidx.health.connect.client.HealthConnectClient

object HealthBridge{
 data class Snapshot(val steps:Long,val sleepMinutes:Long,val available:Boolean)
 fun available(context:Context)=HealthConnectClient.getSdkStatus(context)==HealthConnectClient.SDK_AVAILABLE
}
