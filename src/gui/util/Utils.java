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
	
	public static Integer tryParseToInt(String str) {
		try {
			//converte o n�mero para inteiro e retorna
			return Integer.parseInt(str);
		}//se acontecer problema na convers�o
		catch(NumberFormatException e){
			return null;
		}
	}

}
