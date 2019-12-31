package com.multiplication.gamification.client;

import com.multiplication.gamification.client.dto.MultiplicationResultAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This implementation of MultiplicationResultAttemptClient interface connects to
 * the Game microservice via REST
 */
@Component
public class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient {

    private final RestTemplate template;
    private final String multiplicationHost;

    @Autowired
    public MultiplicationResultAttemptClientImpl(final RestTemplate template,
                                                 @Value("${multiplicationHost}") final String multiplicationHost){
        this.template = template;
        this.multiplicationHost = multiplicationHost;
    }

    @Override
    public MultiplicationResultAttempt retrieveMultiplicationResultAttemptbyId(Long multiplicationId) {
        return template.getForObject(multiplicationHost + "/results/" + multiplicationId, MultiplicationResultAttempt.class);
    }
}
