package com.nadeem.app.finder.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import com.nadeem.app.finder.SearchThread;
import com.nadeem.app.finder.listener.LogListener;
import com.nadeem.app.finder.listener.MatchCountChangeListener;
import com.nadeem.app.finder.modal.SearchCriteria;
import com.nadeem.app.finder.util.DefaultFileFilter;

public class MainFrame extends JFrame implements LogListener, MatchCountChangeListener {

	private static final long serialVersionUID = 1L;
	
	private JList resultModel 			= new JList(new DefaultListModel());
	private JFileChooser fileChooser 	= null;
	private SearchThread searchThread;
	private volatile SearchCriteria searchCriteria;
	
	private JTextField locationField;
	private JTextField fileNameField;
	
	private JButton abortButton;
	private JButton searchButton;
	
	private JCheckBox deepSearchRequired;
	
	private JLabel matchCountLabel;

	public MainFrame() {
		initFrame();
		initFileChooser();
		searchCriteria 	= new SearchCriteria();		
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(newSearchToolBar(), BorderLayout.NORTH);
		getContentPane().add(newStatusPanel(), BorderLayout.SOUTH);
		getContentPane().add(newResultPane(), BorderLayout.CENTER);

	}

	private void initFileChooser() {
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    fileChooser.setMultiSelectionEnabled(false);
	    this.fileChooser.setFileFilter(new DefaultFileFilter());
	}

	private void initFrame() {
		setVisible(false);
		setTitle("Class Finder");
		setIconImage(Resource.getTitleIcon().getImage());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		UiUtil.setDefaultSize(this);
		UiUtil.setDefaultLocation(this);
		UiUtil.setDefaultLookAndFeel();
	}
	
	private JPanel newSearchToolBar() {
		JPanel searchToolBar =  new JPanel();		
		searchToolBar.setLayout(new GridLayout(2, 1));
		
		searchToolBar.add(newSearchPanel());			
		searchToolBar.add(newLocationPanel());
		
		return searchToolBar;
	}
	
	private JToolBar newSearchPanel () {
		JToolBar searchPanel = new JToolBar();
		fileNameField =newSearchField();
		searchPanel.add(fileNameField);
		searchPanel.add(newDeepSearchCheckBox());
		searchPanel.addSeparator();
		searchPanel.add(newSearchButton());
		searchPanel.addSeparator();
		searchPanel.add(newAbortButton());
		return searchPanel;
	}
	
	private JCheckBox newDeepSearchCheckBox() {
		deepSearchRequired = new JCheckBox();
		deepSearchRequired.setText("Deep Search");
		deepSearchRequired.setSelected(false);
		deepSearchRequired.setBounds(206, 6, 97, 22);
		return deepSearchRequired;
	}
	
	private JToolBar newLocationPanel () {
		JToolBar locationPanel = new JToolBar();
		locationField = newLocationField();
		
		locationPanel.add(locationField);
		locationPanel.add(chooseLocationButton());
		
		return locationPanel;
	}
	
	private JButton newSearchButton() {
		searchButton = new JButton();
		searchButton.setIcon(Resource.getSearchIcon());
		searchButton.setToolTipText("Click to Start the search");
		searchButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				abortButton.setEnabled(true);
				searchButton.setEnabled(false);
				searchCriteria.clearPath();
				searchCriteria.setFileName(fileNameField.getText());
				searchCriteria.addPath(locationField.getText());
				searchCriteria.setDeepSearch(deepSearchRequired.isSelected());

				((DefaultListModel)resultModel.getModel()).clear();
				searchThread 	= new SearchThread(searchCriteria, MainFrame.this) {
					@Override
					protected void onSearchFinish() {
						abortButton.setEnabled(false);
						searchButton.setEnabled(true);
					}
				};
				searchThread.addCountChageListener(MainFrame.this);
				searchThread.start();				
				
			}
		});
		return searchButton;
	}

	private JButton newAbortButton() {
		abortButton = new JButton();
		abortButton.setIcon(Resource.getAbortIcon());
		abortButton.setToolTipText("Click to abort the search Operation");
		abortButton.setEnabled(false);
		
		abortButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				searchButton.setEnabled(true);
				abortButton.setEnabled(false);
				searchThread.abortSearch();	
				onLog("Done!!!");
			}
		});
		
		return abortButton;
	}
	
	private JTextField newSearchField() {
		JTextField searchFiled = new JTextField();
		
		searchFiled.setToolTipText("Enter the class name to search");
		searchFiled.setBackground(SystemColor.info);
		searchFiled.setColumns(30);
		
		return searchFiled;
	}
	
	private JPanel newStatusPanel () {
		JPanel statusPanel = new JPanel();
		statusPanel.setPreferredSize(new Dimension(0, 25));
		statusPanel.setLayout(new GridLayout(1, 2, 2, 0));
		
		matchCountLabel = new JLabel("Search Not Started");
		statusPanel.add(matchCountLabel);
		
		return statusPanel;
	}
	
	private JScrollPane newResultPane() {
		JScrollPane resultPane = new JScrollPane();	
		resultPane.setViewportView(resultModel);
		resultPane.setBorder(new EtchedBorder());
		return resultPane;
	}
	
	private JButton chooseLocationButton() {
		JButton chooseDirectory = new JButton("Choose Location");
		chooseDirectory.setFont(new  Font("sansserif", Font.BOLD, 14));  
		chooseDirectory.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				fileChooser.showDialog(MainFrame.this, "Select");
				File selectedFile = fileChooser.getSelectedFile();
				if (selectedFile != null) {
					locationField.setText(selectedFile.getAbsolutePath());
				}				
			}
		});
		chooseDirectory.setMaximumSize(new Dimension(50, 200));
		
		return chooseDirectory;
	}
	
	private JTextField newLocationField() {
		JTextField locationFiled = new JTextField();
		
		locationFiled.setToolTipText("Choose Search Location");
		locationFiled.setBackground(SystemColor.info);
		locationFiled.setColumns(80);
		return locationFiled;		
	}

	public void onLog(String message) {
		((DefaultListModel)resultModel.getModel()).addElement(message);
	}

	public void onCountChange(int newCount) {
		matchCountLabel.setText("Match Count = " + newCount);		
	}
}
