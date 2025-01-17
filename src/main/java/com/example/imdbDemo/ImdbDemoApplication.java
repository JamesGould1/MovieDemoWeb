package com.example.imdbDemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@SpringBootApplication
@Controller
@EnableAutoConfiguration
public class ImdbDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImdbDemoApplication.class, args);
	}

	@GetMapping("/index")
	public String searchScreen() {
		return "index";
	}
	@PostMapping("/index")
	public String searchInput() {
		return "searchResults";
	}

}
