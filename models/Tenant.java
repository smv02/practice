package models;

import java.sql.Date;

public class Tenant {
	int id;
	String nomprenom;
	Date dateOfBirth;
	String tele;
	String cin;
	
	public Tenant() {
		super();
	}
	public Tenant(int id, String nom, Date date, String tele, String cin) {
		this.id = id;
		this.nomprenom = nom;
		this.dateOfBirth = date;
		this.tele = tele;
		this.cin = cin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNomprenom() {
		return nomprenom;
	}
	public void setNomprenom(String nomprenom) {
		this.nomprenom = nomprenom;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateNaiss(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public String getCin() {
		return cin;
	}
	public void setCin(String cin) {
		this.cin = cin;
	}
	
}
