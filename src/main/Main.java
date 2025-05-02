package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.HomePage;

public class Main extends Application{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		new HomePage(stage);
		
		stage.setTitle("Pudding Menu");
		stage.show();
	}

}
