package fr.xebia.mower.service;

import fr.xebia.mower.exception.FileProcessingException;

public interface MowerService {
	public String manageMowers(String inputFilePath) throws FileProcessingException;
}
