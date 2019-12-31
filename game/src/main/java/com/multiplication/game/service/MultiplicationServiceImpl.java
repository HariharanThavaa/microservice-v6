package com.multiplication.game.service;

import com.multiplication.game.domain.Multiplication;
import com.multiplication.game.domain.MultiplicationResultAttempt;
import com.multiplication.game.domain.User;
import com.multiplication.game.event.EventDispatcher;
import com.multiplication.game.event.MultiplicationSolvedEvent;
import com.multiplication.game.repository.MultiplicationResultAttemptRepository;
import com.multiplication.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;

    private MultiplicationResultAttemptRepository attemptRepository;

    private UserRepository userRepository;

    private EventDispatcher eventDispatcher;

    @Autowired
    public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService,
                                     final MultiplicationResultAttemptRepository attemptRepository,
                                     final UserRepository userRepository,
                                     final EventDispatcher eventDispatcher){
        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public Multiplication CreateRandomMultiplication() {
        int factorA = randomGeneratorService.genearateRandomFactor();
        int factorB = randomGeneratorService.genearateRandomFactor();
        return new Multiplication(factorA,factorB);
    }

    @Transactional
    @Override
    public boolean checkAttempt(MultiplicationResultAttempt resultAttempt) {

        //check if the user already exists for that alias
        Optional<User> user = userRepository.findByAlias(resultAttempt.getUser().getAlias());



        boolean correct = resultAttempt.getResultAttempt() ==
                resultAttempt.getMultiplication().getFactorA() *
                resultAttempt.getMultiplication().getFactorB();

        // Avoids 'hack' attempts
        Assert.isTrue(!resultAttempt.isCorrect(), "You cannot send an attempt marked as correct!!");

        // Creates a copy, now setting the 'correct' field accordingly
        MultiplicationResultAttempt checkedAttempt =
                new MultiplicationResultAttempt(user.orElse(resultAttempt.getUser()),
                        resultAttempt.getMultiplication(),
                        resultAttempt.getResultAttempt(),
                        correct);

        Optional<MultiplicationResultAttempt> attempt =
                StreamSupport.stream(attemptRepository.findAll().spliterator(), false)
                .filter(a-> a.getUser().equals(resultAttempt.getUser()))
                .filter(a-> a.getMultiplication().equals(resultAttempt.getMultiplication()))
                .findFirst();

        // Stores the attempt
        if (!attempt.isPresent()){
            attemptRepository.save(checkedAttempt);
        }

        // Communicates the results via event
        eventDispatcher.send(
                new MultiplicationSolvedEvent(checkedAttempt.getId(),
                        checkedAttempt.getUser().getId(),
                        checkedAttempt.isCorrect())
        );

        // return the result
        return correct;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }

    @Override
    public MultiplicationResultAttempt getResultById(Long resultId) {
        return attemptRepository.findById(resultId).get();
    }


}
