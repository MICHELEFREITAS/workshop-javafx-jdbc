package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
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
	//ActionEvent para ter uma referência para o controle que receber o evento
	@FXML
	public void onBtNewAction(ActionEvent event) {
		//pega refencia do stage atual e passa para criar janela formulário
		Stage parentStage = Utils.currentStage(event);
		
		Department obj = new Department();//depart. vazio Padrao MVC
		
		//obj do departament, nome da tela que vai carregar e Stage da janela atual
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
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

	//método recebe como parametro uma referência para o Stage da janela que criou a janela de diálogo
	//função para carregar a janela do formulário para prenchar novo departamento
	
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		//código para instanciar janela de diálogo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			//carregar view
			Pane pane = loader.load();
			
			//pega referência para o controlador. Pegando controlador da tela carregada em cima(formulário)
			DepartmentFormController controller = loader.getController();
			
			controller.setDepartment(obj);
			//injentar manualmente dependencia
			controller.setDepartmentService(new DepartmentService());
			controller.updateFormData();//carregar dados do obj no formulário
			
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);//janela não poderá ser redimensionada
			dialogStage.initOwner(parentStage);//o stage pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL);//janela modal, ficará travada até fechar ela
			dialogStage.showAndWait();
		}
		catch(IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
		
	}
}
