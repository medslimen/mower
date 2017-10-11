package fr.xebia.mower.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.xebia.mower.exception.FileParsingException;
import fr.xebia.mower.exception.FileProcessingException;
import fr.xebia.mower.model.Action;
import fr.xebia.mower.model.Cardinal;
import fr.xebia.mower.model.Lawn;
import fr.xebia.mower.model.Mower;

@Service
public class MowerServiceImpl implements MowerService {

	@Autowired
	FileParserService fileParserService;

	private Lawn lawn;
	private Map<Integer, Mower> mowerMap = new HashMap<>();

	/**
	 * parse source file, validate data: check if file is not empty and has correct
	 * format, build mower list, execute movement actions for each mower and print
	 * final mowers positions and orientations
	 * 
	 * @throws FileProcessingException
	 */
	@Override
	public String manageMowers(String inputFilePath) throws FileProcessingException {
		try {
			// get input file content
			List<String> lineList = fileParserService.readInputFile(inputFilePath);

			// set lawn
			List<Integer> lawnLimits = fileParserService.getLawnLimits(lineList);
			lawn = new Lawn(lawnLimits.get(0), lawnLimits.get(1));

			// split file content into odd and even lists based on line number
			List<String>[] arrayList = fileParserService.splitIntoOddEvenLists(lineList);
			List<String> oddLineList = arrayList[0];
			List<String> evenLineList = arrayList[1];

			// build mower map based on data from even lines
			buildMowerMap(evenLineList);

			// move mowers based on data from odd lines
			moveMowers(oddLineList);

			// print final mowers positions and orientation
			return formatMowerData();

		} catch (FileParsingException e) {
			throw new FileProcessingException("Error parsing input file. Exception: " + e);
		}
	}

	/**
	 * build mower map
	 * 
	 * @param evenLineList
	 * @throws FileProcessingException
	 */
	protected void buildMowerMap(List<String> evenLineList) throws FileProcessingException {
		Mower mower;
		List<String> splitStringList;
		int xCoordinate;
		int yCoordinate;
		Cardinal orientation;

		// build mower list based on data from even lines
		try {
			for (int i = 0; i < evenLineList.size(); i++) {
				splitStringList = Stream.of(evenLineList.get(i).split(" ", 3)).collect(Collectors.toList());
				xCoordinate = Integer.valueOf(splitStringList.get(0));
				yCoordinate = Integer.valueOf(splitStringList.get(1));
				orientation = Cardinal.valueOf(splitStringList.get(2));

				mower = new Mower(xCoordinate, yCoordinate, orientation);
				mowerMap.put(i, mower);
			}
		} catch (NumberFormatException e) {
			throw new FileProcessingException("Error creating mower list. Exception: " + e);
		}
	}

	/**
	 * move mowers based on data from odd lines
	 * 
	 * @param oddLineList
	 * @throws FileProcessingException
	 */
	private void moveMowers(List<String> oddLineList) throws FileProcessingException {
		Mower mower;
		List<String> splitStringList;

		try {
			for (int i = 0; i < oddLineList.size(); i++) {
				splitStringList = Stream.of(oddLineList.get(i).split("")).collect(Collectors.toList());

				// get mower matching list index
				mower = mowerMap.get(i);
				for (String actionString : splitStringList) {
					executeAction(mower, Action.valueOf(actionString));
				}
			}
		} catch (Exception e) {
			throw new FileProcessingException("Error moving mowers. Exception: " + e);
		}
	}

	/**
	 * print mowers positions and orientation
	 */
	private String formatMowerData() {
		return mowerMap
				.entrySet().stream().map(entry -> entry.getValue().getXAxisLocation() + " "
						+ entry.getValue().getYAxisLocation() + " " + entry.getValue().getOrientation())
				.collect(Collectors.joining("\n"));
	}

	/**
	 * execute mower movement command
	 * 
	 * @param mower
	 * @param action
	 */
	private void executeAction(Mower mower, Action action) {
		switch (action) {
		case G:
			// change orientation left
			rotateLeft(mower);
			break;
		case D:
			// change orientation right
			rotateRight(mower);
			break;
		case A:
			// move forward
			moveForward(mower);
			break;
		default:
			break;
		}
	}

	/**
	 * rotate to right
	 * 
	 * @param mower
	 */
	private void rotateRight(Mower mower) {
		switch (mower.getOrientation()) {
		case N:
			mower.setOrientation(Cardinal.E);
			break;
		case E:
			mower.setOrientation(Cardinal.S);
			break;
		case S:
			mower.setOrientation(Cardinal.W);
			break;
		case W:
			mower.setOrientation(Cardinal.N);
			break;
		default:
			break;
		}
	}

	/**
	 * rotate to left
	 * 
	 * @param mower
	 */
	private void rotateLeft(Mower mower) {
		switch (mower.getOrientation()) {
		case N:
			mower.setOrientation(Cardinal.W);
			break;
		case E:
			mower.setOrientation(Cardinal.N);
			break;
		case S:
			mower.setOrientation(Cardinal.E);
			break;
		case W:
			mower.setOrientation(Cardinal.S);
			break;
		default:
			break;
		}
	}

	/**
	 * move forward. Do not move if new coordinates are out of range or occupied
	 * 
	 * @param mower
	 */
	private void moveForward(Mower mower) {
		int xAxisLocation = mower.getXAxisLocation();
		int yAxisLocation = mower.getYAxisLocation();

		switch (mower.getOrientation()) {
		case N:
			yAxisLocation++;
			break;
		case S:
			yAxisLocation--;
			break;
		case E:
			xAxisLocation++;
			break;
		case W:
			xAxisLocation--;
			break;
		default:
			break;
		}

		// out of lawn range and obstacle check
		if (isInLawnRange(xAxisLocation, yAxisLocation) && !isOccupied(xAxisLocation, yAxisLocation)) {
			mower.setXAxisLocation(xAxisLocation);
			mower.setYAxisLocation(yAxisLocation);
		}
	}

	/**
	 * check if coordinates are out of range
	 * 
	 * @param xAxisLocation
	 * @param yAxisLocation
	 * @return boolean
	 */
	protected boolean isInLawnRange(int xAxisLocation, int yAxisLocation) {
		return ((xAxisLocation >= 0 && xAxisLocation <= lawn.getXAxisLimit())
				&& (yAxisLocation >= 0 && yAxisLocation <= lawn.getYAxisLimit()));
	}

	/**
	 * check if position is occupied by another mower
	 * 
	 * @param xAxisLocation
	 * @param yAxisLocation
	 * @return
	 */
	protected boolean isOccupied(int xAxisLocation, int yAxisLocation) {
		Predicate<Entry<Integer, Mower>> isOccupiedPredicate = entry -> entry.getValue()
				.getXAxisLocation() == xAxisLocation && entry.getValue().getYAxisLocation() == yAxisLocation;
		return mowerMap.entrySet().stream().anyMatch(isOccupiedPredicate);
	}

	// setters used by test classes declared in same package
	public void setFileParserService(FileParserService fileParserService) {
		this.fileParserService = fileParserService;
	}

	protected void setLawn(Lawn lawn) {
		this.lawn = lawn;
	}

	protected void setMowerMap(Map<Integer, Mower> mowerMap) {
		this.mowerMap = mowerMap;
	}

}
