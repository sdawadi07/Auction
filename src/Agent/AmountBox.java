package Agent;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * AmountBox is a custom UI component that displays a labeled amount box.
 * It consists of a label for a title (e.g. "Total Balance") and a label for the amount.
 * It is used in the agent's wallet pane to display bank balances.
 */
public class AmountBox extends HBox {
    private final String title;
    private Double amount;
    private Label amountLabel;

    /**
     * Constructs an AmountBox with a title and initial amount.
     * @param title The label describing what the amount represents (e.g., "Available Balance")
     * @param amount The monetary value to display
     */
    public AmountBox(String title, Double amount) {
        this.title = title;
        this.amount = amount;
        setupBox();
    }

    /**
     * Configures the layout and styles of the amount box UI components.
     */
    private void setupBox() {
        setStyle("-fx-border-color: #bababa;");

        Label titleLabel = new Label("  " + title);
        titleLabel.setStyle("-fx-font: 16 sansserif; -fx-font-weight: bold;" +
                "-fx-background-color: #f5f5f5");
        titleLabel.setPrefSize(170, 30);

        amountLabel = new Label("  $" + String.format("%.2f", amount) + "  ");
        amountLabel.setAlignment(Pos.CENTER_RIGHT);
        amountLabel.setStyle("-fx-font: 16 sansserif;-fx-font-weight: bold;" +
                "-fx-background-color: #d1d1d1");
        amountLabel.setPrefSize(130, 30);

        getChildren().add(titleLabel);
        getChildren().add(amountLabel);
    }

    /**
     * Dynamically changes the background color of the amount label.
     * @param color The CSS color string (e.g., "#ffcccc")
     */
    public void setColor(String color) {
        amountLabel.setStyle("-fx-font: 16 sansserif;-fx-font-weight: bold;" +
                "-fx-background-color: " + color);
    }

    /**
     * Updates the displayed monetary amount.
     * @param amount The new value to display
     */
    public void updateAmount(double amount) {
        this.amount = amount;
        amountLabel.setText("  $" + String.format("%.2f", amount) + "  ");
    }
}