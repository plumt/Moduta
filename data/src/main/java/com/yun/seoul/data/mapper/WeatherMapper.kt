package com.yun.seoul.data.mapper

import com.yun.seoul.data.helper.WeatherHelper.Companion.weatherCompare
import com.yun.seoul.data.helper.WeatherHelper.Companion.weatherDetail
import com.yun.seoul.data.helper.WeatherHelper.Companion.weatherDust
import com.yun.seoul.data.helper.WeatherHelper.Companion.weatherIcon
import com.yun.seoul.data.helper.WeatherHelper.Companion.weatherState
import com.yun.seoul.data.helper.WeatherHelper.Companion.weatherTemperature
import com.yun.seoul.data.helper.WeatherHelper.Companion.weatherUDust
import com.yun.seoul.data.helper.WeatherHelper.Companion.weatherUV
import com.yun.seoul.domain.model.weather.NowWeather
import com.yun.seoul.domain.model.weather.WeatherState
import org.jsoup.nodes.Document

class WeatherMapper {

    companion object {

        // document를 NowWeather 모델로 변환
        fun extractWeatherData(document: Document, weatherImageUrl: String): NowWeather {
            with(document) {
                val detailMap = parseWeatherDetail(weatherDetail())
                return NowWeather(
                    state = weatherState(),
                    temperature = extractNumericAndUnits(weatherTemperature()),
                    url = weatherIcon(weatherImageUrl),
//                weatherDetail = doc.weatherDetail(),
                    feelTemperature = extractNumericAndUnits(
                        detailMap["feelTemperature"] ?: ""
                    ),
                    humidity = extractNumericAndUnits(detailMap["humidity"] ?: ""),
                    windSpeed = extractNumericAndUnits(detailMap["windSpeed"] ?: ""),
                    dust = mapToDustState(weatherDust()).value,
                    uDust = mapToDustState(weatherUDust()).value,
                    uv = mapToUVState(weatherUV()).value,
                    compare = weatherCompare()
                )
            }
        }

        // 미세먼지 수치 매핑 후 반환
        private fun mapToDustState(dustString: String?): WeatherState {
            if (dustString.isNullOrEmpty()) return WeatherState.UNKNOWN

            val statePattern = Regex("(좋음|보통|나쁨|매우\\s*나쁨)")
            val stateMatch = statePattern.find(dustString)
            val stateStr = stateMatch?.value?.replace("\\s+".toRegex(), "")

            return when {
                stateStr == null -> WeatherState.UNKNOWN
                stateStr.contains("좋음") -> WeatherState.GOOD
                stateStr.contains("보통") -> WeatherState.NORMAL
                stateStr.contains("매우") -> WeatherState.WORST
                stateStr.contains("나쁨") -> WeatherState.BAD
                else -> WeatherState.UNKNOWN
            }
        }

        // 자외선 수치 매핑 후 반환
        private fun mapToUVState(dustString: String?): WeatherState {
            if (dustString.isNullOrEmpty()) return WeatherState.UNKNOWN

            val statePattern = Regex("(좋음|보통|높음|매우\\s*높음)")
            val stateMatch = statePattern.find(dustString)
            val stateStr = stateMatch?.value?.replace("\\s+".toRegex(), "")

            return when {
                stateStr == null -> WeatherState.UNKNOWN
                stateStr.contains("좋음") -> WeatherState.GOOD
                stateStr.contains("보통") -> WeatherState.NORMAL
                stateStr.contains("매우") -> WeatherState.WORST
                stateStr.contains("높음") -> WeatherState.BAD
                else -> WeatherState.UNKNOWN
            }
        }

        // 체감 온도, 습도, 풍향 및 풍속 매핑 후 수치 반환
        private fun parseWeatherDetail(weatherDetail: String): Map<String, String?> {
            // 체감온도 추출
            val feelTempPattern = Regex("체감\\s*([\\d.]+)°")
            val feelTempMatch = feelTempPattern.find(weatherDetail)
            val feelTemp = feelTempMatch?.groupValues?.get(1)?.plus("°")

            // 습도 추출
            val humidityPattern = Regex("습도\\s+([\\d.]+)%")
            val humidityMatch = humidityPattern.find(weatherDetail)
            val humidity = humidityMatch?.groupValues?.get(1)?.plus("%")

            // 풍향 및 풍속 추출
            val windPattern = Regex("(동풍|서풍|남풍|북풍|북동풍|북서풍|남동풍|남서풍)\\s*([\\d.]+)m/s")
            val windMatch = windPattern.find(weatherDetail)
            val windDirection = windMatch?.groupValues?.get(1)
            val windSpeed = windMatch?.groupValues?.get(2)?.plus("m/s")

            return mapOf(
                "feelTemperature" to feelTemp,    // 체감온도 변수명
                "humidity" to humidity,           // 습도 변수명
                "windDirection" to windDirection, // 풍향 변수명
                "windSpeed" to windSpeed          // 풍속 변수명
            )
        }

        // 숫자, 소수점, 도 기호, 알파벳(m/s 등을 위해)만 추출
        fun extractNumericAndUnits(text: String): String {
            val pattern = Regex("([\\d.]+[°%]|[\\d.]+m/s)")
            val match = pattern.find(text)
            return match?.value ?: ""
        }
    }
}