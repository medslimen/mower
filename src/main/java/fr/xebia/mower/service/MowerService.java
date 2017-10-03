package fr.xebia.mower.service;

import fr.xebia.mower.exception.FileParsingException;

@FunctionalInterface
public interface MowerService {
	public void processInputData() throws FileParsingException;
}
