package Agent;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * InteractionHandler is a custom JavaFX component that manages the bid interaction UI.
 * It includes navigation buttons, bid slider, current bid display, and the place bid button.
 */
public class InteractionHandler extends VBox {
    Label agentBid = new Label();

    /**
     * Constructs the interaction pane with navigation and bidding controls.
     *
     * @param bidSlider The slider used to select the bid amount
     * @param placeBid The button to place the bid
     * @param back The button to navigate to the previous item
     * @param next The button to navigate to the next item
     */
    public InteractionHandler(Slider bidSlider, Button placeBid,
                              Button back, Button next) {
        // Navigation buttons layout
        HBox navigate = new HBox();
        navigate.setStyle("-fx-padding: 0 20 15 20; -fx-background-color: white");
        navigate.setSpacing(20);
        navigate.getChildren().add(back);
        navigate.getChildren().add(next);

        // Bid display and submission layout
        HBox bid = new HBox();
        bid.setStyle("-fx-padding: 10 20 10 20");
        bid.setSpacing(20);
        bid.getChildren().add(agentBid);
        bid.getChildren().add(placeBid);

        // Current bid value label
        agentBid.setAlignment(Pos.CENTER);
        agentBid.setText("$0.00");
        agentBid.setStyle("-fx-background-color: #d1d1d1;" +
                "-fx-font: 16 sansserif; -fx-font-weight: bold");
        agentBid.setPrefSize(190, 28);

        // Bid slider styling
        bidSlider.setStyle("-fx-padding: 0 20 15 20");

        // Layout separators
        Separator separator = new Separator();
        Separator separator1 = new Separator();

        // Assemble the UI components
        addToStage(navigate);
        addToStage(separator);
        addToStage(bid);
        addToStage(bidSlider);
        addToStage(separator1);
    }

    /**
     * Updates the bid label to show the currently selected bid amount.
     * @param bid The new bid amount to display.
     */
    public void updateBidDisplay(double bid) {
        agentBid.setText("$" + String.format("%.2f", bid));
    }

    /**
     * Utility method to add a JavaFX node to this VBox.
     * @param node The UI element to be added to the layout.
     */
    public void addToStage(Node node) {
        getChildren().add(node);
    }
}
