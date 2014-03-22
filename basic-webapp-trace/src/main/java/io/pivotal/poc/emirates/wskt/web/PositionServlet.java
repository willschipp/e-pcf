package io.pivotal.poc.emirates.wskt.web;

import io.pivotal.poc.emirates.wskt.domain.Position;
import io.pivotal.poc.emirates.wskt.domain.PositionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PositionServlet {

	@Autowired
	private PositionRepository positionRepository;
	
	@MessageMapping("/position")
	public void storePosition(Position position) {
		//dump
		System.out.println(position);
		positionRepository.save(position);
	}
	
}
