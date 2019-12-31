package com.multiplication.game.service;

import com.multiplication.game.domain.Multiplication;
import com.multiplication.game.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationService {

    /**
     * Generates a random {@link Multiplication}
     *
     * @return a multiplication of randomly generated numbers.
     */
    Multiplication CreateRandomMultiplication();

    /**
     * @return true if the attempt matches the result of the multiplication, false otherwise.
     */
    boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);

    /**
     * @return  list of last 5 attempts
     */
    List<MultiplicationResultAttempt> getStatsForUser(final String userAlias);

    /**
     * Gets an attempt by its id
     *
     * @param resultId the identifier of the attempt
     * @return the {@link MultiplicationResultAttempt} object matching the id, otherwise null.
     */
    MultiplicationResultAttempt getResultById(final Long resultId);
}
