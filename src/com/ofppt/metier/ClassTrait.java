package com.ofppt.metier;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ofppt.common.InstanceFactory;
import com.ofppt.presentation.crudgui.GuiAdapter;

public class ClassTrait {

	static ClassTrait instance;

	public static ClassTrait getInstance() {
		if (instance == null) {
			instance = new ClassTrait();
		}
		return instance;
	}

	private ClassTrait() {

	}

	/**
	 * Get the fields And Type of the class model
	 * 
	 * @param
	 * @return Map contain the fields of the class as the Key and the type as
	 *         the Value
	 */

	public Map<Field, Class<? extends Object>> getFieldsAndType(Class<? extends Object> modelClass) {

		Map<Field, Class<? extends Object>> fields = new LinkedHashMap<>();

		// Get field of the SuperClass
		Field[] superFields = modelClass.getSuperclass().getDeclaredFields();
		for (Field f : superFields) {
			fields.put(f, f.getType());
		}

		// Get field of the Class Model
		Field[] classFields = modelClass.getDeclaredFields();
		for (Field f : classFields) {
			fields.put(f, f.getType());
		}
		return fields;
	}

	/**
	 * Generate the Getters() names of the Class Model
	 * 
	 * @param
	 * @return List of String contain the getters names of the class
	 */

	public List<String> getGetters(Class<? extends Object> modelClass) {

		List<String> getters = new ArrayList<>();

		// itr fields of the Class Model
		Iterator<Entry<Field, Class<? extends Object>>> fields = getFieldsAndType(modelClass).entrySet().iterator();
		while (fields.hasNext()) {
			String attr = fields.next().getKey().getName();

			// Example : name -> getName()
			getters.add("get" + attr.substring(0, 1).toUpperCase() + attr.substring(1));
		}
		return getters;
	}

	/**
	 * Generate the Setters() names of the Class Model
	 * 
	 * @param class
	 * @return List<String>
	 */

	public List<String> getSetters(Class<? extends Object> modelClass) {

		List<String> setters = new ArrayList<>();

		// itr fields of the Class Model
		Iterator<Entry<Field, Class<? extends Object>>> fields = getFieldsAndType(modelClass).entrySet().iterator();
		while (fields.hasNext()) {
			String attr = fields.next().getKey().getName();

			// Example : name -> setName()
			setters.add("set" + attr.substring(0, 1).toUpperCase() + attr.substring(1));
		}
		return setters;
	}

	/**
	 * Create GUI input and Label for the user
	 * 
	 * @param
	 * @return Map contain the inputs (TextField) as the key and the Label for
	 *         that input as a value
	 */

	public Map<Object, Object> createInputsAndLabels(Class<? extends Object> modelClass) {

		GuiAdapter guiAdapter = InstanceFactory.getGuiAdapter();
		Map<Object, Object> inputsAndLabel = new LinkedHashMap<>();

		// itr fields of the Class Model
		Iterator<Entry<Field, Class<? extends Object>>> fields = getFieldsAndType(modelClass).entrySet().iterator();

		// pass the 'id' field (not need an input for the user, generated by
		// SGBD)
		fields.next();

		while (fields.hasNext()) {
			Entry<Field, Class<? extends Object>> field = fields.next();
			// Create the input GUI and Label for every field
			Object[] oneInputLabel = guiAdapter.createInputsLabels(field);
			inputsAndLabel.put(oneInputLabel[1], oneInputLabel[0]);
		}

		return inputsAndLabel;
	}

	/**
	 * Create the Object
	 * 
	 * @param modelClass
	 * @param inputs
	 * @param errorLabel
	 * @return
	 */

	public Object creatObject(Class<? extends Object> modelClass, Map<Object, Object> inputsLabels, Object msgLabel) {

		GuiAdapter guiAdapter = InstanceFactory.getGuiAdapter();
		Object instanceOfclasseType = null;

		try {
			// Create a null instance of the Class Model
			instanceOfclasseType = modelClass.newInstance();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		boolean valideInput = true;

		Class<? extends Object> typeOfField = null;
		Method m = null;
		String setterName = null;

		// itr fields of the class
		Iterator<Entry<Field, Class<? extends Object>>> itrFieldsType = getFieldsAndType(modelClass).entrySet()
				.iterator();
		// itr inputs and labels
		Iterator<Entry<Object, Object>> itrInputsLabels = inputsLabels.entrySet().iterator();
		// itr setters()
		Iterator<String> itrSetter = getSetters(modelClass).iterator();

		itrFieldsType.next();// pass the input of id
		itrSetter.next();// pass the setId

		try {
			while (itrInputsLabels.hasNext()) {

				Entry<Object, Object> inputLabel = itrInputsLabels.next();

				// Name of the Setter Method
				setterName = itrSetter.next();

				// Type of the Field
				typeOfField = itrFieldsType.next().getValue();

				// Get the method of the name 'setterName' and it has a
				// parameter of type 'typeOfField'
				m = instanceOfclasseType.getClass().getMethod(setterName, typeOfField);

				// Cast the user input 'inputLabel' to Type 'typeOfField' and
				// throw
				// a CastException to the 'msgLabel' in Exception
				Object castedInputValue = guiAdapter.valideTheInput(inputLabel, typeOfField, msgLabel);

				// If the input is validated execute the setter()
				if (castedInputValue != null) {
					m.invoke(instanceOfclasseType, castedInputValue);
				} else {
					valideInput = false;
				}
			}

		} catch (Exception e) {
			e.getMessage();
		}

		// if 'valideInput' is false , return a null Object
		if (!valideInput) {
			instanceOfclasseType = null;
		} else {
			GuiAdapter.showMsg("The Object has been created succuful", msgLabel);
		}
		return instanceOfclasseType;
	}

	/**
	 * Update the Object
	 * 
	 * @param object
	 * @param newObject
	 */

	public void update(Object object, Object newObject) {

		Iterator<Entry<Field, Class<? extends Object>>> fields = getFieldsAndType(object.getClass()).entrySet()
				.iterator();
		Iterator<String> itrGet = getGetters(object.getClass()).iterator();
		Iterator<String> itrSet = getSetters(object.getClass()).iterator();

		fields.next(); // pass id field
		itrSet.next(); // pass setId()
		itrGet.next(); // pass getId()

		while (itrSet.hasNext()) {
			try {

				// Get the Getter Method that has the name 'itrGet.next()'
				// Of the NEW Object
				Method get = newObject.getClass().getMethod(itrGet.next());

				// Get the Setter Method that has the name 'itrSet.next()'
				// And take as parameter of type 'fields.next().getValue()'
				// Of the OLD Object
				Method set = object.getClass().getMethod(itrSet.next(), fields.next().getValue());

				// Execute setter() of the 'OLD object' with the result of
				// getter() of the 'NEW object as parameter
				set.invoke(object, get.invoke(newObject));

			} catch (Exception e) {
				e.getMessage();
			}
		}
	}

	/**
	 * Put the object fields in the GUI inputs
	 * 
	 * @param obj
	 * @param inputs
	 */

	public void setObjectInInputs(Object object, Map<Object, Object> inputs) {

		GuiAdapter guiAdapter = InstanceFactory.getGuiAdapter();

		Iterator<Entry<Object, Object>> itr = inputs.entrySet().iterator();
		Iterator<String> getter = getGetters(object.getClass()).iterator();

		getter.next(); // pass the getId

		try {
			while (itr.hasNext()) {

				Entry<Object, Object> input = itr.next();
				String get = getter.next();
				Method getterMethod = object.getClass().getMethod(get);
				guiAdapter.setObjectInInput(input.getKey(), getterMethod, object);

			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Return the id of the Object.
	 * 
	 * @param modelClass
	 * @param object
	 * @return id of the Object
	 */

	public int getId(Class<? extends Object> modelClass, Object object) {

		String getId = getGetters(modelClass).iterator().next();
		int id = 0;

		try {
			Method getIdMethod = modelClass.getMethod(getId);
			id = (int) getIdMethod.invoke(object);
		} catch (Exception e) {
			e.getMessage();
		}

		return id;
	}

}