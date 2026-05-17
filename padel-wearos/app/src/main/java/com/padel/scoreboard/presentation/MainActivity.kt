package com.padel.scoreboard.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.padel.scoreboard.presentation.screens.ScoreboardScreen
import com.padel.scoreboard.presentation.theme.ScoreboardTheme

/**
 * MainActivity — the sole Activity for this Wear OS app.
 *
 * Responsibilities:
 *  - Set FLAG_KEEP_SCREEN_ON so the watch doesn't dim during play
 *  - Host the Compose content tree
 *  - Provide the ScoreboardViewModel via viewModels()
 *
 * NOTE: When you want to allow the screen to sleep again (e.g. match paused),
 * you can toggle FLAG_KEEP_SCREEN_ON dynamically based on MatchState.isMatchActive.
 * That's left as a TODO for the match-timer future feature.
 */
class MainActivity : ComponentActivity() {

    // ViewModel scoped to this Activity's lifecycle
    private val viewModel: ScoreboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep screen awake during the match — critical for outdoor use
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            WearApp(viewModel = viewModel)
        }
    }

    override fun onPause() {
        super.onPause()
        // Optional: clear KEEP_SCREEN_ON when app goes to background
        // window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onResume() {
        super.onResume()
        // Re-apply when returning to the app
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

/**
 * Root Composable — wraps everything in the scoreboard theme.
 */
@Composable
fun WearApp(viewModel: ScoreboardViewModel) {
    ScoreboardTheme {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            ScoreboardScreen(viewModel = viewModel)
        }
    }
}
