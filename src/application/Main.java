package application;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	//vou guardar referencia nesse atributo
	private static Scene mainScene;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));// caminho da View
			ScrollPane scrollPane = loader.load();
			
			//altura e largura do scrollPane ajustadas
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			// Scene principal instanciado
			mainScene = new Scene(scrollPane);
			// primaryStage é o palco da Scene, seta como principal, depois define título para o palco e mostra palco.
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Sample JavaFX application");
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//método para pegar referencia
	public static Scene getMainScene() {
		return mainScene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
