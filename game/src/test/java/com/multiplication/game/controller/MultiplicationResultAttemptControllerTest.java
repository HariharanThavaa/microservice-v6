package com.multiplication.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multiplication.game.domain.Multiplication;
import com.multiplication.game.domain.MultiplicationResultAttempt;
import com.multiplication.game.domain.User;
import com.multiplication.game.service.MultiplicationService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mvc;

    //This object will be magically initialized by the initFields method below.
    private JacksonTester<MultiplicationResultAttempt> jsonResult;
    private JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;
    private JacksonTester<MultiplicationResultAttempt> jsonAttempt;

    @Before
    public void setup(){
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void postResultReturnCorrect() throws Exception {
        genericParameterizedTest(true);
    }

    @Test
    public void postResultReturnWrong() throws Exception {
        genericParameterizedTest(false);
    }

    void genericParameterizedTest(final boolean correct) throws Exception {

        // given (remember we're not testing here the service itself)
        given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class)))
                .willReturn(correct);

        User user = new User("Hariharan");
        Multiplication multiplication = new Multiplication(70, 30);
        MultiplicationResultAttempt attempt = new
                MultiplicationResultAttempt(user, multiplication, 3500, correct);

        // when
        MockHttpServletResponse response = mvc.perform(post("/results")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonResult.write(attempt).getJson()))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonResult
                .write(new MultiplicationResultAttempt(attempt.getUser(),
                        attempt.getMultiplication(),
                        attempt.getResultAttempt(),
                        correct)).getJson());

    }

    @Test
    public void getUserStats() throws Exception {
        // Given
        User user = new User("Hariharan");
        Multiplication multiplication = new Multiplication(50, 70);

        MultiplicationResultAttempt attempt =
                new MultiplicationResultAttempt(user, multiplication, 3500, true);

        List<MultiplicationResultAttempt> recentAttempts =
                Lists.newArrayList(attempt, attempt);

        given(multiplicationService
                .getStatsForUser("Hariharan"))
                .willReturn(recentAttempts);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/results").param("alias", "Hariharan"))
                .andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonResultAttemptList.write(recentAttempts).getJson());
    }

    @Test
    public void getResultByIdTest() throws Exception{
        // Given
        User user = new User("Hariharan");
        Multiplication multiplication = new Multiplication(60,50);
        MultiplicationResultAttempt expected =
                new MultiplicationResultAttempt(user, multiplication, 600, true);
        given(multiplicationService.getResultById(1L))
                .willReturn(expected);

        // When
        MockHttpServletResponse response = mvc.perform(
                get("/results/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonAttempt.write(expected).getJson());
    }

}