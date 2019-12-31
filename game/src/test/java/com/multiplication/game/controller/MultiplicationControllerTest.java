package com.multiplication.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multiplication.game.domain.Multiplication;
import com.multiplication.game.service.MultiplicationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationController.class)
public class MultiplicationControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mvc;

    //This object will be magically initialized by the initFields method below
    private JacksonTester<Multiplication> json;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void getRandomMultiplicationTest() throws Exception {

        // given
        given (multiplicationService.CreateRandomMultiplication()).willReturn(new Multiplication(70,30));

        // when
        MockHttpServletResponse response = mvc.perform(get("/multiplications/random")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getContentAsString())
                .isEqualTo(json.write(new Multiplication(70,30)).getJson());
    }
}
