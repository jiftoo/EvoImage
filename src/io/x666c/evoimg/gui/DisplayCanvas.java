package io.x666c.evoimg.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DisplayCanvas extends JPanel {
	
	public static int DISPLAY_WIDTH = 300;
	public static int DISPLAY_HEIGHT = 300;
	
	public static final int DISPLAY_SIZE_LIMIT = 500;
	
	public DisplayCanvas() {
		setBounds(10, 10, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		setBackground(new Color(0xd3d3d3));
		
		img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setColor(getBackground());
		g.fillRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		g.dispose();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
	}
	
	private volatile BufferedImage img;
	
	public void drawImg(BufferedImage img) {
		this.img = img;
		repaint();
	}
	
	public void resize() {
		setBounds(10, 10, DISPLAY_WIDTH, DISPLAY_HEIGHT);
	}
	
}