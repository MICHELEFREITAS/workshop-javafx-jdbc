package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener{
	
	//depend�ncia com acoplamento forte
	private DepartmentService service;
	
	//referencias para os componentes do DepartmentList
	@FXML
	private TableView<Department> tableViewDepartment;
	
	//S � o tipo entidade que � o Department, e depois tipo coluna Id
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;
	
	@FXML
	private TableColumn<Department, Department> tableColumnRemove;
	
	@FXML
	private Button btNew;
	
	//carregar os departamento nessa ObservableList
	private ObservableList<Department> obsList;
	
	//m�todo tratamento clique bot�o
	//ActionEvent para ter uma refer�ncia para o controle que receber o evento
	@FXML
	public void onBtNewAction(ActionEvent event) {
		//pega refencia do stage atual e passa para criar janela formul�rio
		Stage parentStage = Utils.currentStage(event);
		
		Department obj = new Department();//depart. vazio Padrao MVC
		
		//obj do departament, nome da tela que vai carregar e Stage da janela atual
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}
	
	//inje��o de depend�ncia 
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
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
	
	//m�todo respons�vel por acessar os servi�os, carregar os departamentos e jogar os depart. na ObervableList
	public void updateTableView() {
		//se for esquecido de lan�ar o servi�o lan�a exce��o
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		//recupera department l� do DepartmentService MOCKADOS
		List<Department> list = service.findAll();
		
		//carregar lista dentro do ObservableList
		obsList = FXCollections.observableArrayList(list);
		
		tableViewDepartment.setItems(obsList);
		
		//acrescenta novo bot�o com texto edit em cada linha da tabela
		initEditButtons();
		initRemoveButtons();
	}

	//m�todo recebe como parametro uma refer�ncia para o Stage da janela que criou a janela de di�logo
	//fun��o para carregar a janela do formul�rio para prenchar novo departamento
	
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		//c�digo para instanciar janela de di�logo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			//carregar view
			Pane pane = loader.load();
			
			//pega refer�ncia para o controlador. Pegando controlador da tela carregada em cima(formul�rio)
			DepartmentFormController controller = loader.getController();
			
			controller.setDepartment(obj);
			//injentar manualmente dependencia
			controller.setDepartmentService(new DepartmentService());
			//inscreve para ele mesmo receber o evento
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();//carregar dados do obj no formul�rio
			
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);//janela n�o poder� ser redimensionada
			dialogStage.initOwner(parentStage);//o stage pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL);//janela modal, ficar� travada at� fechar ela
			dialogStage.showAndWait();
		}
		catch(IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
		
	}

	//quando disparar fun��o que os dados foram alterados chamar fun��o update
	@Override
	public void onDataChanged() {
		updateTableView();		
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		//Cria um obj espec�fico CellFactory respons�vel instanciar os bot�es e configurar o evento do bot�o
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>(){
			private final Button button = new Button("edit");
			
			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if(obj == null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				//na conf do evento chama o m�todo para criar janela formul�rio
				//passa o obj que � o departamento da linha de edi��o que for clicada
				button.setOnAction(
						event -> createDialogForm(
								obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			} 
			
		});
		
	}
	
	//m�todo cria bot�o remove em cada linha. 
	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");
			
			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if(obj == null) {
					setGraphic(null);
					return;
				}
				//para cada linha se apertar bot�o chama o m�todo removeEntity
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	//para remover uma entidade
	private void removeEntity(Department obj) {
		//Vari�vel result recebe resposta do Alerts. Confirmar se tem certeza que quer deletar o departamento selecionado
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		//.get acessar obj detro do Optional
		//testar se obj que est� dentro dele � igual a OK
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				//programador esqueceu de injetar, lan�a exce��o
				throw new IllegalStateException("Service was null");
			}
			try {
				//remove a atualiza dados na tabela
			   service.remove(obj);
			   updateTableView();
			}
			catch(DbIntegrityException e){
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
