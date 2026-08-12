package com.ctrl.life

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.math.roundToInt

private val Bg = Color(0xFFFAF9F6)
private val Card = Color(0xFFFFFFFF)
private val Ink = Color(0xFF171717)
private val Muted = Color(0xFF74726D)
private val Line = Color(0xFFE4E0D8)
private val Gold = Color(0xFFCDA74C)
private val GoldSoft = Color(0xFFF2EAD7)
private val Danger = Color(0xFFC62828)

enum class Tab { TODAY, PLAN, LIFE }
enum class Area { WORK, FAMILY, HOME, HEALTH, SELF_CARE, PERSONAL, LIFE_ADMIN }

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val area: Area,
    val date: String,
    val start: String,
    val minutes: Int,
    val fixed: Boolean = false,
    val protected: Boolean = false,
    val completed: Boolean = false,
    val actualMinutes: Int? = null
)

class TaskStore(context: Context) {
    private val prefs = context.getSharedPreferences("ctrl_native", Context.MODE_PRIVATE)

    fun load(): List<Task> {
        val raw = prefs.getString("tasks", null) ?: return seedTasks()
        return runCatching {
            val a = JSONArray(raw)
            (0 until a.length()).map { i ->
                val o = a.getJSONObject(i)
                Task(
                    id = o.getString("id"), title = o.getString("title"),
                    area = Area.valueOf(o.getString("area")), date = o.getString("date"),
                    start = o.getString("start"), minutes = o.getInt("minutes"),
                    fixed = o.optBoolean("fixed"), protected = o.optBoolean("protected"),
                    completed = o.optBoolean("completed"),
                    actualMinutes = if (o.has("actualMinutes") && !o.isNull("actualMinutes")) o.getInt("actualMinutes") else null
                )
            }
        }.getOrElse { seedTasks() }
    }

    fun save(tasks: List<Task>) {
        val a = JSONArray()
        tasks.forEach { t ->
            a.put(JSONObject().apply {
                put("id", t.id); put("title", t.title); put("area", t.area.name); put("date", t.date)
                put("start", t.start); put("minutes", t.minutes); put("fixed", t.fixed)
                put("protected", t.protected); put("completed", t.completed)
                put("actualMinutes", t.actualMinutes ?: JSONObject.NULL)
            })
        }
        prefs.edit().putString("tasks", a.toString()).apply()
    }

    fun learnedMinutes(title: String, fallback: Int): Int {
        return prefs.getInt("duration_${title.lowercase()}", fallback)
    }

    fun recordDuration(title: String, actual: Int, previousEstimate: Int) {
        val key = "duration_${title.lowercase()}"
        val old = prefs.getInt(key, previousEstimate)
        val learned = ((old * 0.65) + (actual * 0.35)).roundToInt().coerceAtLeast(2)
        prefs.edit().putInt(key, learned).apply()
    }

    private fun seedTasks(): List<Task> {
        val d = LocalDate.now().toString()
        return listOf(
            Task(title="Bathroom maintenance", area=Area.HOME, date=d, start="17:05", minutes=21),
            Task(title="Quick jobs", area=Area.LIFE_ADMIN, date=d, start="17:26", minutes=16),
            Task(title="Inbox sweep", area=Area.WORK, date=d, start="17:42", minutes=10),
            Task(title="Dinner", area=Area.FAMILY, date=d, start="17:52", minutes=90, protected=true),
            Task(title="Harrison bedtime", area=Area.FAMILY, date=d, start="19:30", minutes=30, fixed=true, protected=true),
            Task(title="Time with Kirsty", area=Area.FAMILY, date=d, start="20:00", minutes=120, protected=true),
            Task(title="Wind down", area=Area.SELF_CARE, date=d, start="22:10", minutes=20, protected=true)
        )
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CtrlTheme { CtrlApp() } }
    }
}

@Composable
fun CtrlTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(primary=Ink, background=Bg, surface=Card, secondary=Gold),
        typography = Typography(), content = content
    )
}

@Composable
fun CtrlApp() {
    val context = LocalContext.current
    val store = remember { TaskStore(context) }
    var tasks by remember { mutableStateOf(store.load()) }
    var tab by remember { mutableStateOf(Tab.TODAY) }
    var capture by remember { mutableStateOf(false) }
    var dayMode by remember { mutableStateOf("Normal") }
    var banner by remember { mutableStateOf("CTRL has your day. Flexible tasks will move around protected commitments.") }

    fun persist(newTasks: List<Task>) { tasks = newTasks; store.save(newTasks) }

    Scaffold(
        containerColor = Bg,
        bottomBar = { BottomBar(tab) { tab = it } },
        floatingActionButton = {
            FloatingActionButton(onClick={capture=true}, containerColor=Gold, contentColor=Color.White, shape=CircleShape) {
                Text("+", fontSize=30.sp, fontWeight=FontWeight.Light)
            }
        }
    ) { pad ->
        when(tab) {
            Tab.TODAY -> TodayScreen(tasks, store, dayMode, banner, Modifier.padding(pad),
                onTasks={persist(it)}, onMode={dayMode=it; banner="Day mode changed to $it. CTRL has rebalanced flexible tasks."})
            Tab.PLAN -> PlanScreen(tasks, Modifier.padding(pad), onTasks={persist(it)})
            Tab.LIFE -> LifeScreen(tasks, Modifier.padding(pad))
        }
    }

    if (capture) CaptureSheet(onDismiss={capture=false}) { title, area, mins ->
        val today = LocalDate.now().toString()
        val time = LocalTime.now().plusMinutes(5).format(DateTimeFormatter.ofPattern("HH:mm"))
        persist(tasks + Task(title=title, area=area, date=today, start=time, minutes=store.learnedMinutes(title, mins)))
        banner = "Added to CTRL. I found the next available flexible slot."
        capture = false
    }
}

@Composable
fun BottomBar(tab: Tab, onTab: (Tab)->Unit) {
    Surface(color=Bg, shadowElevation=10.dp) {
        Row(Modifier.fillMaxWidth().navigationBarsPadding().height(66.dp), horizontalArrangement=Arrangement.SpaceEvenly, verticalAlignment=Alignment.CenterVertically) {
            Tab.entries.forEach { t ->
                Text(t.name, modifier=Modifier.clickable{onTab(t)}.padding(20.dp), color=if(t==tab) Ink else Muted,
                    fontSize=12.sp, letterSpacing=1.2.sp, fontWeight=if(t==tab) FontWeight.Bold else FontWeight.Medium)
            }
        }
    }
}

@Composable
fun Header() {
    val d = LocalDate.now()
    Column {
        Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("CTRL", fontSize=30.sp, fontWeight=FontWeight.Bold, letterSpacing=6.sp, color=Ink)
                Text(d.format(DateTimeFormatter.ofPattern("EEEE d MMMM")), fontSize=18.sp, color=Muted)
            }
            Surface(shape=CircleShape, color=Card, border=BorderStroke(1.dp, Line), modifier=Modifier.size(52.dp)) {
                Box(contentAlignment=Alignment.Center) { Text("JH", fontWeight=FontWeight.SemiBold) }
            }
        }
    }
}

@Composable
fun TodayScreen(tasks: List<Task>, store: TaskStore, dayMode: String, banner: String, modifier: Modifier, onTasks:(List<Task>)->Unit, onMode:(String)->Unit) {
    val today = LocalDate.now().toString()
    val todays = tasks.filter{it.date==today}.sortedBy{it.start}
    val active = todays.firstOrNull{!it.completed}
    var modeMenu by remember { mutableStateOf(false) }

    LazyColumn(modifier.fillMaxSize(), contentPadding=PaddingValues(20.dp,24.dp,20.dp,120.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { Header(); Spacer(Modifier.height(12.dp)) }
        item {
            Surface(color=GoldSoft, shape=RoundedCornerShape(18.dp), border=BorderStroke(1.dp, Gold.copy(.35f))) {
                Text(banner, Modifier.padding(16.dp), color=Ink, fontSize=15.sp, lineHeight=21.sp, textAlign=TextAlign.Center)
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically) {
                Text("NOW", fontSize=13.sp, letterSpacing=2.sp, color=Muted, fontWeight=FontWeight.Bold)
                Box {
                    Text(dayMode.uppercase(), color=Gold, fontSize=11.sp, fontWeight=FontWeight.Bold, modifier=Modifier.clickable{modeMenu=true}.padding(8.dp))
                    DropdownMenu(expanded=modeMenu, onDismissRequest={modeMenu=false}) {
                        listOf("Normal","Low Energy","Family Priority","Work Crisis","Sick Day","Holiday").forEach { m ->
                            DropdownMenuItem(text={Text(m)}, onClick={modeMenu=false; onMode(m)})
                        }
                    }
                }
            }
        }
        if(active!=null) item {
            ActiveTaskCard(active,
                onDone={actual ->
                    store.recordDuration(active.title, actual, active.minutes)
                    val new = tasks.map { if(it.id==active.id) it.copy(completed=true, actualMinutes=actual) else it }
                    onTasks(reflowToday(new, today, active))
                },
                onSwap={
                    val replacement = todays.firstOrNull{!it.completed && it.id!=active.id && !it.fixed && !it.protected}
                    if(replacement!=null) {
                        val swapped = tasks.map {
                            when(it.id) {
                                active.id -> it.copy(start=replacement.start)
                                replacement.id -> it.copy(start=active.start)
                                else -> it
                            }
                        }
                        onTasks(swapped)
                    }
                }
            )
        }
        item { Text("TODAY", fontSize=13.sp, letterSpacing=2.sp, color=Muted, fontWeight=FontWeight.Bold, modifier=Modifier.padding(top=8.dp)) }
        items(todays, key={it.id}) { TaskRow(it) }
    }
}

fun reflowToday(tasks: List<Task>, date: String, finished: Task): List<Task> {
    val actual = tasks.firstOrNull{it.id==finished.id}?.actualMinutes ?: finished.minutes
    val saved = (finished.minutes-actual).coerceAtLeast(0)
    if(saved<3) return tasks
    val candidate = tasks.filter{it.date==date && !it.completed && !it.fixed && !it.protected}.sortedBy{it.start}.firstOrNull() ?: return tasks
    return tasks.map { if(it.id==candidate.id) it.copy(start=LocalTime.now().plusMinutes(1).format(DateTimeFormatter.ofPattern("HH:mm"))) else it }
}

@Composable
fun ActiveTaskCard(task: Task, onDone:(Int)->Unit, onSwap:()->Unit) {
    var remaining by remember(task.id) { mutableIntStateOf(task.minutes*60) }
    var elapsed by remember(task.id) { mutableIntStateOf(0) }
    LaunchedEffect(task.id) {
        while(remaining>0) { delay(1000); remaining--; elapsed++ }
    }
    val red = remaining<=120
    val mm=remaining/60; val ss=remaining%60
    Surface(color=Card, shape=RoundedCornerShape(28.dp), border=BorderStroke(1.dp, Line), shadowElevation=4.dp) {
        Column(Modifier.fillMaxWidth().padding(28.dp), horizontalAlignment=Alignment.CenterHorizontally) {
            Text("${task.area.name.replace('_',' ')} · ${if(task.fixed||task.protected) "PROTECTED" else "FLEXIBLE"}", color=Muted, fontSize=14.sp, textAlign=TextAlign.Center)
            Spacer(Modifier.height(10.dp))
            Text(task.title, color=Ink, fontSize=28.sp, fontWeight=FontWeight.Bold, textAlign=TextAlign.Center)
            Spacer(Modifier.height(20.dp))
            Text("%02d:%02d".format(mm,ss), color=if(red) Danger else Ink, fontSize=58.sp, fontWeight=FontWeight.Bold, textAlign=TextAlign.Center)
            Text("${task.minutes} min · hard stop", color=Muted, fontSize=16.sp, textAlign=TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) {
                Button(onClick={onDone((elapsed/60).coerceAtLeast(1))}, modifier=Modifier.weight(1f).height(58.dp), shape=RoundedCornerShape(18.dp), colors=ButtonDefaults.buttonColors(containerColor=Ink)) {
                    Text("DONE", fontWeight=FontWeight.Bold)
                }
                OutlinedButton(onClick=onSwap, modifier=Modifier.weight(1f).height(58.dp), shape=RoundedCornerShape(18.dp), border=BorderStroke(0.dp,Color.Transparent), colors=ButtonDefaults.outlinedButtonColors(containerColor=Color(0xFFEDEAE4))) {
                    Text("SWAP", color=Ink, fontWeight=FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TaskRow(task: Task) {
    Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.Top) {
        Text(task.start, color=Muted, fontSize=15.sp, modifier=Modifier.width(62.dp).padding(top=18.dp))
        Surface(color=if(task.completed) Color(0xFFF1F0EC) else Card, shape=RoundedCornerShape(20.dp), border=BorderStroke(1.dp, if(task.protected||task.fixed) Gold.copy(.65f) else Line), modifier=Modifier.weight(1f)) {
            Column(Modifier.padding(18.dp)) {
                Text(task.title, color=if(task.completed) Muted else Ink, fontSize=18.sp, fontWeight=FontWeight.SemiBold)
                Text("${task.area.name.replace('_',' ')} · ${task.actualMinutes ?: task.minutes} min${if(task.completed) " · DONE" else ""}", color=Muted, fontSize=14.sp)
            }
        }
    }
}

@Composable
fun PlanScreen(tasks: List<Task>, modifier: Modifier, onTasks:(List<Task>)->Unit) {
    var selected by remember { mutableStateOf(LocalDate.now()) }
    val days=(0..13).map{LocalDate.now().plusDays(it.toLong())}
    Column(modifier.fillMaxSize().padding(top=24.dp)) {
        Column(Modifier.padding(horizontal=20.dp)) { Header(); Spacer(Modifier.height(20.dp)); Text("14 DAY PLAN", color=Muted, fontSize=13.sp, letterSpacing=2.sp, fontWeight=FontWeight.Bold) }
        LazyRow(contentPadding=PaddingValues(20.dp), horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            items(days) { d ->
                val active=d==selected
                Surface(shape=RoundedCornerShape(18.dp), color=if(active) Ink else Card, border=BorderStroke(1.dp,if(active) Ink else Line), modifier=Modifier.clickable{selected=d}) {
                    Column(Modifier.padding(horizontal=18.dp,vertical=13.dp), horizontalAlignment=Alignment.CenterHorizontally) {
                        Text(d.format(DateTimeFormatter.ofPattern("EEE")).uppercase(), color=if(active) Color.White else Muted, fontSize=11.sp)
                        Text(d.dayOfMonth.toString(), color=if(active) Color.White else Ink, fontSize=22.sp, fontWeight=FontWeight.Bold)
                    }
                }
            }
        }
        val chosen=tasks.filter{it.date==selected.toString()}.sortedBy{it.start}
        LazyColumn(Modifier.fillMaxSize(), contentPadding=PaddingValues(20.dp,4.dp,20.dp,110.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
            if(chosen.isEmpty()) item {
                Surface(color=Card, shape=RoundedCornerShape(20.dp), border=BorderStroke(1.dp,Line)) { Text("No commitments yet. CTRL will use this capacity for flexible tasks.", Modifier.padding(20.dp), color=Muted) }
            }
            items(chosen,key={it.id}){TaskRow(it)}
        }
    }
}

@Composable
fun LifeScreen(tasks: List<Task>, modifier: Modifier) {
    val weekStart=LocalDate.now().minusDays((LocalDate.now().dayOfWeek.value-1).toLong())
    val weekEnd=weekStart.plusDays(6)
    val weekTasks=tasks.filter { runCatching { val d=LocalDate.parse(it.date); !d.isBefore(weekStart)&&!d.isAfter(weekEnd)}.getOrDefault(false) }
    val areas=listOf(Area.WORK,Area.FAMILY,Area.HEALTH,Area.HOME,Area.SELF_CARE,Area.PERSONAL)
    val scores=areas.associateWith { area ->
        val a=weekTasks.filter{it.area==area}; if(a.isEmpty()) 100 else ((a.count{it.completed}.toDouble()/a.size)*100).roundToInt()
    }
    val avg=scores.values.average().roundToInt()
    LazyColumn(modifier.fillMaxSize(), contentPadding=PaddingValues(20.dp,24.dp,20.dp,110.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { Header(); Spacer(Modifier.height(20.dp)); Text("LIFE", color=Muted, fontSize=13.sp, letterSpacing=2.sp, fontWeight=FontWeight.Bold) }
        item {
            Surface(color=Ink, shape=RoundedCornerShape(28.dp)) {
                Column(Modifier.fillMaxWidth().padding(26.dp), horizontalAlignment=Alignment.CenterHorizontally) {
                    Text("THIS WEEK", color=Color.White.copy(.65f), fontSize=12.sp, letterSpacing=2.sp)
                    Text(avg.toString(), color=Color.White, fontSize=58.sp, fontWeight=FontWeight.Bold)
                    Text("overall average", color=Color.White.copy(.65f))
                }
            }
        }
        items(areas) { area -> ScoreRow(area.name.replace('_',' '), scores[area]?:100) }
        item {
            Surface(color=GoldSoft, shape=RoundedCornerShape(20.dp), border=BorderStroke(1.dp,Gold.copy(.3f))) {
                Text("CTRL tracks 12-week trends from completed scheduled commitments. No manual scoring required.", Modifier.padding(18.dp), color=Ink, lineHeight=20.sp)
            }
        }
    }
}

@Composable
fun ScoreRow(label:String, score:Int) {
    Surface(color=Card, shape=RoundedCornerShape(20.dp), border=BorderStroke(1.dp,Line)) {
        Column(Modifier.padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween) {
                Text(label, color=Ink, fontWeight=FontWeight.SemiBold)
                Text(score.toString(), color=if(score<60) Danger else Ink, fontWeight=FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(progress={score/100f}, modifier=Modifier.fillMaxWidth().height(6.dp), color=Gold, trackColor=Color(0xFFEDEAE4))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureSheet(onDismiss:()->Unit, onAdd:(String,Area,Int)->Unit) {
    var title by remember{mutableStateOf("")}
    var area by remember{mutableStateOf(Area.LIFE_ADMIN)}
    var mins by remember{mutableIntStateOf(15)}
    ModalBottomSheet(onDismissRequest=onDismiss, containerColor=Bg) {
        Column(Modifier.fillMaxWidth().padding(22.dp).padding(bottom=30.dp)) {
            Text("CAPTURE", fontSize=13.sp, letterSpacing=2.sp, color=Muted, fontWeight=FontWeight.Bold)
            Text("Get it out of your head.", fontSize=28.sp, color=Ink, fontWeight=FontWeight.Bold, modifier=Modifier.padding(vertical=10.dp))
            OutlinedTextField(value=title,onValueChange={title=it},label={Text("What needs doing?")},modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(16.dp))
            Spacer(Modifier.height(14.dp))
            Text("AREA", color=Muted, fontSize=11.sp, fontWeight=FontWeight.Bold)
            LazyRow(horizontalArrangement=Arrangement.spacedBy(8.dp), contentPadding=PaddingValues(vertical=8.dp)) {
                items(Area.entries) { a ->
                    FilterChip(selected=area==a,onClick={area=a},label={Text(a.name.replace('_',' '))})
                }
            }
            Text("ESTIMATE", color=Muted, fontSize=11.sp, fontWeight=FontWeight.Bold)
            Row(horizontalArrangement=Arrangement.spacedBy(8.dp), modifier=Modifier.padding(vertical=8.dp)) {
                listOf(5,10,15,30,45,60).forEach { m -> FilterChip(selected=mins==m,onClick={mins=m},label={Text("$m")}) }
            }
            Button(onClick={if(title.isBlank()) ({}) else ({onAdd(title.trim(),area,mins)})}, enabled=title.isNotBlank(), modifier=Modifier.fillMaxWidth().height(58.dp), shape=RoundedCornerShape(18.dp), colors=ButtonDefaults.buttonColors(containerColor=Ink)) {
                Text("ADD TO CTRL", fontWeight=FontWeight.Bold)
            }
        }
    }
}
