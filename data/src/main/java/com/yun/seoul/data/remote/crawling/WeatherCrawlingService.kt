package com.yun.seoul.data.remote.crawling

import org.jsoup.Jsoup

object WeatherCrawlingService {
    fun weatherSearch(
        location: String,
        url: String,
    ) = Jsoup.connect("${url}${location}날씨")
}