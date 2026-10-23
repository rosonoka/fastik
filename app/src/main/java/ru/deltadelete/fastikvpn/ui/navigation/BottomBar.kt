package ru.deltadelete.fastikvpn.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.deltadelete.fastikvpn.navBarItems
import ru.deltadelete.fastikvpn.ui.theme.bgButton
import ru.deltadelete.fastikvpn.ui.theme.bgButtonAddFrom

@Composable
fun BottomBar(controller: NavHostController = rememberNavController()) {
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Box(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .height(80.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            navBarItems.forEach { destination ->
                val isSelected = currentDestination?.hierarchy?.any {
                    it.hasRoute(destination.route::class)
                } == true

                NavigationItem(
                    selected = isSelected,
                    onClick = {
                        controller.navigate(destination.route) {
                            popUpTo(controller.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = destination.label()
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = bgButtonAddFrom,
                        unselectedIconColor = bgButton,
                        indicatorColor = Color.Transparent,
                        selectedTextColor = Color.Transparent,
                        unselectedTextColor = Color.Transparent
                    ),
                    tooltip = { Text(destination.label()) },
                )
            }
        }
    }
}