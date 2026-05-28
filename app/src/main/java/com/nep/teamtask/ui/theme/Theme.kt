package com.nep.teamtask.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = TeamTaskBlueLight,
    onPrimary = TeamTaskSlate900,
    primaryContainer = TeamTaskBlueDark,
    onPrimaryContainer = TeamTaskBlueLight,

    secondary = TeamTaskSlate300,
    onSecondary = TeamTaskSlate900,
    secondaryContainer = TeamTaskSlate800,
    onSecondaryContainer = TeamTaskSlate100,

    background = TeamTaskDarkBackground,
    onBackground = TeamTaskSlate100,

    surface = TeamTaskDarkSurface,
    onSurface = TeamTaskSlate100,

    surfaceVariant = TeamTaskDarkSurfaceVariant,
    onSurfaceVariant = TeamTaskSlate300,

    outline = TeamTaskSlate700,
    outlineVariant = TeamTaskSlate800,

    error = TeamTaskErrorContainer,
    onError = TeamTaskSlate900,
    errorContainer = TeamTaskError,
    onErrorContainer = TeamTaskErrorContainer
)


private val LightColorScheme = lightColorScheme(
    primary = TeamTaskBlue,
    onPrimary = Color.White,
    primaryContainer = TeamTaskBlueLight,
    onPrimaryContainer = TeamTaskBlueDark,

    secondary = TeamTaskSlate600,
    onSecondary = Color.White,
    secondaryContainer = TeamTaskSlate100,
    onSecondaryContainer = TeamTaskSlate800,

    background = TeamTaskSlate50,
    onBackground = TeamTaskSlate900,

    surface = Color.White,
    onSurface = TeamTaskSlate900,

    surfaceVariant = TeamTaskSlate100,
    onSurfaceVariant = TeamTaskSlate600,

    outline = TeamTaskSlate300,
    outlineVariant = TeamTaskSlate200,

    error = TeamTaskError,
    onError = Color.White,
    errorContainer = TeamTaskErrorContainer,
    onErrorContainer = TeamTaskError
)

/**
 * TeamTask Shapes
 *
 * 企业app中，按钮、卡片、输入框的圆角要统一
 *
 * @property extraSmall
 * @property small
 * @property medium
 * @property large
 * @property extraLarge
 */
private val TeamTaskShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)


/**
 * TeamTask Spacing
 *
 * MaterialTheme 默认没有 spacing 字段。
 * 企业项目通常会自己封装一套 spacing token，
 * 避免页面里到处散落 4.dp、8.dp、16.dp。
 */
data class TeamTaskSpacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 24.dp,
    val screenPadding: Dp = 32.dp
)

private val LocalTeamTaskSpacing = staticCompositionLocalOf {
    TeamTaskSpacing()
}

/**
 * 扩展主题入口
 *
 * 使用方式:
 * TeamTaskTheme.spacing.large
 */
object TeamTaskTheme {
    val spacing: TeamTaskSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalTeamTaskSpacing.current
}

@Composable
fun TeamTaskAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme

        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalTeamTaskSpacing provides TeamTaskSpacing()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = TeamTaskTypography,
            shapes = TeamTaskShapes,
            content = content
        )
    }
}