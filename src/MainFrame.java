
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {
	static JFXPanel fxPanel;
	private JPanel contentPane ;
	static MainFrame  frame = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
     			 SwingUtilities.invokeLater(new Runnable(){
					 @Override
					 public void run() {
						 initAndShowGUI();
					 }

					 //new JFXPanel();
				 });
					frame = new MainFrame();
					fxPanel = new JFXPanel();

					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void initAndShowGUI() {
		// This method is invoked on the EDT thread
		JFrame frame = new JFrame("Swing and JavaFX");
		final JFXPanel fxPanel = new JFXPanel();
		frame.add(fxPanel);
		frame.setSize(300, 200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initFX(fxPanel);
			}
		});
	}

	private static void initFX(JFXPanel fxPanel) {
		// This method is invoked on the JavaFX thread
		Scene scene = createScene();
		fxPanel.setScene(scene);
	}

	private static Scene createScene() {
		Group root  =  new  Group();
		Scene  scene  =  new  Scene(root, Color.ALICEBLUE);

		Button video = new Button("Play Video");
		video.setTranslateX(45);
		video.setTranslateY(75);
		root.getChildren().add(video);

		//Video actions
		video.setOnAction(e ->{

		});

		Button audio = new Button("Play Audio");
		audio.setTranslateX(45);
		audio.setTranslateY(41);
		root.getChildren().add(audio);

		return (scene);
	}

	/**
	 * Create the frame.
	 */
//	public MainFrame() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 450, 300);
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		setContentPane(contentPane);
//		contentPane.setLayout(null);
//
//		JButton btnPlayAudio = new JButton("Play Audio");
//		btnPlayAudio.setBounds(45, 41, 117, 23);
//		contentPane.add(btnPlayAudio);
//
//		JButton btnPlayVideo = new JButton("Play Video");
//		btnPlayVideo.setBounds(45, 75, 117, 23);
//		contentPane.add(btnPlayVideo);
//		btnPlayVideo.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				//frame.add(fxPanel);
//				VideoPlayer player = new VideoPlayer();
//				VideoPlayer.video(fxPanel);
//
//				//VideoPlayer.video();
//				//player.sendPanel();
//			}
//		});
//	}

}
