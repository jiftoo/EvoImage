package io.x666c.evoimg.internal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import de.androidpit.colorthief.ColorThief;
import io.x666c.glib4j.graphics.PlainRenderer;
import io.x666c.glib4j.math.MathUtil;
import sun.awt.image.SunVolatileImage;

public class Population {

	Polygon[] members;
	Polygon[] buffer;

	final BufferedImage canvas;
	final BufferedImage ideal;

	double mutatedError = Double.POSITIVE_INFINITY;
	double currentError = Double.POSITIVE_INFINITY;

	float completeness = 0;

	int goodMutations = 0;
	int badMutations = 0;

	int generations = 0;

	Color dominantColor = Color.BLACK;
	Color averageColor = Color.BLACK;

	Population(int amount, BufferedImage image) {
		ideal = image;
		Comparator.setIdeal(image);
		
		canvas = new BufferedImage(Polygonizer.WIDTH, Polygonizer.HEIGHT,
				Comparator.ALGO >= 2 ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_INT_RGB);
		
		g = canvas.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

		members = new Polygon[amount];
		for (int i = 0; i < members.length; i++) {
			members[i] = new Polygon();
		}
	}

	private Graphics2D g;

	void mutate() {
		g = canvas.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);

		generations++;

		buffer = deepCopy(members);

		buffer[MathUtil.random(buffer.length)].mutate();

		// canvas = new BufferedImage(Polygonizer.WIDTH, Polygonizer.HEIGHT,
		// BufferedImage.TYPE_INT_RGB);
		// FIXME: g is a field and canvas never updates. can cause bugs

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Polygonizer.WIDTH, Polygonizer.HEIGHT);

		for (Polygon p : buffer) {
			g.setColor(p.color);
			g.fillPolygon(new int[] {(int) p.p1.x, (int) p.p2.x, (int) p.p3.x}, new int[] {(int) p.p1.y, (int) p.p2.y, (int) p.p3.y}, 3);
		}
	}

	void calculateError() {
		mutatedError = Comparator.calculateError(canvas);
		if (mutatedError < currentError) {
			members = buffer;
			currentError = mutatedError;

			goodMutations++;
		} else {
			badMutations++;
		}
		calculateCompleteness();
		if (colorSwitch == 200) {
			calculateColors();
			colorSwitch = 0;
		}
		colorSwitch++;
	}

	private int colorSwitch = 0;
	private float firstCompleteness = -100;

	private void calculateCompleteness() {
		final float rawVal = (float) (100 * (1 - currentError / (canvas.getWidth() * canvas.getHeight() * 3 * 255)));
		if (firstCompleteness == -100) {
			firstCompleteness = rawVal;
		}
		completeness = MathUtil.map(rawVal, firstCompleteness, 100, 0, 100);
	}

	private void calculateColors() {
		final int[] rgb = ColorThief.getColorMap(canvas, 256, 5, true).vboxes.get(0).avg(false);
		dominantColor = new Color(rgb[0] << 16 | rgb[1] << 8 | rgb[2]);

		int ared = 0;
		int agreen = 0;
		int ablue = 0;
		for (Polygon p : members) {
			ared += p.color.getRed();
			agreen += p.color.getGreen();
			ablue += p.color.getBlue();
		}
		averageColor = new Color(ared / members.length, agreen / members.length, ablue / members.length);
	}

	void draw(PlainRenderer r) {
		r.fill();
		for (Polygon p : members) {
			r.color(p.color);
			r.polygon(p.p1.x, p.p1.y, p.p2.x, p.p2.y, p.p3.x, p.p3.y);
		}
	}

	public Polygon[] getPopulation() {
		return members;
	}

	Polygon[] deepCopy(Polygon[] src) {
		Polygon[] ret = new Polygon[src.length];
		for (int i = 0; i < src.length; i++) {
			ret[i] = src[i].copy();
		}
		return ret;
	}
}