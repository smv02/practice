package controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.ConnexionMysql;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class HomeController implements Initializable {
	Connection cnx;
	public PreparedStatement st;
	public ResultSet result;
	private Parent fxml;
	
	@FXML
	private AnchorPane root;
	
	@FXML
    private ImageView imageUser;

    @FXML
    private Label lab_username;
	
	@FXML
    void accueil() {
		try {
			fxml = FXMLLoader.load(getClass().getResource("/interfaces/Accueil.fxml"));
			root.getChildren().removeAll();
			root.getChildren().setAll(fxml);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void contract() {
    	try {
			fxml = FXMLLoader.load(getClass().getResource("/interfaces/Contracts.fxml"));
			root.getChildren().removeAll();
			root.getChildren().setAll(fxml);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void locataires() {
    	try {
			fxml = FXMLLoader.load(getClass().getResource("/interfaces/Tenants.fxml"));
			root.getChildren().removeAll();
			root.getChildren().setAll(fxml);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void location() {
    	try {
			fxml = FXMLLoader.load(getClass().getResource("/interfaces/Rent.fxml"));
			root.getChildren().removeAll();
			root.getChildren().setAll(fxml);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void logement() {
    	try {
			fxml = FXMLLoader.load(getClass().getResource("/interfaces/AllCars.fxml"));
			root.getChildren().removeAll();
			root.getChildren().setAll(fxml);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
    @Override
    public void initialize(URL location, ResourceBundle resources){
    	cnx = ConnexionMysql.connexionDB();
    	String sql = "select userName,photo from userconnect where id = (select MAX(id) from userconnect)";
    	byte byteimg[];
    	Blob blob;
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			if (result.next()) {
				lab_username.setText(result.getString("userName"));
				blob = result.getBlob("photo");
    			byteimg = blob.getBytes(1, (int) blob.length());
    			Image img = new Image(new ByteArrayInputStream(byteimg), imageUser.getFitWidth(), imageUser.getFitHeight(), true, true);
    			imageUser.setImage(img);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    	try {
    		fxml = FXMLLoader.load(getClass().getResource("/interfaces/Accueil.fxml"));
    		root.getChildren().removeAll();
    		root.getChildren().setAll(fxml);
    	} catch (IOException e){
    		e.printStackTrace();
    	}
    }
}