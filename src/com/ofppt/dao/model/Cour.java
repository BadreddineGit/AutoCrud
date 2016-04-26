package com.ofppt.dao.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Cour {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String nomCour;
	private int salle;
	
	@ManyToOne
	private Professeur professeur;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Collection<Etudiant> etudiants = new ArrayList<Etudiant>();

	public String getNomCour() {
		return nomCour;
	}

	public void setNomCour(String nomCour) {
		this.nomCour = nomCour;
	}

	public int getSalle() {
		return salle;
	}

	public void setSalle(int salle) {
		this.salle = salle;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Professeur getProfesseur() {
		return professeur;
	}

	public void setProfesseur(Professeur professeur) {
		this.professeur = professeur;
	}

	public Collection<Etudiant> getEtudiants() {
		return etudiants;
	}

	public void setEtudiants(Collection<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}

	@Override
	public String toString() {
		return "Cour [id=" + id + ", nomCour=" + nomCour + ", salle=" + salle + ", professeur=" + professeur
				+ ", etudiants=" + etudiants + "]";
	}
	
	

}
