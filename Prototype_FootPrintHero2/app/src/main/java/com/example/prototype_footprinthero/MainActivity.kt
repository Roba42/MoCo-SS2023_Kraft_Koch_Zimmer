package com.example.prototype_footprinthero

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.example.prototype_footprinthero.ui.theme.Prototype_FootPrintHeroTheme
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val vehicles = listOf("Auto", "Fahrrad", "Flugzeug")
    val selectedVehicle = mutableStateOf("Auto")
    val duration = mutableStateOf(0)
    val co2 = mutableStateOf(0f)

    fun onVehicleSelected(vehicle: String) {
        selectedVehicle.value = vehicle
    }

    fun onDurationChanged(duration: Int) {
        this.duration.value = duration
    }

    fun calculateCO2() {
        val transportationCO2 = mapOf("Auto" to 0.3f, "Fahrrad" to 0.0f, "Flugzeug" to 2.0f)
        val co2Emission = transportationCO2[selectedVehicle.value] ?: 0f
        val calculatedCo2 = co2Emission * duration.value
        if (!calculatedCo2.isNaN()) {
            co2.value = calculatedCo2
        }
    }
}

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Prototype_FootPrintHeroTheme {
                Surface(color = Color.White) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val viewModel = MainViewModel() // Create an instance of MainViewModel
    Prototype_FootPrintHeroTheme {
        MainScreen(viewModel = viewModel) // Pass viewModel to MainScreen
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Footprint Hero", style = MaterialTheme.typography.h5)
                    }
                },
                backgroundColor = Color(0xFF214001),
                elevation = 0.dp
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                backgroundColor = Color(0xFF214001),
                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            TransportationList(
                vehicles = viewModel.vehicles,
                selectedVehicle = viewModel.selectedVehicle.value,
                onVehicleSelected = viewModel::onVehicleSelected
            )
            TransportationDuration(
                duration = viewModel.duration.value,
                onDurationChanged = viewModel::onDurationChanged
            )
            CO2Calculation(
                co2 = viewModel.co2.value,
                onCalculateCO2 = viewModel::calculateCO2
            )
            WeekdayOverview()
            WeeklyOverview()
        }
    }
}

@Composable
fun TransportationList(
    vehicles: List<String>,
    selectedVehicle: String,
    onVehicleSelected: (String) -> Unit) {
    val vehicles = listOf("Auto", "Fahrrad", "Flugzeug")
    var selectedVehicle by remember { mutableStateOf("Auto") }

    Column(Modifier.padding(16.dp)) {
        Text(text = "Fortbewegungsmittel", style = MaterialTheme.typography.h6)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF9ABF15))
        ) {
            DropdownMenu(
                expanded = false,
                onDismissRequest = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                vehicles.forEach { vehicle ->
                    DropdownMenuItem(
                        onClick = {
                            selectedVehicle = vehicle
                            onVehicleSelected(vehicle)
                        }
                    ) {
                        Text(text = vehicle)
                    }
                }
            }
            Text(
                text = selectedVehicle,
                style = MaterialTheme.typography.body1.merge(),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun TransportationDuration(
    duration: Int,
    onDurationChanged: (Int) -> Unit) {
    var duration by remember { mutableStateOf(0) }

    Column(Modifier.padding(16.dp)) {
        Text(text = "Fortbewegungsdauer (in Minuten)", style = MaterialTheme.typography.h6)

        Box(Modifier.padding(top = 8.dp)) {
            Slider(
                value = duration.toFloat(),
                onValueChange = { newValue ->
                    duration = newValue.toInt()
                    onDurationChanged(duration)
                },
                valueRange = 0f..120f,
                steps = 1,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = duration.toString(),
                style = MaterialTheme.typography.body1.merge(),
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun CO2Calculation(
    co2: Float,
    onCalculateCO2: () -> Unit) {
    var co2 by remember { mutableStateOf(0f) }
    val transportationCO2 = mapOf("Auto" to 0.3f, "Fahrrad" to 0.0f, "Flugzeug" to 2.0f)
    val selectedTransportation by remember { mutableStateOf("Auto") }
    val duration by remember { mutableStateOf(0) }

    Column(Modifier.padding(16.dp)) {
        Text(text = "CO2-Berechnung", style = MaterialTheme.typography.h6)

        Row(Modifier.padding(top = 8.dp)) {
            Text(text = "Gewähltes Fortbewegungsmittel: $selectedTransportation")
        }

        Row(Modifier.padding(top = 8.dp)) {
            Text(text = "Dauer der Fortbewegung: $duration Minuten")
        }

        Button(
            onClick = {
                val co2Emission = transportationCO2[selectedTransportation] ?: 0f
                val calculatedCo2 = co2Emission * duration
                if (!calculatedCo2.isNaN()) {
                    co2 = calculatedCo2
                }
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp)
                .clip(MaterialTheme.shapes.medium),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9ABF15))
        ) {
            Text(text = "Berechnen")
        }

        Row(Modifier.padding(top = 8.dp)) {
            Text(text = "CO2-Emission: ${co2.takeIf { it.isFinite() } ?: 0f} kg")
        }
    }
}

@Composable
fun WeeklyOverview() {
    val weeks = (1..52).toList()
    var selectedWeek by remember { mutableStateOf(0) }

    Column(Modifier.padding(16.dp)) {
        Text(text = "Wochenübersicht", style = MaterialTheme.typography.h6)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD4D93D))
        ) {
            DropdownMenu(
                expanded = false,
                onDismissRequest = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                weeks.forEach { week ->
                    DropdownMenuItem(
                        onClick = {
                            selectedWeek = week
                        }
                    ) {
                        Text(text = "KW $week")
                    }
                }
            }
            if (selectedWeek != 0) {
                Text(
                    text = "KW $selectedWeek",
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterStart)
                )
            }
        }
    }
}

@Composable
fun WeekdayOverview() {
    val days = listOf("Mo", "Di", "Mi", "Do", "Fr", "Sa", "So")

    val co2Data = remember {
        mutableStateListOf(
            BarData("Mo", 0f),
            BarData("Di", 0f),
            BarData("Mi", 0f),
            BarData("Do", 0f),
            BarData("Fr", 0f),
            BarData("Sa", 0f),
            BarData("So", 0f)
        )
    }

    Column(Modifier.padding(16.dp)) {
        Text(
            text = "Wochentagsübersicht",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(Modifier.fillMaxWidth()) {
            co2Data.forEach { data ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = data.label)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFF467302))
                    ) {
                        val height = data.value / 10f
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height.dp)
                                .background(Color.White)
                        )
                    }
                    Text(
                        text = data.value.toString(),
                        style = MaterialTheme.typography.body1.merge(),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

data class BarData(val label: String, val value: Float)

