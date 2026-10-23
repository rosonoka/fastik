package ru.deltadelete.fastikvpn.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import ru.deltadelete.fastikvpn.ui.theme.bgButton
import ru.deltadelete.fastikvpn.ui.theme.bgButtonAddFrom

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RowScope.NavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    enabled: Boolean = true,
    tooltip: @Composable (() -> Unit)? = null,
    colors: NavigationBarItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = bgButtonAddFrom,
        unselectedIconColor = bgButton,
        indicatorColor = Color.Transparent,
    ),
    interactionSource: MutableInteractionSource? = null,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val styledIcon =
        @Composable {
            val iconColor by animateColorAsState(
                targetValue = colors.iconColor(selected = selected, enabled = enabled),
            )
            val clearSemantics = tooltip != null && selected
            Box(modifier = if (clearSemantics) Modifier.clearAndSetSemantics {} else Modifier) {
                CompositionLocalProvider(LocalContentColor provides iconColor, content = icon)
            }
        }

    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(),
        modifier = Modifier.align(Alignment.CenterVertically)
            .aspectRatio(1f)
            .fillMaxHeight()
    ) {
        styledIcon()
    }
}

fun NavigationBarItemColors.iconColor(selected: Boolean, enabled: Boolean): Color =
    when {
        !enabled -> disabledIconColor
        selected -> selectedIconColor
        else -> unselectedIconColor
    }