package ru.deltadelete.fastikvpn.ui.pages

import android.app.Application
import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemShapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SelectableDropdownMenuItem
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.deltadelete.fastikvpn.R
import ru.deltadelete.fastikvpn.data.LocaleHelper
import ru.deltadelete.fastikvpn.data.theme.ThemeRepository
import ru.deltadelete.fastikvpn.ui.theme.AppTheme
import ru.deltadelete.fastikvpn.ui.utils.rememberGyroscopeOffset
import ru.deltadelete.fastikvpn.ui.utils.shaders.WATER_SHADER
import javax.inject.Inject
import kotlin.math.PI
import kotlin.math.exp

@Composable
fun SettingsPage(
    viewModel: SettingsViewModel = viewModel()
) {
    val currentTheme by viewModel.themeState.collectAsState()
    val themes = AppTheme.entries.filter { !it.isDynamic }

    val themeGroupTitle = stringResource(R.string.settings_group_theme)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        settingsGroup(themeGroupTitle) {
            SettingsSegmentedButtonItem(
                title = stringResource(R.string.settings_group_theme_theme_picker_title),
                options = themes,
                selectedItemProvider = { _, value -> currentTheme.toPlain() == value },
                onOptionSelected = { _, value ->
                    if (currentTheme.isDynamic) {
                        viewModel.setTheme(value.toDynamic())
                    } else {
                        viewModel.setTheme(value.toPlain())
                    }
                },
                nameProvider = { _, value -> stringResource(value.translationKey) },
                icon = { Icon(imageVector = currentTheme.icon, contentDescription = null) })

            SettingsSwitchItem(
                title = stringResource(R.string.dynamic_theme_toggle),
                summary = stringResource(R.string.dynamic_theme_toggle_desc),
                icon = { Icon(Icons.Filled.AutoMode, contentDescription = null) },
                checked = currentTheme.isDynamic
            ) {
                if (it) {
                    viewModel.setTheme(currentTheme.toDynamic())
                } else {
                    viewModel.setTheme(currentTheme.toPlain())
                }
            }

            val selectedLanguage by viewModel.selectedLanguage.collectAsState()
            val languages = viewModel.supportedLanguages
            val currentLanguage =
                languages.firstOrNull { it.tag == selectedLanguage } ?: languages.first()
            var expanded by remember { mutableStateOf(false) }

            SettingsListItem(
                modifier = Modifier.clickable { expanded = !expanded },
                leadingContent = {
                    Icon(Icons.Filled.Language, contentDescription = null)
                },
                supportingContent = {
                    ExposedDropdownMenuBox(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = expanded,
                        onExpandedChange = { expanded = it }) {
                        Text(currentLanguage.label)
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            containerColor = MenuDefaults.groupStandardContainerColor,
                            shape = MenuDefaults.standaloneGroupShape,
                        ) {
                            languages.forEachIndexed { index, language ->
                                SelectableDropdownMenuItem(
                                    text = { Text(language.label) },
                                    selected = language == currentLanguage,
                                    onClick = { viewModel.setLanguage(language.tag) },
                                    shapes = MenuDefaults.itemShape(index, languages.size),
                                    selectedLeadingIcon = {
                                        Icon(
                                            Icons.Filled.Check,
                                            modifier = Modifier.size(MenuDefaults.LeadingIconSize),
                                            contentDescription = null,
                                        )
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
            ) {
                Text(stringResource(R.string.language))
            }
        }

        settingsGroup("Shader (Experimental)") {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val shader = remember { RuntimeShader(WATER_SHADER) }
                val brush = remember { ShaderBrush(shader) }

                val rawOffset by rememberGyroscopeOffset(sensitivity = 1f)
                val smoothOffset by animateOffsetAsState(
                    targetValue = rawOffset, animationSpec = spring(stiffness = 100f)
                )
                val infiniteTransition = rememberInfiniteTransition(label = "waves")
                val time by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = (2 * PI).toFloat(),
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "time"
                )

                val colorStart = Color.Cyan
                val colorEnd = Color.Magenta

                SettingsListItem(leadingContent = {
                    Icon(
                        Icons.Filled.DeveloperMode, contentDescription = null
                    )
                }, supportingContent = {
                    Canvas(
                        modifier = Modifier
                            .size(256.dp)
                            .clip(CircleShape)
                            .onSizeChanged { size ->
                                shader.setFloatUniform(
                                    "uSize", size.width.toFloat(), size.height.toFloat()
                                )
                            }) {
                        shader.setFloatUniform("uSize", size.width, size.height)
                        shader.setFloatUniform("uTime", time)
                        shader.setFloatUniform("uGravity", -smoothOffset.x, smoothOffset.y)
                        drawCircle(brush)
                    }
                }, overlineContent = {
                    Text(rawOffset.toString())
                }) {
                    Text("Gyro")
                }
            } else {
                Text("Shaders aren't supported on your Android version.")
            }
        }
    }
}

// TODO: Рассмотреть как вариант https://github.com/alorma/Compose-Settings
fun LazyListScope.settingsGroup(
    title: String, content: @Composable (SettingsGroupScope.() -> Unit)
) {
    stickyHeader {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
    }
    item {
        val scope: SettingsGroupScope = remember { SettingsGroupScopeImpl() }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
        ) {
            scope.content()
        }
    }
}

interface SettingsGroupScope {
    var count: Int
}

class SettingsGroupScopeImpl : SettingsGroupScope {
    private var _count = mutableIntStateOf(0)

    override var count: Int
        get() = _count.intValue
        set(value) {
            _count.intValue = value
        }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsGroupScope.SettingsSwitchItem(
    title: String,
    summary: String,
    icon: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val index = remember {
        count++
    }
    SegmentedListItem(
        supportingContent = { Text(summary, style = MaterialTheme.typography.bodyMedium) },
        leadingContent = icon,
        trailingContent = {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        },
        modifier = Modifier.clickable { onCheckedChange(!checked) },
        shapes = ListItemDefaults.segmentedShapes(index, count)
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SettingsGroupScope.SettingsClickableItem(
    title: String, summary: String, icon: @Composable () -> Unit, onClick: () -> Unit
) {
    val index = remember {
        count++
    }
    SegmentedListItem(
        supportingContent = { Text(summary, style = MaterialTheme.typography.bodyMedium) },
        leadingContent = icon,
        modifier = Modifier.clickable(onClick = onClick),
        shapes = ListItemDefaults.segmentedShapes(index, count)
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <T> SettingsGroupScope.SettingsSegmentedButtonItem(
    title: String,
    options: List<T>,
    selectedItemProvider: (Int, T) -> Boolean,
    onOptionSelected: (Int, T) -> Unit,
    modifier: Modifier = Modifier,
    nameProvider: @Composable (Int, T) -> String = { _, value -> value.toString() },
    icon: @Composable (() -> Unit)? = null
) {
    val index = remember {
        count++
    }
    SegmentedListItem(
        modifier = modifier,
        leadingContent = icon,
        shapes = ListItemDefaults.segmentedShapes(index, count),
        supportingContent = {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
            ) {
                options.forEachIndexed { index, value ->
                    val isSelected = remember(selectedItemProvider, index, value) { selectedItemProvider(index, value) }
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index, count = options.size
                        ),
                        onClick = { onOptionSelected(index, value) },
                        selected = isSelected
                    ) {
                        Text(nameProvider(index, value))
                    }
                }
            }
        }) {
        Text(title)
    }
}

@Composable
fun SettingsGroupScope.SettingsListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    verticalAlignment: Alignment.Vertical = ListItemDefaults.verticalAlignment(),
    colors: ListItemColors = ListItemDefaults.segmentedColors(),
    elevation: ListItemElevation = ListItemDefaults.elevation(),
    contentPadding: PaddingValues = ListItemDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    val index = remember {
        count++
    }
    SegmentedListItem(
        ListItemDefaults.segmentedShapes(index, count),
        modifier,
        enabled,
        leadingContent,
        trailingContent,
        overlineContent,
        supportingContent,
        verticalAlignment,
        colors,
        elevation,
        contentPadding,
        content
    )
}

data class Language(val tag: String?, val label: String)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val repository: ThemeRepository
) : AndroidViewModel(application) {
    val themeState: StateFlow<AppTheme> = repository.themeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppTheme.SYSTEM
    )

    val supportedLanguages = listOf(
        Language(null, "System"),
        Language("en", "English"),
        Language("ru", "Русский")
    )

    private val _selectedLanguage = MutableStateFlow(
        LocaleHelper.getCurrentLanguageTag(application)
    )

    val selectedLanguage: StateFlow<String?> = _selectedLanguage.asStateFlow()

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            repository.saveTheme(theme)
        }
    }

    fun setLanguage(tag: String?) {
        LocaleHelper.setAppLocale(application, tag)
        _selectedLanguage.value = tag
    }
}