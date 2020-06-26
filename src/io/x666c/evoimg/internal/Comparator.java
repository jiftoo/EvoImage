package io.x666c.evoimg.internal;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;

public class Comparator {

	private static int[] idealRaster;
	private static int WT2 = Polygonizer.WIDTH * 2;

	private static int[] idealDatai;

	private static byte[] idealDatab;

	public static int APPROXIMATION = 1;

	public static volatile int ALGO = 2; // 0 < 1 < 2

	// 29579073

	public static final void setIdeal(BufferedImage ideal) {
		if (ALGO == 0) {
			idealRaster = new int[ideal.getWidth() * ideal.getHeight() * 3];
			for (int x = 0; x < ideal.getWidth(); x++) {
				for (int y = 0; y < ideal.getHeight(); y++) {
					final int[] raster = ideal.getRaster().getPixel(x, y, (int[]) null);
					for (int r = 0; r < 3; r++) {
						idealRaster[x + ideal.getHeight() * (y + ideal.getWidth() * r)] = raster[r];
					}
				}
			}
			WT2 = Polygonizer.WIDTH * 2;
		} else if (ALGO == 1) {
			int[] rawData = ((DataBufferInt) ideal.getRaster().getDataBuffer()).getData();
			idealDatai = new int[rawData.length * 3];

			int counter = 0;
			for (int i = 0; i < rawData.length * 3; i += 3) {
				idealDatai[i] = (rawData[counter] & 0xFF);
				idealDatai[i + 1] = (rawData[counter] >> 8 & 0xFF);
				idealDatai[i + 2] = (rawData[counter] >> 16 & 0xFF);

				counter++;
			}
		} else if (ALGO >= 2) {
			idealDatab = ((DataBufferByte) ideal.getRaster().getDataBuffer()).getData();
		}
	}

	@SuppressWarnings("unused")
	public static long calculateError(BufferedImage canvas) {
		if (ALGO == 0) {
			return alg0(canvas);
		} else if (ALGO == 1) {
			return alg1(canvas);
		} else if (ALGO == 2) {
			return alg2(canvas);
		}
		return -1;
	}

	private static long alg0(BufferedImage canvas) {
		long error = 0;

		final Raster r1 = canvas.getRaster();

		final int[] raster1 = new int[3];

		for (int x = 0; x < Polygonizer.WIDTH; x += APPROXIMATION) {
			for (int y = 0; y < Polygonizer.HEIGHT; y += APPROXIMATION) {
				r1.getPixel(x, y, raster1);

				error += Math.abs(raster1[0] - idealRaster[x + (Polygonizer.HEIGHT * y)]);
				error += Math.abs(raster1[1] - idealRaster[x + (Polygonizer.HEIGHT * (y + Polygonizer.WIDTH))]);
				error += Math.abs(raster1[2] - idealRaster[x + (Polygonizer.HEIGHT * (y + WT2))]);
			}
		}
		return error;
	}

	private static long alg1(BufferedImage canvas) {
		long error = 0;

		final int[] canvasData = ((DataBufferInt) canvas.getRaster().getDataBuffer()).getData();

		for (int i = 0; i < canvasData.length; i += APPROXIMATION) {
			final int d1 = (canvasData[i] & 0xFF) - (idealDatai[(i * 3)]);
			final int d2 = (canvasData[i] >> 8 & 0xFF) - (idealDatai[(i * 3) + 1]);
			final int d3 = (canvasData[i] >> 16 & 0xFF) - (idealDatai[(i * 3) + 2]);

			error += (d1 * d1) + (d2 * d2) + (d3 * d3);
		}

		return error;
	}

	private static long alg2(BufferedImage canvas) {
		long error = 0;

		final byte[] canvasData = ((DataBufferByte) canvas.getRaster().getDataBuffer()).getData();
		for (int i = 0; i < canvasData.length; i += APPROXIMATION) {
			final int d = (canvasData[i] & 0xff) - (idealDatab[i] & 0xff);
			error += d * d;
		}

		return error;
	}

	public static double colorDistance(Color c1, Color c2) {
		long l1 = System.nanoTime();
		final double dist = (Math.sqrt(((c1.getRed() - c2.getRed()) * (c1.getRed() - c2.getRed()))
				+ ((c1.getGreen() - c2.getGreen()) * (c1.getGreen() - c2.getGreen()))
				+ ((c1.getBlue() - c2.getBlue()) * (c1.getBlue() - c2.getBlue()))));
		long l2 = System.nanoTime();
		System.out.println(l2 - l1);
		return dist;
	}

}