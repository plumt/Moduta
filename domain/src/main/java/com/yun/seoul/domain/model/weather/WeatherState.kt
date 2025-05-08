package com.yun.seoul.domain.model.weather

enum class WeatherState(val label: String, val value: Int) {
    GOOD("좋음", 1),
    NORMAL("보통", 2),
    BAD("나쁨", 3),
    WORST("매우 나쁨", 4),
    UNKNOWN("알 수 없음", 0);

    companion object {
        fun fromValue(value: Int): WeatherState = entries.find { it.value == value } ?: UNKNOWN
    }

}