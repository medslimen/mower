package fr.xebia.mower.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import fr.xebia.mower.exception.FileParsingException;

@Service
public class FileParserServiceImpl implements FileParserService {

	@Override
	public List<String> readInputFile(String inputFilePath) throws FileParsingException {
		// throw IllegalArgumentException if input file argument is not set
		if ("undefined".equals(inputFilePath)) {
			throw new FileParsingException("Missing required argument: input-file");
		}

		// parse input file
		try (Stream<String> lineStream = Files.lines(Paths.get(inputFilePath))) {
			return lineStream.collect(Collectors.toList());
		} catch (IOException e) {
			throw new FileParsingException("Failed to read input file. Exception " + e);
		}
	}

	@Override
	public List<Integer> getLawnLimits(List<String> lineList) throws FileParsingException {
		// use first line to set lawn size
		Optional<String> optionalFirstLine = lineList.stream().findFirst();
		if (!optionalFirstLine.isPresent()) {
			// throw exception if file is empty
			throw new FileParsingException("Error parsing input file. Exception: first line not found.");
		}

		// split string into tow integers
		try {
			return Stream.of(optionalFirstLine.get().split(" ", 2)).map(Integer::parseInt).collect(Collectors.toList());
		} catch (NumberFormatException e) {
			throw new FileParsingException("Error parsing int values from first line. Exception: " + e);
		}

	}

	@Override
	public List<String>[] splitIntoOddEvenLists(List<String> lineList) throws FileParsingException {

		// process remaining lines
		List<String> reamainingLines = lineList.stream().skip(1).collect(Collectors.toList());

		/*
		 * split file to even and odd lines list, even lines contain mower position and
		 * orientation, odd lines contain mower action list
		 */
		IntPredicate isEvenPredicate = i -> i % 2 == 0;

		// even lines list
		List<String> evenLineList = IntStream.range(0, reamainingLines.size()).filter(isEvenPredicate)
				.mapToObj(reamainingLines::get).collect(Collectors.toList());

		// odd line list
		List<String> oddLineList = IntStream.range(0, reamainingLines.size()).filter(isEvenPredicate.negate())
				.mapToObj(reamainingLines::get).collect(Collectors.toList());

		// validate file data: check if odd and even lists are equal in size
		if (evenLineList.size() != oddLineList.size()) {
			throw new FileParsingException("Exception: invalid file data.");
		}

		@SuppressWarnings("unchecked")
		List<String>[] arrayLists = new List[2];
		arrayLists[0] = oddLineList;
		arrayLists[1] = evenLineList;

		return arrayLists;
	}
}
