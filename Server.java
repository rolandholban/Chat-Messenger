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
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Project: Exercise31_9
 * Date:    4/18/17
 * Author:  rolandholban
 */

public class Server extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Text areas for displaying the chat
        TextArea taServer = new TextArea();
        taServer.setEditable(false);
        TextArea taClient = new TextArea();

        // Create a VBox to hold the text areas
        VBox vBox = new VBox(5);
        vBox.getChildren().addAll(new Label(" Server"), taServer,
                new Label(" Client"), taClient);

        // Create a scene and place it in the stage
        Scene scene = new Scene(vBox);
        primaryStage.setTitle("Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        new Thread(() -> {

            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);

                // Listen for a connection request
                Socket socket = serverSocket.accept();

                // IO Streams
                DataInputStream inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                        socket.getOutputStream());

                while (true) {
                    // Send message to server
                    taClient.setOnKeyPressed(e -> {
                        if (e.getCode() == KeyCode.ENTER)
                            try {
                                outputToClient.writeUTF(taClient.getText());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                    });

                    // Display message received from the server
                    taServer.setText(inputFromClient.readUTF());
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }).start();
    }
    
    public static void main(String[] args) {
        launch();
    }
}