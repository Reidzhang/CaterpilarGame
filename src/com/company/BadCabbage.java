package com.company;
import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Oval;

public class BadCabbage extends Cabbage {

	Oval badCabbage;

	public BadCabbage(GWindow window, Point center) {
		super(window, center);
		draw();

	}

	@Override
	protected void draw() {
		// TODO Auto-generated method stub
		badCabbage = new Oval(center.x - CABBAGE_RADIUS, center.y
				- CABBAGE_RADIUS, 2 * CABBAGE_RADIUS, 2 * CABBAGE_RADIUS,
				Color.black, true);
		window.add(badCabbage);

	}

	@Override
	public void isEatenBy(Caterpillar cp) {
		// change caterpillar color
		cp.changeColorChar('b');
		// caterpillar shrink
		cp.shrink();
		cp.resetDirection();
		window.remove(badCabbage);
	}

	public void removeBadCabbageFromWindow() {
		window.remove(badCabbage);
	}

}
