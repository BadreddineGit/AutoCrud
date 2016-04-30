package com.ofppt.presentation.crudgui;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ofppt.common.InstanceFactory;
import com.ofppt.dao.model.Cour;
import com.ofppt.dao.model.Etudiant;
import com.ofppt.dao.model.Note;
import com.ofppt.dao.model.Professeur;
import com.ofppt.metier.CrudServiceMetierImpl;
import com.ofppt.presentation.MainApp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CrudGui {

	static public TableView<Object> table;
	static public Class<? extends Object> classModelVar;
	private Scene scene;
	private Label selectLabel, errorLabel;
	private TextField inputId;
	private Button deleteButton, addButton, selectButton, updateButton, refreshButton, findButton;

	private GuiAdapter guiAdapter;
	private CrudServiceMetierImpl crudServiceMetier;

	public CrudGui() {
		guiAdapter = InstanceFactory.getGuiAdapter();
		crudServiceMetier = InstanceFactory.getCrudServiceMetier();
	}

	public Scene getScene(Class<? extends Object> classModel) {
		
		classModelVar = classModel;
		
		Map<Object, Object> inputsLabels = crudServiceMetier.createInputsAndLabels(classModel);

		/* MenuBar */

		VBox TopLayout = new VBox();

		Menu fileMenu = new Menu("_File"); // short cut ALT + F
		Menu editMenu = new Menu("_Edit");
		Menu sourceMenu = new Menu("_Source");

		fileMenu.getItems().add(new MenuItem("Edit ..."));
		fileMenu.getItems().add(new MenuItem("Find ..."));

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, editMenu, sourceMenu);

		/* Model Classes */

		Map<String, Class<? extends Object>> menu = new LinkedHashMap<String, Class<? extends Object>>();

		menu.put("Etudiant", Etudiant.class);
		menu.put("Professeur", Professeur.class);
		menu.put("Cour", Cour.class);
		menu.put("Note", Note.class);
		
		// menu.put("My Class", NewClass.class);

		Iterator<Entry<String, Class<? extends Object>>> itr = menu.entrySet().iterator();
		HBox HboxModels = new HBox();

		while (itr.hasNext()) {

			Entry<String, Class<? extends Object>> button = itr.next();

			Button modelButton = new Button(button.getKey());
			modelButton.setMinWidth(150);
			modelButton.setOnAction(e -> MainApp.window.setScene(getScene(button.getValue())));
			HboxModels.getChildren().add(modelButton);

		}

		HboxModels.setSpacing(10);
		HboxModels.setAlignment(Pos.BASELINE_CENTER);
		HboxModels.setPadding(new Insets(20, 20, 20, 20));
		TopLayout.getChildren().addAll(menuBar, HboxModels);

		/* Tableau */

		table = guiAdapter.createTable(classModel);
		table.setItems(guiAdapter.readAll(classModel));

		/* Inputs */

		VBox VboxLabelInput = new VBox(10);
		VboxLabelInput = guiAdapter.createInputsVbox(classModel, inputsLabels);
		VboxLabelInput.setPadding(new Insets(10, 10, 10, 10));

		/* Buttons */

		addButton = new Button("Nouvelle");
		addButton.setMinWidth(150);
		addButton.setOnAction(e -> crudServiceMetier.create(classModel, inputsLabels, errorLabel, selectLabel));

		deleteButton = new Button("Suprimer");
		deleteButton.setMinWidth(150);
		deleteButton.setOnAction(
				e -> crudServiceMetier.delete(table.getSelectionModel().getSelectedItem(), classModel, errorLabel));

		selectButton = new Button("Selectionner");
		selectButton.setMinWidth(150);
		selectButton.setOnAction(e -> crudServiceMetier.select(classModel, table.getSelectionModel().getSelectedItem(),
				inputsLabels, selectLabel));

		updateButton = new Button("Modifier");
		updateButton.setMinWidth(150);
		updateButton.setOnAction(e -> crudServiceMetier.update(classModel, inputsLabels, errorLabel));

		inputId = new TextField();
		inputId.setPromptText("cherche par Id");

		findButton = new Button("Trouver");
		findButton.setMinWidth(150);
		findButton.setOnAction(e -> crudServiceMetier.find(inputId.getText(), classModel, inputsLabels, selectLabel));

		refreshButton = new Button("Refresh");
		refreshButton.setMinWidth(150);
		refreshButton.setOnAction(e -> guiAdapter.refresh(classModel, table));

		VBox vboxCrudButton = new VBox();
		vboxCrudButton.getChildren().addAll(addButton, deleteButton, selectButton, updateButton, inputId,
				findButton, refreshButton);
		vboxCrudButton.setPadding(new Insets(10, 10, 10, 10));
		vboxCrudButton.setSpacing(10);

		errorLabel = new Label();
		selectLabel = new Label();

		VBox VboxBottom = new VBox(selectLabel, errorLabel);
		VboxBottom.setPadding(new Insets(10, 10, 10, 170));

		BorderPane borderPane = new BorderPane();
		borderPane.setTop(TopLayout);
		borderPane.setLeft(vboxCrudButton);
		borderPane.setCenter(table);
		borderPane.setRight(VboxLabelInput);
		borderPane.setBottom(VboxBottom);
		borderPane.setPrefWidth(1000);

		scene = new Scene(borderPane);
		return scene;
	}

}
