package com.widgetkit.feature.config

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.widgetkit.core.ui.components.PrimaryButton
import com.widgetkit.core.ui.components.WidgetPreviewCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    widgetType: String,
    existingWidgetId: String?,
    onBack: () -> Unit,
    onPlaced: () -> Unit,
    viewModel: ConfigViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(widgetType, existingWidgetId) {
        viewModel.initialize(widgetType, existingWidgetId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configure ${widgetType.replaceFirstChar { it.uppercase() }}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(8.dp))

            WidgetPreviewCard {
                WidgetPreview(
                    state = state,
                    widgetType = widgetType
                )
            }

            Spacer(Modifier.height(16.dp))

            when (widgetType) {
                "clock" -> ClockConfigForm(
                    config = state.clockConfig,
                    onConfigChange = { viewModel.updateClockConfig(it) }
                )
                "note" -> NoteConfigForm(
                    config = state.noteConfig,
                    error = state.validationError,
                    onConfigChange = { viewModel.updateNoteConfig(it) }
                )
                "countdown" -> CountdownConfigForm(
                    config = state.countdownConfig,
                    error = state.validationError,
                    onConfigChange = { viewModel.updateCountdownConfig(it) }
                )
            }

            Spacer(Modifier.height(24.dp))

            PrimaryButton(
                text = "Add to Home Screen",
                enabled = state.isValid,
                onClick = {
                    viewModel.saveAndPlace(existingWidgetId)
                    onPlaced()
                }
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}
