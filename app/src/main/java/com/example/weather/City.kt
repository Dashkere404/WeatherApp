package com.example.weather

data class CityInfo (
    val displayName: String,
    val urlName: String,
    val cityId: Int
)

val citiesList = listOf(
    CityInfo ("Москва", "moscow", 4368),
    CityInfo ("Санкт-Петербург", "sankt-peterburg", 4079),
    CityInfo ("Нижний Новгород", "nizhny-novgorod", 4355),
    CityInfo ("Сочи", "sochi", 5233),
    CityInfo ("Новосибирск", "novosibirsk", 4690),
    CityInfo ("Тольятти", "tolyatti", 4429)
)

data class PeriodInfo (
    val displayName: String,
    val urlName: String
)

val periodsList = listOf(
    PeriodInfo ("Сегодня", "today"),
    PeriodInfo ("Завтра", "tomorrow"),
    PeriodInfo ("3 дня", "3-days"),
    PeriodInfo ("10 дней", "10-days")
)