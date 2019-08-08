package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable{
	//atributos do item do Menu
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;
	
	//métodos para tratar eventos do Menu
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView2("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}

	//método da interface initialize
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	
	}
	
	//synchronized garante processamento não seja interrompido
	private synchronized void loadView(String absoluteName) {
		try {
			//carregar tela
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			VBox newVBox = loader.load();
			
			//pegar referencia lá do Main
			Scene mainScene = Main.getMainScene();
			
			//VBox da janela principal. A partir da cena principal o getRoot pega 1º elemento da View (ScrollPane)
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			//primeiro filho do VBox da janela principal que é o mainMenu
			Node mainMenu = mainVBox.getChildren().get(0);
			
			mainVBox.getChildren().clear();//limpar todos filhos mainVBox
			
			//add o mainMenu e depois filhos newVBox
			mainVBox.getChildren().add(mainMenu);
			//add uma coleção dos filhos do newVBox
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}	
	}
	private synchronized void loadView2(String absoluteName) {
		try {
			//loader obj usado para carregar view
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			//loader pode carregar view ou acessar controller. Pegar uma referencia para o controller dessa view
			DepartmentListController controller = loader.getController();
			
			//chamando operações. Injentar dependencia do service lá no controller
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	

}
