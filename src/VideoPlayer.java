import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;

/**
 * Created by girij_000 on 8/26/2015.
 */
class VideoPlayer extends Main {


  public VideoPlayer(){

      Group root = new Group();

      Media media = new Media("file:///C://Users//girij_000//Desktop//PaniDa.mp4");
      final MediaPlayer player = new MediaPlayer(media);
      MediaView view = new MediaView(player);

      //Creating the controller
      VBox vbox= new VBox();
      Slider slider = new Slider();
      vbox.getChildren().add(slider);
      root.getChildren().add(view);
      root.getChildren().add(vbox);
      scene2 = new Scene (root,1350,700);
      primaryStage.setScene(scene2);
      //window.show();

      player.play();

      //Sets the height and width of the stage according to that of the video
      player.setOnReady(new Runnable() {
          @Override
          public void run() {
              int width = player.getMedia().getWidth();
              int height = player.getMedia().getHeight();

              System.out.println(width + " x " + height);

             primaryStage.setMinHeight(height);
             primaryStage.setMinWidth(width);

              vbox.setMinSize(width, height);
              vbox.setTranslateY(height - 100);

              slider.setMin(0.0);
              slider.setValue(0.0);
              slider.setMax(player.getTotalDuration().toSeconds());
          }
      });

      player.currentTimeProperty().addListener((new ChangeListener<Duration>() {
          @Override
          public void changed(ObservableValue<? extends Duration> observable, Duration duration, Duration current) {

              slider.setValue(current.toSeconds());
          }
      }));

      slider.setOnMouseClicked(new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
              player.seek(Duration.seconds(slider.getValue()));
          }
      });
  }

}
