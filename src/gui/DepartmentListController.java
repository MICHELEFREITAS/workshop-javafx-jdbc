package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
	//dependência com acoplamento forte
	private DepartmentService service;
	
	//referencias para os componentes do DepartmentList
	@FXML
	private TableView<Department> tableViewDepartment;
	
	//S é o tipo entidade que é o Department, e depois tipo coluna Id
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	//carregar os departamento nessa ObservableList
	private ObservableList<Department> obsList;
	
	//método tratamento clique botão
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
		
	}
	
	//injeção de dependência 
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
		
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//criação método auxiliar iniciar componente tela
		initializeNodes();
	}

	//iniciar apropriadamente comportamento colunas tabela
	private void initializeNodes() {
		//Padraão do JavaFX inciar colunas. Instancia e passa os nomes declarado na classe
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//ajustar tableView na tela. Pegar referência stage. Window é super classe Stage
		Stage stage = (Stage) Main.getMainScene().getWindow();
		
		//tableView acompanhar altura da janela
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
	//método responsável por acessar os serviços, carregar os departamentos e jogar os depart. na ObervableList
	public void updateTableView() {
		//se for esquecido de lançar o serviço lança exceção
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		//recupera department lá do DepartmentService MOCKADOS
		List<Department> list = service.findAll();
		
		//carregar lista dentro do ObservableList
		obsList = FXCollections.observableArrayList(list);
		
		tableViewDepartment.setItems(obsList);
	}

}
