package com.padel.scoreboard.presentation

import android.app.Application
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.padel.scoreboard.domain.model.MatchState
import com.padel.scoreboard.domain.usecase.DecrementScoreUseCase
import com.padel.scoreboard.domain.usecase.IncrementScoreUseCase
import com.padel.scoreboard.domain.usecase.ResetMatchUseCase
import com.padel.scoreboard.domain.usecase.TeamId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ScoreboardViewModel
 *
 * Single source of truth for the scoreboard UI.
 * Uses use cases for all business logic — keeps the ViewModel thin.
 *
 * Key responsibilities:
 *  - Hold and expose [MatchState] as a StateFlow
 *  - Debounce rapid taps to prevent accidental double-scoring
 *  - Trigger haptic feedback on every score change
 *  - Expose reset and confirm-reset dialog state
 */
class ScoreboardViewModel(application: Application) : AndroidViewModel(application) {

    // ── Use Cases ────────────────────────────────────────────────────────────
    private val incrementScore = IncrementScoreUseCase()
    private val decrementScore = DecrementScoreUseCase()
    private val resetMatch     = ResetMatchUseCase()

    // ── UI State ─────────────────────────────────────────────────────────────
    private val _matchState = MutableStateFlow(MatchState.newMatch())
    val matchState: StateFlow<MatchState> = _matchState.asStateFlow()

    /** Controls whether the reset-confirmation dialog is shown. */
    private val _showResetDialog = MutableStateFlow(false)
    val showResetDialog: StateFlow<Boolean> = _showResetDialog.asStateFlow()

    // ── Anti-double-tap guard ─────────────────────────────────────────────────
    /**
     * When true, score button taps are ignored.
     * Set to true after each tap, cleared after [DEBOUNCE_MS] milliseconds.
     */
    private var isDebouncing = false
    private companion object {
        const val DEBOUNCE_MS = 500L  // 500 ms debounce window
    }

    // ── Vibrator (haptic feedback) ────────────────────────────────────────────
    private val vibrator: Vibrator? by lazy {
        val ctx = application.applicationContext
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            (ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)
                ?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /** Called when the "+" button is tapped for a team. */
    fun onIncrement(teamId: TeamId) {
        if (isDebouncing) return
        triggerDebounce()
        _matchState.value = incrementScore(_matchState.value, teamId)
        vibrateScore()
    }

    /** Called when the "−" button is tapped for a team. */
    fun onDecrement(teamId: TeamId) {
        if (isDebouncing) return
        triggerDebounce()
        _matchState.value = decrementScore(_matchState.value, teamId)
        vibrateCorrection()
    }

    /** Shows the reset-confirmation dialog. */
    fun onRequestReset() {
        _showResetDialog.value = true
    }

    /** Confirmed: reset the match. */
    fun onConfirmReset() {
        _matchState.value = resetMatch(_matchState.value)
        _showResetDialog.value = false
        vibrateReset()
    }

    /** Dismissed: cancel reset. */
    fun onDismissReset() {
        _showResetDialog.value = false
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Blocks further taps for [DEBOUNCE_MS] ms.
     * Uses a coroutine so it auto-cancels if the ViewModel is cleared.
     */
    private fun triggerDebounce() {
        isDebouncing = true
        viewModelScope.launch {
            delay(DEBOUNCE_MS)
            isDebouncing = false
        }
    }

    /** Short single pulse — point scored. */
    private fun vibrateScore() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(60L, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(60L)
        }
    }

    /** Lighter pulse — score corrected downward. */
    private fun vibrateCorrection() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(30L, 80))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(30L)
        }
    }

    /** Double pulse — match reset. */
    private fun vibrateReset() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val pattern = VibrationEffect.createWaveform(
                longArrayOf(0, 80, 100, 80), -1
            )
            vibrator?.vibrate(pattern)
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(longArrayOf(0, 80, 100, 80), -1)
        }
    }
}
