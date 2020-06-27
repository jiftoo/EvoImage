package io.x666c.evoimg.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultFormatter;

import io.x666c.evoimg.EvoGui;

public class ControlsPanel extends JPanel {

	public JTextField autosaveLocation;
	public JTextField loadedImagePath;
	public JCheckBox autosave;
	public JCheckBox separateFile;
	public JButton autosaveLocationSelector;
	public JButton loadImage;
	public JSpinner vertexAmount;
	public JButton start;
	private JLabel imagePreview;
	public JTextField seedField;

	private EvoGui gui;

	public ControlsPanel(InfoPane p, EvoGui gui) {
		this.gui = gui;

		setPreferredSize(new Dimension(200, 500));
		setBorder(new CompoundBorder(new EmptyBorder(1, 0, 0, 0), new EtchedBorder(EtchedBorder.LOWERED)));
		setLayout(null);

		start = new JButton("Start");
		start.setEnabled(false);
		start.setFont(new Font("Tahoma", Font.PLAIN, 25));
		start.setBounds(10, 11, 180, 47);
		add(start);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 69, 180, 2);
		add(separator);

		vertexAmount = new JSpinner(new SpinnerNumberModel(50, 5, 10000, 1));
		((DefaultFormatter) ((JSpinner.NumberEditor) vertexAmount.getEditor()).getTextField().getFormatter())
				.setCommitsOnValidEdit(true);
		vertexAmount.addChangeListener(ev -> {
			if (start.getText().equals("Start"))
				p.setVertexCount(Integer.parseInt(vertexAmount.getValue().toString()));
		});
		vertexAmount.setBounds(110, 82, 80, 20);
		add(vertexAmount);

		JLabel vertexAmountL = new JLabel("Vertex amount");
		vertexAmountL.setBounds(10, 82, 80, 20);
		add(vertexAmountL);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 138, 180, 2);
		add(separator_1);

		autosave = new JCheckBox("Autosave");
		autosave.setEnabled(false);
		autosave.setHorizontalAlignment(SwingConstants.RIGHT);
		autosave.setHorizontalTextPosition(SwingConstants.LEFT);
		autosave.setBounds(119, 147, 71, 23);
		add(autosave);

		separateFile = new JCheckBox("Create new file each time?");
		separateFile.setEnabled(false);
		separateFile.setHorizontalAlignment(SwingConstants.RIGHT);
		separateFile.setHorizontalTextPosition(SwingConstants.LEFT);
		separateFile.setBounds(37, 175, 153, 23);
		add(separateFile);

		autosaveLocationSelector = new JButton("Browse...");
		autosaveLocationSelector.setEnabled(false);
		autosaveLocationSelector.setMargin(new Insets(0, 0, 0, 0));
		autosaveLocationSelector.setBounds(10, 205, 60, 23);
		add(autosaveLocationSelector);

		autosaveLocation = new JTextField();
		autosaveLocation.setHorizontalAlignment(SwingConstants.LEFT);
		autosaveLocation.setEditable(false);
		autosaveLocation.setBounds(80, 206, 110, 20);
		add(autosaveLocation);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 237, 180, 2);
		add(separator_2);

		imagePreview = new JLabel();
		imagePreview.setHorizontalAlignment(SwingConstants.CENTER);
		imagePreview.setBounds(10, 250, 180, 180);
		imagePreview.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		add(imagePreview);

		loadImage = new JButton("Open...");
		loadImage.setBounds(94, 441, 96, 23);
		add(loadImage);

		loadedImagePath = new JTextField();
		loadedImagePath.setEditable(false);
		loadedImagePath.setHorizontalAlignment(SwingConstants.LEFT);
		loadedImagePath.setBounds(10, 469, 180, 20);
		add(loadedImagePath);

		JLabel seedL = new JLabel("Seed");
		seedL.setBounds(10, 113, 46, 14);
		add(seedL);

		seedField = new JTextField();
		seedField.setText("seed");
		seedField.setHorizontalAlignment(JTextField.RIGHT);
		seedField.setBounds(110, 110, 80, 20);
		add(seedField);
	}

	private BufferedImage notPreview;

	public void setImageForPreview(BufferedImage img) {
		notPreview = img;

		BufferedImage resize = new BufferedImage(imagePreview.getWidth(), imagePreview.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resize.createGraphics();
		g.drawImage(img, 0, 0, imagePreview.getWidth(), imagePreview.getHeight(), null);

		imagePreview.setIcon(new ImageIcon(resize));

		final BufferedImage sel = getSelectedImage();
		int tw = sel.getWidth();
		int th = sel.getHeight();
		// This is utterly dumb
//		int b = tw < th ? 1 : 2;
//		if(b == 1) {
//			while (tw > DisplayCanvas.DISPLAY_SIZE_LIMIT) {
//				tw--;
//				th--;
//			}
//		} else {
//			while (th > DisplayCanvas.DISPLAY_SIZE_LIMIT) {
//				tw--;
//				th--;
//			}
//		}
		final Dimension scaled = getScaledDimension(new Dimension(tw, th), new Dimension(DisplayCanvas.DISPLAY_SIZE_LIMIT, DisplayCanvas.DISPLAY_SIZE_LIMIT));
		
		DisplayCanvas.DISPLAY_WIDTH = scaled.width;
		DisplayCanvas.DISPLAY_HEIGHT = scaled.height;
		gui.packCanvas();
	}

	// Am i stupid?
	// -- yes i am heck
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = original_width;
		int new_height = original_height;

		if (original_width > bound_width) {
			new_width = bound_width;
			new_height = (new_width * original_height) / original_width;
		}

		if (new_height > bound_height) {
			new_height = bound_height;
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);
	}

	public BufferedImage getSelectedImage() {
		return notPreview;// (BufferedImage)((ImageIcon)imagePreview.getIcon()).getImage();
	}

	public boolean isAutosave() {
		return autosave.isSelected();
	}

	public boolean isNewFileEachTime() {
		return separateFile.isSelected();
	}

	public int getVertexAmount() {
		return (int) vertexAmount.getValue();
	}
}