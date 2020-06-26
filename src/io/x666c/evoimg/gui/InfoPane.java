package io.x666c.evoimg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;

import io.x666c.evoimg.EvoImage;
import io.x666c.evoimg.internal.Polygon;
import io.x666c.evoimg.web.WebStats;

public class InfoPane extends JPanel {
	
	private JLabel timeSinceStart;
	private JLabel usefulMutatuions;
	private JLabel uselessMutatuions;
	private JLabel progress;
	private JLabel progressLog;
	private JLabel estTime;
	private JLabel error;
	private JLabel dominantColorIcon;
	private JLabel averageColorIcon;
	private JLabel verticesAmt;
	private JLabel evolutionRate;
	
	private JButton savePng;
	private JLabel evolutionsPerSecond;
	
	private static JRadioButton colorInitBlack;
	private static JRadioButton colorInitRandom;
	private static JRadioButton colorInitMean;
	private static JRadioButton colorInitDominant;
	
	public volatile static int mutationsIncrement = 0;
	private JLabel genL;
	private JLabel generations;
	private JButton saveSvg;
	
	public InfoPane() {
		setPreferredSize(new Dimension(200, 500));
		setBorder(new CompoundBorder(new EmptyBorder(1,1,0,0), new EtchedBorder(EtchedBorder.LOWERED)));
		setLayout(null);
		
		JLabel sinceStartL = new JLabel("Elapsed time");
		sinceStartL.setHorizontalAlignment(SwingConstants.LEFT);
		sinceStartL.setBounds(10, 11, 79, 14);
		add(sinceStartL);
		
		timeSinceStart = new JLabel("0:00:00");
		timeSinceStart.setHorizontalAlignment(SwingConstants.RIGHT);
		timeSinceStart.setBounds(138, 11, 54, 14);
		add(timeSinceStart);
		
		JLabel lblUsefulMutations = new JLabel("Useful mutations");
		lblUsefulMutations.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsefulMutations.setBounds(8, 124, 85, 14);
		add(lblUsefulMutations);
		
		usefulMutatuions = new JLabel("0");
		usefulMutatuions.setHorizontalAlignment(SwingConstants.RIGHT);
		usefulMutatuions.setBounds(136, 124, 56, 14);
		add(usefulMutatuions);
		
		uselessMutatuions = new JLabel("0");
		uselessMutatuions.setHorizontalAlignment(SwingConstants.RIGHT);
		uselessMutatuions.setBounds(136, 149, 56, 14);
		add(uselessMutatuions);
		
		JLabel lblVainMutations = new JLabel("Vain mutations");
		lblVainMutations.setHorizontalAlignment(SwingConstants.LEFT);
		lblVainMutations.setBounds(8, 149, 85, 14);
		add(lblVainMutations);
		
		JLabel lblNewLabel = new JLabel("Progress");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setBounds(8, 174, 46, 14);
		add(lblNewLabel);
		
		progress = new JLabel("0.0%");
		progress.setHorizontalAlignment(SwingConstants.RIGHT);
		progress.setBounds(136, 174, 56, 14);
		add(progress);
		
		JLabel lblNewLabel2 = new JLabel("Progress Log");
		lblNewLabel2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel2.setBounds(8, 185, 70, 14);
		add(lblNewLabel2);
		
		progressLog = new JLabel("0.0%");
		progressLog.setHorizontalAlignment(SwingConstants.RIGHT);
		progressLog.setBounds(136, 185, 56, 14);
		add(progressLog);
		
		JLabel estL = new JLabel("Est. time");
		estL.setHorizontalAlignment(SwingConstants.LEFT);
		estL.setBounds(10, 36, 46, 14);
		add(estL);
		
		estTime = new JLabel("0:00:00.00");
		estTime.setHorizontalAlignment(SwingConstants.RIGHT);
		estTime.setBounds(138, 36, 54, 14);
		add(estTime);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 86, 182, 2);
		add(separator);
		
		JLabel lblError = new JLabel("Error");
		lblError.setHorizontalAlignment(SwingConstants.LEFT);
		lblError.setBounds(8, 249, 46, 14);
		add(lblError);
		
		error = new JLabel("0");
		error.setHorizontalAlignment(SwingConstants.RIGHT);
		error.setBounds(116, 249, 76, 14);
		add(error);
		
		JLabel lblDominantColor = new JLabel("Dominant color");
		lblDominantColor.setBounds(8, 274, 85, 14);
		add(lblDominantColor);
		
		JLabel lblMeanColor = new JLabel("Mean color");
		lblMeanColor.setBounds(8, 299, 85, 14);
		add(lblMeanColor);
		
		dominantColorIcon = new JLabel("");
		dominantColorIcon.setOpaque(true);
		dominantColorIcon.setBorder(new EtchedBorder(Color.BLACK, Color.WHITE));
		dominantColorIcon.setBackground(Color.WHITE);
		dominantColorIcon.setHorizontalAlignment(SwingConstants.RIGHT);
		dominantColorIcon.setBounds(170, 270, 22, 22);
		add(dominantColorIcon);
		
		averageColorIcon = new JLabel("");
		averageColorIcon.setOpaque(true);
		averageColorIcon.setBorder(new EtchedBorder(Color.BLACK, Color.WHITE));
		averageColorIcon.setBackground(Color.WHITE);
		averageColorIcon.setHorizontalAlignment(SwingConstants.RIGHT);
		averageColorIcon.setBounds(170, 295, 22, 22);
		add(averageColorIcon);
		
		JLabel verticesL = new JLabel("Vertices");
		verticesL.setHorizontalAlignment(SwingConstants.LEFT);
		verticesL.setBounds(10, 61, 46, 14);
		add(verticesL);
		
		verticesAmt = new JLabel("50");
		verticesAmt.setHorizontalAlignment(SwingConstants.RIGHT);
		verticesAmt.setBounds(138, 61, 54, 14);
		add(verticesAmt);
		
		JLabel evoRateL = new JLabel("Evolution rate");
		evoRateL.setHorizontalAlignment(SwingConstants.LEFT);
		evoRateL.setBounds(8, 199, 79, 14);
		add(evoRateL);
		
		evolutionRate = new JLabel("0.0");
		evolutionRate.setHorizontalAlignment(SwingConstants.RIGHT);
		evolutionRate.setBounds(136, 199, 56, 14);
		add(evolutionRate);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(8, 324, 182, 2);
		add(separator_1);
		
		savePng = new JButton("Save PNG");
		savePng.setEnabled(false);
		savePng.setBounds(8, 337, 85, 23);
		add(savePng);
		
		JLabel evoPerSecL = new JLabel("Evolutions per second");
		evoPerSecL.setHorizontalAlignment(SwingConstants.LEFT);
		evoPerSecL.setBounds(8, 224, 116, 14);
		add(evoPerSecL);
		
		evolutionsPerSecond = new JLabel("0");
		evolutionsPerSecond.setHorizontalAlignment(SwingConstants.RIGHT);
		evolutionsPerSecond.setBounds(136, 224, 56, 14);
		add(evolutionsPerSecond);
		
		colorInitBlack = new JRadioButton("Black");
		colorInitBlack.setSelected(false);
		colorInitBlack.setHorizontalAlignment(SwingConstants.RIGHT);
		colorInitBlack.setFocusable(false);
		colorInitBlack.setHorizontalTextPosition(JRadioButton.LEFT);
		colorInitBlack.setBounds(116, 367, 76, 23);
		add(colorInitBlack);
		
		colorInitRandom = new JRadioButton("Random color");
		colorInitRandom.setSelected(true);
		colorInitRandom.setHorizontalAlignment(SwingConstants.RIGHT);
		colorInitRandom.setFocusable(false);
		colorInitRandom.setHorizontalTextPosition(JRadioButton.LEFT);
		colorInitRandom.setBounds(83, 393, 109, 23);
		add(colorInitRandom);
		
		colorInitMean = new JRadioButton("Image mean");
		colorInitMean.setHorizontalAlignment(SwingConstants.RIGHT);
		colorInitMean.setFocusable(false);
		colorInitMean.setHorizontalTextPosition(JRadioButton.LEFT);
		colorInitMean.setBounds(83, 419, 109, 23);
		add(colorInitMean);
		
		colorInitDominant = new JRadioButton("Image dominant");
		colorInitDominant.setHorizontalAlignment(SwingConstants.RIGHT);
		colorInitDominant.setFocusable(false);
		colorInitDominant.setHorizontalTextPosition(JRadioButton.LEFT);
		colorInitDominant.setBounds(83, 445, 109, 23);
		add(colorInitDominant);
		
		JLabel colorInitL = new JLabel("Color initialization");
		colorInitL.setHorizontalAlignment(SwingConstants.LEFT);
		colorInitL.setBounds(10, 371, 100, 14);
		add(colorInitL);
		
		ButtonGroup g = new ButtonGroup();
		g.add(colorInitBlack);
		g.add(colorInitRandom);
		g.add(colorInitMean);
		g.add(colorInitDominant);
		
		genL = new JLabel("Generations");
		genL.setHorizontalAlignment(SwingConstants.LEFT);
		genL.setBounds(8, 99, 85, 14);
		add(genL);
		
		generations = new JLabel("0");
		generations.setHorizontalAlignment(SwingConstants.RIGHT);
		generations.setBounds(136, 99, 56, 14);
		add(generations);
		
		saveSvg = new JButton("Save SVG");
		saveSvg.setEnabled(false);
		saveSvg.setBounds(105, 337, 85, 23);
		add(saveSvg);
		
		JButton startWebGui = new JButton("WEB");
		startWebGui.setBounds(8, 419, 30, 30);
		startWebGui.setMargin(new Insets(0, 0, 0, 0));
		add(startWebGui);
		
		startWebGui.addActionListener(ev -> {
			startWebGui.setEnabled(false);
			WebStats.launch();
		});
		
		
		Timer autoSaver = new Timer();

		JButton crappyAutosaveButton = new JButton("CA");
		crappyAutosaveButton.setBounds(8, 456, 30, 30);
		crappyAutosaveButton.setMargin(new Insets(0, 0, 0, 0));
		add(crappyAutosaveButton);
		
		crappyAutosaveButton.addActionListener(ev -> {
			crappyAutosaveButton.setEnabled(false);
			
			JTextField pngPath = new JTextField();
			JTextField svgPath = new JTextField();
			Object[] message = {
				"Save images to:", null,	
			    "png:", pngPath,
			    "svg:", svgPath
			};

			int option = JOptionPane.showConfirmDialog(null, message, "Input", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.CANCEL_OPTION) {
				crappyAutosaveButton.setEnabled(true);
			    return;
			} else {
				if(pngPath.getText().isEmpty() || svgPath.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "ля у тебя пусто");
					crappyAutosaveButton.setEnabled(true);
					return;
				}
				
				File f1 = new File(pngPath.getText());
				if(!f1.exists() && !f1.mkdir()) {
					JOptionPane.showMessageDialog(null, "тупа неправильный путь: png");
					crappyAutosaveButton.setEnabled(true);
					return;
				}
				File f2 = new File(pngPath.getText());
				if(!f2.exists() && !f2.mkdir()) {
					JOptionPane.showMessageDialog(null, "тупа неправильный путь: svg");
					crappyAutosaveButton.setEnabled(true);
					return;
				}
			}
			
			System.out.println("SVG save is disabled");
			
			autoSaver.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					File f1 = new File(pngPath.getText() + generations.getText() + ".png");
					//File f2 = new File(pngPath.getText() + generations.getText() + ".svg");
					
					BufferedImage copy = null;
					try {
						f1.createNewFile();
						//f2.createNewFile();

						copy = deepCopy(EvoImage.polygonizer.getImage());
						ImageIO.write(copy, "png", f1);

						/*SVGGraphics2D g2 = new SVGGraphics2D(copy.getWidth() * 3, copy.getHeight() * 3);
						Polygon[] polygons = EvoImage.polygonizer.getPopulation().getPopulation();
						g2.scale(3, 3);
						for (Polygon p : polygons) {
							g2.setColor(p.color);
							g2.fillPolygon(new int[] { (int) p.p1.x, (int) p.p2.x, (int) p.p3.x },
									new int[] { (int) p.p1.y, (int) p.p2.y, (int) p.p3.y }, 3);
						}
						SVGUtils.writeToSVG(f2, g2.getSVGElement());
						g2.dispose();
						copy.flush();*/
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, 5000, TimeUnit.MILLISECONDS.convert(180, TimeUnit.SECONDS));
		});
		
		savePng.addActionListener(ev -> {
			JFileChooser chooser = new JFileChooser(){
			    @Override
			    public void approveSelection(){
			        File f = getSelectedFile();
			        if(f.exists() && getDialogType() == SAVE_DIALOG){
						int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_OPTION);
			            switch(result){
			                case JOptionPane.YES_OPTION:
			                    super.approveSelection();
			                    return;
			                case JOptionPane.NO_OPTION:
			                    return;
			                case JOptionPane.CLOSED_OPTION:
			                    return;
			                case JOptionPane.CANCEL_OPTION:
			                    cancelSelection();
			                    return;
			            }
			        }
			        super.approveSelection();
			    }        
			};
			chooser.setAcceptAllFileFilterUsed(true);
			if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				File f = chooser.getSelectedFile();
				if(!f.exists()) {
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					BufferedImage copy = deepCopy(EvoImage.polygonizer.getImage());
					ImageIO.write(copy, "png", f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		saveSvg.addActionListener(ev -> {
			JFileChooser chooser = new JFileChooser(){
			    @Override
			    public void approveSelection(){
			        File f = getSelectedFile();
			        if(f.exists() && getDialogType() == SAVE_DIALOG){
						int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_OPTION);
			            switch(result){
			                case JOptionPane.YES_OPTION:
			                    super.approveSelection();
			                    return;
			                case JOptionPane.NO_OPTION:
			                    return;
			                case JOptionPane.CLOSED_OPTION:
			                    return;
			                case JOptionPane.CANCEL_OPTION:
			                    cancelSelection();
			                    return;
			            }
			        }
			        super.approveSelection();
			    }        
			};
			chooser.setAcceptAllFileFilterUsed(true);
			if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				File f = chooser.getSelectedFile();
				if(!f.exists()) {
					try {
						f.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					BufferedImage notcopy = EvoImage.polygonizer.getImage();
					
					SVGGraphics2D g2 = new SVGGraphics2D(notcopy.getWidth()*3, notcopy.getHeight()*3);
					Polygon[] polygons = EvoImage.polygonizer.getPopulation().getPopulation();
					g2.scale(3, 3);
					for (Polygon p : polygons) {
						g2.setColor(p.color);
						g2.fillPolygon(new int[] {(int) p.p1.x, (int) p.p2.x, (int) p.p3.x}, new int[] {(int) p.p1.y, (int) p.p2.y, (int) p.p3.y}, 3);
					}
					SVGUtils.writeToSVG(f, g2.getSVGElement());
					g2.dispose();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 System.out.println(isAlphaPremultiplied);
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		}
	
	public static int colorInitMode() {
		if(colorInitBlack.isSelected())
			return 0;
		if(colorInitRandom.isSelected())
			return 1;
		if(colorInitMean.isSelected())
			return 2;
		if(colorInitDominant.isSelected())
			return 3;
		
		throw new RuntimeException("No init mode");
	}
	
	public void enableSaveButton() {
		savePng.setEnabled(true);
		saveSvg.setEnabled(true);
	}
	
	public void updateInformation(long sinceStart, long est, int usefulMutatuions, int uselessMutatuions, double progress, double error, Color dominantColor, Color averageColor, int generaions) {
		this.timeSinceStart.setText(convertSecondsToHMmSs(sinceStart));
//		this.estTime.setText("lol idk");
		this.usefulMutatuions.setText(String.valueOf(usefulMutatuions));
		this.uselessMutatuions.setText(String.valueOf(uselessMutatuions));
		this.progress.setText(String.format("%.1f", progress) + "%");
		this.progressLog.setText(String.format("%.1f", Math.log(progress)) + "%");
		this.evolutionRate.setText(String.format("%.1f", ((float)usefulMutatuions) / (float)(uselessMutatuions) - 1));
		this.error.setText(String.format("%.1f", error));
		this.dominantColorIcon.setBackground(dominantColor);
		this.dominantColorIcon.setToolTipText(dominantColor.toString());
		this.averageColorIcon.setBackground(averageColor);
		this.averageColorIcon.setToolTipText(averageColor.toString());
		this.generations.setText(String.valueOf(generaions));
		
		WebStats.updateInformation(sinceStart, est, usefulMutatuions, uselessMutatuions, progress, error, dominantColor, averageColor, generaions);
	}
	
	public void updateFPSInformation() {
		this.evolutionsPerSecond.setText(String.valueOf(mutationsIncrement));
		mutationsIncrement = 0;
	}
	
	public void setVertexCount(int count) {
		verticesAmt.setText(String.valueOf(count));
	}
	
	public static String convertSecondsToHMmSs(long nanos) {
		long millis = nanos / 1000000;
		long seconds = millis / 1000;
	    long s = seconds % 60;
	    long m = (seconds / 60) % 60;
	    long h = (seconds / (60 * 60)) % 24;
	    return String.format("%d:%02d:%02d", h,m,s);
	}
}