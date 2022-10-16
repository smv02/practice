package controllers;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.ConnexionMysql;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AccueilController implements Initializable {
	Connection cnx;
	public PreparedStatement st;
	public ResultSet result;
	@FXML
    private ImageView imageLog;

    @FXML
    private Label lab_nbr;

    @FXML
    private Button precedant;

    @FXML
    private Button sulvant;

    @FXML
    private TextField txt_brand;

    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_price;

    @FXML
    private TextField txt_type;

    @FXML
    void showPrecedant() {
    	String name = txt_name.getText();
    	String sql3 = "select idCar from cars where nomCar ='" + name + "'";
    	int position = 0;
    	try {
			st = cnx.prepareStatement(sql3);
			result = st.executeQuery();
			if (result.next()) {
				position = result.getInt("idCar");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	String sql4 = "select nomCar, priceCar, nomType, nomBrand, image from cars,type,brand where idCar not in(select cars from contracts) and type.idType = cars.type and brand.idBrand = cars.brand and cars.idCar < '"+position+"'";
    	int price = 0;
    	Blob blob;
    	byte byteImg[];
    	try {
			st = cnx.prepareStatement(sql4);
			result = st.executeQuery();
			if (result.next()) {
				price = result.getInt("priceCar");
				txt_price.setText(Integer.toString(price));
				txt_brand.setText(result.getString("nomBrand"));
				txt_type.setText(result.getString("nomType"));
				txt_name.setText(result.getString("nomCar"));
				blob = result.getBlob("image");
				byteImg = blob.getBytes(1, (int) blob.length());
				Image img = new Image(new ByteArrayInputStream(byteImg), imageLog.getFitWidth(), imageLog.getFitHeight(), true, true);
				imageLog.setImage(img);
			} else {
				System.out.println("aucun");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void showSulvant() {
    	String name = txt_name.getText();
    	String sql3 = "select idCar from cars where nomCar ='" + name + "'";
    	int position = 0;
    	try {
			st = cnx.prepareStatement(sql3);
			result = st.executeQuery();
			if (result.next()) {
				position = result.getInt("idCar");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	String sql4 = "select nomCar, priceCar, nomType, nomBrand, image from cars,type,brand where idCar not in(select cars from contracts) and type.idType = cars.type and brand.idBrand = cars.brand and cars.idCar > '"+position+"'";
    	int price = 0;
    	Blob blob;
    	byte byteImg[];
    	try {
			st = cnx.prepareStatement(sql4);
			result = st.executeQuery();
			if (result.next()) {
				price = result.getInt("priceCar");
				txt_price.setText(Integer.toString(price));
				txt_brand.setText(result.getString("nomBrand"));
				txt_type.setText(result.getString("nomType"));
				txt_name.setText(result.getString("nomCar"));
				blob = result.getBlob("image");
				byteImg = blob.getBytes(1, (int) blob.length());
				Image img = new Image(new ByteArrayInputStream(byteImg), imageLog.getFitWidth(), imageLog.getFitHeight(), true, true);
				imageLog.setImage(img);
			} else {
				System.out.println("aucun");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    public void showLogement(){
    	String sql = "select count(*) from cars where idCar not in(select cars from contracts)";
    	int i = 0;
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			if(result.next()) {
				i = result.getInt(1);
			}
			lab_nbr.setText(Integer.toString(i));
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	String sql2 = "select nomCar, priceCar, nomType, nomBrand, image from cars,type,brand where idCar not in(select cars from contracts) and type.idType = cars.type and brand.idBrand = cars.brand";
    	int price = 0;
    	byte byteImg[];
    	Blob blob;
    	try {
			st = cnx.prepareStatement(sql2);
			result = st.executeQuery();
			if(result.next()) {
				price = result.getInt("priceCar");
				txt_price.setText(Integer.toString(price));
				txt_brand.setText(result.getString("nomBrand"));
				txt_type.setText(result.getString("nomType"));
				txt_name.setText(result.getString("nomCar"));
				blob = result.getBlob("image");
				byteImg = blob.getBytes(1, (int) blob.length());
				Image img = new Image(new ByteArrayInputStream(byteImg), imageLog.getFitWidth(), imageLog.getFitHeight(), true, true);
				imageLog.setImage(img);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	cnx = ConnexionMysql.connexionDB();
    	showLogement();
    }
}