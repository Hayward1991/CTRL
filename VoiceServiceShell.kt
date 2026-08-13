package com.ctrl.life

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.app.NotificationCompat
import java.time.LocalTime

class VoiceServiceShell:Service(),RecognitionListener{
 private var recognizer:SpeechRecognizer?=null
 override fun onBind(intent:Intent?):IBinder?=null
 override fun onCreate(){
  super.onCreate()
  CtrlNotifications.create(this)
  val notification=NotificationCompat.Builder(this,CtrlNotifications.LISTEN)
   .setSmallIcon(android.R.drawable.ic_btn_speak_now)
   .setContentTitle("CTRL · Listening")
   .setContentText("Hey Control is active")
   .setOngoing(true).build()
  startForeground(4101,notification)
  if(SpeechRecognizer.isRecognitionAvailable(this)){
   recognizer=SpeechRecognizer.createSpeechRecognizer(this).also{it.setRecognitionListener(this)}
   listen()
  }
 }
 private fun listen(){
  recognizer?.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply{
   putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
   putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true)
  })
 }
 private fun handle(text:String){
  val marker="hey control"
  val lower=text.lowercase()
  val pos=lower.indexOf(marker)
  if(pos<0)return
  val command=text.substring(pos+marker.length).trim(' ', ',', '.')
  if(command.isBlank())return
  val p=CaptureParser.parse(command)
  val task=CtrlTask(title=p.title,area=p.area,priority=p.priority,date=p.date.toString(),start=(p.time?:LocalTime.now().plusMinutes(5)).toString().take(5),cleaning=p.cleaning,snitchEligible=p.cleaning,hardDeadline=p.hard,source="voice")
  val store=CtrlStore(this);val tasks=store.loadTasks()+task;store.saveTasks(tasks);AlarmScheduler.scheduleAll(this,tasks)
  CtrlNotifications.task(this,"Added: ${task.title}")
 }
 override fun onResults(results:Bundle?){results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.let(::handle);listen()}
 override fun onError(error:Int){listen()}
 override fun onReadyForSpeech(params:Bundle?){}
 override fun onBeginningOfSpeech(){}
 override fun onRmsChanged(rmsdB:Float){}
 override fun onBufferReceived(buffer:ByteArray?){}
 override fun onEndOfSpeech(){}
 override fun onPartialResults(partialResults:Bundle?){}
 override fun onEvent(eventType:Int,params:Bundle?){}
 override fun onDestroy(){recognizer?.destroy();super.onDestroy()}
}
