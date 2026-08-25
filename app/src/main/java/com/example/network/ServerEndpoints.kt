package com.example.network

import com.example.model.ServerLocation

object ServerEndpoints {
    val CLOUDFLARE_ANYCAST = ServerLocation(
        id = "cf_anycast",
        name = "Cloudflare Global Edge",
        regionName = "Auto Nearest Regional Edge",
        country = "Global Anycast",
        countryCode = "🌐",
        flagEmoji = "⚡",
        provider = "Cloudflare CDN Edge",
        downloadBaseUrl = "https://speed.cloudflare.com/__down",
        uploadUrl = "https://speed.cloudflare.com/__up",
        pingUrl = "https://speed.cloudflare.com/__down?bytes=0",
        isEdgeAnycast = true
    )

    val SERVERS_LIST = listOf(
        CLOUDFLARE_ANYCAST,
        ServerLocation(
            id = "sg_singapore",
            name = "Singapore Edge (SIN)",
            regionName = "Southeast Asia",
            country = "Singapore",
            countryCode = "SG",
            flagEmoji = "🇸🇬",
            provider = "Cloudflare Edge",
            downloadBaseUrl = "https://speed.cloudflare.com/__down",
            uploadUrl = "https://speed.cloudflare.com/__up",
            pingUrl = "https://speed.cloudflare.com/__down?bytes=0"
        ),
        ServerLocation(
            id = "in_mumbai",
            name = "Mumbai Edge (BOM)",
            regionName = "South Asia",
            country = "India",
            countryCode = "IN",
            flagEmoji = "🇮🇳",
            provider = "Cloudflare Edge",
            downloadBaseUrl = "https://speed.cloudflare.com/__down",
            uploadUrl = "https://speed.cloudflare.com/__up",
            pingUrl = "https://speed.cloudflare.com/__down?bytes=0"
        ),
        ServerLocation(
            id = "jp_tokyo",
            name = "Tokyo Edge (NRT)",
            regionName = "East Asia",
            country = "Japan",
            countryCode = "JP",
            flagEmoji = "🇯🇵",
            provider = "Cloudflare Edge",
            downloadBaseUrl = "https://speed.cloudflare.com/__down",
            uploadUrl = "https://speed.cloudflare.com/__up",
            pingUrl = "https://speed.cloudflare.com/__down?bytes=0"
        ),
        ServerLocation(
            id = "de_frankfurt",
            name = "Frankfurt Edge (FRA)",
            regionName = "Central Europe",
            country = "Germany",
            countryCode = "DE",
            flagEmoji = "🇩🇪",
            provider = "Cloudflare Edge",
            downloadBaseUrl = "https://speed.cloudflare.com/__down",
            uploadUrl = "https://speed.cloudflare.com/__up",
            pingUrl = "https://speed.cloudflare.com/__down?bytes=0"
        ),
        ServerLocation(
            id = "uk_london",
            name = "London Edge (LHR)",
            regionName = "Western Europe",
            country = "United Kingdom",
            countryCode = "GB",
            flagEmoji = "🇬🇧",
            provider = "Cloudflare Edge",
            downloadBaseUrl = "https://speed.cloudflare.com/__down",
            uploadUrl = "https://speed.cloudflare.com/__up",
            pingUrl = "https://speed.cloudflare.com/__down?bytes=0"
        ),
        ServerLocation(
            id = "us_ashburn",
            name = "US East - Virginia (IAD)",
            regionName = "North America",
            country = "United States",
            countryCode = "US",
            flagEmoji = "🇺🇸",
            provider = "Cloudflare Edge",
            downloadBaseUrl = "https://speed.cloudflare.com/__down",
            uploadUrl = "https://speed.cloudflare.com/__up",
            pingUrl = "https://speed.cloudflare.com/__down?bytes=0"
        ),
        ServerLocation(
            id = "us_sanjose",
            name = "US West - Silicon Valley (SJC)",
            regionName = "North America",
            country = "United States",
            countryCode = "US",
            flagEmoji = "🇺🇸",
            provider = "Cloudflare Edge",
            downloadBaseUrl = "https://speed.cloudflare.com/__down",
            uploadUrl = "https://speed.cloudflare.com/__up",
            pingUrl = "https://speed.cloudflare.com/__down?bytes=0"
        ),
        ServerLocation(
            id = "au_sydney",
            name = "Sydney Edge (SYD)",
            regionName = "Oceania",
            country = "Australia",
            countryCode = "AU",
            flagEmoji = "🇦🇺",
            provider = "Cloudflare Edge",
            downloadBaseUrl = "https://speed.cloudflare.com/__down",
            uploadUrl = "https://speed.cloudflare.com/__up",
            pingUrl = "https://speed.cloudflare.com/__down?bytes=0"
        ),
        ServerLocation(
            id = "ae_dubai",
            name = "Dubai Edge (DXB)",
            regionName = "Middle East",
            country = "United Arab Emirates",
            countryCode = "AE",
            flagEmoji = "🇦🇪",
            provider = "Cloudflare Edge",
            downloadBaseUrl = "https://speed.cloudflare.com/__down",
            uploadUrl = "https://speed.cloudflare.com/__up",
            pingUrl = "https://speed.cloudflare.com/__down?bytes=0"
        )
    )
}
