import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;

import java.io.File;
import java.security.Key;
import java.util.ArrayList;

/**
 * Created by girij_000 on 8/26/2015.
 */
public class LogIn {
	static String id = null;

	static Group group = new Group();
	static Stage primaryStage = new Stage();
	public static DataConnection dbConn = new DataConnection();

	public static void display(Scene scene) throws Exception {

		TextField userName = new TextField();
		userName.setPromptText("Username");
		userName.setTranslateX(60);
		userName.setTranslateY(80);

		PasswordField password = new PasswordField();
		password.setPromptText("Password");
		password.setTranslateX(60);
		password.setTranslateY(120);

		Button logIn = new Button("LogIn");
		logIn.setTranslateX(80);
		logIn.setTranslateY(150);
		logIn.setOnAction(event -> {
			if (userName.getText().equals("")) {

				JOptionPane.showMessageDialog(null, " Please enter username!");

			} else if (password.getText().equals("")) {

				JOptionPane.showMessageDialog(null, " Please enter password!");
			} else {

				String query = "SELECT Password, UserID FROM USER_MEMBER WHERE UserName = '"
						+ userName.getText() + "'";

				ArrayList<String[]> res = dbConn
						.executeSelectQueryEnhanced(query);

				if (res.isEmpty()) {
					JOptionPane
							.showMessageDialog(null,
									"Please Sign Up for distinguishing yourself as a member of MediaPlayer library!");
				}

				if (res != null && res.size() > 0) {
					System.out.println("Password=" + res.get(0)[0]);

					String key = "Bar12345Bar12345"; // 128 bit key
					boolean isCorrect;
					try {
						// Create key and cipher
						Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
						Cipher cipher = Cipher.getInstance("AES");

						// encrypt the text
						cipher.init(Cipher.ENCRYPT_MODE, aesKey);
						byte[] encrypted = cipher.doFinal(password.getText()
								.getBytes());
						String passwordFieldEncrypted = new String(encrypted);
						System.out.println("Encrypted password : "
								+ passwordFieldEncrypted);
						isCorrect = passwordFieldEncrypted.equals(res.get(0)[0]);
						id = res.get(0)[1];
						System.out.println();
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					if (isCorrect = true) {
						showProfile(scene);
					} else {
						JOptionPane.showMessageDialog(null,
								" Password didn't match! Try again! ");
					}
				} else {
					JOptionPane.showMessageDialog(null,
							" Username is not in access! ");
				}

			}
		});
		// primaryStage.setTitle("Profile LogIn");

		group.getChildren().addAll(userName, password, logIn);
		// scene1 = new Scene(group, 500, 300);
		// scene1.setFill(Color.DARKGRAY);
		// primaryStage.setScene(scene1);
		scene.setRoot(group);
		// primaryStage.showAndWait();
		// primaryStage.show();

	}

	public static void showProfile(Scene scene) {

		Group components = new Group();

		Button view = new Button("View Songs in the Play List");
		view.setTranslateX(60);
		view.setTranslateY(110);

		ComboBox<String> playListCombo = new ComboBox<String>();
		playListCombo.setPromptText("PlayLists");
		playListCombo.setTranslateX(10);
		playListCombo.setTranslateY(50);
		String sql1 = "SELECT ListName,PlayListID FROM PLAY_LIST WHERE UserID ="
				+ id;
		ArrayList<String[]> list = dbConn.executeSelectQueryEnhanced(sql1);
		for (int i = 0; i < list.size(); i++) {
			playListCombo.getItems().add(list.get(i)[0]);
		}
		// playListCombo.getItems().addAll("TurnMeOn.mp3", "PaniDa.mp4");
		group.getChildren().add(playListCombo);
		
		Button winamp = new Button(" Go To Winamp");
		winamp.setTranslateX(700);
		winamp.setTranslateY(10);
		winamp.setOnAction(event -> {
		setWinamp(scene);	
			
		});	

		components.getChildren().addAll(view, playListCombo,winamp);
		// Scene sc = new Scene(components, 500, 300);
		// sc.setFill(Color.BROWN);
		// primaryStage.setScene(sc);
		// primaryStage.showAndWait();
		scene.setRoot(components);

		ComboBox<String> songsCombo = new ComboBox<String>();
		songsCombo.setPromptText("Songs");
		songsCombo.setTranslateX(180);
		songsCombo.setTranslateY(50);
		components.getChildren().add(songsCombo);
		songsCombo.getValue();

		view.setOnAction(event -> {

			songsCombo.getItems().clear();
			songsCombo.setValue("");

			String sql2 = "SELECT \"Song Title\" FROM SONG WHERE SongID in (SELECT SongID FROM  SONGPLAY_LIST WHERE  PlayListID IN ( SELECT  PlayListID FROM PLAY_LIST WHERE ListName = '"
					+ playListCombo.getValue() + "'))";
			ArrayList<String[]> list1 = dbConn.executeSelectQueryEnhanced(sql2);

			for (int i = 0; i < list1.size(); i++) {
				songsCombo.getItems().add(list1.get(i)[0]);
			}

			play(components, songsCombo, scene);
		});

	}

	static public void play(Group components, ComboBox<String> songsCombo,
			Scene scene) {
		Button btnPlay = new Button("Play");
		btnPlay.setTranslateX(350);
		btnPlay.setTranslateY(50);
		components.getChildren().add(btnPlay);

		// Video actions
		btnPlay.setOnAction(e -> {

			if (songsCombo.getValue() == null) {
				JOptionPane.showMessageDialog(null,
						"Please select a song and enter play!");
				return;
			}

			Group playerGroup = new Group();
			String path = "file:///C://Users//girij_000//Desktop//Songs//"
					+ songsCombo.getValue();

			Media media = new Media(path);
			final MediaPlayer player = new MediaPlayer(media);
			MediaView view = new MediaView(player);
			player.play();

			view.setFitWidth(1350);

			// Creating the controller
			VBox vbox = new VBox();
			Slider slider = new Slider();
			vbox.getChildren().add(slider);
			playerGroup.getChildren().add(view);
			playerGroup.getChildren().add(vbox);
			Scene playerScene = new Scene(playerGroup, 1350, 700);
			Stage playerStage = new Stage();
			playerStage.setScene(playerScene);
			playerStage.show();
			// scene.setRoot(playerGroup);

			Button backBtn = new Button("Back");
			playerGroup.getChildren().add(backBtn);

			backBtn.setOnAction(event -> {
				player.stop();
				scene.setRoot(components);
				// primaryStage.setScene(sc);
				playerStage.close();
			});

			// Sets the height and width of the stage according to that of the
			// video
			player.setOnReady(new Runnable() {
				@Override
				public void run() {
					int width = player.getMedia().getWidth();
					int height = player.getMedia().getHeight();

					System.out.println(width + " x " + height);

					// playerStage.setMinHeight(height);
					// playerStage.setMinWidth(width);
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

	}
	
	public static void setWinamp(Scene scene){
		BorderPane border = new BorderPane();
		
		
		 scene.setFill(Color.BLACK);
	   scene.setRoot(border);
	   
	  
	   ComboBox<String> songsCombo = new ComboBox<String>();
		songsCombo.setPromptText("Songs");
		//songsCombo.setTranslateX(10);
		//songsCombo.setTranslateY(50);
		 File fileFolderPath = new File("C:\\Users\\girij_000\\Desktop\\Songs");
	        File[] files = fileFolderPath.listFiles();
	        System.out.println("files[0] : "+files[0]);
	        System.out.println("files[1] : "+ files[1]);
	        for(File f: files){
	            System.out.println(f.getName());
	            songsCombo.getItems().add(f.getName());            
	        }
		 
	   
	   Label winamplbl = new Label("                   Winamp                    ");
	   Label lblArtist = new Label();
	   Label lblAlbum = new Label();
	   Label lblComposer = new Label();
	   Label lblGenre = new Label();
	   Label lblYear = new Label();
	   Label lblSize = new Label();
	   Label lblLength = new Label();
	   
	   Button playBtn = new Button("play");
	   Button pauseBtn = new Button("pause");
	   Button stopBtn = new Button("Stop");	
	   Button toggleBtn = new Button("Toggle");	
	   Button getOnPauseBtn = new Button("GetOnPause");	
	   Button getOnPlayBtn = new Button("GetOnPlay");
	   
	   //Button songDetailBtn = new Button("Song Detail");
	   
	   Button AudioBtn = new Button("Audio");
	   Button VideoBtn = new Button("Video");	  
	 
	   AudioBtn.setPrefSize(165, 21);
	   VideoBtn.setPrefSize(165, 21);
	   
	   HBox hboxWinamp = new HBox();
	   hboxWinamp.getChildren().addAll(winamplbl,playBtn,pauseBtn,stopBtn,getOnPauseBtn,getOnPlayBtn,toggleBtn);
	   
	   VBox vboxWinamp = new VBox();
	   
	   vboxWinamp.getChildren().add(AudioBtn);
	   vboxWinamp.getChildren().add(VideoBtn);
	   vboxWinamp.getChildren().add(songsCombo);	 
	   vboxWinamp.getChildren().addAll(lblArtist,lblAlbum, lblComposer,lblGenre, lblYear, lblSize, lblLength );
		
			   border.setTop(hboxWinamp);
			   border.setLeft(vboxWinamp);			  
	
	   
	   playBtn.setOnAction(e -> {		  
			
			if (songsCombo.getValue() == null) {
				JOptionPane.showMessageDialog(null, "Please select a song and enter play!");
				return;
			}

			Group root = new Group();
			String path = "file:///C://Users//girij_000//Desktop//Songs//"
					+ songsCombo.getValue();
			
			//added final because to ensure one song playing at a time!
			final Media media = new Media(path);
			final MediaPlayer player = new MediaPlayer(media);
			final MediaView view = new MediaView(player);
			player.play();
			
			//player.isAutoPlay();			
			
			
			String query = "SELECT artist,album,composer,genre, \"Year\",\"Size\",\"Length\" FROM SONG WHERE \"Song Title\" = '"+songsCombo.getValue()+"'";
			ArrayList<String[]> list = dbConn.executeSelectQueryEnhanced(query);
		try{
			lblArtist.setText(" Artist : "+list.get(0)[0]);
			lblAlbum.setText(" Album : "+list.get(0)[1]);
			lblComposer.setText(" Composer : "+list.get(0)[2]);
			lblGenre.setText(" Genre : "+list.get(0)[3]);
			lblYear.setText(" Year : "+list.get(0)[4]);
			lblSize.setText(" Size : "+list.get(0)[5]);
			lblLength.setText(" Length : "+list.get(0)[6]);
		}catch(Exception ex){
			System.out.println("Exception in getting data from database!");
		}
			
			   stopBtn.setOnAction(event ->{
				player.stop(); 
				
			   });
			   
			   pauseBtn.setOnAction(event ->{
				player.pause(); 			
			   });
			   
			   getOnPauseBtn.setOnAction(event ->{
				player.getOnPaused(); 			
			   });
			   
			   getOnPlayBtn.setOnAction(event ->{
				player.getOnPlaying(); 			
			   });
			   
			   toggleBtn.setOnAction(event ->{
				player.getOnRepeat(); 			
			   });
			   
			
			view.setFitWidth(1350);
			
			// Creating the controller
			VBox vbox = new VBox();
			Slider slider = new Slider();
			vbox.getChildren().add(slider);
			root.getChildren().add(view);
			root.getChildren().add(vbox);
						 
			  border.setCenter(root);
			
			
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
	   
	  
	}

}
