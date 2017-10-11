package fr.xebia.mower.service;

import java.util.List;

import fr.xebia.mower.exception.FileParsingException;

public interface FileParserService {

	/**
	 * check if input file argument is set, read input file content
	 * 
	 * @return list of strings representing file lines
	 * @throws FileParsingException
	 */
	public List<String> readInputFile(String inputFilePath) throws FileParsingException;

	/**
	 * check if file is not empty, parse data from first line
	 * 
	 * @param lineList:
	 *            file line string list
	 * @return lawn x and y axis limits
	 * @throws FileParsingException
	 */
	public List<Integer> getLawnLimits(List<String> lineList) throws FileParsingException;

	/**
	 * split file lines (except first line) to tow separate lists based on line
	 * number
	 * 
	 * @param lineList:
	 *            file line list
	 * @return array of tow lists: first element is odd line lists, second is even
	 *         line list
	 * @throws FileParsingException
	 */
	public List<String>[] splitIntoOddEvenLists(List<String> lineList) throws FileParsingException;
}
