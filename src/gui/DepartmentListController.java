package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable{
	
	//referencias para os componentes do DepartmentList
	@FXML
	private TableView<Department> tableViewDepartment;
	
	//S � o tipo entidade que � o Department, e depois tipo coluna Id
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	//m�todo tratamento clique bot�o
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
		
	}
		
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//cria��o m�todo auxiliar iniciar componente tela
		initializeNodes();
	}

	//iniciar apropriadamente comportamento colunas tabela
	private void initializeNodes() {
		//Padra�o do JavaFX inciar colunas. Instancia e passa os nomes declarado na classe
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//ajustar tableView na tela. Pegar refer�ncia stage. Window � super classe Stage
		Stage stage = (Stage) Main.getMainScene().getWindow();
		
		//tableView acompanhar altura da janela
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
	}

}
