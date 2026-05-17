package com.padel.scoreboard.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.padel.scoreboard.domain.model.TeamState
import com.padel.scoreboard.presentation.theme.AccentGreen
import com.padel.scoreboard.presentation.theme.AccentRed
import com.padel.scoreboard.presentation.theme.SurfaceDark
import com.padel.scoreboard.presentation.theme.TextSecondary

/**
 * TeamScorePanel
 *
 * Displays one team's section of the scoreboard:
 *   ┌─────────────────────────┐
 *   │  TEAM 1                 │
 *   │       30         [+] [−]│
 *   │  Games: 2               │
 *   └─────────────────────────┘
 *
 * The score number animates with a slide when it changes.
 *
 * @param teamState   Current state for this team
 * @param onIncrement Called when "+" is tapped
 * @param onDecrement Called when "−" is tapped
 * @param modifier    Optional modifier
 */
@Composable
fun TeamScorePanel(
    teamState: TeamState,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .padding(horizontal = 6.dp, vertical = 4.dp)
    ) {
        // ── Team Name ────────────────────────────────────────────────
        Text(
            text      = teamState.name.uppercase(),
            color     = TextSecondary,
            fontSize  = 10.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            letterSpacing = 1.5.sp,
        )

        Spacer(modifier = Modifier.height(2.dp))

        // ── Score + Buttons row ───────────────────────────────────────
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Animated score number
            AnimatedContent(
                targetState   = teamState.pointDisplay,
                transitionSpec = {
                    // Slide up when incrementing, slide down when decrementing
                    slideInVertically { height -> -height } togetherWith
                    slideOutVertically { height -> height }
                },
                label = "score_anim",
                modifier = Modifier.weight(1f),
            ) { scoreText ->
                Text(
                    text      = scoreText,
                    color     = if (scoreText == "GAME") AccentGreen else androidx.compose.ui.graphics.Color.White,
                    fontSize  = if (scoreText == "GAME") 18.sp else 32.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                )
            }

            // Increment / decrement buttons stacked vertically
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ScoreButton(
                    label   = "+",
                    onClick = onIncrement,
                    color   = AccentGreen,
                    size    = 36.dp,
                )
                ScoreButton(
                    label   = "−",
                    onClick = onDecrement,
                    color   = AccentRed,
                    size    = 36.dp,
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // ── Games counter ─────────────────────────────────────────────
        Text(
            text     = "Games: ${teamState.gamesWon}",
            color    = TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}
