package com.ofppt.dao.model;

import javax.persistence.Entity;

@Entity
public class Professeur extends Personne {

	private String cnss;

	public String getCnss() {
		return cnss;
	}

	public void setCnss(String cnss) {
		this.cnss = cnss;
	}

	@Override
	public String toString() {
		return "Professeur [cnss=" + cnss + ", toString()=" + super.toString() + "]";
	}

}
