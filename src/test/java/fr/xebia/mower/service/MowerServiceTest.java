package fr.xebia.mower.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.xebia.mower.exception.FileProcessingException;
import fr.xebia.mower.model.Cardinal;
import fr.xebia.mower.model.Lawn;
import fr.xebia.mower.model.Mower;

@RunWith(SpringJUnit4ClassRunner.class)
public class MowerServiceTest {

	private MowerServiceImpl mowerService;
	private FileParserService fileParserService;

	@Value("classpath:input.data")
	private Resource resource;

	@Before
	public void setUp() {
		mowerService = new MowerServiceImpl();
		fileParserService = new FileParserServiceImpl();
		mowerService.setFileParserService(fileParserService);
	}

	@Test
	public void manageMowers_sampleData() throws IOException, FileProcessingException {
		String inputFilePath = resource.getFile().getAbsolutePath();
		String expectedValue = "1 3 N\n" + "5 1 E";
		String returnedValue = mowerService.manageMowers(inputFilePath);
		assertEquals(expectedValue, returnedValue);
	}

	@Test
	public void isInLawnRange_outOfRange() {
		Lawn lawn = new Lawn(5, 5);
		mowerService.setLawn(lawn);
		assertFalse(mowerService.isInLawnRange(6, 3));
	}

	@Test
	public void isOccupied_positionUsed() {
		Lawn lawn = new Lawn(5, 5);

		Map<Integer, Mower> mowerMap = new HashMap<>();
		mowerMap.put(1, new Mower(2, 3, Cardinal.valueOf("N")));
		mowerMap.put(2, new Mower(4, 6, Cardinal.valueOf("N")));

		mowerService.setLawn(lawn);
		mowerService.setMowerMap(mowerMap);

		assertTrue(mowerService.isOccupied(2, 3));
	}

}
