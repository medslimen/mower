package fr.xebia.mower.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mower {

	int xAxisLocation;
	int yAxisLocation;
	Cardinal orientation;
}
