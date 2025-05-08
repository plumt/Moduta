package com.yun.seoul.data.helper

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject

class WeatherHelper {

    companion object {
        /**
         * 날씨 아이콘
         */
        fun Document.weatherIcon(weatherImageUrl: String): String {
            val result = select(".weather_main i")
            return result.firstOrNull()?.let { element ->
                val url = element.className()
                val urlConvert =
                    url.replace("wt_", "").replace("ico_", "flat_").replace(" ", "_") + ".svg"
                weatherImageUrl + urlConvert
            } ?: ""
        }

        /**
         * 날씨 상태
         */
        fun Document.weatherState() = select(".weather_main").firstOrNull()?.text() ?: ""

        /**
         * 날씨 온도
         */
        fun Document.weatherTemperature() = select(".temperature_text").firstOrNull()?.text() ?: ""

        /**
         * 날씨 상세
         */
        fun Document.weatherDetail() =
            select(".temperature_info .summary_list").firstOrNull()?.text() ?: ""

        fun Document.weatherDust() = select(".today_chart_list .item_today")?.get(0)?.text() ?: ""
        fun Document.weatherUDust() = select(".today_chart_list .item_today")?.get(1)?.text() ?: ""

        fun Document.weatherUV() = select(".today_chart_list .item_today")?.get(2)?.text() ?: ""

        /**
         * 어제보다 높거나 낮은 정도
         */
        fun Document.weatherCompare() =
            select(".temperature_info .temperature").firstOrNull()?.text() ?: ""

        fun Document.week() = select(".weekly_forecast_area._toggle_panel .week_item")
        fun Element.dayOfWeek() = select(".cell_date .day").text() ?: ""
        fun Element.date() =
            select(".cell_date .date").text()
                .let { it.substring(0, it.length - 1).replace(".", "/") }
                ?: ""

        fun Element.amPrecipitation() =
            select(".cell_weather .weather_left")[0].select(".rainfall").text() ?: ""

        fun Element.pmPrecipitation() =
            select(".cell_weather .weather_left")[1].select(".rainfall").text() ?: ""

        fun Element.lowTemperature() =
            select(".cell_temperature .lowest").text().replace("기온", "") ?: ""

        fun Element.highTemperature() =
            select(".cell_temperature .highest").text().replace("기온", "") ?: ""

        fun Element.amImg() = select(".cell_weather .weather_inner i")[0].className() ?: ""
        fun Element.pmImg() = select(".cell_weather .weather_inner i")[1].className() ?: ""

        fun Document.hour() = select(".forecast_wrap._selectable_tab .hourly_forecast._tab_content")
        fun Element.weatherTime() = select(".weather_graph_box .time")
        fun Element.weatherNumber() = select(".weather_graph_box .num")
        fun Element.weatherInfo() = select(".weather_graph_box .blind")
        fun Element.weatherIcon() = select(".weather_box .wt_icon")
    }
}