package com.multiplication.game.service;

import com.multiplication.game.domain.Multiplication;
import com.multiplication.game.domain.MultiplicationResultAttempt;
import com.multiplication.game.domain.User;
import com.multiplication.game.event.EventDispatcher;
import com.multiplication.game.event.MultiplicationSolvedEvent;
import com.multiplication.game.repository.MultiplicationResultAttemptRepository;
import com.multiplication.game.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


public class MultiplicationServiceTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventDispatcher eventDispatcher;

    @Before
    public void setUp() {
        //With this call to initMocks we tell Mockito to process the annotations
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService,
                attemptRepository, userRepository, eventDispatcher);
    }


    @Test
    public void checkCorrectAttemptTest() {

        // Given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Hariharan");
        MultiplicationResultAttempt attempt =
                new MultiplicationResultAttempt(user, multiplication, 3000, false);
        MultiplicationResultAttempt verifiedAttempt =
                new MultiplicationResultAttempt(user, multiplication, 3000, true);
        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(verifiedAttempt.getId(),
                user.getId(),
                true);

        given(userRepository.findByAlias("Hariharan")).willReturn(Optional.empty());

        // When
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        // Then
        assertThat(attemptResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
        verify(eventDispatcher).send(event);
    }

    @Test
    public void checkWrongAttemptTest() {

        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Hariharan");
        MultiplicationResultAttempt attempt =
                new MultiplicationResultAttempt(user, multiplication, 3010, false);
        given(userRepository.findByAlias("Hariharan")).willReturn(Optional.empty());
        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(attempt.getId(),
                user.getId(),
                false);

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        //then
        assertThat(attemptResult).isFalse();
        verify(attemptRepository).save(attempt);
        verify(eventDispatcher).send(event);
    }

    @Test
    public void testMultiplicationEqual() {
        Multiplication m1 = new Multiplication(60, 50);
        Multiplication m2 = new Multiplication(60, 50);
        assertThat(m1.equals(m2)).isTrue();
    }

    @Test
    public void checkSameAttemptTest() {

        // Given
        Multiplication multiplication = new Multiplication(60, 60);
        User user = new User("Hariharan");
        MultiplicationResultAttempt attempt =
                new MultiplicationResultAttempt(user, multiplication, 3600, false);
        MultiplicationResultAttempt verifiedAttempt =
                new MultiplicationResultAttempt(user, multiplication, 3600, true);

        given(userRepository.findByAlias("Hariharan")).willReturn(Optional.of(user));
        given(attemptRepository.findAll()).willReturn(Arrays.asList(verifiedAttempt));

        // When
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        // Then
        assertThat(attemptResult).isTrue();
        verify(attemptRepository, never()).save(verifiedAttempt);
    }

    @Test
    public void retrieveStatsTest(){

        // Given
        User user = new User("Hariharan");

        Multiplication multiplication1 = new Multiplication(50,60);
        MultiplicationResultAttempt attempt1 =
                new MultiplicationResultAttempt(user, multiplication1, 3010, false);
        MultiplicationResultAttempt attempt2 =
                new MultiplicationResultAttempt(user, multiplication1, 3050, false);
        List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);

        given(userRepository.findByAlias("Hariharan")).willReturn(Optional.empty());
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("Hariharan")).willReturn(latestAttempts);

        // When
        List<MultiplicationResultAttempt> latestAttemtResult =
                multiplicationServiceImpl.getStatsForUser("Hariharan");

        // Then
        assertThat(latestAttemtResult).isEqualTo(latestAttempts);

    }

    @Test
    public void getResultByIdTest() {
        // Given
        User user = new User("Hariharan");
        Multiplication multiplication = new Multiplication(50, 60);
        MultiplicationResultAttempt expected =
                new MultiplicationResultAttempt(user, multiplication, 3000, true);
        given(attemptRepository.findById(1L)).willReturn(Optional.of(expected));

        // When
        MultiplicationResultAttempt actual =
                multiplicationServiceImpl.getResultById(1L);

        // Then
        assertThat(actual).isEqualTo(expected);
    }
}