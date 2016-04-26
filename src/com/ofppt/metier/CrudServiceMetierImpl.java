package com.ofppt.metier;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.ofppt.common.InstanceFactory;
import com.ofppt.dao.crud.CrudDaoImpl;
import com.ofppt.presentation.crudgui.GuiAdapter;

public class CrudServiceMetierImpl {

	private static CrudServiceMetierImpl intanace;
	private CrudDaoImpl dao;
	private ClassTrait classTrait;
	public int selectedObjectId = 0;

	private CrudServiceMetierImpl() {
		dao = InstanceFactory.getCrudDao();
		classTrait = InstanceFactory.getClassTrait();
	}

	public static synchronized CrudServiceMetierImpl getInstnace() {
		if (intanace == null) {
			intanace = new CrudServiceMetierImpl();
		}
		return intanace;
	}

	public void create(Class<? extends Object> modelClass, Map<Object, Object> inputsLabels, Object msglabel, Object msgLabel2) {
		Object obj = classTrait.creatObject(modelClass, inputsLabels, msglabel);

		if (obj != null) {
			dao.create(obj);
		}else{
			GuiAdapter.showMsg("The Object has Not been Created verifies the inputs !!", msgLabel2);
		}
	}

	public Object read(String inputId, Class<? extends Object> classModel, Map<Object, Object> inputs,
			Object msglabel) {
		int objectId = 0;
		Object objectFound = null;

		try {
			objectId = Integer.parseInt(inputId);
			selectedObjectId = objectId;
		} catch (NumberFormatException e) {
			GuiAdapter.showMsg("Input Id must be integer", msglabel);
		}

		if (objectId != 0) {
			objectFound = dao.read(objectId, classModel);

			if (objectFound != null) {
				classTrait.setObjectInInputs(objectFound, inputs);
				GuiAdapter.showMsg("The object of the id : '" + Integer.toString(selectedObjectId)
						+ "' has been selected successfully", msglabel);
			} else {
				GuiAdapter.showMsg("No object of the id : " + objectId, msglabel);
			}
		}
		return objectFound;

	}

	public void select(Class<? extends Object> classModel, Object object, Map<Object, Object> inputs, Object msglabel) {
		selectedObjectId = classTrait.getId(classModel, object);
		GuiAdapter.showMsg("The id of selected Object : " + Integer.toString(selectedObjectId), msglabel);

		if (object != null) {
			classTrait.setObjectInInputs(object, inputs);
		}

	}

	// update() must call after calling select()
	public void update(Class<? extends Object> classModel, Map<Object, Object> inputs, Object msglabel) {

		Object newObject = classTrait.creatObject(classModel, inputs, msglabel);

		if (newObject != null && selectedObjectId != 0) {
			dao.update(selectedObjectId, newObject);
			GuiAdapter.showMsg("The object has been update successfully", msglabel);

		} else if (selectedObjectId == 0) {
			GuiAdapter.showMsg("You must SELECT first the Object !", msglabel);
		}

	}

	public void delete(Object obj, Class<? extends Object> classModel, Object msglabel) {

		int objectId = classTrait.getId(classModel, obj);
		if (objectId != 0) {
			dao.delete(objectId, classModel);
			GuiAdapter.showMsg("The object has been deleted successfully", msglabel);
		} else {
			GuiAdapter.showMsg("You must SELECT first the Object !", msglabel);
		}

	}

	public List<Object> readAll(Class<? extends Object> classModel) {
		return dao.readAll(classModel);
	}

	public Map<Field, Class<? extends Object>> getFieldsAndType(Class<? extends Object> classModel) {
		return classTrait.getFieldsAndType(classModel);

	}

	public Map<Object, Object> createInputsAndLabels(Class<? extends Object> classModel) {
		return classTrait.createInputsAndLabels(classModel);
	}

}
