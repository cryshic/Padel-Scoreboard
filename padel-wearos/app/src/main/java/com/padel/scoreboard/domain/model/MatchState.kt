package com.padel.scoreboard.domain.model

/**
 * Represents a single padel game point value.
 *
 * Padel (and tennis) scoring: 0 → 15 → 30 → 40 → GAME
 * GAME is treated as a sentinel that triggers game completion logic.
 */
enum class PadelPoint(val display: String) {
    ZERO("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    GAME("GAME");  // Triggers game-won logic

    /** Move one step forward. Returns null if already at GAME. */
    fun next(): PadelPoint? = when (this) {
        ZERO     -> FIFTEEN
        FIFTEEN  -> THIRTY
        THIRTY   -> FORTY
        FORTY    -> GAME
        GAME     -> null
    }

    /** Move one step backward. Returns null if already at ZERO. */
    fun previous(): PadelPoint? = when (this) {
        ZERO     -> null
        FIFTEEN  -> ZERO
        THIRTY   -> FIFTEEN
        FORTY    -> THIRTY
        GAME     -> FORTY
    }
}

/**
 * Represents one team's state within the current game.
 *
 * Future-ready fields (commented) can be uncommented for
 * set counter, serve indicator, and match history features.
 */
data class TeamState(
    val name: String,
    val currentPoint: PadelPoint = PadelPoint.ZERO,
    val gamesWon: Int = 0,
    // val setsWon: Int = 0,           // Future: set counter
    // val isServing: Boolean = false,  // Future: serve indicator
) {
    /** Display-ready point string for the scoreboard UI. */
    val pointDisplay: String get() = currentPoint.display
}

/**
 * The overall match state — single source of truth for the UI.
 *
 * Holds both teams and match-level metadata.
 * Future extensions are stubbed below.
 */
data class MatchState(
    val team1: TeamState = TeamState(name = "Team 1"),
    val team2: TeamState = TeamState(name = "Team 2"),
    val isMatchActive: Boolean = true,

    // ── Future-ready fields ──────────────────────────────────────────
    // val matchTimerSeconds: Long = 0,     // Future: match timer
    // val currentSet: Int = 1,             // Future: set tracking
    // val matchHistory: List<GameRecord> = emptyList(), // Future: history
) {
    companion object {
        /** Create a fresh match with default team names. */
        fun newMatch(
            team1Name: String = "Team 1",
            team2Name: String = "Team 2"
        ) = MatchState(
            team1 = TeamState(name = team1Name),
            team2 = TeamState(name = team2Name)
        )
    }
}
