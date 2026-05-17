package com.padel.scoreboard.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text

/**
 * ScoreButton
 *
 * A circular, touch-friendly button for "+"/"-" score actions.
 * Features:
 *  - Press-scale animation (shrinks slightly on press, bounces back)
 *  - Minimum 44dp tap target (accessibility best practice)
 *  - Configurable color and size
 *
 * @param label    Text displayed inside the button (typically "+" or "−")
 * @param onClick  Callback when tapped
 * @param color    Button fill color
 * @param size     Diameter of the button
 * @param modifier Optional modifier
 */
@Composable
fun ScoreButton(
    label: String,
    onClick: () -> Unit,
    color: Color,
    size: Dp = 44.dp,
    modifier: Modifier = Modifier,
) {
    // ── Press animation state ────────────────────────────────────────────────
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.82f else 1f,
        animationSpec = tween(durationMillis = 80),
        label = "button_scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .scale(scale)
            .clip(CircleShape)
            .background(color)
            .pointerInput(onClick) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            }
    ) {
        Text(
            text       = label,
            color      = Color.Black,
            fontWeight = FontWeight.Black,
            fontSize   = (size.value * 0.45f).sp,
        )
    }
}
