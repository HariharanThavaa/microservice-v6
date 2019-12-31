package com.multiplication.game.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomGeneratorServiceTest {

    private static RandomGeneratorService randomGeneratorService;

    @BeforeAll
    public static void setup(){
        randomGeneratorService = new RandomGeneratorServiceImpl();
    }

    @Test
    public void generateRandomFactorIsBetweenExpectedLimits() throws Exception {

        //when a good sample of randomly generated factors is generated
        List<Integer> randomFactors = IntStream.range(0, 1000)
                .map(i -> randomGeneratorService.genearateRandomFactor())
                .boxed().collect(Collectors.toList());

        //then all of them should be between 11 and 100
        //because we want middle-complexity calculation
        assertThat(randomFactors).containsOnlyElementsOf(
                IntStream.range(11, 100).boxed().collect(Collectors.toList()));
    }
}
