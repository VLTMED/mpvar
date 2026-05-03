package app.marlboroadvance.mpvar.ui.utils

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.NavBackStack
import app.marlboroadvance.mpvar.presentation.Screen

val LocalBackStack: ProvidableCompositionLocal<NavBackStack<Screen>> =
  compositionLocalOf { error("LocalBackStack not initialized!") }
