package com.company;
import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Oval;

public class SuperCabbage extends Cabbage {

	public SuperCabbage(GWindow window, Point center) {
		super(window, center);
		draw();
		// TODO Auto-generated constructor stub
	}

	private Oval SuperCabbage;

	@Override
	protected void draw() {
		// TODO Auto-generated method stub
		SuperCabbage = new Oval(center.x - CABBAGE_RADIUS, center.y
				- CABBAGE_RADIUS, 2 * CABBAGE_RADIUS, 2 * CABBAGE_RADIUS,
				Color.BLUE, true);
		window.add(SuperCabbage);
	}

	@Override
	public void isEatenBy(Caterpillar cp) {
		// TODO Auto-generated method stub
		window.remove(SuperCabbage);
		// change caterpillar color
		cp.changeColorChar('s');
		cp.resetDirection();
	}

}
