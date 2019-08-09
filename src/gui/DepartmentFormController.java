package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable{
	
	//entidade relacionada a esse formulário
	private Department entity;

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
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
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
