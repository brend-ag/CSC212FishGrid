package edu.smith.cs.csc212.fishgrid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import me.jjfoley.gfx.GFX;
import me.jjfoley.gfx.IntPoint;
import me.jjfoley.gfx.TextBox;

/**
 * This is the graphical <b><i>view</i></b> of our "FishGame" class. Don't worry
 * about modifying this file at all for the assignment besides the very first lab-based comments.
 * 
 * @author jfoley
 */
public class Main extends GFX {
	/**
	 * Game size (visual). TODO(lab) Try changing this to 600.
	 */
	public static int VISUAL_GRID_SIZE = 500; //400 by 400
	/**
	 * Game size (logical). TODO(lab) Try changing this to 15 or so.
	 */
	public static int LOGICAL_GRID_SIZE = 12; 
	/*the lines/grid, 10 squares across and down; 
	 * if small number it'll be v big 3x3 (no space for fishes/game)
	 * and more squares = lottsss of squares (too much space maybe)
	 */
	
	/**
	 * The words appear in the top part of the screen.
	 */
	public static int TOP_PART = 50; //50 units down fr top
	/**
	 * There's a border to make it look pretty (the board is inset by this much).
	 */
	public static int BORDER = 5;
	/**
	 * This is where the game logic lives.
	 */
	FishGame game;
	/**
	 * This TextBox wraps up making fonts and centering text.
	 */
	TextBox gameState = new TextBox(""); 
	/**
	 * This is a rectangle representing the TOP_PART of the screen.
	 */
	Rectangle2D topRect;

	/**
	 * Construct a new fish game.
	 */
	public Main() {
		super(VISUAL_GRID_SIZE + BORDER * 2, VISUAL_GRID_SIZE + BORDER * 2 + TOP_PART); //super class = Main 
		game = new FishGame(LOGICAL_GRID_SIZE, LOGICAL_GRID_SIZE);
		gameState.color = Color.WHITE; //remember this is the text
		gameState.setFont(TextBox.BOLD_FONT);
		gameState.setFontSize(TOP_PART / 3.0); //1/3 of the top part aka 1/3 of 50 
		topRect = new Rectangle2D.Double(0, 0, getWidth(), TOP_PART); 
	}

	/**
	 * How big is a tile?
	 * @return this returns the tile width.
	 */
	private int getTileW() {
		return VISUAL_GRID_SIZE / game.world.getWidth(); //for each indiv tile on screen so 500/game's world's width (fr worldclass prob)
		//same for height
	}

	/**
	 * How big is a tile?
	 * @return this returns the tile height.
	 */
	private int getTileH() {
		return VISUAL_GRID_SIZE / game.world.getWidth();
	}

	public static Color OCEAN_COLOR = new Color(0, 100, 255); //nicer blue than old color.blue
	public static Color GRID_COLOR = new Color(0, 100, 225); //dark blue for grid
	/**
	 * Draw the game state.
	 */
	@Override
	public void draw(Graphics2D g) {
		// Background of window is dark-dark green.
		g.setColor(Color.green.darker().darker());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// Get a a reference to the game world to draw.
		World world = game.world;

		// Draw TOP_PART TextBox.
		this.gameState.centerInside(this.topRect);
		this.gameState.draw(g);

		// Slide the world down, and into the box.
		// This makes our rendering of the board easier.
		g.translate(BORDER, BORDER + TOP_PART); //translate(x,y) so 5, 5+50

		// Use the tile-sizes.
		int tw = getTileW();
		int th = getTileH();

		// Draw the ocean (not the whole screen).
		g.setColor(OCEAN_COLOR);
		g.fillRect(0, 0, tw * world.getWidth(), th * world.getHeight()); //int x, int y, int width, int height
			//below toprect; multiplying 1 tile width 
		//* width of "world" = like multiplying the amt of tiles? same for height
		
		// Draw a grid to better picture how the game works.
		g.setColor(GRID_COLOR);
		for (int x = 0; x < world.getWidth(); x++) {
			for (int y = 0; y < world.getHeight(); y++) {
				g.drawRect(x * tw, y * th, tw, th); //x*tile width; y*tile height; tile width; t height
			}
		}

		// For everything in our world/for all the items in the world: 
		for (WorldObject wo : world.viewItems()) { 
			// Draw it with a 1x1 graphical world, with the center right in the middle of the tile.
			// I fiddled with this translate to get pixel-perfect. Maybe there's a nicer way, but it works for now.

			Graphics2D forWo = (Graphics2D) g.create(); //copy of graphics obj?
			forWo.translate((int) ((wo.getX() + 0.5) * tw) + 1, (int) ((wo.getY() + 0.5) * th) + 1); //don't have to get (self)
			forWo.scale(tw, th);
			wo.draw(forWo); //draws to g
			forWo.dispose(); //cleanup basically
		}
		
		IntPoint hover = mouseToGame(this.getMouseLocation());
		/*Intpoint: This class represents a location,  for a mouse click 
		 * or other reasons. It implements Java's Point2D class, 
		 * which is meant for "decimal" points,e.g., 
		 * Point2D.Double or Point2D.Float.
		 */
		if (hover != null) { //aka if hover is not just hovering over nothing
			g.setColor(new Color(0,1,0,0.5f)); //change color and fill tile basically
			g.fillRect(hover.x * tw, hover.y * th, tw, th);
		}
	}
	
	/**
	 * Convert Mouse coordinates to Grid coordinates.
	 * @param mouse may be a Mouse location (or null).
	 * @return null or the grid coordinates of the Mouse.
	 */
	public IntPoint mouseToGame(IntPoint mouse) {
		if (mouse == null) return null; //notice 1 line if statement! no {}
		int x = mouse.x - BORDER; 
		int y = mouse.y - BORDER - TOP_PART;
		if (x > 0 && x <= VISUAL_GRID_SIZE &&
				y > 0 && y <= VISUAL_GRID_SIZE) {
			int tx = x / getTileW();
			int ty = y / getTileH();
			return new IntPoint(tx, ty);
		}
		return null;
	}

	/**
	 * We separate our "Main" game logic update here.
	 * @param secondsSinceLastUpdate - my GFX code can tell us how long it is between each update, but we don't actually care here.
	 */
	@Override
	public void update(double secondsSinceLastUpdate) {
		// Handle game-over and restart.
		if (game.gameOver()) {
			this.gameState.setString("You win! Click anywhere start again!");
			if (this.processClick() != null) { //aka if they do click anywhere
				this.game = new FishGame(LOGICAL_GRID_SIZE, LOGICAL_GRID_SIZE);
			}
			return;
		}
		
		// Update the text in the TextBox.
		this.gameState.setString( //again, what's in that toprect part/gamestatee
				"Step #: " + game.stepsTaken + 
				" ... Fish Left: " + game.missingFishLeft() +
				" ... Score: "+ game.score); //concatenating strings w ints

		// Read the state of the keyboard:
		boolean up = this.processKey(KeyEvent.VK_W) || this.processKey(KeyEvent.VK_UP);
		boolean down = this.processKey(KeyEvent.VK_S) || this.processKey(KeyEvent.VK_DOWN);
		boolean left = this.processKey(KeyEvent.VK_A) || this.processKey(KeyEvent.VK_LEFT);
		boolean right = this.processKey(KeyEvent.VK_D) || this.processKey(KeyEvent.VK_RIGHT);
		boolean skip = this.processKey(KeyEvent.VK_SPACE);

		// Move the player if we can:
		boolean moved = false; //assume player = not moving first
		if (up) { 
			moved = this.game.player.moveUp();
		} else if (down) { //if keyboard = clicked then
			moved = this.game.player.moveDown(); //true and moves down acc to worldobject class
		} else if (left) {
			moved = this.game.player.moveLeft(); 
		} else if (right) {
			moved = this.game.player.moveRight();
		}
		
		IntPoint click = mouseToGame(this.processClick());
		
		// Only advance the game if the player presses something!
		if (skip || moved || click != null) { 
			if (click != null) {
				this.game.click(click.x, click.y);
			}
			// Update game logic!
			this.game.step();
			// Update message at the top!
		}
	}

	/**
	 * Create and start the game!
	 * @param args - not run from the command line so no args are used.
	 */
	public static void main(String[] args) {
		Main game = new Main(); //public class above
		game.start();
	}

}
