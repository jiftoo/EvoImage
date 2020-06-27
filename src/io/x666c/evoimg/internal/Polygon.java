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
			color = new Color(MathUtil.random(255), MathUtil.random(255), MathUtil.random(255), MathUtil.random(249));
		} else if (InfoPane.colorInitMode() == 2) {
			int r = Polygonizer.IDEAL_MEAN.getRed();
			int g = Polygonizer.IDEAL_MEAN.getGreen();
			int b = Polygonizer.IDEAL_MEAN.getBlue();
			color = new Color(r, g, b, MathUtil.random(249));
		}
	}

	void mutate() {
		int rnd = MathUtil.random(16);

		if (rnd <= 2)
			mutateColor(rnd);
		else if (rnd <= 9)
			mutateCoordinate(rnd);
		else
			mutateAlpha();

	}

	void mutateCoordinate(int rnd) {
		switch (rnd) {
		case 4:
			p1 = Vector.create(MathUtil.random(Polygonizer.WIDTH), p1.y);
			break;
		case 5:
			p2 = Vector.create(MathUtil.random(Polygonizer.WIDTH), p2.y);
			break;
		case 6:
			p3 = Vector.create(MathUtil.random(Polygonizer.WIDTH), p3.y);
			break;
		case 7:
			p1 = Vector.create(p1.x, MathUtil.random(Polygonizer.HEIGHT));
			break;
		case 8:
			p2 = Vector.create(p1.x, MathUtil.random(Polygonizer.HEIGHT));
			break;
		case 9:
			p3 = Vector.create(p1.x, MathUtil.random(Polygonizer.HEIGHT));
			break;
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
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), MathUtil.random(249));
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