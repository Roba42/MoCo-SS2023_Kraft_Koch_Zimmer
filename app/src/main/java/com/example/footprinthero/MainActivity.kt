package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Footprint Hero", style = MaterialTheme.typography.h5)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            FortbewegungsListe(onVehicleSelected = { /* TODO */ })
            FortbewegungsDauer(onDurationChanged = { /* TODO */ })
            CO2Berechnung()
            WochentagsUebersicht()
            WochenUebersicht()
        }
    }
}

@Composable
fun FortbewegungsListe(onVehicleSelected: (String) -> Unit) {
    val vehicles = listOf("Auto", "Fahrrad", "Flugzeug")
    val (selectedVehicle, setSelectedVehicle) = remember { mutableStateOf("Auto") }
    Column(Modifier.padding(16.dp)) {
        Text(text = "Fortbewegungsmittel", style = MaterialTheme.typography.h6)
        for (vehicle in vehicles) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                RadioButton(
                    selected = (vehicle == selectedVehicle),
                    onClick = {
                        setSelectedVehicle(vehicle)
                        onVehicleSelected(vehicle)
                    }
                )
                Text(
                    text = vehicle,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun FortbewegungsDauer(onDurationChanged: (Int) -> Unit) {
    val duration = remember { mutableStateOf(0) }
    Column(Modifier.padding(16.dp)) {
        Text(text = "Fortbewegungsdauer (in Minuten)", style = MaterialTheme.typography.h6)
        TextField(
            value = duration.value.toString(),
            onValueChange = {
                val newValue = it.toIntOrNull() ?: 0
                duration.value = newValue
                onDurationChanged(newValue)
            },
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun CO2Berechnung() {
    val (co2, setCo2) = remember { mutableStateOf(0f) }
    val fortbewegungsmittelCo2 = mapOf("Auto" to 0.3f, "Fahrrad" to 0.0f, "Flugzeug" to 2.0f)
    val selectedFortbewegungsmittel = remember { mutableStateOf("Auto") }
    val dauer = remember { mutableStateOf(0) }

    Column(Modifier.padding(16.dp)) {
        Text(text = "CO2-Berechnung", style = MaterialTheme.typography.h6)

        Row(Modifier.padding(top = 8.dp)) {
            Text(text = "Gewähltes Fortbewegungsmittel: ${selectedFortbewegungsmittel.value}")
        }

        Row(Modifier.padding(top = 8.dp)) {
            Text(text = "Dauer der Fortbewegung: ${dauer.value} Minuten")
        }

        Button(
            onClick = {
                val co2Emission = fortbewegungsmittelCo2[selectedFortbewegungsmittel.value] ?: 0f
                setCo2(co2Emission * dauer.value)
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Berechnen")
        }

        Row(Modifier.padding(top = 8.dp)) {
            Text(text = "CO2-Emission: $co2 kg")
        }
    }
}

@Composable
fun WochentagsUebersicht() {
    val days = listOf("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag")
    val (selectedDay, setSelectedDay) = remember { mutableStateOf(0) }

    Column(Modifier.padding(16.dp)) {
        Text(text = "Wochentagsübersicht", style = MaterialTheme.typography.h6)

        for ((index, day) in days.withIndex()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                RadioButton(
                    selected = (index == selectedDay),
                    onClick = {
                        setSelectedDay(index)
                    }
                )
                Text(
                    text = day,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun WochenUebersicht() {
    val (selectedWeek, setSelectedWeek) = remember { mutableStateOf(0) }

    Column(Modifier.padding(16.dp)) {
        Text(text = "Wochenübersicht", style = MaterialTheme.typography.h6)

        for (week in 1..52) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                RadioButton(
                    selected = (week == selectedWeek),
                    onClick = {
                        setSelectedWeek(week)
                    }
                )
                Text(
                    text = "KW $week",
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}


