package com.example.weather

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = this
            var selectedCity by remember { mutableStateOf(citiesList[0])}
            var cityExpanded by remember { mutableStateOf(false)}
            var selectedPeriod by remember { mutableStateOf( "today")}
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(254, 230, 223)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Погода Gismeteo",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(150, 54, 28),
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        )

                        CitySelector (
                            selectedCity,
                            citiesList,
                            {newCity -> selectedCity = newCity}
                        )

                        PeriodSelector (
                            selectedPeriod,
                            periodsList,
                            { newPeriod -> selectedPeriod = newPeriod }
                        )

                        LaunchButton (
                            onClick = { openGismeteo(context, selectedCity, selectedPeriod)}
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun CitySelector (
    selectedCity: CityInfo,
    citiesList: List<CityInfo>,
    onCitySelected: (CityInfo) -> Unit
) {
    var cityExpanded by remember { mutableStateOf(false)}
    Text(
        text = "Выберите город:",
        style = MaterialTheme.typography.bodyLarge,
        color = Color(150, 54, 28),
        fontFamily = FontFamily.Serif
    )
    Spacer(modifier = Modifier.height(8.dp))
    Box{
        Button(
            onClick = { cityExpanded = true},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(150, 54, 28),
                contentColor = Color(254, 230, 223)
            )) {
            Text(
                text = selectedCity.displayName,
                fontFamily = FontFamily.Serif
            )
        }
        DropdownMenu(
            expanded = cityExpanded,
            onDismissRequest = { cityExpanded = false },
            modifier = Modifier.background(Color(254, 230, 223))
        ) {
            citiesList.forEach { city ->
                DropdownMenuItem(
                    text = { Text(city.displayName, fontFamily = FontFamily.Serif, color = Color(150, 54, 28)) },
                    onClick = {
                        onCitySelected(city)
                        cityExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PeriodSelector (
    selectedPeriod: String,
    periodsList: List<PeriodInfo>,
    onPeriodSelected: (String) -> Unit

) {
    Spacer(modifier = Modifier.height(24.dp))
    Text("Выберите период:", style = MaterialTheme.typography.bodyLarge, fontFamily = FontFamily.Serif, color = Color(150, 54, 28),)
    Spacer(modifier = Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
        periodsList.forEach { period ->
            FilterChip(
                selected = selectedPeriod == period.urlName,
                onClick = { onPeriodSelected(period.urlName) },
                label = { Text (period.displayName)},
                colors = FilterChipDefaults.filterChipColors(
                    selectedLabelColor = Color(254, 230, 223),
                    selectedContainerColor = Color(150, 54, 28),

                    containerColor = Color(254, 230, 223),
                    labelColor = Color(150, 54, 28)
                )
            )
        }
    }
}

@Composable
fun LaunchButton (
    onClick: () -> Unit
) {
    Spacer(modifier = Modifier.height(40.dp))
    Button(
        onClick = onClick,
        modifier = Modifier.widthIn(min = 200.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF5D4037),
            contentColor = Color.White
        )
    ) {
        Text("Запустить", style = MaterialTheme.typography.titleMedium, fontFamily = FontFamily.Serif)
    }
}


fun openGismeteo (context: Context, city: CityInfo, period: String) {
    val url = "https://www.gismeteo.ru/weather-${city.urlName}-${city.cityId}/${period}/"
    val uri = url.toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)
    try {
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "Браузер не найден!", Toast.LENGTH_LONG).show()
        }
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Ошибка открытия браузера", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Произошла ошибка: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

