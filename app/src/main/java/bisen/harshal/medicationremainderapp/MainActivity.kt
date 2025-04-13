package bisen.harshal.medicationremainderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import bisen.harshal.medicationremainderapp.domain.model.Reminder
import bisen.harshal.medicationremainderapp.presentation.MainViewModel
import bisen.harshal.medicationremainderapp.presentation.cancelAlarm
import bisen.harshal.medicationremainderapp.presentation.setUpAlarm
import bisen.harshal.medicationremainderapp.presentation.setUpPeriodicAlarm
import bisen.harshal.medicationremainderapp.presentation.ui.theme.MedicationRemainderAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedicationRemainderAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel = hiltViewModel<MainViewModel>()
                    MainScreen(viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val isTimePickerVisible = remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()

    val format = remember {
        SimpleDateFormat("hh:mm a", Locale.getDefault())
    }
    val timeInMillis = remember { mutableStateOf(0L) }

    // Enhanced color scheme
    val primaryColor = Color(0xFF5E35B1) // Deep purple primary color
    val primaryVariant = Color(0xFF3700B3) // Darker primary variant
    val accentColor = Color(0xFF00E5FF) // Bright cyan accent color
    val cardBackgroundColor = Color(0xFF2D2D3A) // Dark navy card background
    val backgroundColor = Color(0xFF121212) // Dark background for the app
    val gradientStart = Color(0xFF5E35B1)
    val gradientEnd = Color(0xFF7B1FA2)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White,
                                Color(0xFFF8F8F8)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Text(
                    text = "Add Medication Reminder",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Form(
                    time = if (timeInMillis.value > 0) format.format(timeInMillis.value) else "Select Time",
                    onTimeClick = { isTimePickerVisible.value = true },
                    onClick = { name, dosage, check ->
                        val reminder = Reminder(
                            name, dosage, timeInMillis.value, isTaken = false,
                            isRepeat = check
                        )
                        viewModel.insert(reminder)
                        if (check) {
                            setUpPeriodicAlarm(context, reminder)
                        } else {
                            setUpAlarm(context, reminder)
                        }
                        scope.launch {
                            sheetState.hide()
                        }
                    },
                    primaryColor = primaryColor,
                    accentColor = accentColor
                )
            }
        },
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetBackgroundColor = Color.White,
        sheetElevation = 16.dp
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Med Reminder",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                    },
                    backgroundColor = primaryColor,
                    elevation = 0.dp,
                    actions = {
                        IconButton(
                            onClick = { scope.launch { sheetState.show() } },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(accentColor.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Reminder",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                )
            },
            backgroundColor = backgroundColor
        ) { paddingValues ->
            if (isTimePickerVisible.value) {
                Dialog(onDismissRequest = { isTimePickerVisible.value = false }) {
                    Surface(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        elevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Select Reminder Time",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryColor,
                                modifier = Modifier.padding(bottom = 20.dp)
                            )

                            TimePicker(state = timePickerState)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = { isTimePickerVisible.value = false }
                                ) {
                                    Text("Cancel", color = Color.Gray)
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Button(
                                    onClick = {
                                        val calendar = Calendar.getInstance().apply {
                                            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                            set(Calendar.MINUTE, timePickerState.minute)
                                            set(Calendar.SECOND, 0)
                                        }
                                        timeInMillis.value = calendar.timeInMillis
                                        isTimePickerVisible.value = false
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = ButtonDefaults.elevation(4.dp)
                                ) {
                                    Text("Set Time", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (uiState.data.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            cardBackgroundColor.copy(alpha = 0.3f),
                                            backgroundColor.copy(alpha = 0.1f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_schedule),
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                tint = accentColor.copy(alpha = 0.7f)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "No Reminders Yet",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Tap the + button to add your medication reminders",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 16.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { scope.launch { sheetState.show() } },
                            colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.elevation(8.dp),
                            modifier = Modifier.padding(horizontal = 32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add Medication",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.data) { reminder ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { },
                                backgroundColor = cardBackgroundColor,
                                elevation = 8.dp
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    // Add a colored header based on repeat status
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .background(
                                                if (reminder.isRepeat)
                                                    Brush.horizontalGradient(
                                                        colors = listOf(accentColor, primaryColor)
                                                    )
                                                else
                                                    Brush.horizontalGradient(
                                                        colors = listOf(Color(0xFFFF9800), Color(0xFFE65100))
                                                    )
                                            )
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Time pill
                                        Box(
                                            modifier = Modifier
                                                .width(80.dp)
                                                .background(
                                                    color = accentColor.copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .padding(vertical = 8.dp, horizontal = 12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = format.format(reminder.timeInMillis),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = accentColor
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(16.dp))

                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(end = 16.dp)
                                        ) {
                                            Text(
                                                text = reminder.name,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Spacer(modifier = Modifier.height(6.dp))

                                            Text(
                                                text = reminder.dosage,
                                                fontSize = 14.sp,
                                                color = Color.White.copy(alpha = 0.7f),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    painter = if (reminder.isRepeat)
                                                        painterResource(id = R.drawable.ic_schedule)
                                                    else
                                                        painterResource(id = R.drawable.ic_schedule),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.dp),
                                                    tint = if (reminder.isRepeat)
                                                        accentColor.copy(alpha = 0.7f)
                                                    else
                                                        Color(0xFFFF9800).copy(alpha = 0.7f)
                                                )

                                                Spacer(modifier = Modifier.width(4.dp))

                                                Text(
                                                    text = if (reminder.isRepeat) "Repeating Daily" else "One-time Reminder",
                                                    fontSize = 12.sp,
                                                    color = if (reminder.isRepeat)
                                                        accentColor.copy(alpha = 0.7f)
                                                    else
                                                        Color(0xFFFF9800).copy(alpha = 0.7f)
                                                )
                                            }
                                        }

                                        if (reminder.isRepeat) {
                                            IconButton(
                                                onClick = {
                                                    cancelAlarm(context, reminder)
                                                    viewModel.update(reminder.copy(isTaken = true, isRepeat = false))
                                                },
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .background(accentColor.copy(alpha = 0.1f))
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Mark as Taken",
                                                    tint = accentColor,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(8.dp))
                                        }

                                        IconButton(
                                            onClick = {
                                                cancelAlarm(context, reminder)
                                                viewModel.delete(reminder)
                                            },
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(Color.Red.copy(alpha = 0.1f))
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_delete),
                                                contentDescription = "Delete",
                                                tint = Color.Red.copy(alpha = 0.8f),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Add bottom spacing
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }

                    // Add floating action button for quick access
                    FloatingActionButton(
                        onClick = { scope.launch { sheetState.show() } },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(24.dp)
                            .size(56.dp),
                        backgroundColor = primaryColor,
                        contentColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Reminder",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Form(
    time: String,
    onTimeClick: () -> Unit,
    onClick: (String, String, Boolean) -> Unit,
    primaryColor: Color,
    accentColor: Color
) {
    val name = remember { mutableStateOf("") }
    val dosage = remember { mutableStateOf("") }
    val isChecked = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            label = { Text("Medication Name") },
            placeholder = { Text("e.g., Amoxicillin", Modifier.alpha(0.6f)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor,
                focusedLabelColor = primaryColor,
                cursorColor = primaryColor
            )
        )

        OutlinedTextField(
            value = dosage.value,
            onValueChange = { dosage.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            label = { Text("Dosage") },
            placeholder = { Text("e.g., 1 pill, 5ml", Modifier.alpha(0.6f)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor,
                focusedLabelColor = primaryColor,
                cursorColor = primaryColor
            )
        )

        OutlinedTextField(
            value = time,
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { onTimeClick.invoke() },
            enabled = false,
            label = { Text("Reminder Time") },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_schedule),
                    contentDescription = "Select Time",
                    tint = primaryColor
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Color.Black,
                disabledBorderColor = Color.Gray,
                disabledLabelColor = Color.Gray
            )
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color(0xFFF5F5F5),
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Repeating Schedule",
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Medication reminder will repeat daily",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Switch(
                    checked = isChecked.value,
                    onCheckedChange = { isChecked.value = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = primaryColor,
                        checkedTrackAlpha = 1f
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (name.value.isNotBlank() && dosage.value.isNotBlank() && time != "Select Time") {
                    onClick.invoke(name.value, dosage.value, isChecked.value)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = primaryColor,
                disabledBackgroundColor = primaryColor.copy(alpha = 0.4f)
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            ),
            enabled = name.value.isNotBlank() && dosage.value.isNotBlank() && time != "Select Time"
        ) {
            Text(
                text = "Save Reminder",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}