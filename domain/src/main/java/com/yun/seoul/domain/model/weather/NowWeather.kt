package com.yun.seoul.domain.model.weather

data class NowWeather(
    var location: String? = null,               // 날씨 주소
    val state: String? = null,                  // 날씨 상태(맑음, 흐림 등)
    val temperature: String? = null,            // 현재 온도
    val url: String? = null,                    // 날씨 이미지
//    val weatherDetail: String? = null,
    val feelTemperature: String? = null,        // 체감온도
    val humidity: String? = null,               // 습도
    val windSpeed: String? = null,              // 풍속
    val dust: Int? = null,                      // 미세먼지
    val uDust: Int? = null,                     // 초미세먼지
    val uv: Int? = null,                        // 자외선
    val compare: String? = null,                // 어제 비교

//    val hourly: Hourly? = null,
//    val weekly: Weekly? = null,
) {
    fun dustToString() = when(dust) {
        WeatherState.GOOD.value -> "좋음"
        WeatherState.NORMAL.value -> "보통"
        WeatherState.BAD.value -> "매우 나쁨"
        WeatherState.WORST.value -> "나쁨"
        else -> "알 수 없음"
    }

    fun uDustToString() = when(uDust) {
        WeatherState.GOOD.value -> "좋음"
        WeatherState.NORMAL.value -> "보통"
        WeatherState.BAD.value -> "나쁨"
        WeatherState.WORST.value -> "매우 나쁨"
        else -> "알 수 없음"
    }

    fun uvToString() = when(uv) {
        WeatherState.GOOD.value -> "좋음"
        WeatherState.NORMAL.value -> "보통"
        WeatherState.BAD.value -> "높음"
        WeatherState.WORST.value -> "매우 높음"
        else -> "알 수 없음"
    }
}
