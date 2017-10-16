package fr.xebia.mower;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.util.ResourceUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.xebia.mower.exception.FileProcessingException;
import fr.xebia.mower.service.FileParserService;
import fr.xebia.mower.service.FileParserServiceImpl;
import fr.xebia.mower.service.MowerServiceImpl;

public class MowerSteps {
	private MowerServiceImpl mowerService;
	private String inputFilePath;
	private String actualResult;

	@Given("^Provide sample input file \"([^\"]*)\"$")
	public void provide_sample_input_file(String inputFileName) throws FileNotFoundException {
		File file = ResourceUtils.getFile(inputFileName);
		inputFilePath = file.getAbsolutePath();
	}

	@When("^Run mower commands$")
	public void run_mower_commands() throws FileProcessingException {
		mowerService = new MowerServiceImpl();
		FileParserService fileParserService = new FileParserServiceImpl();
		mowerService.setFileParserService(fileParserService);

		actualResult = mowerService.manageMowers(inputFilePath);
	}

	@Then("^I should be told the final mowers position is \"([^\"]*)\"\\ and \"([^\"]*)\"$")
	public void I_should_be_told_the_final_mowers_position_is_n(String firstMowerPosition, String secondMowerPosition) {
		String finalPositions[] = actualResult.split("\\r\\n|\\n|\\r");
		assertEquals(firstMowerPosition, finalPositions[0]);
		assertEquals(secondMowerPosition, finalPositions[1]);
	}

}
