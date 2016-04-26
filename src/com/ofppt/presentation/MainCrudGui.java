package com.ofppt.presentation;

import com.ofppt.dao.model.Etudiant;
import com.ofppt.presentation.crudgui.CrudGui;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainCrudGui extends Application {

	public static Stage window;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		window = primaryStage;
		window.setTitle("Auto CRUD Application");

		CrudGui crudGui = new CrudGui();

		// Set 'Etudiant' GUI as an entry page to the Application
		// Replace 'Etudiant' by your Entry Class
		window.setScene(crudGui.getScene(Etudiant.class));
		window.show();

	}

}
