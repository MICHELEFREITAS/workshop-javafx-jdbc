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
	
	//métodos para tratar eventos do Menu
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		//parâmetro com função de inicialização com expressão lambda
		//ação de inicialização do controller DepartmentListController
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
			
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		//também terá uma ação de inicialização, porém como não tem nada coloco x que leva nada
		loadView("/gui/About.fxml", x -> {});
	}

	//método da interface initialize
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	
	}
	
	//synchronized garante processamento não seja interrompido. Interface funcional Consumer com nome Ação de inicialização
	//função loadView agora é função genérica tipo T generics. Parametrização Consumer<T>
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
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
			
			//essas duas linhas abaixo executam função passada como argumento no loadView 
			//Ativar função passada como parâmetro. Resulado do getController vai para variável tipo T
			T controller = loader.getController();
			
			//para executar a ação initializingAction
			initializingAction.accept(controller);
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}	
	}
	
	

}
