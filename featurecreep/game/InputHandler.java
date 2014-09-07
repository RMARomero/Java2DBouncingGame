package featurecreep.game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InputHandler implements MouseListener {
	
	private int mouseX, mouseY;
	private boolean isClicked = false;
	
	public InputHandler(Game game) {
		game.addMouseListener(this);
	}
	
	public boolean isClicked() { return isClicked; }

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//System.out.println("Mouse pressed: # of clicks " + e.getClickCount());
		mouseX = e.getX();
		mouseY = e.getY();
		isClicked = !isClicked;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println("Mouse released: # of clicks " + e.getClickCount());
		isClicked = !isClicked;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//System.out.println("Mouse entered");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//System.out.println("Mouse exited");
	}
	
	public int getMouseX() { return mouseX; }
	public int getMouseY() { return mouseY; }
}
