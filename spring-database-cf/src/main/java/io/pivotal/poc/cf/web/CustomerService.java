package io.pivotal.poc.cf.web;


import io.pivotal.poc.cf.domain.Customer;
import io.pivotal.poc.cf.domain.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public Iterable<Customer> findAll() {
		return customerRepository.findAll();
	}
	
	@RequestMapping(value="/size",method=RequestMethod.GET)
	public Long size() {
		return customerRepository.count();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Customer save(@RequestBody Customer customer) {
		return customerRepository.save(customer);
	}
}
