package com.ofppt.dao.model;

import javax.persistence.Entity;

@Entity
public class Etudiant extends Personne {

	private String cne;

	public String getCne() {
		return cne;
	}

	public void setCne(String cne) {
		this.cne = cne;
	}

	@Override
	public String toString() {
		return "Etudiant [cne=" + cne + ", toString()=" + super.toString() + "]";
	}


}
