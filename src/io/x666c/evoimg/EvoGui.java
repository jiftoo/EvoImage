package io.x666c.evoimg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import io.x666c.evoimg.gui.ControlsPanel;
import io.x666c.evoimg.gui.DisplayCanvas;
import io.x666c.evoimg.gui.InfoPane;

public class EvoGui extends JFrame {

	private DisplayCanvas canvas;
	public ControlsPanel controls;
	private InfoPane infoPane;

	public static boolean startedOnce = false;

	private JPanel canvasPane;
	
public static final String VERSION = "1.4.3";	
	
	EvoGui() {
		setTitle("EvoGui " + VERSION);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new BorderLayout());

		canvas = new DisplayCanvas();
		canvas.setBorder(new LineBorder(Color.BLACK, 1));
		canvasPane = new JPanel();
		canvasPane.setLayout(null);
		canvasPane.setPreferredSize(new Dimension(DisplayCanvas.DISPLAY_WIDTH + 20, DisplayCanvas.DISPLAY_HEIGHT + 20));
		canvasPane.add(canvas);
		add(canvasPane);

		infoPane = new InfoPane();
		add(infoPane, BorderLayout.WEST);

		controls = new ControlsPanel(infoPane, this);
		add(controls, BorderLayout.EAST);

		pack();
		setLocationRelativeTo(null);
	}

	public InfoPane getInfoPane() {
		return infoPane;
	}

	public void packCanvas() {
		canvasPane.setPreferredSize(new Dimension(DisplayCanvas.DISPLAY_WIDTH + 20, DisplayCanvas.DISPLAY_HEIGHT + 20));
		canvas.resize();
		pack();
	}

	public void setLoadImageCallback(Supplier<String> cb) {
		controls.loadImage.addActionListener(ev -> {
			try {
				String path = cb.get();
				if (path != null) {
					SwingUtilities.invokeLater(() -> {
						controls.start.setEnabled(true);
						controls.loadedImagePath.setText(path);
						try {
							setImageForPreview(ImageIO.read(new File(path)));
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex);
			}
		});
	}

	public void setStartCallback(Function<ControlsPanel, String> cb) {
		controls.start.addActionListener(ev -> {
			controls.start.setText(cb.apply(controls));
			infoPane.enableSaveButton();
		});
	}

	public void setImageForPreview(BufferedImage img) {
		controls.setImageForPreview(img);
	}

	public BufferedImage getSelectedImage() {
		return controls.getSelectedImage();
	}

	public boolean isAutosave() {
		return controls.isAutosave();
	}

	public boolean isNewFileEachTime() {
		return controls.isNewFileEachTime();
	}

	public int getVertexAmount() {
		return controls.getVertexAmount();
	}

	public void redrawCanvas(BufferedImage img) {
		canvas.drawImg(img);
	}

	public void enableAll(boolean should) {
		SwingUtilities.invokeLater(() -> {
			// controls.autosaveLocationSelector.setEnabled(should);
			controls.loadImage.setEnabled(should);
			// controls.autosave.setEnabled(should);
			controls.separateFile.setEnabled(should && controls.autosave.isSelected());
			controls.vertexAmount.setEnabled(should);
			controls.seedField.setEnabled(should);
		});
	}

}