package controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.ConnexionMysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import models.Car;

public class AllCarsController implements Initializable {
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
    private ComboBox<String> cb_brand;

    @FXML
    private ComboBox<String> cb_type;

    @FXML
    private TableColumn<Car, String> cin_brand;

    @FXML
    private TableColumn<Car, String> cin_dsc;
    
    @FXML
    private TableColumn<Car, Integer> cin_price;

    @FXML
    private TableColumn<Car, Integer> cin_id;

    @FXML
    private TableColumn<Car, String> cin_type;

    @FXML
    private Button icon_importer;
    
    private FileInputStream fs;


    @FXML
    private ImageView image_logement;

    @FXML
    private TableView<Car> table_cars;

    @FXML
    private TextField txt_desc;
    
    @FXML
    private TextField txt_price;

    @FXML
    private TextField txt_searchid;
    
    private String urlImage = "";

    @FXML
    void addCar() {
    	String name = txt_desc.getText();
    	String price = txt_price.getText();
    	String typ = cb_type.getValue();
    	String sqll = "select idType from type where nomType ='"+typ+"'";
    	int type = 0;
    	try {
    		st = cnx.prepareStatement(sqll);
    		result = st.executeQuery();
    		if (result.next()) {
    			type = result.getInt("idType");
    		}
    	} catch (SQLException e){
    		e.printStackTrace();
    	}
    	
    	String brand = cb_brand.getValue();
    	String sql2 = "select idBrand from brand where nomBrand ='"+brand+"'";
    	int brand1 = 0;
    	try {
    		st = cnx.prepareStatement(sql2);
    		result = st.executeQuery();
    		if (result.next()) {
    			brand1 = result.getInt("idBrand");
    		}
    	} catch (SQLException e){
    		e.printStackTrace();
    	}
    	File image = new File(urlImage);
    	
    	String sql = "insert into cars(nomCar, priceCar, type, brand, image) values(?,?,?,?,?)";
    	try {
			st = cnx.prepareStatement(sql);
			st.setString(1, name);
			st.setString(2, price);
			st.setInt(3, type);
			st.setInt(4, brand1);
			fs = new FileInputStream(image);
			st.setBinaryStream(5, fs, image.length());
			st.executeUpdate();
			showLogement();
			txt_desc.setText("");
			txt_price.setText("");
			txt_searchid.setText("");
			cb_brand.setValue("brand");
			cb_type.setValue("type");
			image_logement.setImage(null);
			Alert alert = new Alert(AlertType.CONFIRMATION, "car add!", javafx.scene.control.ButtonType.OK);
			alert.showAndWait();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void deleteCar() {
    	String sql = "delete from cars where idCar = '" + txt_searchid.getText() + "'";
    	try {
			st = cnx.prepareStatement(sql);
			st.executeUpdate();
			showLogement();
			txt_desc.setText("");
			txt_price.setText("");
			txt_searchid.setText("");
			cb_brand.setValue("brand");
			cb_type.setValue("type");
			image_logement.setImage(null);
			Alert alert = new Alert(AlertType.CONFIRMATION, "car delete!", javafx.scene.control.ButtonType.OK);
			alert.showAndWait();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void modCar() {
    	String name = txt_desc.getText();
    	String price = txt_price.getText();
    	String typ = cb_type.getValue();
    	String sqll = "select idType from type where nomType ='"+typ+"'";
    	int type = 0;
    	try {
    		st = cnx.prepareStatement(sqll);
    		result = st.executeQuery();
    		if (result.next()) {
    			type = result.getInt("idType");
    		}
    	} catch (SQLException e){
    		e.printStackTrace();
    	}
    	
    	String brand = cb_brand.getValue();
    	String sql2 = "select idBrand from brand where nomBrand ='"+brand+"'";
    	int brand1 = 0;
    	try {
    		st = cnx.prepareStatement(sql2);
    		result = st.executeQuery();
    		if (result.next()) {
    			brand1 = result.getInt("idBrand");
    		}
    	} catch (SQLException e){
    		e.printStackTrace();
    	}
    	
    	
    	String sql = "update cars set nomCar=?, priceCar=?, type=?, brand=? where idCar = '" + txt_searchid.getText() + "'";
    	try {
			st = cnx.prepareStatement(sql);
			st.setString(1, name);
			st.setString(2, price);
			st.setInt(3, type);
			st.setInt(4, brand1);
			st.executeUpdate();
			showLogement();
			txt_desc.setText("");
			txt_price.setText("");
			txt_searchid.setText("");
			cb_brand.setValue("nomBrand");
			cb_type.setValue("type");
			image_logement.setImage(null);
			Alert alert = new Alert(AlertType.CONFIRMATION, "car edit!", javafx.scene.control.ButtonType.OK);
			alert.showAndWait();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void rempirBrand() {
    	String sql = "select nomBrand from brand where type = (select idType from type where nomType = '" + cb_type.getValue() + "')";
    	List<String> brand = new ArrayList<String>();
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while(result.next()) {
				brand.add(result.getString("nomBrand"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	cb_brand.setItems(FXCollections.observableArrayList(brand));
    }

    @FXML
    void rempirType() {

    }

    @FXML
    void searchLogement() {
    	String sql = "select nomBrand from brand";
    	List<String> brand = new ArrayList<String>();
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while(result.next()) {
				brand.add(result.getString("nomBrand"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	cb_brand.setItems(FXCollections.observableArrayList("nomBrand"));
    	
    	
    	String sql2 = "select idCar, nomCar, priceCar, nomType, nomBrand, image from cars,type,brand where cars.type=type.idType and cars.brand=brand.idBrand and idCar=?";
    	try {
    		st = cnx.prepareStatement(sql2);
    		st.setString(1, txt_searchid.getText());
    		result = st.executeQuery();
    		byte byteImage[];
    		Blob blob;
    		while(result.next()) {
    			int id = result.getInt("idCar");
    			txt_searchid.setText(String.valueOf(id));
    			txt_desc.setText(result.getString("nomCar"));
    			txt_price.setText(result.getString("priceCar"));
    			cb_type.setValue(result.getString("nomType"));
    			cb_brand.setValue(result.getString("nomBrand"));
    			blob = result.getBlob("image");
    			byteImage = blob.getBytes(1, (int) blob.length());
    			Image img = new Image(new ByteArrayInputStream(byteImage), image_logement.getFitWidth(), image_logement.getFitHeight(), true, true);
    			image_logement.setImage(img);
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void importerImage() {
    	FileChooser fc = new FileChooser();
    	fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
    	File f = fc.showOpenDialog(null);
    	if(f != null) {
    		urlImage = f.getAbsolutePath();
    		Image image = new Image(f.toURI().toString(), image_logement.getFitWidth(), image_logement.getFitHeight(), true, true);
    		image_logement.setImage(image);
    	}
    }
    @FXML
    void tableLogEvent() {
    	String sqll = "select nomBrand from brand";
    	List<String> brand = new ArrayList<String>();
    	try {
			st = cnx.prepareStatement(sqll);
			result = st.executeQuery();
			while(result.next()) {
				brand.add(result.getString("nomBrand"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	cb_brand.setItems(FXCollections.observableArrayList(brand));
    	
    	Car car = table_cars.getSelectionModel().getSelectedItem();
    	String sql2 = "select idCar, nomCar, priceCar, nomType, nomBrand, image from cars,type,brand where cars.type=type.idType and cars.brand=brand.idBrand and idCar=?";
    	try {
    		st = cnx.prepareStatement(sql2);
    		st.setInt(1, car.getId());
    		result = st.executeQuery();
    		byte byteImage[];
    		Blob blob;
    		while(result.next()) {
    			int id = result.getInt("idCar");
    			txt_searchid.setText(String.valueOf(id));
    			txt_desc.setText(result.getString("nomCar"));
    			txt_price.setText(result.getString("priceCar"));
    			cb_type.setValue(result.getString("nomType"));
    			cb_brand.setValue(result.getString("nomBrand"));
    			blob = result.getBlob("image");
    			byteImage = blob.getBytes(1, (int) blob.length());
    			Image img = new Image(new ByteArrayInputStream(byteImage), image_logement.getFitWidth(), image_logement.getFitHeight(), true, true);
    			image_logement.setImage(img);
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }

    ObservableList<Car> listLog = FXCollections.observableArrayList();
    public void showLogement() {
    	table_cars.getItems().clear();
    	String sql = "select idCar, nomCar, priceCar, nomType, nomBrand, image from cars,type,brand where cars.type=type.idType and cars.brand=brand.idBrand";
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while (result.next()) {
				listLog.add(new Car(result.getInt(1), result.getString(2), result.getInt(3), result.getString(4), result.getString(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	cin_id.setCellValueFactory(new PropertyValueFactory<Car, Integer>("id"));
    	cin_dsc.setCellValueFactory(new PropertyValueFactory<Car, String>("name"));
    	cin_price.setCellValueFactory(new PropertyValueFactory<Car, Integer>("price"));
    	cin_type.setCellValueFactory(new PropertyValueFactory<Car, String>("type"));
    	cin_brand.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));
    	table_cars.setItems(listLog);
    }
    public void remplirType() {
    	String sql = "select nomType from type";
    	List<String> types = new ArrayList<String>();
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while(result.next()) {
				types.add(result.getString("nomType"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	cb_type.setItems(FXCollections.observableArrayList(types));
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		showLogement();
		remplirType();
	}
}