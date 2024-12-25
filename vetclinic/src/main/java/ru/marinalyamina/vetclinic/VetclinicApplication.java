package ru.marinalyamina.vetclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.marinalyamina.vetclinic.utils.FileManager;

@SpringBootApplication
public class VetclinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(VetclinicApplication.class, args);

		FileManager.checkRootDir();
	}

}
