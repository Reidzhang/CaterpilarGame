package com.company;
import uwcse.graphics.*;

import java.util.*;
import java.awt.Color;
import java.awt.Point;
import javax.swing.JOptionPane;

/**
 * A CaterpillarGame displays a garden that contains good and bad cabbages and a
 * constantly moving caterpillar. The player directs the moves of the
 * caterpillar. Every time the caterpillar eats a cabbage, the caterpillar
 * grows. The player wins when all of the good cabbages are eaten and the
 * caterpillar has left the garden. The player loses if the caterpillar eats a
 * bad cabbage or crawls over itself.
 */

/**
 * 
 * @author Wei-Wen Cheng + Zhitao Zhang
 * 
 */
public class CaterpillarGame extends GWindowEventAdapter implements
		CaterpillarGameConstants
// The class inherits from GWindowEventAdapter so that it can handle key events
// (in the method keyPressed), and timer events.
// All of the code to make this class able to handle key events and perform
// some animation is already written.
{

	public static int animation_Period = ANIMATION_PERIOD;

	// Game window
	private GWindow window;

	// The caterpillar
	private Caterpillar cp1;
	private Caterpillar cp2;

	// Direction of motion given by the player
	private int dirFromKeyboard1;
	private int dirFromKeyboard2;

	// Do we have a keyboard event
	private boolean isKeyboardEventNew1 = false;
	private boolean isKeyboardEventNew2 = false;

	// The list of all the cabbages
	private ArrayList<Cabbage> cabbages;

	// is the current game over?
	private boolean gameOver = false;

	private String messageGameOver;

	private ArrayList<Rectangle> wall = new ArrayList<Rectangle>();

	/**
	 * Constructs a CaterpillarGame
	 */
	public CaterpillarGame() {
		// Create the graphics window
		window = new GWindow("Caterpillar game", WINDOW_WIDTH, WINDOW_HEIGHT);

		window.setExitOnClose();
		// Any key or timer event while the window is active is sent to this
		// CaterpillarGame
		window.addEventHandler(this);

		// Display the game rules
		JOptionPane
				.showMessageDialog(
						null,
						"Welcome!\n"
								+ "Eat all of the WHITE Cabbage heads,\n"
								+ "and exit the garden\n"
								+ "Don't crawl over yourself\n"
								+ "To move left,press\"a\"\n"
								+ "To move right,press\"d\"\n"
								+ "To move up, press\"w\"\n"
								+ "To move down, press\"s\"\n\n"
								+ "Blue cabbage is super cabbage, you can crawl over yourself and pass through the fence until you eat another non-super cabbage\n"
								+ "Yellow cabbages slow you down and make you colorful and shrink, they changes the direction from left to right and up to down\n"
								+ "White cabbages make your grow and increase the speed\n"
								+ "Black cabbages make you die unless you're super, if you are super, black cabbages make your shrink\n",
						"Caterpillar Game Rule",
						JOptionPane.INFORMATION_MESSAGE);

		// Set up the game (fence, cabbages, caterpillar)
		initializeGame();
	}

	/**
	 * Initializes the game (draw the garden, garden fence, cabbages,
	 * caterpillar)
	 */
	private void initializeGame() {
		// Clear the window
		window.erase();

		// New game
		gameOver = false;

		// No keyboard event yet
		isKeyboardEventNew1 = false;

		isKeyboardEventNew2 = false;

		// Background (the garden)
		window.add(new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT,
				Color.WHITE, true));

		// Create the fence around the garden
		Rectangle wall1 = new Rectangle(WINDOW_WIDTH * 2 / 5, 0, WALL_WIDTH,
				WINDOW_HEIGHT / 2 - GARDEN_DOOR_WIDTH / 2, Color.BLACK, true);
		Rectangle wall2 = new Rectangle(WINDOW_WIDTH * 2 / 5, 0,
				WINDOW_WIDTH * 3 / 5, WALL_WIDTH, Color.BLACK, true);
		Rectangle wall3 = new Rectangle(WINDOW_WIDTH - WALL_WIDTH, 0,
				WALL_WIDTH, WINDOW_HEIGHT, Color.BLACK, true);
		Rectangle wall4 = new Rectangle(WINDOW_WIDTH * 2 / 5, WINDOW_HEIGHT
				- WALL_WIDTH, WINDOW_WIDTH * 3 / 5, WALL_WIDTH, Color.BLACK,
				true);
		Rectangle wall5 = new Rectangle(WINDOW_WIDTH * 2 / 5, WINDOW_HEIGHT / 2
				+ GARDEN_DOOR_WIDTH / 2, WALL_WIDTH, WINDOW_HEIGHT / 2
				- GARDEN_DOOR_WIDTH / 2, Color.BLACK, true);
		// // Create the fence around the garden
		// Rectangle wall1 = new Rectangle(200, 0, WALL_WIDTH, 175, Color.BLACK,
		// true);
		// Rectangle wall2 = new Rectangle(200 + WALL_WIDTH, 0,
		// 300 - 2 * WALL_WIDTH, WALL_WIDTH, Color.BLACK, true);
		// Rectangle wall3 = new Rectangle(500 - WALL_WIDTH, 0, WALL_WIDTH, 500,
		// Color.BLACK, true);
		// Rectangle wall4 = new Rectangle(200, 500 - WALL_WIDTH,
		// 300 - WALL_WIDTH, WALL_WIDTH, Color.BLACK, true);
		// Rectangle wall5 = new Rectangle(200, 325, WALL_WIDTH, 175 -
		// WALL_WIDTH,
		// Color.BLACK, true);
		wall.add(wall1);
		wall.add(wall2);
		wall.add(wall3);
		wall.add(wall4);
		wall.add(wall5);
		for (int i = 0; i < wall.size(); i++) {
			window.add(wall.get(i));
		}

		// Cabbages
		// Initialize the elements of the ArrayList = cabbages
		// (they should not overlap and be in the garden) ....
		cabbages = new ArrayList<Cabbage>(N_GOOD_CABBAGES + N_BAD_CABBAGES
				+ N_COLOR_CABBAGES);
		for (int i = 0; i < N_GOOD_CABBAGES + (int) (Math.random() * 5) - 2; i++) {
			Point p = cabbageCenter(cabbages);
			GoodCabbage gc = new GoodCabbage(window, p);
			cabbages.add(gc);
		}
		for (int i = 0; i < N_BAD_CABBAGES + (int) (Math.random() * 5) - 2; i++) {
			Point p = cabbageCenter(cabbages);
			BadCabbage bc = new BadCabbage(window, p);
			cabbages.add(bc);
		}
		for (int i = 0; i < N_COLOR_CABBAGES + (int) (Math.random() * 5) - 2; i++) {
			Point p = cabbageCenter(cabbages);
			ColorCabbage cc = new ColorCabbage(window, p);
			cabbages.add(cc);
		}
		for (int i = 0; i < N_SUPER_CABBAGES + (int) (Math.random() * 3) - 1; i++) {
			Point p = cabbageCenter(cabbages);
			SuperCabbage sc = new SuperCabbage(window, p);
			cabbages.add(sc);
		}

		// Create the caterpillar
		do{
		cp1 = new Caterpillar(window, Color.CYAN);
		cp2 = new Caterpillar(window, Color.DARK_GRAY);
		}while(cp1.getHead().y==cp2.getHead().y);

		// start timer events (to do the animation)
		this.window.startTimerEvents(animation_Period);
	}

	/**
	 * create a point inside the garden
	 * 
	 * @return
	 */
	private Point cabbageCenter(ArrayList<Cabbage> cabbages) {
		Point p;
		boolean b;
		do {
			b = false;
			p = new Point(
					(int) (Math.random()
							* (WINDOW_WIDTH / 5 * 3 - 2 * WALL_WIDTH - 2 * CABBAGE_RADIUS)
							+ WINDOW_WIDTH / 5 * 2 + WALL_WIDTH + CABBAGE_RADIUS),
					(int) (Math.random() * (WINDOW_HEIGHT - 2 * WALL_WIDTH - 2 * CABBAGE_RADIUS))
							+ WALL_WIDTH + CABBAGE_RADIUS);
			for (int j = 0; j < cabbages.size(); j++) {
				if (((Cabbage) cabbages.get(j)).overlap(p))
					b = true;
			}
		} while (b);
		return p;
	}

	/**
	 * Moves the caterpillar within the graphics window every ANIMATION_PERIOD
	 * milliseconds.
	 * 
	 * @param e
	 *            the timer event
	 */
	public void timerExpired(GWindowEvent e) {
		// Did we get a new direction from the user?
		// Use isKeyboardEventNew to take the event into account
		// only once

		if (isKeyboardEventNew1) {
			isKeyboardEventNew1 = false;
			cp1.move(dirFromKeyboard1);
		} else
			cp1.move();

		if (isKeyboardEventNew2) {
			isKeyboardEventNew2 = false;
			cp2.move(dirFromKeyboard2);
		} else
			cp2.move();

		// Is it crawling over itself?
		if (cp1.isCrawlingOverItself() && !cp1.getIfIsSuper()) {
			messageGameOver = "LOSER\nCaterpillar crawl over itself";
			gameOver = true;
		} else if (intersectWithFence1() && !cp1.getIfIsSuper()) {
			messageGameOver = "LOSER\n Caterpillar crawl over the fence";
			gameOver = true;
		}
		if (cp2.isCrawlingOverItself() && !cp2.getIfIsSuper()) {
			messageGameOver = "LOSER\nCaterpillar crawl over itself";
			gameOver = true;
		} else if (intersectWithFence2() && !cp2.getIfIsSuper()) {
			messageGameOver = "LOSER\nCaterpillar crawl over the fence";
			gameOver = true;
		}

		// Is the caterpillar eating a cabbage?
		Point head1 = cp1.getHead();
		Point head2 = cp2.getHead();

		for (int i = 0; i < cabbages.size(); i++) {
			if (cabbages.get(i).isPointInCabbage(head1)) {

				cabbages.get(i).isEatenBy(cp1);

				if (cabbages.get(i) instanceof SuperCabbage) {
					cp1.becomeSuper();
					 cabbages.remove(cabbages.get(i));

				} else if (cabbages.get(i) instanceof BadCabbage) {
					if (cp1.getIfIsSuper()) {
						((BadCabbage) cabbages.get(i))
								.removeBadCabbageFromWindow();
						cp1.notSuper();
					} else {
						messageGameOver = "LOSER\nYou eat the bad cabbage";
						gameOver = true;
					}
					// break;
				} else {

					// isSuper becomes false whenever caterpillar eats some
					// other cabbages
					cp1.notSuper();
					cabbages.remove(cabbages.get(i));

				}

			} else if (cabbages.get(i).isPointInCabbage(head2)) {

				cabbages.get(i).isEatenBy(cp2);

				if (cabbages.get(i) instanceof SuperCabbage) {
					cp2.becomeSuper();
					 cabbages.remove(cabbages.get(i));
				} else if (cabbages.get(i) instanceof BadCabbage) {
					if (cp2.getIfIsSuper()) {
						((BadCabbage) cabbages.get(i))
								.removeBadCabbageFromWindow();
						cp2.notSuper();
					} else {
						messageGameOver = "LOSER\nYou eat the bad cabbage";
						gameOver = true;
					}
					// break;
				} else {

					// isSuper becomes false whenever caterpillar eats some
					// other cabbages
					cp2.notSuper();
					cabbages.remove(cabbages.get(i));

				}

			}
		}
		
		if(crowingWithOthers()){
			messageGameOver = "You Lose!";
			endTheGame();
		}

		// (do all of these checks in a private method)...
		int count = 0;
		for (int i = 0; i < cabbages.size(); i++) {
			if (cabbages.get(i) instanceof GoodCabbage)
				count++;
		}

		if (gameOver) {
			endTheGame();
		} else if (cp1.isOutsideGarden() && cp2.isOutsideGarden() && count == 0) {
			messageGameOver = "^_^ YOU GUYS WIN!!!";
			endTheGame();
		}

	}

	/**
	 * The game is over. Starts a new game or ends the application
	 */
	private void endTheGame() {
		cp1.timeCounter = 0;
		cp1.timeIdentifier = 0;
		cp2.timeCounter = 0;
		cp2.timeIdentifier = 0;
		animation_Period = ANIMATION_PERIOD;
		cp1.notSuper();
		cp2.notSuper();
		cp1.resetDirection();
		cp2.resetDirection();

		window.stopTimerEvents();
		// messageGameOver is an instance String that
		// describes the outcome of the game that just ended
		// (e.g. congratulations! you win)
		boolean again = anotherGame(messageGameOver);

		if (again) {
			initializeGame();
		} else {
			System.exit(0);
		}

	}

	/**
	 * Moves the caterpillar according to the selection of the user i: NORTH, j:
	 * WEST, k: EAST, m: SOUTH
	 * 
	 * @param e
	 *            the keyboard event
	 */
	public void keyPressed(GWindowEvent e) {
		char c = e.getKey();

		switch (Character.toLowerCase(c)) {
		case 'w':
			dirFromKeyboard1 = cp1.north;
			isKeyboardEventNew1 = true;

			break;
		case 'a':
			dirFromKeyboard1 = cp1.west;
			isKeyboardEventNew1 = true;
			break;
		case 'd':
			dirFromKeyboard1 = cp1.east;
			isKeyboardEventNew1 = true;
			break;
		case 's':
			dirFromKeyboard1 = cp1.south;
			isKeyboardEventNew1 = true;
			break;
		case 'q':
			System.exit(0);
			break;
		case 'c':
			this.window.stopTimerEvents();
			break;
		default:
			isKeyboardEventNew1 = false;
		}

		switch (Character.toLowerCase(c)) {
		case 'i':
			dirFromKeyboard2 = cp2.north;
			isKeyboardEventNew2 = true;

			break;
		case 'j':
			dirFromKeyboard2 = cp2.west;
			isKeyboardEventNew2 = true;
			break;
		case 'l':
			dirFromKeyboard2 = cp2.east;
			isKeyboardEventNew2 = true;
			break;
		case 'k':
			dirFromKeyboard2 = cp2.south;
			isKeyboardEventNew2 = true;
			break;
		default:
			isKeyboardEventNew2 = false;
		}

		// isKeyboardEventNew1 = true;
		// isKeyboardEventNew2 = true;
		//
		// switch (Character.toLowerCase(e.getKey())) {
		// case 'w':
		// dirFromKeyboard1 = cp1.north;
		// isKeyboardEventNew2 = false;
		//
		// break;
		// case 'a':
		// dirFromKeyboard1 = cp1.west;
		// isKeyboardEventNew2 = false;
		//
		// break;
		// case 'd':
		// dirFromKeyboard1 = cp1.east;
		// isKeyboardEventNew2 = false;
		//
		// break;
		// case 's':
		// dirFromKeyboard1 = cp1.south;
		// isKeyboardEventNew2 = false;
		//
		// case 'i':
		// dirFromKeyboard2 = cp2.north;
		// isKeyboardEventNew1 = false;
		//
		// break;
		// case 'j':
		// dirFromKeyboard2 = cp2.west;
		// isKeyboardEventNew1 = false;
		//
		// break;
		// case 'l':
		// dirFromKeyboard2 = cp2.east;
		// isKeyboardEventNew1 = false;
		//
		// break;
		// case 'k':
		// dirFromKeyboard2 = cp2.south;
		// isKeyboardEventNew1 = false;
		//
		// break;
		// case 'q':
		// System.exit(0);
		// break;
		// case 'c':
		// this.window.stopTimerEvents();
		// break;
		// default:
		// isKeyboardEventNew1 = false;
		// isKeyboardEventNew2 = false;
		// }

	}

	/**
	 * Does the player want to play again?
	 */
	private boolean anotherGame(String s) {
		int choice = JOptionPane.showConfirmDialog(null, s
				+ "\nDo you want to play again?", "Game over",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (choice == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}

	/**
	 * check if the caterpillar1 is crossing the fence
	 */
	private boolean intersectWithFence1() {
		Rectangle r1 = new Rectangle(cp1.getHead().x - CATERPILLAR_WIDTH / 2,
				cp1.getHead().y - CATERPILLAR_WIDTH / 2, CATERPILLAR_WIDTH,
				CATERPILLAR_WIDTH);
		for (int i = 0; i < wall.size(); i++) {
			if (wall.get(i).intersects(r1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * check if the caterpillar2 is crossing the fence
	 */
	private boolean intersectWithFence2() {
		Rectangle r2 = new Rectangle(cp2.getHead().x - CATERPILLAR_WIDTH / 2,
				cp2.getHead().y - CATERPILLAR_WIDTH / 2, CATERPILLAR_WIDTH,
				CATERPILLAR_WIDTH);
		for (int i = 0; i < wall.size(); i++) {
			if (wall.get(i).intersects(r2)) {
				return true;
			}
		}
		return false;
	}
	private boolean crowingWithOthers(){
		boolean result = false;
		if(cp1.crawingOverOthers(cp2.getHead())||cp2.crawingOverOthers(cp1.getHead())){
			result = true;
		}
				return result;
	}

	/**
	 * Starts the application
	 */
	public static void main(String[] args) {
		new CaterpillarGame();
	}
}