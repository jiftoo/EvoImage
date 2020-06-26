package io.x666c.evoimg.internal;

import java.awt.Color;

import io.x666c.evoimg.gui.InfoPane;
import io.x666c.glib4j.math.MathUtil;

public class Polygon implements Cloneable {

	public Vector p1, p2, p3;
	public Color color;

	public Polygon() {
		p1 = Vector.create(MathUtil.random(Polygonizer.WIDTH), MathUtil.random(Polygonizer.HEIGHT));
		p2 = Vector.create(MathUtil.random(Polygonizer.WIDTH), MathUtil.random(Polygonizer.HEIGHT));
		p3 = Vector.create(MathUtil.random(Polygonizer.WIDTH), MathUtil.random(Polygonizer.HEIGHT));

		if (InfoPane.colorInitMode() == 0) {
			color = new Color(0, 0, 0, MathUtil.random());
		} else if (InfoPane.colorInitMode() == 1) {
			color = new Color(MathUtil.random(255), MathUtil.random(255), MathUtil.random(255), MathUtil.random(246));
		} else if (InfoPane.colorInitMode() == 2) {
			int r = Polygonizer.IDEAL_MEAN.getRed();
			int g = Polygonizer.IDEAL_MEAN.getGreen();
			int b = Polygonizer.IDEAL_MEAN.getBlue();
			color = new Color(r, g, b, MathUtil.random(246));
		} else if (InfoPane.colorInitMode() == 3) {
			int r = Polygonizer.IDEAL_DOMINANT.getRed();
			int g = Polygonizer.IDEAL_DOMINANT.getGreen();
			int b = Polygonizer.IDEAL_DOMINANT.getBlue();
			color = new Color(r, g, b, MathUtil.random(246));
		}
	}

	void mutate() {
		int rnd = MathUtil.random(4 + (3 * 2));

		if (rnd < 3)
			mutateColor(rnd);
		else if (rnd == 3)
			mutateAlpha();
		else
			mutateCoordinate(rnd);
	}

	void mutateCoordinate(int rnd) {
		if (rnd == 4) {
			p1 = Vector.create(MathUtil.random(Polygonizer.WIDTH), p1.y);
		} else if (rnd == 5) {
			p2 = Vector.create(MathUtil.random(Polygonizer.WIDTH), p2.y);
		} else if (rnd == 6) {
			p3 = Vector.create(MathUtil.random(Polygonizer.WIDTH), p3.y);
		} else if (rnd == 7) {
			p1 = Vector.create(p1.x, MathUtil.random(Polygonizer.HEIGHT));
		} else if (rnd == 8) {
			p2 = Vector.create(p1.x, MathUtil.random(Polygonizer.HEIGHT));
		} else if (rnd == 9) {
			p3 = Vector.create(p1.x, MathUtil.random(Polygonizer.HEIGHT));
		}
	}

	void mutateColor(int rnd) {

		if (rnd == 0) {
			color = new Color(MathUtil.random(255), color.getGreen(), color.getBlue(), color.getAlpha());
		} else if (rnd == 1) {
			color = new Color(color.getRed(), MathUtil.random(255), color.getBlue(), color.getAlpha());
		} else if (rnd == 2) {
			color = new Color(color.getRed(), color.getGreen(), MathUtil.random(255), color.getAlpha());
		}
	}

	void mutateAlpha() {
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), MathUtil.random(255));
	}

	Polygon copy() {
		try {
			return (Polygon) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}