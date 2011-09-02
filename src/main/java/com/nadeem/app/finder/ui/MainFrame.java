package com.nadeem.app.finder.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import com.nadeem.app.finder.util.LogListener;

public class MainFrame extends JFrame implements LogListener {

	private static final long serialVersionUID = 1L;

	public MainFrame() {
		initFrame();
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(newSearchToolBar(), BorderLayout.NORTH);
		getContentPane().add(newStatusPanel(), BorderLayout.SOUTH);
		getContentPane().add(newResultPane(), BorderLayout.CENTER);

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
	
	private JToolBar newSearchToolBar() {
		JToolBar searchToolBar =  new JToolBar();
		searchToolBar.add(newSearchField());
		searchToolBar.add(newSearchButton());
		searchToolBar.addSeparator();
		searchToolBar.add(newAbortButton());
		
		return searchToolBar;
	}
	
	private JButton newSearchButton() {
		JButton searchButton = new JButton();
		searchButton.setIcon(Resource.getSearchIcon());
		searchButton.setToolTipText("Click to Start the search");
		return searchButton;
	}

	private JButton newAbortButton() {
		JButton abortButton = new JButton();
		abortButton.setIcon(Resource.getAbortIcon());
		abortButton.setToolTipText("Click to abort the search Operation");
		return abortButton;
	}
	
	private JTextField newSearchField() {
		JTextField searchFiled = new JTextField();
		
		searchFiled.setToolTipText("Enter the class name to search");
		searchFiled.setBackground(SystemColor.info);
		searchFiled.setMinimumSize(new Dimension(200, 20));
		searchFiled.setColumns(30);
		
		return searchFiled;
	}
	
	private JPanel newStatusPanel () {
		JPanel statusPanel = new JPanel();
		statusPanel.setPreferredSize(new Dimension(0, 20));
		statusPanel.setLayout(new GridLayout(1, 2, 2, 0));
		statusPanel.add(new JLabel("Work In Progress"));
		
		return statusPanel;
	}
	
	private JScrollPane newResultPane() {
		JScrollPane resultPane = new JScrollPane();		
		return resultPane;
	}

	public void onLog(String message) {
		
	}
}
