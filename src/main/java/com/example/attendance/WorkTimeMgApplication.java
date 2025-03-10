package com.example.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class WorkTimeMgApplication {

	public static void main(String[] args) {
		// .envファイルを読み込む
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		// Spring Boot アプリケーションを起動
		SpringApplication.run(WorkTimeMgApplication.class, args);
	}

}
