package com.example.demo.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = { "classpath:application-test.properties" })
@Sql(scripts = "/sql/data.sql")
public class RegistrationControllerTest {

    private static final String USERSERVICE = "/userservice";
    private static final String LOGIN = "/login";
    private static final String REGISTER = "/register";

    @Autowired
    private MockMvc mvc;

    @Test
    public void testSuccessLoginUserWithGoodCredentials() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(USERSERVICE + LOGIN + "?userName=userNameTest&password=passw0rd").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.ALL)).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());
    }

    @Test
    public void testFailedLoginUserWithWrongPassword() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(USERSERVICE + LOGIN + "?userName=userNameTest&password=bla").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.ALL)).andDo(MockMvcResultHandlers.print()).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void testFailedLoginUserWithWrongUserName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(USERSERVICE + LOGIN + "?userName=userName&password=passw0rd").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.ALL)).andDo(MockMvcResultHandlers.print()).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void testSuccessRegistrationNewUserInstance() throws Exception {
        String expectedResult = "{\"firstName\":\"test\",\"lastName\":\"test\",\"id\":\"6\",\"userName\":\"test\"}";
        String actualResult = mvc.perform(MockMvcRequestBuilders.post(USERSERVICE + REGISTER).header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "\t\"firstName\":\"test\",\n" +
                        "\t\"lastName\":\"test\",\n" +
                        "\t\"userName\":\"test\",\n" +
                        "\t\"password\":\"test\"\n" +
                        "}")
                .accept(MediaType.ALL))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testRegistrationNewUserInstanceThatAlreadyExists() throws Exception {
        String expectedResult = "{\"code\":\"USER_ALREADY_EXISTS\",\"description\":\"A user with the given username already exists\"}";
        String actualResult = mvc.perform(MockMvcRequestBuilders.post(USERSERVICE + REGISTER).header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .content("{\n" +
                        "\t\"firstName\":\"firstNameTest\",\n" +
                        "\t\"lastName\":\"lastNameTest\",\n" +
                        "\t\"userName\":\"userNameTest\",\n" +
                        "\t\"password\":\"test\"\n" +
                        "}")
                .accept(MediaType.ALL))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().is(HttpStatus.CONFLICT.value()))
                .andReturn().getResponse().getContentAsString();

        Assert.assertEquals(expectedResult, actualResult);
    }
}
