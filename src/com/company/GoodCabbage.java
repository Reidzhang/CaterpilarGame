package com.company;
import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Oval;

public class GoodCabbage extends Cabbage {

	private Oval goodCabbage;

	public GoodCabbage(GWindow window, Point center) {
		super(window, center);
		draw();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void draw() {
		// TODO Auto-generated method stub
		goodCabbage = new Oval(center.x - CABBAGE_RADIUS, center.y
				- CABBAGE_RADIUS, 2 * CABBAGE_RADIUS, 2 * CABBAGE_RADIUS,
				Color.red, true);
		window.add(goodCabbage);
	}

	@Override
	public void isEatenBy(Caterpillar cp) {
		cp.grow();
		// cp.shrink();
		window.remove(goodCabbage);
		// change caterpillar color
		cp.changeColorChar('g');
		cp.resetDirection();

		if (CaterpillarGame.animation_Period > 45) {
			window.stopTimerEvents();
			CaterpillarGame.animation_Period -= 5;
			window.startTimerEvents(CaterpillarGame.animation_Period);
		}
	}

}
