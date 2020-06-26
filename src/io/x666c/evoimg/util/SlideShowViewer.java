package io.x666c.evoimg.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class SlideShowViewer {
	
	public static void main(String[] args) throws Exception {
		List<Path> files = Files.list(Paths.get("C:\\Users\\Jiftoo\\Desktop\\autosavedImages\\dio")).collect(Collectors.toList());
		
		files.sort(new Comparator<Path>() {
			public int compare(Path o1, Path o2) {
				try {
					int n1 = Integer.parseInt(o1.getFileName().toString().split("\\.")[0]);//.replace("gen", ""));
					int n2 = Integer.parseInt(o2.getFileName().toString().split("\\.")[0]);//.replace("gen", ""));
					return n1 - n2;
				} catch (Exception e) {
					return 0;
				}
			};
		});
		
		List<BufferedImage> imgs = files.stream().map(p -> {
			try {
				return ImageIO.read(p.toFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
		
		JFrame f = new JFrame();
		JLabel l = new JLabel();
		l.setPreferredSize(new Dimension(imgs.get(0).getWidth(), imgs.get(0).getHeight()));
		f.setLayout(new BorderLayout());
		f.add(l, BorderLayout.CENTER);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		for (BufferedImage bi : imgs) {
			SwingUtilities.invokeAndWait(() -> {
				l.setIcon(new ImageIcon(bi));
			});
			Thread.sleep(16);
		}
	}
	
}