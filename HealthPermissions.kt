package com.ctrl.life

import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord

object HealthPermissions{
 val read=setOf(
  HealthPermission.getReadPermission(StepsRecord::class),
  HealthPermission.getReadPermission(SleepSessionRecord::class)
 )
}
