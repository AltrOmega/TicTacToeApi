package io.altr.ticTacToe.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicTacToeApiApplication{
	@Autowired

	public static void main(String[] args) {
		SpringApplication.run(TicTacToeApiApplication.class, args);
	}
}
