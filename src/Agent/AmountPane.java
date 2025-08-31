package Agent;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * AmountPane is a vertical layout component used to display the agent's wallet.
 * It shows both the total and available balances and the associated bank ID.
 */
public class AmountPane extends VBox {
    private AmountBox totalAmount;
    private AmountBox availableAmount;
    private double initialAmount;
    private Label bankIDLabel;

    /**
     * Constructs the AmountPane and initializes its layout.
     */
    public AmountPane() {
        setupAmountPane();
    }

    /**
     * Configures the visual layout of the amount pane, including wallet labels and balance boxes.
     */
    private void setupAmountPane() {
        setSpacing(10);
        setStyle("-fx-padding: 20 20 20 20; -fx-background-color: #e8e8e8;");
        setPrefHeight(170);

        HBox hBox = new HBox();
        Label walletLabel = new Label("Wallet");
        this.bankIDLabel = new Label();

        hBox.getChildren().add(walletLabel);
        hBox.getChildren().add(bankIDLabel);

        walletLabel.setStyle("-fx-font: 20 sansserif; -fx-font-weight: bold;");
        bankIDLabel.setStyle("-fx-font: 12 sansserif; -fx-font-weight: bold;");
        bankIDLabel.setPrefSize(240, 30);
        bankIDLabel.setAlignment(Pos.BASELINE_RIGHT);

        availableAmount = new AmountBox("Available Balance", initialAmount);
        totalAmount = new AmountBox("Total Balance", initialAmount);

        getChildren().addAll(hBox, availableAmount, totalAmount);
    }

    /**
     * Updates both the available and total balance values displayed.
     * @param total The total bank balance.
     * @param available The unblocked, available balance.
     */
    public void updateAmount(double total, double available) {
        availableAmount.updateAmount(available);
        totalAmount.updateAmount(total);
    }

    /**
     * Sets the bank account ID label in the wallet pane.
     * @param bankID The unique ID assigned to the agent's bank account.
     */
    public void setBankIDLabel(int bankID) {
        bankIDLabel.setText("bankID: " + bankID);
    }
}