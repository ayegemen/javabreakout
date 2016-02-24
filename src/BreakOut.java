import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

/**
 * BreakOut game Stanford course
 * 
 * 
 * */

public class BreakOut extends GraphicsProgram{
	
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;
	
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;
	
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;
	
	private static final int PADDLE_Y_OFFSET = 30;
	
	private static final int BRICKS_PER_ROW = 10;
	
	private static final int NUMBER_OF_ROWS = 10;
	
	private static final int BRICK_SEP = 4;
	
	private static final int BRICK_WIDTH =
		(WIDTH - (BRICKS_PER_ROW - 1) * BRICK_SEP) / BRICKS_PER_ROW;
		
	private static final int BRICK_HEIGHT = 8;
	
	private static final int BALL_RADIUS = 10;
	
	private static final int BRICK_Y_OFFSET = 70;
	
	private static final int TURNS = 3;
	
	
	
	public void run(){
		long start = System.nanoTime();
		addMouseListeners();
		addKeyListeners();
		setup();
		playGame();
		
	}
	
	/*initialize world state*/
	private void setup(){
		addBricks();
		addPaddle();
	}
	
	/*add GRect object paddle to the Canvas*/
	private void addPaddle(){
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle, (WIDTH-PADDLE_WIDTH)/2, HEIGHT-PADDLE_Y_OFFSET);
	}
	
	/*add 10 * 10 GRect objects to the Canvas*/
	private void addBricks(){
		for (int i = 0; i < NUMBER_OF_ROWS; i++){
			for (int j = 0; j < BRICKS_PER_ROW; j++){
				GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				switch(i/2){
				case 0: brick.setColor(Color.red); break;
				case 1: brick.setColor(Color.orange); break;
				case 2: brick.setColor(Color.yellow); break;
				case 3: brick.setColor(Color.green); break;
				case 4: brick.setColor(Color.cyan); break;
				}
				int rowWidth = BRICK_WIDTH * BRICKS_PER_ROW + 9 * BRICK_SEP;
				int x =  (WIDTH/2 - rowWidth/2) + j * (BRICK_WIDTH+BRICK_SEP);
				int y = (BRICK_Y_OFFSET + i*(BRICK_HEIGHT+BRICK_SEP));
				add(brick, x, y);
			}
		}
	}
	
	/*initialize instance variable ball with given velocity
	 * run game loop, frame by frame.*/
	public void playGame(){
		ball = new GOval(2*BALL_RADIUS, 2*BALL_RADIUS);
		ball.setFilled(true);
		add(ball, WIDTH/2-BALL_RADIUS, HEIGHT/2-BALL_RADIUS);
		
		vy = 3.0;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		
		while (true){
			oneFrame();
			pause(delay);
		}
	}
	
	/*actions that happen every frame: move ball, check for boundary and collision with bricks*/
	public void oneFrame(){
		ball.move(vx, vy);
		checkBoundary();
		GObject colider = checkColision();
		
		if (colider == paddle){
			vy = -vy;
		} else if (colider != null){
			remove(colider);
			vy = -vy;
		}
	}
	
	
	public GObject checkColision(){
		GObject ulColider = getElementAt(ball.getX(), ball.getY());
		if (ulColider != null){
			return ulColider;
		}
		GObject urColider = getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY());
		if (urColider != null){
			return urColider;
		}
		GObject llColider = getElementAt(ball.getX(), ball.getY()+2*BALL_RADIUS);
		if (llColider != null){
			return llColider;
		}
		GObject lrColider = getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY()+2*BALL_RADIUS);
		if (lrColider != null){
			return lrColider;
		}
		return null;
		
	}
	
	
	
	
	/*checks if ball hits the outer edges of the Canvas negates velocity if so.*/
	public void checkBoundary(){
		if (ball.getX()+vx < 0 || vx+ball.getX()+2*BALL_RADIUS > WIDTH){
			vx = -vx;
		}
		
		if (vy+ball.getY() < 0 || vy+ball.getY()+2*BALL_RADIUS > HEIGHT){
			vy = -vy;
		}
	}
	
	
	public void mousePressed(MouseEvent e){
		lastX = e.getX();
	}
	
	
	public void mouseDragged(MouseEvent e){
		if (paddle.getX() + (e.getX()-lastX) >= 0 && (paddle.getX()+PADDLE_WIDTH) + (e.getX()-lastX) < WIDTH){
			paddle.move(e.getX()-lastX, 0);
			lastX = e.getX();
		}	
		
		//println(paddle.getX()+ " "+paddle.getY());
	}
	
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_Z)
		{
			System.exit(0);
		}
	}
	
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	private GRect paddle;
	private int lastX;
	private double vx, vy;
	private GOval ball;
	private int delay = 10;
	

}
