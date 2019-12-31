package com.multiplication.gamification.service;

import com.multiplication.gamification.domain.GameStats;

/**
 * This service includes the main logic for gamifying the system.
 */
public interface GameService {

    /**
     * Process a new attempt from a given user
     *
     * @param userId the user's unique Id
     * @param attemptId the attempt id, can be used to retrieve extra data if needed
     * @param correct indicated if the attempt was correct
     *
     * @return a {@link GameStats} object containing the new score and badge cards obtained
     */
    GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct);

    /**
     * Gets the game statistics for a given user
     *
     * @param userId the user
     * @return the total statistics for that user
     */
    GameStats retrieveStatsForUser(Long userId);
}
