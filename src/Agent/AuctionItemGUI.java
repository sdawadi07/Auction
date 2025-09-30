 package Agent;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * AuctionItemGUI represents the visual UI component for a single item in an auction.
 * It displays the item name, current bid, bid status, and optionally an image.
 */
public class AuctionItemGUI extends VBox {
    private String auctionItem;
    private String auctionItemName;
    private String auctionItemImage;
    private double currentBid;
    Label statusLabel;
    private VBox bidStatus;
    private AmountBox amountBox;

    /**
     * Constructs a GUI card for an individual auction item.
     * @param auctionItem The internal name of the item (e.g., "gold-sword")
     */
    public AuctionItemGUI(String auctionItem) {
        setSpacing(15);
        this.auctionItem = auctionItem;
        auctionItemImage = auctionItem + ".jpg";
        auctionItemName = auctionItem.replace("-", " ");
        setup();
    }

    /**
     * Sets up all layout and visual elements for the item.
     */
    private void setup() {
        HBox hBox = new HBox();
        Label itemTitle = new Label();
        itemTitle.setText(auctionItemName);
        itemTitle.setStyle("-fx-font: 18 sansserif; -fx-font-weight: bold");

        Label timerLabel = new Label();
        timerLabel.setStyle("-fx-font: 18 futura; -fx-font-weight: bold");
        timerLabel.setText("30:00");

        hBox.getChildren().add(itemTitle);
        hBox.getChildren().add(timerLabel);

        // Optional image section (commented out for now)
        // Image image = new Image(this.auctionItemImage);
        // ImageView itemImage = new ImageView();
        // itemImage.setImage(image);
        // itemImage.setFitHeight(300);
        // itemImage.setFitWidth(300);

        amountBox = new AmountBox("Current Bid", 0.00);
        amountBox.setColor("#ebbfb5");

        // Setup bid status display
        bidStatus = new VBox();
        bidStatus.setStyle("-fx-border-color: #bababa");

        Label statusTitle = new Label(" Bid Status");
        statusTitle.setPrefWidth(300);
        statusTitle.setStyle("-fx-font: 14 sansserif; -fx-font-weight: bold;" +
                "-fx-background-color: #d6d6d6");

        statusLabel = new Label();
        statusLabel.setStyle("-fx-font: 18 sansserif; -fx-font-weight: bold;" +
                "-fx-background-color: #ffffff");
        statusLabel.setPrefSize(300, 30);
        statusLabel.setAlignment(Pos.CENTER);

        bidStatus.getChildren().add(statusTitle);
        bidStatus.getChildren().add(statusLabel);

        addToStage(itemTitle);
        // addToStage(itemImage); // optional image placeholder
        addToStage(amountBox);
        addToStage(bidStatus);
    }

    /**
     * Returns the internal item identifier.
     */
    public String getItem() {
        return auctionItem;
    }

    /**
     * Updates the displayed bid amount and colors it based on ownership.
     * @param bid The new bid amount.
     * @param otherBid True if another agent owns the bid; false if this agent does.
     */
    public void setCurrentBid(double bid, boolean otherBid) {
        currentBid = bid;
        amountBox.updateAmount(currentBid);
        if (otherBid) amountBox.setColor("#c6d9bd"); // green shade for others
        else amountBox.setColor("#ebbfb5"); // pink shade for own bid
    }

    /**
     * Updates the bid status message (e.g., "Outbid", "Accepted").
     * @param status The bid status string.
     */
    public void updateStatus(String status) {
        statusLabel.setText(status);
    }

    /**
     * Retrieves the current bid amount.
     * @return The double value of the current bid.
     */
    public double getCurrentBid() {
        return currentBid;
    }

    /**
     * Adds a JavaFX node to this item component.
     * @param node The node to add.
     */
    private void addToStage(Node node) {
        getChildren().add(node);
    }
}