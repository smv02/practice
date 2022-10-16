package controllers;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import application.ConnexionMysql;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ContractsController implements Initializable{
	Connection cnx;
	public PreparedStatement st;
	public ResultSet result;
    @FXML
    private ComboBox<Integer> combo_searchLocation;

    @FXML
    private ImageView imageLog;

    @FXML
    private TextField txt_montly;

    @FXML
    private TextField txt_adr;

    @FXML
    private TextField txt_dateDebut;

    @FXML
    private TextField txt_dateFin;

    @FXML
    private TextField txt_total;

    @FXML
    private TextField txt_locataire;

    @FXML
    private TextField txt_logement;

    @FXML
    private TextField txt_loyer;

    @FXML
    private TextField txt_region;

    @FXML
    private TextField txt_type;

    @FXML
    void imrimer() {
    	Document doc = new Document();
    	try {
			PdfWriter.getInstance(doc, new FileOutputStream("contract.pdf"));
			doc.open();
			String format = "dd/mm/yy hh:mm";
			
			SimpleDateFormat formater = new SimpleDateFormat(format);
			java.util.Date date = new java.util.Date();
			com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance("C:\\Users\\Николай\\eclipse-workspace\\12\\src\\images\\logo_print.jpg");
			img.setAlignment(com.itextpdf.text.Image.ALIGN_CENTER);
			doc.add(img);
			doc.add(new Paragraph("Car Lease Agreement"
					+"\nPrepared for: "+txt_locataire.getText()
					+"\nCreated for: Ivanov Ivan"
						+"\nThis Car Lease Agreement sets out the terms and conditions upon"
						+ " which Ivanov Ivan is Company duly registered under the laws of State with registered"
						+ " number and having its registered address at Main Street, shall lease a Vehicle to "+txt_locataire.getText()
						+" being a Company duly registered under the laws of State with registered number and having its registered address at Maint Street."
						+"\nTotal: "+txt_total.getText()
						+"\nMontly payment: "+txt_montly.getText()
						+"\nPeriod from: "+txt_dateDebut.getText()+" to "+txt_dateFin.getText(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLACK)));
			doc.close();
			Desktop.getDesktop().open(new File("contract.pdf"));
    	} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }

    @FXML
    void searchLocation() {
    	String sql = "select cars,nomprenomL,dateDebut,dateFin from contracts, tenants where idContracts = '" +combo_searchLocation.getValue()+"' and tenants.idL=contracts.tenants";
    	int log = 0;
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			if (result.next()) {
				log = result.getInt("cars");
				txt_logement.setText(String.valueOf(log));
				txt_locataire.setText(result.getString("nomprenomL"));
				Date dated = result.getDate("dateDebut");
				txt_dateDebut.setText(String.valueOf(dated));
				Date datef = result.getDate("dateFin");
				txt_dateFin.setText(String.valueOf(datef));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	String sql2 = "select nomCar, priceCar, Period, nomType, nomBrand, image from cars, type, brand, contracts where cars.type=type.idType and cars.brand=brand.idBrand and cars.idCar = '"+ txt_logement.getText() +"'";
    	int price = 0;
    	int period = 0;
    	float total = 0;
    	try {
			st = cnx.prepareStatement(sql2);
			result = st.executeQuery();
			byte byteimg[];
			Blob blob;
			if(result.next()) {
				txt_adr.setText(result.getString("nomCar"));
    			txt_loyer.setText(result.getString("priceCar"));
    			txt_type.setText(result.getString("nomType"));
    			txt_region.setText(result.getString("nomBrand"));
				period = result.getInt("Period");
				price = result.getInt("priceCar");
				total = price * period;
				txt_total.setText(String.valueOf(total));
				txt_montly.setText(String.valueOf(price));
				blob = result.getBlob("image");
				byteimg = blob.getBytes(1, (int) blob.length());
				Image img= new Image(new ByteArrayInputStream(byteimg), imageLog.getFitWidth(), imageLog.getFitHeight(), true, true);
				imageLog.setImage(img);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    public void remplirCombo(){
    	String sql = "select idContracts from contracts";
    	List<Integer> list = new ArrayList<Integer>();
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while(result.next()) {
				list.add(result.getInt("idContracts"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	combo_searchLocation.setItems(FXCollections.observableArrayList(list));
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		remplirCombo();
	}

}
