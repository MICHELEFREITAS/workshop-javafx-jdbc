package gui.util;


import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	//retorna um Stage com nome palco atual e recebe como argumento o ActionEvento que � o evento que o bot�o recebeu
	public static Stage currentStage(ActionEvent event) {
		//Acesar Stage onde o controller que recebeu evento est�
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
		
	}

}
