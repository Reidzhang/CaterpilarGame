package com.company;
/**
 * Interface that lists useful constants for the caterpillar game
 */

public interface CaterpillarGameConstants {
	// Possible directions for the motion of the caterpillar
	public static final int NORTH = 1;

	public static final int EAST = 2;

	public static final int WEST = 3;

	public static final int SOUTH = 4;

	// Distance covered by the caterpillar in one move
	public static final int STEP = 10;

	// Number of body elements added to the caterpillar when it grows
	// (after eating a good cabbage)
	public static final int GROWTH_SPURT = 3;

	// Thickness of the caterpillar
	public static final int CATERPILLAR_WIDTH = 6;

	// Number of good cabbages
	public static final int N_GOOD_CABBAGES = 5;

	// Number of bad cabbages
	public static final int N_BAD_CABBAGES = 3;

	// Number of color cabbages
	public static final int N_COLOR_CABBAGES = 5;

	// Number of super cabbages
	public static final int N_SUPER_CABBAGES = 2;

	// Radius of a cabbage head
	public static final int CABBAGE_RADIUS = 8;

	// Size of the graphics window
	public static final int WINDOW_HEIGHT = 800;

	public static final int WINDOW_WIDTH = 1300;

	// door of the garden
	public static final int GARDEN_DOOR_WIDTH = 150;


	// Period of the animation (in ms)
	public static final int ANIMATION_PERIOD = 60;

	public static final int WALL_WIDTH = 15;
}