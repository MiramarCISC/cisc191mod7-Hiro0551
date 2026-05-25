package edu.sdccd.cisc191.model;

import java.util.concurrent.atomic.AtomicInteger;

public class MatchViewModel {
    private String matchId;
    private final Player player = new Player("Player");
    private final Player opponent = new Player("Opponent");
    private boolean matchOver;
    private String winnerName = "";

    // Use either an AtomicInteger field or synchronized methods so background tasks cannot lose updates.
    private final AtomicInteger completedMatchCount = new AtomicInteger(0);

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public boolean isMatchOver() {
        return matchOver;
    }

    public void setMatchOver(boolean matchOver) {
        this.matchOver = matchOver;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName == null ? "" : winnerName;
    }

    public int getCompletedMatchCount() {
        return completedMatchCount.get();
    }

    /**
     *
     * This model may be updated after JavaFX background tasks finish. Make sure concurrent
     * calls do not lose completed-match updates. You may use synchronized methods or an
     * AtomicInteger.
     *
     * Requirements:
     * - Increase the completed match count exactly once per call.
     * - Store the winner name using the existing null-safe setter.
     * - Mark the match as over.
     * - Protect shared state from race conditions.
     */
    public synchronized void recordCompletedMatchThreadSafely(String winnerName) {
        completedMatchCount.incrementAndGet();
        setWinnerName(winnerName);
        matchOver = true;
    }

    public boolean hasJoinedMatch() {
        return matchId != null && !matchId.isBlank();
    }

    public boolean canPlayMatch() {
        return hasJoinedMatch() && !matchOver;
    }

    public String buildMatchSummary(String difficulty, boolean ranked) {
        if (!hasJoinedMatch()) {
            return "No match";
        }

        String effectiveDifficulty = (difficulty == null || difficulty.isBlank()) ? "Normal" : difficulty.trim();
        String rankedLabel = ranked ? "ranked" : "casual";

        return "Match " + matchId + ": "
                + player.getName() + " vs " + opponent.getName()
                + " (" + effectiveDifficulty + ", " + rankedLabel + ")";
    }

    public void resetLocalState() {
        matchId = null;
        player.setName("Player");
        opponent.setName("Opponent");
        matchOver = false;
        winnerName = "";
        completedMatchCount.set(0);
    }
}
