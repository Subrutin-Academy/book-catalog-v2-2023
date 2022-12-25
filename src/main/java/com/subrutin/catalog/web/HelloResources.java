package com.subrutin.catalog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.subrutin.catalog.dto.HelloMessageResponseDTO;
import com.subrutin.catalog.service.GreetingService;

@RestController
public class HelloResources {

	private GreetingService greetingService;

	Logger log = LoggerFactory.getLogger(HelloResources.class);

	public HelloResources(GreetingService greetingService) {
		super();
		this.greetingService = greetingService;
	}

	// get adalah salah satu jenis dari http method/ http verb
	// POST, PUT, DELETE, OPTION, TRACE, HEAD, PATCH
	@GetMapping("/hello")
    public ResponseEntity<HelloMessageResponseDTO> helloWorld() {
		log.trace("this is log TRACE");
		log.debug("this is log DEBUG");
		log.info("this is log INFO");
		log.warn("this is log WARN");
		log.error("this is log ERROR");
		HelloMessageResponseDTO dto = new HelloMessageResponseDTO();
		dto.setMessage(greetingService.sayGreeting());
        return ResponseEntity.accepted().body(dto);	}
}