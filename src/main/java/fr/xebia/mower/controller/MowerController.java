package fr.xebia.mower.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.xebia.mower.exception.FileParsingException;
import fr.xebia.mower.service.MowerService;

@Controller
public class MowerController {

	@Autowired
	MowerService mowerService;

	@RequestMapping("/")
	public void process() {
		try {
			mowerService.processInputData();
		} catch (FileParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
