package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import application.ConnexionMysql;

import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

public class SignInController implements Initializable {
	Connection cnx;
	public PreparedStatement st;
	public ResultSet result;

    @FXML
    private Button btn_seconnecter;

    @FXML
    private TextField txt_password;

    @FXML
    private TextField txt_userName;
    
    @FXML
    private VBox VBox;
    private Parent fxml;
    
    @FXML
    void openHome() {
    	String nom = txt_userName.getText();
    	String pass = txt_password.getText();
    	String sql = "select userName, password, photo from admin";
    	int nb = 0;
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while(result.next()) {
				if (nom.equals(result.getString("userName"))&&pass.equals(result.getString("password"))){
					nb = 1;
					String sql2 = "insert into userconnect(userName,password,photo) values(?,?,?)";
					st = cnx.prepareStatement(sql2);
					st.setString(1, result.getString("userName"));
					st.setString(2, result.getString("password"));
					st.setBlob(3, result.getBlob("photo"));
					st.executeUpdate();
		    		System.out.println("bien!");
		    		VBox.getScene().getWindow().hide();
		    		Stage home = new Stage();
		    		try {
		    			fxml = FXMLLoader.load(getClass().getResource("/interfaces/Home.fxml"));
		    			Scene scene = new Scene(fxml);
		    			home.setScene(scene);
		    			home.show();
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    		}
		    		
		    	}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    	if (nb == 0){
    		Alert alert = new Alert(AlertType.ERROR, "Incorrect login or password!", javafx.scene.control.ButtonType.OK);
    		alert.showAndWait();
    		txt_password.setText("");
    		txt_userName.setText("");
    	}
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle argl) {
    	cnx = ConnexionMysql.connexionDB();
    }
}