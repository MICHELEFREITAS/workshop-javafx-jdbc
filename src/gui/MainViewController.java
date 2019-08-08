package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
	
	//m�todos para tratar eventos do Menu
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		//par�metro com fun��o de inicializa��o com express�o lambda
		//a��o de inicializa��o do controller DepartmentListController
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
			
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		//tamb�m ter� uma a��o de inicializa��o, por�m como n�o tem nada coloco x que leva nada
		loadView("/gui/About.fxml", x -> {});
	}

	//m�todo da interface initialize
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	
	}
	
	//synchronized garante processamento n�o seja interrompido. Interface funcional Consumer com nome A��o de inicializa��o
	//fun��o loadView agora � fun��o gen�rica tipo T generics. Parametriza��o Consumer<T>
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			//carregar tela
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			
			VBox newVBox = loader.load();
			
			//pegar referencia l� do Main
			Scene mainScene = Main.getMainScene();
			
			//VBox da janela principal. A partir da cena principal o getRoot pega 1� elemento da View (ScrollPane)
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			//primeiro filho do VBox da janela principal que � o mainMenu
			Node mainMenu = mainVBox.getChildren().get(0);
			
			mainVBox.getChildren().clear();//limpar todos filhos mainVBox
			
			//add o mainMenu e depois filhos newVBox
			mainVBox.getChildren().add(mainMenu);
			//add uma cole��o dos filhos do newVBox
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			//essas duas linhas abaixo executam fun��o passada como argumento no loadView 
			//Ativar fun��o passada como par�metro. Resulado do getController vai para vari�vel tipo T
			T controller = loader.getController();
			
			//para executar a a��o initializingAction
			initializingAction.accept(controller);
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}	
	}
	
	

}
