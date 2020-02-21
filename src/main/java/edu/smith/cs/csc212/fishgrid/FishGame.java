package edu.smith.cs.csc212.fishgrid;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class manages our model of gameplay: missing and found fish, etc.
 * @author jfoley
 *
 */
public class FishGame {
	/**
	 * This is the world in which the fish are missing. (It's mostly a List!).
	 */
	World world;
	/**
	 * The player (a Fish.COLORS[0]-colored fish) goes seeking their friends. !!
	 *point of the games!
	 */
	Fish player;
	/**
	 * The home location.
	 */
	FishHome home;
	/**
	 * the missing fish
	 */
	List<Fish> missing; //simple list creation!
	
	/** fish we've found
	 */
	List<Fish> found; 
	//hearts collected
	List<Heart> heartsC;
	List<Heart> missingH;
	List<Fish> homeL;
	/**
	 * Number of steps!
	 */
	int stepsTaken; //+1 every time move or click screen
	int score;
	int NUM_ROCKS;
	int rockX;
	int rockY;
	/**
	 * Create a FishGame of a particular size.
	 * @param w how wide is the grid?
	 * @param h how tall is the grid?
	 */
	public FishGame(int w, int h) {
		world = new World(w, h); //create new world! 
		Random rand = ThreadLocalRandom.current(); 
			//really tried to not rewrite this code but world. or this. didn't work :(
		missing = new ArrayList<Fish>(); //making an array of the list w fish
		found = new ArrayList<Fish>();
		heartsC = new ArrayList<Heart>();
		missingH = new ArrayList<Heart>();
		homeL = new ArrayList<Fish>();
		NUM_ROCKS = rand.nextInt(8);
		
		// Add a home!
		home = world.insertFishHome(); //little house
		
		// random rocks
		for (int i=0; i<NUM_ROCKS; i++) {
			Rock rk = world.insertRockRandomly(i);
 			rockX = rk.getX();
			rockY = rk.getY();
		}
		
		// random snails
		for (int i=0; i<6; i++) {
			Snail sl = world.insertSnailRandomly();
		}
		for (int i=0; i<6; i++) {
			Heart ht = world.insertHeartRandomly(i);
			missingH.add(ht);
		}
		
		// Make the player out of the 0th fish color. !!
		player = new Fish(0, world);
		int homeX = home.getX();
		int homeY = home.getY();
		
		// Start the player at "home".
		player.setPosition(homeX, homeY); //set initial position to home
		player.markAsPlayer(); //bool that this (fish) = player (true!)
		world.register(player); //console
		int playerX = player.getX();
		int playerY = player.getY();
		
//		if (playerX==homeX && playerY==homeY) {
//			for (int i=0; i<found.size(); i++) {
//				
//				.setPosition(homeX, homeY);
//				
//				world.remove(i);
//			}
//			}
//			if (this.player.isSameSpot(this.home)) {
//				for (int i=0; i<this.found.size(); i++) {
//					moveToHome(this.found.get(i));
//		}
		// Generate fish of all the colors except the first (which is why start @ 1)
		//into the "missing" List.
		for (int ft = 1; ft < Fish.COLORS.length; ft++) { 
			Fish friend = world.insertFishRandomly(ft);
			missing.add(friend);
		}		
	}
	
	
	/**
	 * game is over if missingFishLeft() == 0. aka all been found
	 * or in found list
	 * @return the size of the missing list.
	 */
	public int missingFishLeft() {
		return missing.size();
	}
	
	/**
	 * This method is how the Main app tells whether we're done.
	 * @return true if the player has won (or maybe lost?).
	 */
	public boolean gameOver() {
		
		return missing.isEmpty();
	}

	/**
	 * Update positions of everything (the user has just pressed a button).
	 */
	public void step() {
		// Keep track of how long the game has run.
		this.stepsTaken += 1;
				
		//all the objects in the world in the same cell as the player.
		List<WorldObject> overlap = this.player.findSameCell();
		// The player is there, too, let's skip them.
		overlap.remove(this.player);
		
		// If we find a fish, remove it from missing.
		for (WorldObject wo : overlap) {
		
			// if worldobj item in our missing list = missing
			if (missing.contains(wo)) { 
				if (!(wo instanceof Fish)) {  //if not a fish
					throw new AssertionError("wo must be a Fish since it was in missing!");
				}
				// Convince Java it's a Fish bc we know it is
				Fish justFound = (Fish) wo; 
				//casting to make sure what was found = fish!
				
				// add to found instead! (So we see objectsFollow work!)
				//fish now follows player after overlap w them	
				found.add(justFound); 
				
				// Remove from missing list.
				missing.remove(justFound);
			  //justFound.remove(); removed from world
				
				// If you find the cyan, magenta, or orange fish = 20 points vs just 10!
				if (justFound.color==3 || justFound.color == 5 || 
					justFound.color==6) {
					score+=20;
				}
				else {
					score += 10;
				}
				
			}
		    if (missingH.contains(wo)) { 
//				if (!(wo instanceof Heart)) { 
//						//only things in missing list = hearts
//						throw new AssertionError("wo must be a Heart since it was in missing!");
//				}
				  //justFound.remove(); removed from world
					
				Heart heartCollected = (Heart) wo;
				heartsC.add(heartCollected);
				missingH.remove(heartCollected);
				 
				if (heartCollected.color==0 || heartCollected.color == 1 || 
						heartCollected.color==2 ||
						heartCollected.color==3 || heartCollected.color == 4 || 
						heartCollected.color==5) {
						    score+=5; 
						    
						
//				if(justFound.color==0 || justFound.color == 1 || 
//						justFound.color==2 ||
//						justFound.color==3 || justFound.color == 4 || 
//								justFound.color==5) {
						heartCollected.remove();
				
				     }
			      }	
		   }
		
		
		// Make sure missing fish *do* something.
		wanderMissingFish(); //didn't have world before cast
		// When fish get added to "found" they will follow the player around.
		World.objectsFollow(player, found);
		// Step any world-objects that run themselves.
		world.stepAll();
    }
	
	/**
	 * Call moveRandomly() on all of the missing fish to make them seem alive.
	 */
	private void wanderMissingFish() {
		//super(world);
		Random rand = ThreadLocalRandom.current();
		//WorldObject wt;
		for (Fish lost : missing) {
			// 30% of the time, lost fish move randomly.
			if (rand.nextDouble() < 0.3) {
				// goes here?
				//if random double is less than 0.3, or less than 30% of time, 
				//then move randomly!
			//	Fish lost = (Fish) wo; 
			//	((WorldObject) missing).moveRandomly();
				//Fish mg = (Fish) wt;
				lost.moveRandomly();
			}
			
		}
	}

	/**
	 * This gets a click on the grid. We want it to destroy rocks that ruin the game.
	 * @param x - the x-tile.
	 * @param y - the y-tile.
	 */
	public void click(int x, int y) {

		System.out.println("Clicked on: "+x+","+y+ " world.canSwim(player,...)="+world.canSwim(player, x, y));
		List<WorldObject> atPoint = world.find(x, y);
		
		for (WorldObject wobj: atPoint) {
			
				if (wobj instanceof Rock) { //oo keyword instanceof
			
//			if ((x==rockX) && (y==rockY)){
  				 // Rock rk = (Rock) wobj; 
				  wobj.remove(); 
				     //works w any world obj! don't need to do rock specifically
			}
		}
	}
	
}
