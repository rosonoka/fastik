package ru.deltadelete.fastikvpn.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.deltadelete.fastikvpn.R
import ru.deltadelete.fastikvpn.ui.theme.InterFontFamily
import ru.deltadelete.fastikvpn.ui.theme.bgFirstCircleGradient
import ru.deltadelete.fastikvpn.ui.theme.bgSecondCircle
import ru.deltadelete.fastikvpn.ui.theme.bgThirdCircle

@Composable
fun HomePage() {
    var connected by remember { mutableStateOf(false) }
    var enabled by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.8f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .clip(CircleShape)
                    .background(bgThirdCircle)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
                    .clip(CircleShape)
                    .background(bgSecondCircle)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(60.dp)
                    .clip(CircleShape)
                    .background(brush = bgFirstCircleGradient)
                    .clickable(enabled) {
                        connected = !connected
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Wifi,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.desc_connect_button),
                    modifier = Modifier.fillMaxSize(0.5f)
                )
            }
        }
        Text(
            if (connected) stringResource(R.string.state_connected) else stringResource(R.string.state_not_connected),
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
    }
}