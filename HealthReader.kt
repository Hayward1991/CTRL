package com.ctrl.life

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.*

object HealthReader{
 suspend fun read(context:Context):HealthBridge.Snapshot{
  if(!HealthBridge.available(context))return HealthBridge.Snapshot(0,0,false)
  val client=HealthConnectClient.getOrCreate(context)
  if(!client.permissionController.getGrantedPermissions().containsAll(HealthPermissions.read))return HealthBridge.Snapshot(0,0,true)
  val zone=ZoneId.systemDefault();val now=Instant.now()
  val dayStart=LocalDate.now().atStartOfDay(zone).toInstant()
  val steps=client.aggregate(AggregateRequest(setOf(StepsRecord.COUNT_TOTAL),TimeRangeFilter.between(dayStart,now)))[StepsRecord.COUNT_TOTAL]?:0L
  val sleepStart=LocalDate.now().minusDays(1).atTime(18,0).atZone(zone).toInstant()
  val sleep=client.readRecords(ReadRecordsRequest(SleepSessionRecord::class,TimeRangeFilter.between(sleepStart,now))).records.sumOf{Duration.between(it.startTime,it.endTime).toMinutes()}
  return HealthBridge.Snapshot(steps,sleep,true)
 }
}
