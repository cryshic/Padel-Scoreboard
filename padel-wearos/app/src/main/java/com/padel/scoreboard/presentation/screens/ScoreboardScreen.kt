package com.padel.scoreboard.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.padel.scoreboard.domain.usecase.TeamId
import com.padel.scoreboard.presentation.ScoreboardViewModel
import com.padel.scoreboard.presentation.components.ResetConfirmDialog
import com.padel.scoreboard.presentation.components.TeamScorePanel

/**
 * ScoreboardScreen
 *
 * The main and only screen of the app.
 *
 * Layout on the round Pixel Watch display:
 *
 *   ┌───────────────────────────┐
 *   │  ⏱ 10:42               │  ← TimeText (top arc)
 *   │  ┌──────────┐            │
 *   │  │ TEAM 1   │            │
 *   │  │  30  + − │            │
 *   │  │ Games: 1 │            │
 *   │  └──────────┘            │
 *   │  ────── VS ──────        │
 *   │  ┌──────────┐            │
 *   │  │ TEAM 2   │            │
 *   │  │  15  + − │            │
 *   │  │ Games: 0 │            │
 *   │  └──────────┘            │
 *   │       [RESET]            │
 *   └───────────────────────────┘
 *
 * Vignette: applied at top+bottom for circular display aesthetics.
 * TimeText: shown in the top arc — required by Wear OS UX guidelines.
 */
@Composable
fun ScoreboardScreen(viewModel: ScoreboardViewModel) {
    val matchState     by viewModel.matchState.collectAsState()
    val showResetDialog by viewModel.showResetDialog.collectAsState()

    Scaffold(
        timeText = { TimeText() },   // Clock in top arc
        vignette = {
            // Circular vignette — fades edges for round Pixel Watch display
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // ── Main scoreboard content ──────────────────────────────
            if (!showResetDialog) {
                ScalingLazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                ) {
                    // ── Team 1 Panel ─────────────────────────────────
                    item {
                        TeamScorePanel(
                            teamState   = matchState.team1,
                            onIncrement = { viewModel.onIncrement(TeamId.TEAM1) },
                            onDecrement = { viewModel.onDecrement(TeamId.TEAM1) },
                            modifier    = Modifier.fillMaxWidth(),
                        )
                    }

                    // ── "VS" separator ───────────────────────────────
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(Color(0xFF2C2C2E))
                            )
                            Text(
                                text       = " VS ",
                                color      = Color(0xFF555558),
                                fontSize   = 9.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(Color(0xFF2C2C2E))
                            )
                        }
                    }

                    // ── Team 2 Panel ─────────────────────────────────
                    item {
                        TeamScorePanel(
                            teamState   = matchState.team2,
                            onIncrement = { viewModel.onIncrement(TeamId.TEAM2) },
                            onDecrement = { viewModel.onDecrement(TeamId.TEAM2) },
                            modifier    = Modifier.fillMaxWidth(),
                        )
                    }

                    // ── Reset button ──────────────────────────────────
                    item {
                        Spacer(Modifier.height(4.dp))
                        androidx.wear.compose.material.Button(
                            onClick = { viewModel.onRequestReset() },
                            colors  = androidx.wear.compose.material.ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF1C1C1E)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp),
                        ) {
                            Text(
                                text      = "RESET MATCH",
                                color     = Color(0xFF8E8E93),
                                fontSize  = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp,
                            )
                        }
                    }
                }
            }

            // ── Reset Confirmation Dialog ────────────────────────────
            AnimatedVisibility(
                visible = showResetDialog,
                enter   = scaleIn() + fadeIn(),
                exit    = scaleOut() + fadeOut(),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xCC000000))  // Dim overlay
                ) {
                    ResetConfirmDialog(
                        onConfirm = { viewModel.onConfirmReset() },
                        onDismiss = { viewModel.onDismissReset() },
                    )
                }
            }
        }
    }
}
