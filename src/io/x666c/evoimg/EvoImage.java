package io.x666c.evoimg;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.x666c.evoimg.gui.ControlsPanel;
import io.x666c.evoimg.gui.DisplayCanvas;
import io.x666c.evoimg.internal.Comparator;
import io.x666c.evoimg.internal.Polygonizer;
import io.x666c.glib4j.math.MathUtil;
import li.flor.nativejfilechooser.NativeJFileChooser;

// USE *TitledBorder* IT'S AWESOME!!11!

public class EvoImage {
	
	static {
//		System.setProperty("sun.java2d.opengl", "true");
	}
	 
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		JFrame dummy = new JFrame("Approximation");
		dummy.setUndecorated(true);
		dummy.setVisible(true);
		dummy.setLocationRelativeTo(null);
		JDialog f = new JDialog(dummy, true);
		f.setTitle("Approximation");
		f.setDefaultCloseOperation(0);
		JPanel p2 = new JPanel();
		p2.setBorder(new EmptyBorder(20,10,20,10));
		p2.setLayout(new BorderLayout());
		f.setContentPane(p2);
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p2.add(p, BorderLayout.CENTER);
		JLabel txt = new JLabel("Please select an approximation level:");
		txt.setBorder(new EmptyBorder(1,0,25,0));
		ButtonGroup gr = new ButtonGroup();
		JRadioButton none = new JRadioButton("None (every pixel)");
		JRadioButton low = new JRadioButton("Low (every second pixel)");
		JRadioButton high = new JRadioButton("High (every third pixel)");
		JRadioButton extreme = new JRadioButton("Extreme (every forth pixel)");
		JButton ok = new JButton("Ok");
		ok.setMargin(new Insets(4, 25, 4, 25));
		gr.add(none);
		gr.add(low);
		gr.add(high);
		gr.add(extreme);
		gr.setSelected(low.getModel(), true);
		p.add(txt);
		p.add(none);
		p.add(low);
		p.add(high);
		p.add(extreme);
		p.add(ok);
		ok.addActionListener(ev -> {
			f.setVisible(false);
			f.dispose();
			dummy.setVisible(false);
			dummy.dispose();
			if(none.isSelected()) {
				Comparator.APPROXIMATION = 1;
			} else if(low.isSelected()) {
				Comparator.APPROXIMATION = 2;
			} else if(high.isSelected()) {
				Comparator.APPROXIMATION = 3;
			} else if(extreme.isSelected()) {
				Comparator.APPROXIMATION = 4;
			}
		});
		
		f.pack();
		f.setLocationRelativeTo(null);
		
		f.setVisible(true);
		
		new EvoImage().start();
	}
	
	public static Polygonizer polygonizer;
	
	private Thread infoPanelUpdater;
	private Thread infoPanelFPSUpdater;
	
	private EvoGui gui;
	
	private void start() throws Exception {
		gui = new EvoGui();
		
		MathUtil.randomSeed(gui.controls.seedField.getText().hashCode());

		gui.setLoadImageCallback(this::loadImage);
		gui.setStartCallback(this::startCb);

		gui.setVisible(true);
	}
	
	private String loadImage() {
		JFileChooser chooser = new JFileChooser();
//		JFileChooser chooser = new NativeJFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new FileNameExtensionFilter("Images (png, jpg, gif, bmp)", "png", "jpg", "jpeg", "gif", "bmp"));
		if(chooser.showDialog(gui, "Open") == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}
	
	private String startCb(ControlsPanel p) {
		String retString = "";
		
		MathUtil.randomSeed(gui.controls.seedField.getText().hashCode());
		
		gui.controls.start.setEnabled(false);
		
		if(polygonizer == null || polygonizer.isBroken()) {
			final BufferedImage sel = p.getSelectedImage();
			startPolygonizer(sel, p.getVertexAmount(), DisplayCanvas.DISPLAY_WIDTH, DisplayCanvas.DISPLAY_HEIGHT);
			polygonizer.setDrawCallback(() -> {
				gui.redrawCanvas(polygonizer.getImage());
			});
			gui.enableAll(false);
			
			loop = true;
			
			infoPanelUpdater = new Thread(this::infoUpdate);
			infoPanelFPSUpdater = new Thread(this::infoFPSUpdate);
			infoPanelUpdater.start();
			infoPanelFPSUpdater.start();
			
			retString = "Stop";
		} else {
			stopPolygonizerAndInfoThread();
			
			gui.enableAll(true);
			retString = "Start";
		}
		
		gui.controls.start.setEnabled(true);
		
		return retString;
	}
	
	private volatile boolean loop = true;
	private void infoUpdate() {
		long startTime = System.nanoTime();
		while(loop) {
			gui.getInfoPane().updateInformation(System.nanoTime() - startTime, 0, polygonizer.getGoodMutations(), polygonizer.getBadMutations(), polygonizer.getCompleteness(), polygonizer.getError(), polygonizer.getDominantColor(), polygonizer.getAverageColor(), polygonizer.getGenerations());
			try {
				Thread.sleep(20);
			} catch (Exception e) {}
		}
	}
	
	private void infoFPSUpdate() {
		while(loop) {
			try {
				Thread.sleep(500);
				gui.getInfoPane().updateFPSInformation();
			} catch (Exception e) {}
			
		}
	}
	
	private void startPolygonizer(BufferedImage goal, int vertices, int width, int height) {
		polygonizer = new Polygonizer(goal, vertices, width, height);
		polygonizer.start();
	}
	
	private void stopPolygonizerAndInfoThread() {
		polygonizer.release();
		loop = false;
		try {
			infoPanelUpdater.join();
			infoPanelFPSUpdater.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}