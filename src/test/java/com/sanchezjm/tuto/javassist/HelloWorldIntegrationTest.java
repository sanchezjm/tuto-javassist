package com.sanchezjm.tuto.javassist;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = HelloWorldIntegrationTest.Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class HelloWorldIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Configuration
	@EnableAutoConfiguration
	@RestController
	protected static class Application {

		private HelloWorld helloWorld = new HelloWorld();
		@GetMapping("/hi/{name}")
		public String sayHi(@PathVariable("name") String name) {
            return helloWorld.sayHi(name);
        }
	}
	
	@Test
	public void mustSayHi() throws Exception{
		final MvcResult result = mockMvc.perform(get("/hi/Tip")).andExpect(status().isOk()).andReturn();
		final String content = result.getResponse().getContentAsString();
		assertEquals("Hi Tip!", content);

	}
	
	public void mustThrowsIllegalArgumentExceptionIfTheName() throws Exception{
		mockMvc.perform(get("/hi/Coll")).andExpect(status().is5xxServerError());
	}
	
}