package ru.deltadelete.fastikvpn.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val bgFirstCircleFrom = Color(0xFFA855F7)
val bgFirstCircleTo = Color(0xFF7C3AED)
val bgFirstCircleGradient = Brush.linearGradient(
    colors = listOf(bgFirstCircleFrom, bgFirstCircleTo),
    start = Offset(0f, 0f),
    end = Offset(1000f, 1000f)
)

val bgSecondCircle = Color(0x598B5CF6)
val bgThirdCircle = Color(0x338B5CF6)

val bgMenuDark = Color(0xFF0D1935)
val bgMainDark = Color(0xFF0B142A)
val bgButton = Color(0xFF4A1C76)

val bgMainLight = Color(0xFFEDF1FD)
val bgMenuLight = Color(0xFFE0E1E2)

val bgCloud = Color(0xFFA855F7)
val bgCloudLinear = Color(0xFF7C3AED)
val bgButtonAddFrom = Color(0xFFA855F7)
val bgButtonAddTo = Color(0xFF7CAED)
val bgButtonGradient = Brush.linearGradient(
    colors = listOf(bgButtonAddFrom, bgButtonAddTo),
    start = Offset(0f, 0f),
    end = Offset(1000f, 1000f)
)