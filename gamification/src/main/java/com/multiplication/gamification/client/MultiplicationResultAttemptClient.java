package com.multiplication.gamification.client;

import com.multiplication.gamification.client.dto.MultiplicationResultAttempt;

/**
 * This interface allows us to connect the Multiplication microservice.
 * Note that it's agnostic to the way of communication.
 */
public interface MultiplicationResultAttemptClient {

    MultiplicationResultAttempt retrieveMultiplicationResultAttemptbyId(final Long multiplicationId);
}
