package fr.xebia.mower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fr.xebia.mower.exception.FileProcessingException;
import fr.xebia.mower.service.MowerService;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Value("${input-file:undefined}")
	private String inputFilePath;

	@Autowired
	private MowerService mowerService;

	public static void main(String[] args) throws Throwable {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			System.out.println("Position finale des tondeuses:");
			String mowerFinalPositions = mowerService.manageMowers(inputFilePath);
			System.out.println(mowerFinalPositions);
		} catch (FileProcessingException e) {
			System.out.println("Exception" + e);
		}
	}

}
