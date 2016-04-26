package com.ofppt.presentation.crudgui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.ofppt.common.InstanceFactory;
import com.ofppt.metier.CrudServiceMetierImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class GuiAdapter {

	CrudServiceMetierImpl crudServiceMetier;
	static GuiAdapter instance;

	public static GuiAdapter getInstance() {

		if (instance == null) {
			instance = new GuiAdapter();
		}
		return instance;
	}

	private GuiAdapter() {
		crudServiceMetier = InstanceFactory.getCrudServiceMetier();
	}

	/**
	 * Set message to the Label
	 * 
	 * @param message
	 * @param screen
	 */
	public static void showMsg(String message, Object screen) {
		if (screen.getClass().equals(Label.class)) {
			((Label) screen).setText(message);
		}
	}

	/**
	 * Refresh The Table by recall the readAll()
	 * 
	 * @param type
	 * @param table
	 */
	public void refresh(Class<? extends Object> classModel, TableView<Object> table) {
		table.setItems(readAll(classModel));
	}

	/**
	 * Create an empty javaFx Table that has columns names equal to fields names
	 * of the model Class
	 * 
	 * @param type
	 * @return
	 */
	public TableView<Object> createTable(Class<? extends Object> classModel) {

		TableView<Object> table = new TableView<>();

		Iterator<Entry<Field, Class<? extends Object>>> fields = crudServiceMetier.getFieldsAndType(classModel)
				.entrySet().iterator();

		while (fields.hasNext()) {
			Entry<Field, Class<? extends Object>> field = fields.next();
			String fieldName = field.getKey().getName();

			// Create a column for each field in the class model
			TableColumn<Object, String> column = new TableColumn<Object, String>(fieldName);
			column.setMinWidth(150);
			// Set the data type of the column
			column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
			table.getColumns().add(column);
		}

		return table;
	}

	/**
	 * Convert the List of data return from DB, to ObservableList<Object> that
	 * accepted by the table of javaFx
	 * 
	 * @param type
	 * @return
	 */
	public ObservableList<Object> readAll(Class<? extends Object> classModel) {
		Iterator<Object> itr = crudServiceMetier.readAll(classModel).iterator();
		ObservableList<Object> list = FXCollections.observableArrayList();

		while (itr.hasNext()) {
			list.add(itr.next());
		}

		return list;
	}

	/**
	 * Create a specific input to every type of fields of the class model
	 * 
	 * @param field
	 * @return
	 */

	public Object[] createInputsLabels(Entry<Field, Class<? extends Object>> field) {

		CrudServiceMetierImpl sm = InstanceFactory.getCrudServiceMetier();
		Object[] inputsLabels = new Object[2];

		// Create Label for the input
		Label label = new Label(field.getKey().getName());
		inputsLabels[0] = label;

		// For Collection create a ListView<Object>
		if (field.getValue().equals(Collection.class)) {

			/*
			 * get the type of the Collection<PARAMETER> 'PARAMETER', so we can
			 * fetch the objects and put them in the choiceBox of the GUI
			 */
			ParameterizedType collectionClass = (ParameterizedType) field.getKey().getGenericType();
			Class<?> classOfParamColl = (Class<?>) collectionClass.getActualTypeArguments()[0];

			Collection<Object> values = sm.readAll(classOfParamColl);

			ListView<Object> listView = new ListView<Object>();
			listView.setMaxSize(300, 200);

			listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			listView.getItems().addAll(values);
			inputsLabels[1] = listView;

			// For Date create a DatePicker
		} else if (field.getValue().equals(Date.class)) {

			DatePicker datePicker = new DatePicker();
			inputsLabels[1] = datePicker;

			// For int,double,float or String create TextField
		} else if (field.getValue().equals(String.class) || field.getValue().equals(int.class)
				|| field.getValue().equals(double.class) || field.getValue().equals(float.class)) {

			TextField textField = new TextField();
			textField.setMaxWidth(300);
			textField.setPromptText(field.getKey().getName() + "  [ " + field.getValue().getName() + " ]");
			inputsLabels[1] = textField;

			// For Objects of other class create a ComboBox
		} else {

			Collection<Object> values = sm.readAll(field.getValue());

			ComboBox<Object> comboBox = new ComboBox<Object>();
			comboBox.setMaxWidth(300);
			comboBox.getItems().addAll(values);
			inputsLabels[1] = comboBox;
		}

		return inputsLabels;

	}

	/**
	 * Create a VBox that will show in the right side of the GUI
	 * 
	 * @param classModel
	 * @param inputsLabels
	 * @return
	 */
	public VBox createInputsVbox(Class<? extends Object> classModel, Map<Object, Object> inputsLabels) {

		Iterator<Entry<Object, Object>> itr = inputsLabels.entrySet().iterator();
		VBox VboxLabelInput = new VBox(10);
		while (itr.hasNext()) {

			Entry<Object, Object> inputAndLabel = itr.next();

			if (inputAndLabel.getKey().getClass().equals(TextField.class)) {
				VboxLabelInput.getChildren()
						.add(new VBox((Label) inputAndLabel.getValue(), (TextField) inputAndLabel.getKey()));

			} else if (inputAndLabel.getKey().getClass().equals(DatePicker.class)) {
				VboxLabelInput.getChildren()
						.add(new VBox((Label) inputAndLabel.getValue(), (DatePicker) inputAndLabel.getKey()));

			} else if (inputAndLabel.getKey().getClass().equals(ListView.class)) {
				VboxLabelInput.getChildren()
						.add(new VBox((Label) inputAndLabel.getValue(), (ListView<?>) inputAndLabel.getKey()));

			} else if (inputAndLabel.getKey().getClass().equals(ComboBox.class)) {
				VboxLabelInput.getChildren()
						.add(new VBox((Label) inputAndLabel.getValue(), (ComboBox<?>) inputAndLabel.getKey()));
			}
		}
		return VboxLabelInput;
	}

	/**
	 * Put the object attributes in the GUI inputs so we can modified them
	 * 
	 * @param input
	 * @param getterMethod
	 * @param object
	 */
	public void setObjectInInput(Object input, Method getterMethod, Object object) {

		try {

			if (input.getClass().equals(TextField.class)) {
				((TextField) input).setText((String) getterMethod.invoke(object).toString());
			}

			else if (input.getClass().equals(DatePicker.class)) {

				// Convert java.sql.Timestamp (DB)->
				// java.time.LocalDate(DatePicker)
				Timestamp timeStamp = (Timestamp) getterMethod.invoke(object);
				LocalDate localDate = (LocalDate) timeStamp.toLocalDateTime().toLocalDate();
				((DatePicker) input).setValue(localDate);
			}

		} catch (Exception e) {
			e.getMessage();
		}

	}

	/**
	 * Valid the user inputs
	 * 
	 * @param inpute
	 * @param typeOfField
	 * @param errorLabel
	 * @return Object
	 */

	public Object valideTheInput(Entry<Object, Object> inputLabel, Class<? extends Object> typeOfField, Object screen) {

		String labelOfTheInput = ((Label) inputLabel.getValue()).getText();
		Object castedValue = null;
		String inputValue = null;
		Date date = null;

		try {

			// Input for object field (class has entity of other class)
			if (inputLabel.getKey().getClass().equals(ComboBox.class)) {

				Object object = ((ComboBox) inputLabel.getKey()).getSelectionModel().getSelectedItem();
				castedValue = object;

			}

			// Input for the collection field
			else if (inputLabel.getKey().getClass().equals(ListView.class)) {

				Collection<Object> collection = new ArrayList<Object>();

				ObservableList<Object> itemsSelected = ((ListView<Object>) inputLabel.getKey()).getSelectionModel()
						.getSelectedItems();

				Iterator<Object> itr = itemsSelected.iterator();
				while (itr.hasNext()) {
					collection.add(itr.next());
				}
				castedValue = collection;

			}

			// Input for Date field
			else if (inputLabel.getKey().getClass().equals(DatePicker.class)) {

				// Convert java.time.LocalDate (DatePicker) -> java.util.Date
				LocalDate localDate = ((DatePicker) inputLabel.getKey()).getValue();
				date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				castedValue = date;

				// Input for int, double, float and String
			} else if (!inputLabel.getKey().getClass().equals(DatePicker.class)) {

				inputValue = ((TextField) inputLabel.getKey()).getText();

				if (typeOfField.equals(int.class)) {
					castedValue = Integer.parseInt(inputValue);
				} else if (typeOfField.equals(String.class)) {
					castedValue = inputValue;
				} else if (typeOfField.equals(double.class)) {
					castedValue = Double.parseDouble(inputValue);
				} else if (typeOfField.equals(float.class)) {
					castedValue = Float.parseFloat(inputValue);
				}
			}

		} catch (Exception e) { // NumberFormatException
			if (screen.getClass().equals(Label.class)) {
				((Label) screen).setText("[ " + labelOfTheInput + " ] " + " Must be of type : " + typeOfField);
			}
		}
		return castedValue;
	}

}
