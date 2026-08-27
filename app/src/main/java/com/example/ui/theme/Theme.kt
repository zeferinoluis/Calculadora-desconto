package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = SkyBlue80,
    onPrimary = Slate950,
    primaryContainer = Slate800,
    onPrimaryContainer = SkyBlueLight,
    secondary = Indigo80,
    onSecondary = Slate950,
    secondaryContainer = Slate700,
    onSecondaryContainer = Slate100,
    tertiary = Emerald80,
    onTertiary = Slate950,
    background = Slate900,
    onBackground = Slate100,
    surface = Slate800,
    onSurface = Slate100,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate300,
    outline = Slate600
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SkyBlue40,
    onPrimary = Slate50,
    primaryContainer = SkyBlueLight,
    onPrimaryContainer = Slate900,
    secondary = Indigo40,
    onSecondary = Slate50,
    secondaryContainer = Slate100,
    onSecondaryContainer = Slate900,
    tertiary = Emerald40,
    onTertiary = Slate50,
    background = Slate50,
    onBackground = Slate900,
    surface = Slate100,
    onSurface = Slate900,
    surfaceVariant = Slate200,
    onSurfaceVariant = Slate700,
    outline = Slate400
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
