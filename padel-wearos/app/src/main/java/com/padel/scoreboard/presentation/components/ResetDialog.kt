package com.padel.scoreboard.presentation.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.padel.scoreboard.presentation.theme.AccentGreen
import com.padel.scoreboard.presentation.theme.AccentRed
import com.padel.scoreboard.presentation.theme.SurfaceDark

/**
 * ResetConfirmDialog
 *
 * Minimal in-place confirmation UI for resetting the match.
 * Shown over the main screen — no navigation needed.
 *
 * Tap ✓ to confirm, ✗ to cancel.
 */
@Composable
fun ResetConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Text(
            text      = "Reset Match?",
            color     = Color.White,
            fontSize  = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text     = "All scores will be cleared",
            color    = Color(0xFF8E8E93),
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Cancel
            Button(
                onClick = onDismiss,
                colors  = ButtonDefaults.buttonColors(backgroundColor = SurfaceDark),
                modifier = Modifier.weight(1f),
            ) {
                Text("✗", color = AccentRed, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            // Confirm
            Button(
                onClick = onConfirm,
                colors  = ButtonDefaults.buttonColors(backgroundColor = AccentGreen),
                modifier = Modifier.weight(1f),
            ) {
                Text("✓", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
