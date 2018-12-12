package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SnakeGame {

	Random rand = ThreadLocalRandom.current(); 
	/**
	 * This is the world in which the snake head has to find its parts. (It's mostly a List!).
	 */
	World world;
	/**
	 * The player goes seeking for its body parts
	 */
	SnakeHead player;

	/**
	 * These are the missing parts!
	 */
	List<SnakePart> missing;

	/**
	 * These are parts we've found!
	 */
	List<SnakePart> found;

	/**
	 * Number of steps!
	 */
	int stepsTaken;

	PowerUps powerup;

	/**
	 * Score!
	 */
	int score;

	int z;
	int u;
	int highScore=0;
	boolean gameOver1 = false;

	/**
	 * Create a SnakeGame of a particular size.
	 * 
	 */
	public SnakeGame(int w, int h) {
		world = new World(w, h);

		missing = new ArrayList<SnakePart>();
		found = new ArrayList<SnakePart>();

		z = 10;
		u = 8;

		// Make the player out of the 0th fish color.
		player = new SnakeHead(world);
		world.register(player);

		// Generate snake parts of all the colors but the first into the "missing" List.
		for (int ft = 1; ft < SnakePart.COLORS.length; ft++) {
			SnakePart friend = world.insertSnakePartRandomly(ft);
			missing.add(friend);
		}
		for (int i=0; i<2; i++) {
			PowerUps powerup = new PowerUps(world);
			world.insertRandomly(powerup);
		}
	}

	/**
	 * How you win: if missingFishLeft() == 0.
	 * 
	 * @return the size of the missing list.
	 */
	public int missingFishLeft() {
		return missing.size();
	}

	/**
	 * This method is how the P5 app tells whether we're done.
	 * 
	 * @return true if the player has won (or maybe lost?).
	 */
	public boolean gameOverLOSE() {


		if (player.getX() < 0 || player.getX() >= world.getWidth() || player.getY() < 0
				|| player.getY() >= world.getHeight()) {

			return true;
		} return false;


		// if you collect all of the snake parts
		//return missing.isEmpty();

	}
	public boolean gameOverLOSE2() {
		if (gameOver1 == false) {
			//do nothing
			return false;
		}
		else {
			return true;
		}

	}

	public boolean gameOverWIN() {
		return missing.isEmpty();
	}

	/**
	 * Update positions of everything (the user has just pressed a button).
	 * @param moved 
	 */
	public void step(boolean moved) {

		// Keep track of how long the game has run.
		this.stepsTaken += 1;

		//Insert new snake part randomly
		if (rand.nextInt(50) == 0) {
			for (int ft = 1; ft < (SnakePart.COLORS.length)/4; ft++) {
				SnakePart friendRand = world.insertSnakePartRandomly(ft);
				missing.add(friendRand);
			}
		}

		//Insert new powerup randomly
		if (rand.nextInt(75) == 0) {
			this.world.insertRandomly(new PowerUps(world));
		}

		// These are all the objects in the world in the same cell as the player.
		List<WorldObject> overlap = this.player.findSameCell();
		// The player is there, too, let's skip them.
		overlap.remove(this.player);

		// If we find a snake, remove it from missing.
		for (WorldObject wo : overlap) {
			// It is missing if it's in our missing list.
			if (missing.contains(wo)) {
				// Remove this fish from the missing list.
				missing.remove(wo);

				SnakePart justFound = (SnakePart) wo;
				found.add(justFound);

				// Increase score when you find a snake part!
				score += 10;

			} else if (found.contains(wo) && this.player.recentPositions.size() > found.size()) {
				// you've just overlapped 
				// game over!
				gameOver1= true;


			}
			// Points for Powerups
			//If player comes across powerups, get 20 pts and fish food disappear from world
			if (wo instanceof PowerUps) {
				score += 20;
				world.remove(wo);
			}
		}

		// When snake part get added to "found" they will follow the player around.
		World.objectsFollow(player, found);
		// Step any world-objects that run themselves.
		world.stepAll();
	}

	/**
	 * This gets a click on the grid. We want it to destroy rocks that ruin the
	 * game.
	 * 
	 * @param x - the x-tile.
	 * @param y - the y-tile.
	 */
	public void click(int x, int y) {

	}

}
