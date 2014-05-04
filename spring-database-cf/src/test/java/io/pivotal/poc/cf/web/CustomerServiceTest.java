package io.pivotal.poc.cf.web;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import io.pivotal.poc.cf.Application;
import io.pivotal.poc.cf.domain.Customer;
import io.pivotal.poc.cf.domain.CustomerRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes=Application.class)
public class CustomerServiceTest {

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private CustomerRepository repository;
	
	private MockMvc mockMvc;
	
	@Before
	public void before() {
		mockMvc = webAppContextSetup(context).build();
	}
	
	@After
	public void after() {
		repository.deleteAllInBatch();
	}
	
	@Test
	public void testFindAll() throws Exception {
		mockMvc.perform(get("/customers").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testSize() throws Exception {
		mockMvc.perform(get("/customers/size").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testSave() throws Exception {
		long initialCount = repository.count();
		//build an object
		Customer customer = new Customer();
		String customerString = new ObjectMapper().writeValueAsString(customer);
		//send
		mockMvc.perform(post("/customers").content(customerString).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		//check
		assertTrue(repository.count() == (initialCount + 1));
	}

}
