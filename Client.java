import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Project: Exercise31_9
 * Date:    4/18/17
 * Author:  rolandholban
 */

public class Client extends Application {

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {

        // Text areas for displaying the chat
        TextArea taServer = new TextArea();
        taServer.setEditable(false);
        TextArea taClient = new TextArea();

        // Pane to hold the text areas
        VBox vBox = new VBox(5);
        vBox.getChildren().addAll(new Label(" Server"), taServer,
                new Label(" Client"), taClient);

        // Create a scene and place it in the stage
        Scene scene = new Scene(vBox);
        primaryStage.setTitle("Client"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        new Thread(() -> {

            try {
                // Create a socket to connect to the server
                Socket socket = new Socket("localhost", 8000);

                // Server IO Streams
                DataInputStream inputFromServer =
                        new DataInputStream(socket.getInputStream());
                DataOutputStream outputToServer =
                        new DataOutputStream(socket.getOutputStream());

                while (true) {
                    // Send message to server
                    taClient.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.ENTER)
                            try {
                                outputToServer.writeUTF(taClient.getText());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                    });

                    // Display message received from the server
                    taServer.setText(inputFromServer.readUTF());
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }).start();
    }
}