package fr.xebia.mower.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.xebia.mower.exception.FileParsingException;
import fr.xebia.mower.model.Lawn;
import fr.xebia.mower.model.Mower;

@Service
public class MowerServiceImpl implements MowerService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("input.data.filename")
	private String inputFilePath;

	private Lawn lawn;
	private List<Mower> mowers;

	public void processInputData() throws FileParsingException {

		try (Stream<String> stream = Files.lines(Paths.get(inputFilePath))) {

			Optional<String> optionalFirstLine = stream.findFirst();
			if (optionalFirstLine.isPresent()) {
				// if first line is present: extract data and set lawn
				List<Integer> strings = Stream.of(optionalFirstLine.get().split(" ", 2)).map(Integer::parseInt)
						.collect(Collectors.toList());
				// lawn = new Lawn(strings.get(0), strings.get(1));
			} else {
				throw new FileParsingException("First line is not found.");
			}

			/*
			 * Map<Integer,String> map = IntStream.range(0,stream.size())
			 * .boxed() .collect(Collectors.toMap (i -> i, i -> items.get(i)));
			 */

		} catch (NumberFormatException e) {
			throw new FileParsingException("Failed to parse int values from string. Exception: " + e.getMessage());
		} catch (IOException e) {
			throw new FileParsingException("Failed to parse file. Exception: " + e.getMessage());
		}

		return;
	}

}
