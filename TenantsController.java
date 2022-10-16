package controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ResourceBundle;

import application.ConnexionMysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Tenant;

public class TenantsController implements Initializable{
	Connection cnx;
	public PreparedStatement st;
	public ResultSet result;
	@FXML
    private Button btn_add;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_edit;

    @FXML
    private TableColumn<Tenant, String> cin_cin;

    @FXML
    private TableColumn<Tenant, Date> cin_date;

    @FXML
    private TableColumn<Tenant, Integer> cin_id;

    @FXML
    private TableColumn<Tenant, String> cin_nom;

    @FXML
    private TableColumn<Tenant, String> cin_tele;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Tenant> table_tenants;

    @FXML
    private TextField txt_CIN;

    @FXML
    private TextField txt_nom;

    @FXML
    private TextField txt_searchCIN;

    @FXML
    private TextField txt_tele;
    
    public ObservableList<Tenant> data = FXCollections.observableArrayList();
    

    @FXML
    void addTenant() {
    	String nom = txt_nom.getText();
    	String tele = txt_tele.getText();
    	String cin = txt_CIN.getText();
    	
    	String sql = "insert into tenants(nomprenomL,datenaissL,teleL,CIN) values(?,?,?,?)";
    	if (!nom.equals("")&&!tele.equals("")&&!cin.equals("")&&!datePicker.getValue().equals(null)) {
    		try {
    			st = cnx.prepareStatement(sql);
    			st.setString(1, nom);
    			java.util.Date date = java.util.Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    			Date sqlDate = new Date(date.getTime());
    			st.setDate(2, sqlDate);
    			st.setString(3, tele);
    			st.setString(4, cin);
    			st.execute();
    			txt_CIN.setText("");
    			txt_nom.setText("");
    			txt_searchCIN.setText("");
    			txt_tele.setText("");
    			datePicker.setValue(null);
    			Alert alert = new Alert(AlertType.CONFIRMATION, "Tenant add!", javafx.scene.control.ButtonType.OK);
    			alert.showAndWait();
    			showTenants();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	} else {
    		Alert alert = new Alert(AlertType.WARNING, "Error add!", javafx.scene.control.ButtonType.OK);
			alert.showAndWait();
    	}
    }

    @FXML
    void deleteTenant() {
    	String sql = "delete from tenants where CIN = '"+txt_searchCIN.getText()+"'";
    	try {
			st = cnx.prepareStatement(sql);
			st.executeUpdate();
			txt_CIN.setText("");
			txt_nom.setText("");
			txt_searchCIN.setText("");
			txt_tele.setText("");
			datePicker.setValue(null);
			Alert alert = new Alert(AlertType.CONFIRMATION, "Tenant delete!", javafx.scene.control.ButtonType.OK);
			alert.showAndWait();
			showTenants();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void tableTenantsEvent() {
    	Tenant tenant = table_tenants.getSelectionModel().getSelectedItem();
    	String sql = "select * from tenants where idL = ?";
    	try {
			st = cnx.prepareStatement(sql);
			st.setInt(1, tenant.getId());
			result = st.executeQuery();
			if (result.next()) {
				txt_CIN.setText(result.getString("CIN"));
				txt_tele.setText(result.getString("teleL"));
				txt_nom.setText(result.getString("nomprenomL"));
				Date date = result.getDate("datenaissL");
				datePicker.setValue(date.toLocalDate());
				txt_searchCIN.setText(result.getString("CIN"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void editTenant() {
    	String nom = txt_nom.getText();
    	String tele = txt_tele.getText();
    	String cin = txt_CIN.getText();
    	
    	String sql = "update tenants set nomprenomL=?, datenaissL=?, teleL=?, CIN=? where CIN = '" + txt_searchCIN.getText()+"'";
    	if (!nom.equals("")&&!tele.equals("")&&!cin.equals("")&&!datePicker.getValue().equals(null)) {
    		try {
    			st = cnx.prepareStatement(sql);
    			st.setString(1, nom);
    			java.util.Date date = java.util.Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    			Date sqlDate = new Date(date.getTime());
    			st.setDate(2, sqlDate);
    			st.setString(3, tele);
    			st.setString(4, cin);
    			st.executeUpdate();
    			txt_CIN.setText("");
    			txt_nom.setText("");
    			txt_searchCIN.setText("");
    			txt_tele.setText("");
    			datePicker.setValue(null);
    			Alert alert = new Alert(AlertType.CONFIRMATION, "Tenant edit!", javafx.scene.control.ButtonType.OK);
    			alert.showAndWait();
    			showTenants();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}else {
    		Alert alert = new Alert(AlertType.WARNING, "Error edit!", javafx.scene.control.ButtonType.OK);
			alert.showAndWait();
    	}
    }

    @FXML
    void searchTenant() {
    	String sql = "select nomprenomL, CIN, datenaissL, teleL from tenants where CIN = '" + txt_searchCIN.getText()+"'";
    	int m = 0;
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			if (result.next()) {
				txt_CIN.setText(result.getString("CIN"));
				txt_tele.setText(result.getString("teleL"));
				txt_nom.setText(result.getString("nomprenomL"));
				Date date = result.getDate("datenaissL");
				datePicker.setValue(date.toLocalDate());
				m = 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	if (m == 0) {
    		Alert alert = new Alert(AlertType.ERROR, "Tenant CIN = " + txt_searchCIN.getText() + "", javafx.scene.control.ButtonType.OK);
    		alert.showAndWait();
    	}
    }
    
    public void showTenants() {
    	table_tenants.getItems().clear();
    	String sql = "select * from tenants";
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while (result.next()) {
				data.add(new Tenant(result.getInt("idL"), result.getString("nomprenomL"), result.getDate("datenaissL"), result.getString("teleL"), result.getString("CIN")));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	cin_cin.setCellValueFactory(new PropertyValueFactory<Tenant, String>("cin"));
    	cin_date.setCellValueFactory(new PropertyValueFactory<Tenant, Date>("dateOfBirth"));
    	cin_id.setCellValueFactory(new PropertyValueFactory<Tenant, Integer>("id"));
    	cin_nom.setCellValueFactory(new PropertyValueFactory<Tenant, String>("nomprenom"));
    	cin_tele.setCellValueFactory(new PropertyValueFactory<Tenant, String>("tele"));
    	table_tenants.setItems(data);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		showTenants();
	}
	
}
