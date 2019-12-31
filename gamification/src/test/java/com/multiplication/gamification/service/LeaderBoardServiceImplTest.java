package com.multiplication.gamification.service;

import com.multiplication.gamification.domain.LeaderBoardRow;
import com.multiplication.gamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class LeaderBoardServiceImplTest {

    private LeaderBoardServiceImpl leaderBoardService;

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        leaderBoardService = new LeaderBoardServiceImpl(scoreCardRepository);
    }

    @Test
    public void retrieveLeaderBoardTest() {
        //given
        Long userId = 1L;
        LeaderBoardRow leaderBoardRow =  new LeaderBoardRow(userId, 300L);
        List<LeaderBoardRow> expected = Collections.singletonList(leaderBoardRow);
        given(scoreCardRepository.findFirst10()).willReturn(expected);

        //when
        List<LeaderBoardRow> actual = leaderBoardService.getCurrentLeaderBoard();

        //then
        assertThat(actual).isEqualTo(expected);
    }

}