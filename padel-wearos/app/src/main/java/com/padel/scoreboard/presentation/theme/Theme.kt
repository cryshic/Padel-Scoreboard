package com.padel.scoreboard.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Typography

// ── Brand / Sporty Palette ────────────────────────────────────────────────────

/** Deep black background — maximises AMOLED battery savings on Pixel Watch. */
val ScoreboardBackground = Color(0xFF000000)

/** Pure white for primary text — maximum contrast outdoors. */
val ScoreboardOnBackground = Color(0xFFFFFFFF)

/** Neon green accent — "+", game counters, active elements. */
val AccentGreen = Color(0xFF39FF14)

/** Soft coral for "−" / undo actions. */
val AccentRed = Color(0xFFFF4444)

/** Mid-grey surface for team cards. */
val SurfaceDark = Color(0xFF1A1A1A)

/** Lighter surface for button fills. */
val SurfaceMid = Color(0xFF2C2C2E)

/** Muted text for secondary labels. */
val TextSecondary = Color(0xFF8E8E93)

/** Score highlight: large point number in accent color. */
val ScoreHighlight = Color(0xFFFFFFFF)

// ── Wear OS Material Color Scheme ─────────────────────────────────────────────

val ScoreboardColors = Colors(
    primary           = AccentGreen,
    primaryVariant    = Color(0xFF28CC0C),
    secondary         = AccentRed,
    secondaryVariant  = Color(0xFFCC2222),
    background        = ScoreboardBackground,
    surface           = SurfaceDark,
    error             = Color(0xFFFF6B6B),
    onPrimary         = Color(0xFF000000),
    onSecondary       = Color(0xFFFFFFFF),
    onBackground      = ScoreboardOnBackground,
    onSurface         = ScoreboardOnBackground,
    onSurfaceVariant  = TextSecondary,
    onError           = Color(0xFF000000),
)

// ── Typography — big, bold, outdoor-readable ──────────────────────────────────

val ScoreboardTypography = Typography(
    // Main score number — must be readable at arm's length
    display1 = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize   = 40.sp,
        letterSpacing = 2.sp,
    ),
    // Games counter
    display2 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 28.sp,
    ),
    // Team name
    title1 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize   = 14.sp,
        letterSpacing = 0.5.sp,
    ),
    // Button labels
    button = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize   = 18.sp,
    ),
    // Secondary labels
    caption1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize   = 11.sp,
    ),
)

// ── App Theme Wrapper ─────────────────────────────────────────────────────────

@Composable
fun ScoreboardTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors     = ScoreboardColors,
        typography = ScoreboardTypography,
        content    = content,
    )
}
