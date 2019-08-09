package gui.util;


import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	//retorna um Stage com nome palco atual e recebe como argumento o ActionEvento que é o evento que o botão recebeu
	public static Stage currentStage(ActionEvent event) {
		//Acesar Stage onde o controller que recebeu evento está
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
		
	}
	
	public static Integer tryParseToInt(String str) {
		try {
			//converte o número para inteiro e retorna
			return Integer.parseInt(str);
		}//se acontecer problema na conversão
		catch(NumberFormatException e){
			return null;
		}
	}

}
