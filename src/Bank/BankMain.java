package Bank;


/**
 *
 * CS-351L (Distributed Auction)
 * The Bank component of the Distributed Auction System can be accessed through BankMain.
 * It launches the BankManager, which manages * all core functions including account creation,
 * fund transfers, and client connection management, and it extends the JavaFX Application
 * class. Additionally, BankMain generates a basic JavaFX GUI window that shows the bank's
 * IP address and port number, enabling agents and auction houses to * know where to connect.
 * When the Bank is launched, this class makes sure that the frontend display and the backend
 * server are initialized.
 *
 */

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BankMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BankManager manager = new BankManager();
        Thread managerThread = new Thread(manager);
        managerThread.start();

        Label ipAddress = new Label("IP: " + manager.getIpAddress());
        ipAddress.setFont(new Font("Arial", 20));
        Label portNumber = new Label("Port no: " + manager.getPortNo());
        portNumber.setFont(new Font("Arial", 20));

        AnchorPane box = new AnchorPane();
        AnchorPane.setBottomAnchor(ipAddress, 250.0);
        AnchorPane.setLeftAnchor(ipAddress, 100.0);
        AnchorPane.setLeftAnchor(portNumber, 100.0);
        AnchorPane.setBottomAnchor(portNumber, 220.0);
        ObservableList list = box.getChildren();
        list.addAll(ipAddress, portNumber);

        primaryStage.setScene(new Scene(box, 400, 400));
        primaryStage.setTitle("Bank Network Information");


        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }
}