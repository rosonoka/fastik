package ru.deltadelete.fastikvpn

import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.serialization.Serializable
import ru.deltadelete.fastikvpn.ui.pages.HomePage
import ru.deltadelete.fastikvpn.ui.navigation.BottomBar
import ru.deltadelete.fastikvpn.ui.pages.SettingsPage
import ru.deltadelete.fastikvpn.ui.pages.SettingsViewModel
import ru.deltadelete.fastikvpn.ui.theme.FastikVPNTheme
import ru.deltadelete.fastikvpn.ui.theme.InterFontFamily

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuildContent()
        }
    }
}

@Composable
@Preview
fun BuildContent() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val currentTheme by settingsViewModel.themeState.collectAsState()

    FastikVPNTheme(currentTheme) {
        val controller = rememberNavController()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TitleBar() },
            bottomBar = { BottomBar(controller) }
        ) { innerPadding ->
            NavHost(
                controller,
                startDestination = HomeRoute,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<HomeRoute> {
                    HomePage()
                }
                composable<ServersRoute> {
                    // TODO: Скорее всего хранить по профилям/подпискам в Room, читать через ViewModel, содержащий Repository или другой бойлерплейт
                    Greeting(
                        name = "Servers screen"
                    )
                }
                composable<SettingsRoute> {
                    SettingsPage(settingsViewModel)
                }
            }
        }
    }
}

@Serializable
object HomeRoute

@Serializable
object ServersRoute

@Serializable
object SettingsRoute

// TODO: Перейти на `dev.olshevski.navigation:reimagined`
data class TopLevelDestination<T : Any>(
    val label: @Composable (() -> String),
    val icon: ImageVector,
    val route: T,
)

val navBarItems = listOf(
    TopLevelDestination(
        { stringResource(R.string.app_name) }, Icons.Filled.Home,
        HomeRoute
    ),
    TopLevelDestination({ stringResource(R.string.app_name) }, Icons.Filled.Person, ServersRoute),
    TopLevelDestination({ stringResource(R.string.app_name) }, Icons.Filled.Settings, SettingsRoute)
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TitleBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        title = {
            Text(
                stringResource(R.string.app_name),
                color = Color(0xFF8442AA),
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFontFamily
            )
        },
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
