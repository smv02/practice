package models;

public class Location {
	int id;
	int logement;
	String locataire;
	String dateDebut;
	String dateFin;
	
	public Location() {
		super();
	}
	public Location(int id, int logement, String locataire, String dateDebut, String dateFin) {
		this.id = id;
		this.logement = logement;
		this.locataire = locataire;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLogement() {
		return logement;
	}
	public void setLogement(int logement) {
		this.logement = logement;
	}
	public String getLocataire() {
		return locataire;
	}
	public void setLocataire(String locataire) {
		this.locataire = locataire;
	}
	public String getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}
	public String getDateFin() {
		return dateFin;
	}
	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}
}
