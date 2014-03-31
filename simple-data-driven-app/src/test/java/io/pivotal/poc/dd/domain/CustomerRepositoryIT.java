package io.pivotal.poc.dd.domain;

import static org.junit.Assert.assertTrue;
import io.pivotal.poc.dd.Application;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
	DbUnitTestExecutionListener.class})
public class CustomerRepositoryIT {

	@Autowired
	private CustomerRepository repository;
	
	@Autowired
	private DataSource dataSource;
	
	@After
	@DatabaseTearDown("/output.xml")
	public void after() {
		repository.deleteAllInBatch();
	}
	
	@Test
	@DatabaseSetup("/output.xml")
	public void test() {
		assertTrue(repository.count() == 1000);
	}

}
