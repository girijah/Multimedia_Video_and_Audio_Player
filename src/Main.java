import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Set;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;

/**
 * Created by girij_000 on 8/26/2015.
 */
public class Main extends Application {

	public static Stage primaryStage;
	public static Scene scene2;	
	

	public static void main(String[] args) {
		launch(args);
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//to prevent closing parent frame when child frame is open 		
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Media Player");
		
		//Stage stg = new Stage();
		//stg.initOwner(Main.primaryStage);

		Group rootLayout = new Group();
		Scene scene = new Scene(rootLayout, 850, 500, Color.BLACK);
		
		 
		
		ComboBox<String> songsCombo = new ComboBox<String>();
		songsCombo.setPromptText("Songs");
		songsCombo.setTranslateX(10);
		songsCombo.setTranslateY(10);
		songsCombo.setPrefSize(450, 20);
//		 File fileFolderPath = new File("C:\\Users\\girij_000\\Desktop\\Songs");
		File fileFolderPath = new File("src\\Songs");
	        File[] files = fileFolderPath.listFiles();
	        System.out.println("files[0] : "+files[0]);
	        System.out.println("files[1] : "+ files[1]);
	        for(File f: files){
	            //System.out.println(f.getName());
	            songsCombo.getItems().add(f.getName());            
	        }
		//songsCombo.getItems().addAll("TurnMeOn.mp3", "PaniDa.mp4");
		rootLayout.getChildren().add(songsCombo);
		
		
		Button playBtn = new Button("Play");
		playBtn.setTranslateX(550);
		playBtn.setTranslateY(10);
		rootLayout.getChildren().add(playBtn);
		
		// Video actions
				playBtn.setOnAction(e -> {
					
					if (songsCombo.getValue() == null) {
						JOptionPane.showMessageDialog(null, "Please select a song and enter play!");
						return;
					}

					Group root = new Group();
					String path = "file:///D://Girijah//GiriStuffs//Projects//eclipseMyProjects//Media//src//Songs//"+ songsCombo.getValue();

					Media media = new Media(path);

					final MediaPlayer player = new MediaPlayer(media);
					MediaView view = new MediaView(player);
					//String set = player.getMedia().getMetadata().get("author").toString();
					//for(String item: set){
					//System.out.println("Item : "+set);
					System.out.println("Hello!!!");
					//}
					player.play();
					
					
					view.setFitWidth(1350);
					
					// Creating the controller
					VBox vbox = new VBox();
					Slider slider = new Slider();
					vbox.getChildren().add(slider);
					root.getChildren().add(view);
					root.getChildren().add(vbox);
					//scene2 = new Scene(root, 1350, 700);
					//primaryStage.setScene(scene2);
					scene.setRoot(root);
					//primaryStage.show();

					
					Button back = new Button("Back");
					root.getChildren().add(back);
					
					back.setOnAction(event -> {
						player.stop();
						//primaryStage.setScene(scene);
						scene.setRoot(rootLayout);
					});
					
					

					// Sets the height and width of the stage according to that of the
					// video
					player.setOnReady(new Runnable() {
						@Override
						public void run() {
							int width = player.getMedia().getWidth();
							int height = player.getMedia().getHeight();

							System.out.println(width + " x " + height);

							primaryStage.setMinHeight(height);
							primaryStage.setMinWidth(width);

							vbox.setMinSize(1350, 700);
							vbox.setTranslateY(670);

							slider.setMin(0.0);
							slider.setValue(0.0);
							slider.setMax(player.getTotalDuration().toSeconds());
						}
					});
					
					

					player.currentTimeProperty().addListener(
							(new ChangeListener<Duration>() {
								@Override
								public void changed(
										ObservableValue<? extends Duration> observable,
										Duration duration, Duration current) {

									slider.setValue(current.toSeconds());
								}
							}));

					slider.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							player.seek(Duration.seconds(slider.getValue()));
						}
					});

				});

				
				

		Button loginBtn = new Button("LogIn");
		loginBtn.setTranslateX(650);
		loginBtn.setTranslateY(10);
		rootLayout.getChildren().add(loginBtn);		
		
		loginBtn.setOnAction(event1 -> {
			try {
				LogIn.display(scene);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});	
		scene.setFill(Color.BLACK);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		// Button audio = new Button("Play Audio");
		// audio.setTranslateX(45);
		// audio.setTranslateY(41);
		// rootMain.getChildren().add(audio);

		
	}
}
