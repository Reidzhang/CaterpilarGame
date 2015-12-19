package com.company;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import uwcse.graphics.GWindow;
import uwcse.graphics.Oval;

public class ColorCabbage extends Cabbage {

	public ColorCabbage(GWindow window, Point center) {
		super(window, center);
		draw();
		// TODO Auto-generated constructor stub
	}

	private ArrayList<Oval> colorCabbages = new ArrayList<Oval>();

	protected void draw() {
		// TODO Auto-generated method stub
		Oval colorCabbage;
		for (int i = CABBAGE_RADIUS; i >= 0; i--) {
			Color c;
			if (i < 4)
				c = Color.YELLOW;
			else if (i < 7)
				c = Color.ORANGE;
			else
				c = Color.BLACK;
			colorCabbage = new Oval(center.x - i, center.y - i, 2 * i, 2 * i,
					c, true);
			colorCabbages.add(colorCabbage);
			window.add(colorCabbage);
		}
	}

	@Override
	public void isEatenBy(Caterpillar cp) {
		cp.changeColorChar('c');
		for (int i = 0; i < CABBAGE_RADIUS; i++) {
			window.remove(colorCabbages.get(i));

		}

		cp.north = SOUTH;
		cp.east = WEST;
		cp.west = EAST;
		cp.south = NORTH;
		cp.shrink();
		cp.timeIdentifier = cp.timeCounter;

		window.stopTimerEvents();
		CaterpillarGame.animation_Period += 5;
		window.startTimerEvents(CaterpillarGame.animation_Period);

	}

}
