package com.example.chessBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChessBackendApplication {

	public ChessBackendApplication(){
		System.out.println("test");
	}

	public static void main(String[] args) {
		SpringApplication.run(ChessBackendApplication.class, args);
	}


}
