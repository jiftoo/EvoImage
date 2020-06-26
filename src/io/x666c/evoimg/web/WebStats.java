package io.x666c.evoimg.web;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import io.x666c.evoimg.EvoImage;
import io.x666c.evoimg.gui.InfoPane;
import spark.Request;
import spark.Response;
import spark.Spark;

public class WebStats {
	
	private static boolean launched = false;
	
	private static String tss;
	private static String est;
	private static String usf;
	private static String usl;
	private static String pgr;
	private static String evr;
	private static String err;
	private static String gen;
	private static Color  dcl;
	private static Color  mcl;
	
	public static void launch() {
		Spark.port(80);
		Spark.get("/evo", WebStats::mainRoute);
		launched = true;
	}
	
	private static String mainRoute(Request req, Response resp) {
		String base64img = "ban";
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			BufferedImage copy = InfoPane.deepCopy(EvoImage.polygonizer.getImage());
			ImageIO.write(copy, "png", out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		base64img = Base64.getEncoder().encodeToString(out.toByteArray());
		
		String ret = "<div>" + 
					 "<img src=\"data:image/png;base64, " + base64img + "\" alt=\"U dumb xdee\" />" + 
					 "</div>";
		
		ret += inDiv("Elapsed time: " + tss);
		ret += inDiv("Estimated time: " + est);
		ret += inDiv("Useful mutations: " + usf);
		ret += inDiv("Useless mutations: " + usl);
		ret += inDiv("Progress: " + pgr);
		ret += inDiv("Evolution rate: " + evr);
		ret += inDiv("Error: " + err);
		ret += inDiv("Generation: " + gen);
		
		ret += "<div style=\"display:inline-block\">" +
				"Dominant color: " +
				"<div style=\"background-color: rgb(" + dcl.getRed() + "," + dcl.getGreen() + "," + dcl.getBlue() + ")" + "; width: 25px; height: 25px; display:inline-block;\"></div>" + 
				"</div>";
		
		ret += "<br>";
		
		ret += "<div style=\"display:inline-block\">" +
				"Mean color: " +
				"<div style=\"background-color: rgb(" + mcl.getRed() + "," + mcl.getGreen() + "," + mcl.getBlue() + ")" + "; width: 25px; height: 25px; display:inline-block;\"></div>" + 
				"</div>";
		
		return ret;
	}
	
	private static String inDiv(String content) {
		return "<div>" + content + "</div>";
	}
	
	public static void updateInformation(long sinceStart, long estt, int usefulMutatuions, int uselessMutatuions, double progress, double error, Color dominantColor, Color averageColor, int generaions) {
		if(!launched)
			return;

		tss = InfoPane.convertSecondsToHMmSs(sinceStart);
		est = String.valueOf(estt);
		usf = String.valueOf(usefulMutatuions);
		usl = String.valueOf(uselessMutatuions);
		pgr = String.format("%.1f", progress) + "%";
		evr = String.format("%.1f", ((double) usefulMutatuions) / (double) (uselessMutatuions) - 1);
		err = String.format("%.1f", error);
		dcl = dominantColor;
		mcl = averageColor;
		gen = String.valueOf(generaions);
	}
	
}