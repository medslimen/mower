package fr.xebia.mower.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.xebia.mower.exception.FileParsingException;

@RunWith(SpringJUnit4ClassRunner.class)
public class FileParserServiceTests {

	private FileParserService fileParserService;

	@Value("classpath:input.data")
	private Resource resource;

	@Before
	public void setUp() {
		fileParserService = new FileParserServiceImpl();
	}

	@Test
	public void readInputFile_validFile() throws FileParsingException, IOException {
		List<String> expectedReturnValue = Arrays.asList("5 5", "1 2 N", "GAGAGAGAA", "3 3 E", "AADAADADDA");
		String inputFilePath = resource.getFile().getAbsolutePath();
		List<String> returnValue = fileParserService.readInputFile(inputFilePath);
		assertEquals(expectedReturnValue, returnValue);
	}

	@Test(expected = FileParsingException.class)
	public void readInputFile_noFileArgumentPassed() throws FileParsingException {
		fileParserService.readInputFile("undefined");
	}

	@Test
	public void getLawnLimit_validData() throws FileParsingException {
		List<Integer> expectedReturnValue = Arrays.asList(5, 5);
		List<String> lineList = Arrays.asList("5 5", "1 2 N", "GAGAGAGAA", "3 3 E", "AADAADADDA");
		List<Integer> returnValue = fileParserService.getLawnLimits(lineList);
		assertEquals(expectedReturnValue, returnValue);
	}

	@Test(expected = FileParsingException.class)
	public void getLawnLimit_invalidFirstLineData() throws FileParsingException {
		List<String> lineList = Arrays.asList("A B", "1 2 N", "GAGAGAGAA", "3 3 E", "AADAADADDA");
		fileParserService.getLawnLimits(lineList);
	}

	@Test
	public void splitIntoOddEvenLists_validData() throws FileParsingException {
		List<String> lineList = Arrays.asList("5 5", "1 2 N", "GAGAGAGAA", "3 3 E", "AADAADADDA");
		List<String> expectedOddReturnValue = Arrays.asList("GAGAGAGAA", "AADAADADDA");
		List<String> expectedEvenReturnValue = Arrays.asList("1 2 N", "3 3 E");
		List<String>[] returnedValue = fileParserService.splitIntoOddEvenLists(lineList);

		assertEquals(expectedOddReturnValue, returnedValue[0]);
		assertEquals(expectedEvenReturnValue, returnedValue[1]);
	}

	@Test(expected = FileParsingException.class)
	public void splitIntoOddEvenLists_invalidLineNumber() throws FileParsingException {
		List<String> lineList = Arrays.asList("A B", "1 2 N", "GAGAGAGAA", "3 3 E");
		fileParserService.splitIntoOddEvenLists(lineList);
	}
}
