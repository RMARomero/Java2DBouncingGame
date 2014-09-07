package featurecreep.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import featurecreep.entity.Ball;
import featurecreep.entity.ContainerBox;

public class Game extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int UPDATE_RATE = 30; // Frames per second (fps)

	private Ball ball; // A single bouncing Ball's instance
	private ContainerBox box; // The container rectangular box
	
	private Animation explosionAnimation;
	private BufferedImage explosionImage;
	private ArrayList<Animation> explosionList;

	private DrawCanvas canvas; // Custom canvas for drawing the box/ball
	private int canvasWidth;
	private int canvasHeight;
	
	private InputHandler input;

	public Game(int width, int height) {
		canvasWidth = width;
		canvasHeight = height;
		
		input = new InputHandler(this);

		// Init the ball at a random location (inside the box) and moveAngle
		Random rand = new Random();
		int radius = 40;
		int x = rand.nextInt(canvasWidth - radius * 2 - 20) + radius + 10;
		int y = rand.nextInt(canvasHeight - radius * 2 - 20) + radius + 10;
		int speed = 10;
		int angleInDegree = rand.nextInt(360);
		ball = new Ball(x, y, radius, speed, angleInDegree, Color.BLUE);

		box = new ContainerBox(0, 0, canvasWidth, canvasHeight, Color.GRAY,	Color.WHITE);
		
		//init animation
		explosionList = new ArrayList<Animation>();
		URL explosionAnimImgUrl = this.getClass().getResource("/explosion_anim.png");
		try {
			explosionImage = ImageIO.read(explosionAnimImgUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int frameWidth = 134; 
		int frameHeight = 134;
		int numberOfFrames = 12;
		long frameTime = 45;
		Boolean loopAnim = false;
		long showDelay = 0;
		explosionAnimation = new Animation(explosionImage, frameWidth, frameHeight, numberOfFrames, frameTime, loopAnim, x, y , showDelay);
		explosionList.add(explosionAnimation);
		
		canvas = new DrawCanvas();
		this.setLayout(new BorderLayout());
		this.add(canvas, BorderLayout.CENTER);		

		run();
	}

	public void run() {
		Thread gameThread = new Thread() {
			public void run() {
				while (true) {
					// Execute one time-step for the game
					update();
					
					// Refresh the display
					repaint();
					
					// Delay and give other thread a chance
					try {
						Thread.sleep(1000 / UPDATE_RATE);
					} catch (InterruptedException ex) {}
				}
			}
		};
		gameThread.start();
	}

	public void update() {
		ball.moveOneStepWithCollisionDetection(box);
		
		if (input.isClicked()) {
			ball.mouseBallCollisionDetection(input);
			for (int i = 0; i < explosionList.size(); i++) {
				explosionList.get(i).changeCoordinates((int)ball.getRealPositionX(), (int)ball.getRealPositionY());
			}
		}
	}
	
	class DrawCanvas extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			box.draw(g);
			ball.draw(g);
			
			for(int i = 0; i < explosionList.size(); i++){
				// If the animation is over we remove it from the list.
				if(explosionList.get(i).active && ball.isAlive() == false)
					explosionList.get(i).Draw((Graphics2D)g);
				else if (!explosionList.get(i).active)
					explosionList.remove(i);
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return (new Dimension(canvasWidth, canvasHeight));
		}
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Game");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setContentPane(new Game(640, 480));
				frame.setResizable(false);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

}
