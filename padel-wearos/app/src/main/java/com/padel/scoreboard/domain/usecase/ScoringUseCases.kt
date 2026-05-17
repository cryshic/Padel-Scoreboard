package com.padel.scoreboard.domain.usecase

import com.padel.scoreboard.domain.model.MatchState
import com.padel.scoreboard.domain.model.PadelPoint
import com.padel.scoreboard.domain.model.TeamState

/**
 * Identifies which team to operate on.
 */
enum class TeamId { TEAM1, TEAM2 }

/**
 * UseCase: Increment a team's score by one padel point.
 *
 * If the team reaches GAME:
 *   - Increment that team's games-won counter
 *   - Reset both teams' points to ZERO for the next game
 *
 * This is a pure function — no side effects, fully testable.
 */
class IncrementScoreUseCase {
    operator fun invoke(state: MatchState, teamId: TeamId): MatchState {
        val team = state.teamFor(teamId)
        val next = team.currentPoint.next() ?: return state // Already at GAME, no-op

        return if (next == PadelPoint.GAME) {
            // ── Team wins this game ──────────────────────────────────
            state.copy(
                team1 = if (teamId == TeamId.TEAM1)
                    state.team1.copy(gamesWon = state.team1.gamesWon + 1, currentPoint = PadelPoint.ZERO)
                else
                    state.team1.copy(currentPoint = PadelPoint.ZERO),
                team2 = if (teamId == TeamId.TEAM2)
                    state.team2.copy(gamesWon = state.team2.gamesWon + 1, currentPoint = PadelPoint.ZERO)
                else
                    state.team2.copy(currentPoint = PadelPoint.ZERO)
            )
        } else {
            // ── Normal point advancement ─────────────────────────────
            state.updateTeam(teamId, team.copy(currentPoint = next))
        }
    }
}

/**
 * UseCase: Decrement a team's score by one padel point.
 *
 * At ZERO, no change (can't go below zero).
 * Pure function — fully testable.
 */
class DecrementScoreUseCase {
    operator fun invoke(state: MatchState, teamId: TeamId): MatchState {
        val team = state.teamFor(teamId)
        val prev = team.currentPoint.previous() ?: return state // Already at ZERO, no-op
        return state.updateTeam(teamId, team.copy(currentPoint = prev))
    }
}

/**
 * UseCase: Reset the entire match back to zero.
 */
class ResetMatchUseCase {
    operator fun invoke(state: MatchState): MatchState =
        MatchState.newMatch(
            team1Name = state.team1.name,
            team2Name = state.team2.name
        )
}

// ── Extension helpers (package-private) ─────────────────────────────────────

private fun MatchState.teamFor(id: TeamId): TeamState =
    if (id == TeamId.TEAM1) team1 else team2

private fun MatchState.updateTeam(id: TeamId, updated: TeamState): MatchState =
    if (id == TeamId.TEAM1) copy(team1 = updated) else copy(team2 = updated)
