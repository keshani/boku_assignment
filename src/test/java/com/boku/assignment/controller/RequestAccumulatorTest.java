package com.boku.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@SpringBootTest
@Rollback
class RequestAccumulatorTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void test_request_send_with_one_end_request() {
        try {
            String[] params = new String[]{"10","20","30","-20","end firstRound"};
            MvcResult[] results = new MvcResult[12];

            for(int i=0; i<params.length; i++) {
                MvcResult result = mockMvc.perform(post("/boku/accumulator/process_requests")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content(params[i]))
                        .andExpect(request().asyncStarted())
                        .andReturn();
                results[i] = result;
            }

            for(int i=0; i<results.length; i++) {
                mockMvc.perform(asyncDispatch(results[i]))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("text/plain"))
                        .andExpect(content().string("40 firstRound"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_request_send_with_several_end_request() {
        try {
            String[] params = new String[]{"10","20","30","-20","end firstRound", "20","30",
                              "end secondRound", "20","-40","90", "end thirdRound"};
            MvcResult[] results = new MvcResult[12];

            for(int i=0; i<params.length; i++) {
                MvcResult result = mockMvc.perform(post("/boku/accumulator/process_requests")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content(params[i]))
                        .andExpect(request().asyncStarted())
                        .andReturn();
                results[i] = result;
            }
            for(int i=0; i<results.length; i++) {
                String expected = "40 firstRound";
                if(8<=i) {
                    expected = "70 thirdRound";
                } else if(5<=i) {
                    expected = "50 secondRound";
                }
                mockMvc.perform(asyncDispatch(results[i]))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("text/plain"))
                        .andExpect(content().string(expected));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_request_send_with_consecutive_end_request() {
        try {
            String[] params = new String[]{"10","20","30","-20","end firstRound", "20","30",
                    "end secondRound", "end thirdRound"};
            MvcResult[] results = new MvcResult[12];

            for(int i=0; i<params.length; i++) {
                MvcResult result = mockMvc.perform(post("/boku/accumulator/process_requests")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content(params[i]))
                        .andExpect(request().asyncStarted())
                        .andReturn();
                results[i] = result;
            }

            for(int i=0; i<results.length; i++) {
                String expected = "40 firstRound";
                if(8<=i) {
                    expected = "0 thirdRound";
                } else if(5<=i) {
                    expected = "50 secondRound";
                }
                mockMvc.perform(asyncDispatch(results[i]))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("text/plain"))
                        .andExpect(content().string(expected));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_request_send_with_ony_end_request() {
        try {
            String[] params = new String[]{"end firstRound", "end secondRound", "end thirdRound"};
            MvcResult[] results = new MvcResult[3];

            for(int i=0; i<params.length; i++) {
                MvcResult result = mockMvc.perform(post("/boku/accumulator/process_requests")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content(params[i]))
                        .andExpect(request().asyncStarted())
                        .andReturn();
                results[i] = result;
            }

            for(int i=0; i<results.length; i++) {
                String expected = "0 firstRound";
                if(2<=i) {
                    expected = "0 thirdRound";
                } else if(1<=i) {
                    expected = "0 secondRound";
                }
                mockMvc.perform(asyncDispatch(results[i]))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("text/plain"))
                        .andExpect(content().string(expected));
            }

        } catch (Exception e) {
            fail(e);
        }
    }

    @Nested
    class test_request_with_different_param{
        @Test
        void test_request_with_non_numeric() {
            try {
                MvcResult result = mockMvc.perform(post("/boku/accumulator/process_requests")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content("UU"))
                        .andExpect(request().asyncStarted())
                        .andReturn();

               MvcResult content =  mockMvc.perform(asyncDispatch(result))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                String retriveContent = content.getResponse().getContentAsString();

                CompletableFuture<String> expect = new ObjectMapper().readValue(retriveContent, CompletableFuture.class);
                assertTrue(expect.isCompletedExceptionally());
                assertThrows(IllegalArgumentException.class,()->expect.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Test
        void test_request_with_empty() {
            try {
                MvcResult result = mockMvc.perform(post("/boku/accumulator/process_requests")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content("  "))
                        .andExpect(request().asyncStarted())
                        .andReturn();

                MvcResult content =  mockMvc.perform(asyncDispatch(result))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                String retriveContent = content.getResponse().getContentAsString();

                CompletableFuture<String> expect = new ObjectMapper().readValue(retriveContent, CompletableFuture.class);
                assertTrue(expect.isCompletedExceptionally());
                assertThrows(IllegalArgumentException.class,()->expect.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Test
        void test_request_with_wrong_end_request_format() {
            try {
                MvcResult result = mockMvc.perform(post("/boku/accumulator/process_requests")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content("end fff hhhh"))
                        .andExpect(request().asyncStarted())
                        .andReturn();

                MvcResult content =  mockMvc.perform(asyncDispatch(result))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                String retriveContent = content.getResponse().getContentAsString();

                CompletableFuture<String> expect = new ObjectMapper().readValue(retriveContent, CompletableFuture.class);
                assertTrue(expect.isCompletedExceptionally());
                assertThrows(IllegalArgumentException.class,()->expect.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Test
        void test_request_with_wrong_end_request_without_id() {
            try {
                MvcResult result = mockMvc.perform(post("/boku/accumulator/process_requests")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content("end"))
                        .andExpect(request().asyncStarted())
                        .andReturn();

                MvcResult content =  mockMvc.perform(asyncDispatch(result))
                        .andExpect(status().isBadRequest())
                        .andReturn();

                String retriveContent = content.getResponse().getContentAsString();

                CompletableFuture<String> expect = new ObjectMapper().readValue(retriveContent, CompletableFuture.class);
                assertTrue(expect.isCompletedExceptionally());
                assertThrows(IllegalArgumentException.class,()->expect.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}