package com.ctrl.life

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

@Composable fun CtrlApp(){
 val context=LocalContext.current
 val store=remember{CtrlStore(context)}
 var tasks by remember{mutableStateOf(store.loadTasks().toList())}
 var tab by remember{mutableStateOf(Tab.TODAY)}
 var capture by remember{mutableStateOf(false)}
 var message by remember{mutableStateOf("CTRL has your day.")}
 var over by remember{mutableIntStateOf(0)}
 fun save(next:List<CtrlTask>){
  val prepared=RoutineLocks.apply(RoutineFactory.unlockDependencies(next))
  val planned=Planner.planDay(prepared,CalendarBridge.read(context),LocalDate.now())
  tasks=planned.tasks;over=planned.overCapacityMinutes;store.saveTasks(tasks);AlarmScheduler.scheduleAll(context,tasks)
 }
 LaunchedEffect(Unit){
  save(RoutineFactory.ensureToday(tasks,store,LocalDate.now().dayOfWeek.value in 1..5))
  val synced=withContext(Dispatchers.IO){runCatching{CloudSync.reconcile(context,tasks)}.getOrDefault(tasks)}
  save(synced)
 }
 Scaffold(containerColor=CtrlBg,bottomBar={CtrlBottom(tab){tab=it}},floatingActionButton={if(tab==Tab.TODAY)FloatingActionButton({capture=true}){Text("+")}}){pad->
  when(tab){
   Tab.TODAY->TodayScreenV1(tasks,over,message,pad,onTasks={save(it)},store=store)
   Tab.PLAN->PlanScreenV1(tasks,pad)
   Tab.LIFE->LifeScreenV1(tasks,pad)
  }
 }
 if(capture)CaptureV1(onDismiss={capture=false}){raw->
  val p=CaptureParser.parse(raw)
  val t=CtrlTask(title=p.title,area=p.area,priority=p.priority,date=p.date.toString(),start=(p.time?:java.time.LocalTime.now().plusMinutes(5)).toString().take(5),cleaning=p.cleaning,snitchEligible=p.cleaning,hardDeadline=p.hard,source="manual")
  save(tasks+t);message="Added. CTRL found the next available slot.";capture=false
 }
}
