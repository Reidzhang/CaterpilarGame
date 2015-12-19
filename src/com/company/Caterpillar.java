package com.company;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import uwcse.graphics.GWindow;
import uwcse.graphics.Rectangle;

/**
 * A Caterpillar is the representation and the display of a caterpillar
 */

public class Caterpillar implements CaterpillarGameConstants {

	public int timeCounter;

	public int timeIdentifier;

	public int north = 1;

	public int east = 2;

	public int west = 3;

	public int south = 4;

	private Color caterpillarColor;
	// the char represents the color of the caterpillar
	private char colorChar;

	// The body of a caterpillar is made of Points stored
	// in an ArrayList
	private ArrayList<Point> body = new ArrayList<Point>();

	// Store the graphical elements of the caterpillar body
	// Useful to erase the body of the caterpillar on the screen
	private ArrayList<Rectangle> bodyUnits = new ArrayList<Rectangle>();

	// The window the caterpillar belongs to
	private GWindow window;

	// Direction of motion of the caterpillar (EAST initially)
	private int dir = this.east;

	private boolean isSuper = false;

	// Length of a unit of the body of the caterpillar
	// MUST be equal to the distance covered by the caterpillar
	// at every step of the animation.
	private final int bodyUnitLength = STEP;

	// Width of a unit of the body of the caterpillar
	private final int bodyUnitWidth = CATERPILLAR_WIDTH;

	/**
	 * Constructs a caterpillar
	 * 
	 * @param window
	 *            the graphics where to draw the caterpillar.
	 */
	public Caterpillar(GWindow window, Color color) {
		// Initialize the graphics window for this Caterpillar
		this.window = window;
		this.caterpillarColor = color;

		// Create the caterpillar (10 points initially)
		// First point
		Point p = new Point();
		p.x = 5;
		p.y = (int) (WINDOW_HEIGHT / 5 * Math.random() * 5);
		body.add(p);

		// Other points
		for (int i = 0; i < 9; i++) {
			Point q = new Point(p);
			q.translate(STEP, 0);
			body.add(q);
			p = q;
		}

		// Other initializations (if you have more instance fields)

		// Display the caterpillar (call a private method)
		// Show the caterpillar
		draw();
	}

	/**
	 * Draw the caterpillar in the graphics window
	 */
	private void draw() {
		// Connect with Rectangles the points of the body
		Point p = body.get(0);

		for (int i = 1; i < body.size(); i++) {
			Point q = body.get(i);
			// add a body unit between p and q
			addBodyUnit(p, q, bodyUnits.size());
			p = q;
		}

		window.doRepaint();
	}

	/**
	 * Moves the caterpillar in the current direction (complete)
	 */
	public void move() {
		move(dir);
		if (this.timeCounter++ == this.timeIdentifier + 20) {
			this.resetDirection();
			if (colorChar == 'c')
				// change caterpillar color
				colorChar = 'g';
		}
	}

	/**
	 * Move the caterpillar in the direction newDir. <br>
	 * If the new direction is illegal, select randomly a legal direction of
	 * motion and move in that direction.<br>
	 * 
	 * @param newDir
	 *            the new direction.
	 */
	public void move(int newDir) {

		// reset the direction if the caterpillar's head is hitting its neck
		newDir = resetDirectionIfHeadMovingTowardNeck(newDir);

		// Is the move illegal?
		boolean isMoveNotOK = false;

		// newDir might not be legal
		// Before trying a random direction, try first
		// the current direction of motion (if not newDir)
		boolean isFirstTry = true;

		Point head;

		// move the caterpillar in direction newDir
		do {
			// new position of the head
			head = getHead();
			switch (newDir) {
			case NORTH:
				head.y -= STEP;
				break;
			case EAST:
				head.x += STEP;
				break;
			case SOUTH:
				head.y += STEP;
				break;
			case WEST:
				head.x -= STEP;
				break;
			}

			// Is the new position in the window?
			if (isPointInTheWindow(head)) {
				// update the position of the caterpillar
				body.add(head);
				body.remove(0);
				isMoveNotOK = false;
			} else {
				// Select another direction
				isMoveNotOK = true;
				if (isFirstTry && newDir != dir) {
					newDir = dir;
					isFirstTry = false;
				} else { // random selection
					int[] adir = { this.north, this.south, this.west, this.east };
					newDir = adir[(int) (Math.random() * adir.length)];
					newDir = resetDirectionIfHeadMovingTowardNeck(newDir);
				}
			}
		} while (isMoveNotOK);

		// Update the current direction of motion
		dir = newDir;
		window.suspendRepaints();
		// Show the new location of the caterpillar
		moveCaterpillarOnScreen();
		window.resumeRepaints();
		
	}

	/**
	 * if the caterpillar is moving in the opposite direction it was moving, we
	 * make it an invalid movement because it might crawl over itself
	 * 
	 * @param newDir
	 * @return
	 */
	private int resetDirectionIfHeadMovingTowardNeck(int newDir) {
		Point head = getHead();
		Point neck = body.get(body.size() - 2);
		if (head.x > neck.x && newDir == WEST) {
			newDir = EAST;
		} else if (head.x < neck.x && newDir == EAST) {
			newDir = WEST;
		} else if (head.y < neck.y && newDir == SOUTH) {
			newDir = NORTH;
		} else if (head.y > neck.y && newDir == NORTH) {
			newDir = SOUTH;
		}
		return newDir;
	}

	/**
	 * Move the caterpillar on the screen
	 */
	private void moveCaterpillarOnScreen() {
		// Erase the body unit at the tail
		Rectangle r = bodyUnits.remove(0);
		window.remove(r);
		// Add a new body unit at the head
		Point p = body.get(body.size() - 1);
		Point q = body.get(body.size() - 2);
		addBodyUnit(p, q, bodyUnits.size());

		window.remove(bodyUnits.get(bodyUnits.size() - 2));
		bodyUnits.get(bodyUnits.size() - 2).setColor(caterpillarColor());
		window.add(bodyUnits.get(bodyUnits.size() - 2));

		window.remove(bodyUnits.get(bodyUnits.size() - 1));
		bodyUnits.get(bodyUnits.size() - 1).setColor(Color.BLACK);
		window.add(bodyUnits.get(bodyUnits.size() - 1));

		// show it
		window.doRepaint();
	}

	/**
	 * Add a body unit to the caterpillar. The body unit connects Point p and
	 * Point q.<br>
	 * Insert this body unit at position index in bodyUnits.<br>
	 * e.g. 0 to insert at the tail and bodyUnits.size() to insert at the head.
	 */
	private void addBodyUnit(Point p, Point q, int index) {
		// Connect p and q with a rectangle.
		// To allow for a smooth look of the caterpillar, p and q
		// are not on the edges of the Rectangle

		// Upper left corner of the rectangle
		int x = Math.min(q.x, p.x) - bodyUnitWidth / 2;
		int y = Math.min(q.y, p.y) - bodyUnitWidth / 2;

		// Width and height of the rectangle (vertical or horizontal rectangle?)
		int width = ((q.y == p.y) ? (bodyUnitLength + bodyUnitWidth)
				: bodyUnitWidth);
		int height = ((q.x == p.x) ? (bodyUnitLength + bodyUnitWidth)
				: bodyUnitWidth);

		// Create the rectangle and place it in the window
		Rectangle r = new Rectangle(x, y, width, height, caterpillarColor(),
				true);

		window.add(r);

		// keep track of that rectangle (we will erase it at some point)
		bodyUnits.add(index, r);

	}

	/**
	 * Is the caterpillar crawling over itself?
	 * 
	 * @return true if the caterpillar is crawling over itself and false
	 *         otherwise.
	 */
	public boolean isCrawlingOverItself() {
		// Is any point of the body equal to any other point of the caterpillar?
		for (int i = 0; i < body.size(); i++) {
			for (int j = i + 1; j < body.size(); j++) {
				if (body.get(i).equals(body.get(j)))
					return true;
			}
		}

		return false;
	}

	/**
	 * Are all of the points of the caterpillar outside the garden
	 * 
	 * @return true if the caterpillar is outside the garden and false
	 *         otherwise.
	 */
	public boolean isOutsideGarden() {
		for (int i = 0; i < body.size(); i++) {
			if (body.get(i).getX() > WINDOW_WIDTH * 2 / 5) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Is Point p in the window?
	 */
	private boolean isPointInTheWindow(Point p) {
		return (p.x >= 0 && p.x <= window.getWindowWidth() && p.y >= 0 && p.y <= window
				.getWindowHeight());
	}

	/**
	 * Return the location of the head of the caterpillar (complete)
	 * 
	 * @return the location of the head of the caterpillar.
	 */
	public Point getHead() {
		return new Point((Point) body.get(body.size() - 1));
	}

	/**
	 * Increase the length of the caterpillar (by GROWTH_SPURT elements) Add the
	 * elements at the tail of the caterpillar.
	 */
	public void grow() {
		Point first = body.get(0);
		Point second = body.get(1);

		int x = 0;
		int y = 0;

		if (first.x > second.x) {
			x = STEP;
		} else if (first.x < second.x) {
			x = -STEP;
		} else if (first.y < second.y) {
			y = -STEP;
		} else if (first.y > second.y) {
			y = STEP;
		}

		for (int i = 1; i <= GROWTH_SPURT; i++) {

			Point q = new Point(first.x + x * i, first.y + y * i);
			body.add(0, q);
			// Add a new body unit at the tail
			addBodyUnit(body.get(0), body.get(1), 0);
		}
	}

	/**
	 * returns different color for caterpillar, if the caterpillar eats color
	 * cabbage, we make random color
	 */
	private Color caterpillarColor() {
		switch (colorChar) {
		case 'c':
			return new Color((int) (Math.random() * 256),
					(int) (Math.random() * 256), (int) (Math.random() * 256));
		case 's':
			return Color.blue;
		default:
			return this.caterpillarColor;
		}

	}

	// /**
	// * change the color of the caterpillar. It differs when caterpillar eat
	// * different cabbages
	// */
	// public void changeColor(char colorChar) {
	// this.colorChar = colorChar;
	// }

	/**
	 * the caterpillar reduces its size. It cannot keep shrinking because
	 * ultimately caterpillar may disappear. Its size is at least two because in
	 * other codes we compare its head and the point next to head.
	 */
	public void shrink() {
		if (body.size() > 2) {
			body.remove(0);
			window.remove(bodyUnits.get(0));
			bodyUnits.remove(0);
		}
	}

	/**
	 * reset the direction so the direction is back to normal
	 */
	public void resetDirection() {
		north = NORTH;
		east = EAST;
		west = WEST;
		south = SOUTH;
	}

	public void notSuper() {
		isSuper = false;
	}

	public void becomeSuper() {
		isSuper = true;
	}

	public boolean getIfIsSuper() {
		return isSuper;
	}
	
	public void changeColorChar(char c) {
		this.colorChar = c;
	}
	public boolean crawingOverOthers(Point p){
		boolean result = false ;
		for(int i=0;i<body.size();i++){
			if(body.get(i).distance(p)<CATERPILLAR_WIDTH){
				result = true;
				break;
			}
		}
		return result;
	}

}