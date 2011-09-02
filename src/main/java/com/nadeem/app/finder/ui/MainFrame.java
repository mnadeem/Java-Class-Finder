package com.nadeem.app.finder.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainFrame() {
		initFrame();
		
		getContentPane().add(new JLabel("Work in progress !!!"));
	}

	private void initFrame() {
		this.setVisible(false);

		setIconImage(Resource.getTitleIcon().getImage());
		setTitle("Class Finder");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		UiUtil.setDefaultSize(this);
		UiUtil.setDefaultLocation(this);
		UiUtil.setDefaultLookAndFeel();
	}
}
