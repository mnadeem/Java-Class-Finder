package com.nadeem.app.finder.ui;

public class GuiDisplay {
	
	private MainFrame mainFrame;
	private SplashScreen splashScreen;
	
	public GuiDisplay() {

		splashScreen 	= createSplashScreen();
		mainFrame 		= new MainFrame();
	}

	private SplashScreen createSplashScreen() {
		return new SplashScreen() {
			private static final long serialVersionUID = 1L;

			protected void onSplashScreenTimeOut() {
				this.setVisible(false);
				this.dispose();
				mainFrame.setVisible(true);
			}
		};
	}

	public void show() {
		splashScreen.splash();		
	}
}
