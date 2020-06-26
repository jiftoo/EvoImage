package io.x666c.evoimg.internal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.androidpit.colorthief.ColorThief;
import io.x666c.evoimg.gui.InfoPane;
import io.x666c.glib4j.graphics.PlainRenderer;

public class Polygonizer {

	private static volatile boolean hasOneInstance = false;
	{
		if (hasOneInstance)
			throw new RuntimeException("Only one instance is allowed per program.");
		else
			hasOneInstance = true;
	}

	private volatile Thread workerThread;
	private volatile Thread drawCallbackThread;
	private volatile boolean loop = false;

	public static int WIDTH;
	public static int HEIGHT;

	Population population;

	int seed;

	private boolean broken = false;

	static Color IDEAL_DOMINANT;
	static Color IDEAL_MEAN;

	public Polygonizer(BufferedImage goal, int vertices, int width, int height) {
		WIDTH = width;
		HEIGHT = height;

		picture = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		r = new PlainRenderer(picture.createGraphics());

		BufferedImage scaled = new BufferedImage(Polygonizer.WIDTH, Polygonizer.HEIGHT,
				Comparator.ALGO >= 2 ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaled.createGraphics();
		g.drawImage(goal, 0, 0, Polygonizer.WIDTH, Polygonizer.HEIGHT, null);
		g.dispose();

		int[] rgb = ColorThief.getColorMap(scaled, 256, 1, true).vboxes.get(0).avg(false);
		IDEAL_DOMINANT = new Color(rgb[0] << 16 | rgb[1] << 8 | rgb[2]);

		int ared = 0;
		int agreen = 0;
		int ablue = 0;
		final int linearSize = scaled.getWidth() * scaled.getHeight();
		for (int x = 0; x < scaled.getWidth(); x++) {
			for (int y = 0; y < scaled.getHeight(); y++) {
				Color c = new Color(scaled.getRGB(x, y));

				ared += c.getRed();
				agreen += c.getGreen();
				ablue += c.getBlue();
			}
		}
		IDEAL_MEAN = new Color(ared / linearSize, agreen / linearSize, ablue / linearSize);

		population = new Population(vertices, scaled);
	}

	public void start() {
		if (broken)
			throw new RuntimeException("Broken");

		loop = true;

		workerThread = new Thread(this::update);
		drawCallbackThread = new Thread(this::draw);

		workerThread.start();
		drawCallbackThread.start();
	}

	public void stop() {
		loop = false;
		try {
			workerThread.join();
			drawCallbackThread.join();
		} catch (Exception e) {
		}
		workerThread = null;
		r = null;
	}

	private void update() {
		while (loop) {
			population.mutate();

			population.calculateError();

			InfoPane.mutationsIncrement += 1 * 2;
		}
	}

	private final Object IMAGE_LOCK = new Object();

	private volatile BufferedImage picture;
	private volatile PlainRenderer r;

	private volatile Runnable drawThreadCallback = null;

	public void draw() {
		while (loop) {
			if (broken)
				throw new RuntimeException("This instance is broken");

			synchronized (IMAGE_LOCK) {
				r.fill();
				r.color(0xd3d3d3);
				r.rectangle(0, 0, picture.getWidth(), picture.getHeight());

				population.draw(r);
			}

			if (drawThreadCallback != null)
				drawThreadCallback.run();

			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setDrawCallback(Runnable cb) {
		drawThreadCallback = cb;
	}

	public BufferedImage getImage() {
		synchronized (IMAGE_LOCK) {
			return picture;
		}
	}

	public BufferedImage getImageNoSync() {
		return picture;
	}

	public void release() {
		stop();
		broken = true;

		hasOneInstance = false;
	}

	public int getGoodMutations() {
		return population.goodMutations;
	}

	public int getBadMutations() {
		return population.badMutations;
	}

	public int getGenerations() {
		return population.generations;
	}

	public double getError() {
		return population.currentError;
	}

	public double getCompleteness() {
		return population.completeness;
	}

	public Color getDominantColor() {
		return population.dominantColor;
	}

	public Color getAverageColor() {
		return population.averageColor;
	}

	public boolean isBroken() {
		return broken;
	}

	public Population getPopulation() {
		return population;
	}

}