package controllers;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import application.ConnexionMysql;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RentController implements Initializable{
	Connection cnx;
	public PreparedStatement st;
	public ResultSet result;
    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;

    @FXML
    private TextField txt_CIN;

    @FXML
    private TextField txt_adr;

    @FXML
    private TextField txt_cinSearch;

    @FXML
    private TextField txt_loyer;

    @FXML
    private TextField txt_nomPrenom;

    @FXML
    private TextField txt_periode;

    @FXML
    private TextField txt_region;

    @FXML
    private TextField txt_searchLogementid;

    @FXML
    private TextField txt_tele;

    @FXML
    private TextField txt_type;
    @FXML
    private ImageView imageLog;
    @FXML
    void periode() {
    	Date dated = Date.from(dateDebut.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    	java.sql.Date dateDebut = new java.sql.Date(dated.getTime());
    	Date datef = Date.from(dateFin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    	java.sql.Date dateFin = new java.sql.Date(datef.getTime());
    	int days = daysBetween(dateDebut, dateFin);
    	int mois = days/30;
    	txt_periode.setText(String.valueOf(mois));
    }
    public int daysBetween(java.sql.Date d1, java.sql.Date d2) {
    	return (int) ((d2.getTime()-d1.getTime())/(1000*60*60*24));
    }
    public Boolean isBetween(java.sql.Date my_date, java.sql.Date my_debut, java.sql.Date my_fin) {
    	return (my_date.equals(my_debut) || my_date.after(my_debut)) && (my_date.equals(my_fin) || my_date.after(my_fin));
    }
    public Boolean isOut(java.sql.Date dateDebut, java.sql.Date dateFin, java.sql.Date my_debut, java.sql.Date my_fin) {
    	return (dateDebut.before(my_debut) && dateFin.after(my_fin));
    }
    @FXML
    void addLocation() {
    	String period = txt_periode.getText();
    	String sql = "select idL from tenants where CIN = '" + txt_CIN.getText() + "'";
    	int tenants = 0;
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			if(result.next()) {
				tenants = result.getInt("idL");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	String sql1 = "select idCar from cars where nomCar = '" + txt_adr.getText() + "'";
    	int car = 0;
    	try {
			st = cnx.prepareStatement(sql1);
			result = st.executeQuery();
			if(result.next()) {
				car = result.getInt("idCar");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	Date datedd = Date.from(dateDebut.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    	java.sql.Date dateDebut = new java.sql.Date(datedd.getTime());
    	Date datedf = Date.from(dateFin.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    	java.sql.Date dateFin = new java.sql.Date(datedf.getTime());
    	
    	String sql2 = "select dateDebut, dateFin from contracts where cars = '" + car + "'";
    	Boolean debut = false;
    	Boolean fin = false;
    	java.sql.Date dated= null;
    	java.sql.Date datef= null;
    	Date d = null;
    	Date f = null;
    	try {
			st = cnx.prepareStatement(sql2);
			result = st.executeQuery();
			while(result.next()) {
				dated = result.getDate("dateDebut");
				datef = result.getDate("dateFin");
				if (isBetween(dateFin, dated, datef) == true) {
					fin = true;
				}
				if (isBetween(dateDebut, dated, datef) == true) {
					debut = true;
				}
				if (isOut(dateDebut, dateFin, dated, datef) == true) {
					fin = true;
					debut = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	if(fin == true || debut == true) {
    		Alert alert = new Alert(AlertType.WARNING, "This car is occupied during the period between" + dated +" and "+ datef +"'", ButtonType.OK);
    		alert.showAndWait();
    	}else {
    		String sql0 = "insert into contracts(cars,tenants,dateDebut,dateFin,Period) values(?,?,?,?,?)";
    		try {
				st = cnx.prepareStatement(sql0);
				st.setInt(1, car);
				st.setInt(2, tenants);
				st.setDate(3, dateDebut);
				st.setDate(4, dateFin);
				st.setString(5, period);
				st.executeUpdate();
				txt_adr.setText("");
				txt_CIN.setText("");
				txt_loyer.setText("");
				txt_nomPrenom.setText("");
				txt_periode.setText("");
				txt_tele.setText("");
				txt_type.setText("");
				txt_region.setText("");
				this.dateDebut.setValue(null);
				this.dateFin.setValue(null);
				imageLog.setImage(null);
				Alert alert = new Alert(AlertType.CONFIRMATION, "Car added successfuly", ButtonType.OK);
	    		alert.showAndWait();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		
    	}
    }

    @FXML
    void searchLocataire() {
    	String sql = "select nomprenomL, teleL, CIN from tenants where CIN = '"+ txt_cinSearch.getText()+"'";
    	int nbr = 0;
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			if(result.next()) {
				txt_CIN.setText(result.getString("CIN"));
				txt_nomPrenom.setText(result.getString("nomprenomL"));
				txt_tele.setText(result.getString("teleL"));
				txt_cinSearch.setText("");
				nbr = 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	if (nbr == 0) {
    		Alert alert = new Alert(AlertType.ERROR, "No tenant found with CIN = "+txt_cinSearch.getText()+"", ButtonType.OK);
    		alert.showAndWait();
    	}
    }

    @FXML
    void searchLogement() {
    	String sql = "select nomCar, priceCar, nomType, nomBrand, image from cars, type, brand where cars.type=type.idType and cars.brand=brand.idBrand and cars.idCar = '"+ txt_searchLogementid.getText() +"'";
    	int nb = 0;
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			byte ByteImg[];
			Blob blob;
			if(result.next()) {
    			txt_adr.setText(result.getString("nomCar"));
    			txt_loyer.setText(result.getString("priceCar"));
    			txt_type.setText(result.getString("nomType"));
    			txt_region.setText(result.getString("nomBrand"));
				blob = result.getBlob("image");
				ByteImg = blob.getBytes(1, (int) blob.length());
				Image img = new Image(new ByteArrayInputStream(ByteImg), imageLog.getFitWidth(), imageLog.getFitHeight(), true, true);
				imageLog.setImage(img);
				txt_searchLogementid.setText("");
				nb = 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	if (nb == 0) {
    		Alert alert = new Alert(AlertType.ERROR, "No car found with identified = "+txt_searchLogementid.getText()+"", ButtonType.OK);
    		alert.showAndWait();
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		
	}

}
