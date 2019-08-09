package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{
	
	//entidade relacionada a esse formulário
	private Department entity;
	
	private DepartmentService service;

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	//caso tenha erro preenchimento nome
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	//controlador terá instância do Department
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		//caso tenha esquecido injetar dependencia
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			//pega os dados do formulário e joga no entity
			entity = getFormData();
			//salva no BD
			service.saveOrUpdate(entity);
			
			//pega referencia janela atual e fecha
			Utils.currentStage(event).close();
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
			
		
	}
	
	//getFormData pega os dados da caixinha do formulário e instanciar um departamento e retorna novo obj
	private Department getFormData() {
		Department obj = new Department();
		//converte para inteiro o valor, se não for int retorna null
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		obj.setName(txtName.getText());
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	//jogar os dados que estão no Departament entity para os campos do formulário
	public void updateFormData() {
		//programação defensiva, caso o entity esteja nula
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		//Caixa de textro trabalha com String, por isso converter Id para String
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		
	}

}
