package com.example.qr_bank;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class QrBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrBankApplication.class, args);
	}

}
