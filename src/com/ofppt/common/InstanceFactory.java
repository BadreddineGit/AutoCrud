package com.ofppt.common;

import com.ofppt.dao.CrudDaoImpl;
import com.ofppt.metier.ClassTrait;
import com.ofppt.metier.CrudServiceMetierImpl;
import com.ofppt.presentation.crudgui.GuiAdapter;

public class InstanceFactory {

	public static CrudServiceMetierImpl getCrudServiceMetier() {
		return CrudServiceMetierImpl.getInstnace();
	}

	public static CrudDaoImpl getCrudDao() {
		return CrudDaoImpl.getInstance();
	}

	public static ClassTrait getClassTrait() {
		return ClassTrait.getInstance();
	}

	public static GuiAdapter getGuiAdapter() {
		return GuiAdapter.getInstance();
	}

}
