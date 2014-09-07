package featurecreep.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

import featurecreep.game.Animation;
import featurecreep.game.InputHandler;

public class Ball {

	float x, y; 
	float speedX, speedY; 
	float radius;
	private Color color;
	private boolean alive = true;
	private static final Color DEFAULT_COLOR = Color.BLUE;

	public Ball(float x, float y, float radius, float speed, float angleInDegree, Color color) {
		this.x = x;
		this.y = y;
		
		// Convert (speed, angle) to (x, y), with y-axis inverted
		this.speedX = (float) (speed * Math.cos(Math.toRadians(angleInDegree)));
		this.speedY = (float) (-speed * (float) Math.sin(Math.toRadians(angleInDegree)));
		this.radius = radius;
		this.color = color;
	}

	public Ball(float x, float y, float radius, float speed, float angleInDegree) {
		this(x, y, radius, speed, angleInDegree, DEFAULT_COLOR);
	}

	public void draw(Graphics g) {
		if (alive) {
			g.setColor(color);
			g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
		}
	}
	
	public void tick() {
		
	}
	
	public void mouseBallCollisionDetection(InputHandler input) {
		if (alive) {
			if (input.getMouseX() >= (x - radius)
					&& input.getMouseX() <= (x + radius)
					&& input.getMouseY() > (y - radius)
					&& input.getMouseY() <= (y + radius)) {
				recalculateMovement();
				//alive = false;
			}
		}
	}

	public void moveOneStepWithCollisionDetection(ContainerBox box) {
		if (alive) {
			// Get the ball's bounds, offset by the radius of the ball
			float ballMinX = box.minX + radius;
			float ballMinY = box.minY + radius;
			float ballMaxX = box.maxX - radius;
			float ballMaxY = box.maxY - radius;
			// Calculate the ball's new position
			x += speedX;
			y += speedY;
			// Check if the ball moves over the bounds. If so, adjust the position
			// and speed.
			if (x < ballMinX) {
				speedX = -speedX; // Reflect along normal
				x = ballMinX; // Re-position the ball at the edge
			} else if (x > ballMaxX) {
				speedX = -speedX;
				x = ballMaxX;
			}
			// May cross both x and y bounds
			if (y < ballMinY) {
				speedY = -speedY;
				y = ballMinY;
			} else if (y > ballMaxY) {
				speedY = -speedY;
				y = ballMaxY;
			}
		}
	}
	
	public void recalculateMovement() {
		Random rand = new Random();
		int angleInDegree = rand.nextInt(360);
		this.speedX = (float) (10 * Math.cos(Math.toRadians(angleInDegree)));
		this.speedY = (float) (-10 * (float) Math.sin(Math.toRadians(angleInDegree)));
	}
	
	public float getSpeed() {
		return (float) Math.sqrt(speedX * speedX + speedY * speedY);
	}

	public float getMoveAngle() {
		return (float) Math.toDegrees(Math.atan2(-speedY, speedX));
	}

	public float getMass() {
		return radius * radius * radius / 1000f; // Normalize by a factor
	}

	public float getKineticEnergy() {
		return 0.5f * getMass() * (speedX * speedX + speedY * speedY);
	}
	
	public float getRealPositionX() {
		return this.x-radius;
	}
	
	public float getRealPositionY() {
		return this.y-radius;
	}
	
	public boolean isAlive() {
		return alive;
	}
}
