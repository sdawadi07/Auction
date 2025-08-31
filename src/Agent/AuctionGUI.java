package Agent;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

/**
 * AuctionGUI is a JavaFX component that displays the auction items from a single Auction House.
 * It supports navigation between multiple items, updating bid values, and dynamically adding items.
 */
public class AuctionGUI extends VBox {
    String[] itemList;
    private int itemIndex = 0;
    LinkedList<AuctionItemGUI> auctionItemPane = new LinkedList<>();

    /**
     * Constructs the GUI view for a single auction house, given an initial item list.
     * @param itemList The array of item names offered by this auction house.
     */
    public AuctionGUI(String[] itemList) {
        this.itemList = itemList;
        populateItems();
        setupStage();
        reloadItem();
    }

    /**
     * Applies spacing and background styling to the component.
     */
    private void setupStage() {
        setSpacing(15);
        setStyle("-fx-padding: 10 20 15 20; -fx-background-color: white;");
    }

    /**
     * Converts the item names into visual item panes.
     */
    private void populateItems() {
        for (String item : itemList) {
            auctionItemPane.add(new AuctionItemGUI(item));
        }
    }

    /**
     * Dynamically adds a new item if not already present.
     * @param item The new item name.
     */
    public void addItem(String item) {
        if (!containsItem(item)) {
            auctionItemPane.add(new AuctionItemGUI(item));
        }
    }

    /**
     * Checks whether a given item already exists in the auction list.
     * @param item The item name to check.
     * @return True if already present; false otherwise.
     */
    public boolean containsItem(String item) {
        for (AuctionItemGUI itemInAH : auctionItemPane) {
            if (itemInAH.getItem().equals(item)) return true;
        }
        return false;
    }

    /**
     * Displays the currently selected auction item on the screen.
     */
    public void reloadItem() {
        if (!auctionItemPane.isEmpty()) {
            addToStage(auctionItemPane.get(itemIndex));
        }
    }

    /**
     * Moves to the next item in the list, looping if needed.
     */
    public void getNextItem() {
        if (auctionItemPane.isEmpty()) return;
        getChildren().clear();
        if (itemIndex == auctionItemPane.size() - 1) itemIndex = 0;
        else itemIndex++;
        addToStage(auctionItemPane.get(itemIndex));
    }

    /**
     * Moves to the previous item in the list, looping if needed.
     */
    public void getPrevItem() {
        if (auctionItemPane.isEmpty()) return;
        getChildren().clear();
        if (itemIndex == 0) itemIndex = auctionItemPane.size() - 1;
        else itemIndex--;
        addToStage(auctionItemPane.get(itemIndex));
    }

    /**
     * Updates the bid status message for a specific item.
     * @param status The status message (e.g., "Outbid", "Accepted")
     * @param item The item name.
     */
    public void updateStatus(String status, String item) {
        for (AuctionItemGUI itemPane : auctionItemPane) {
            if (itemPane.getItem().equals(item)) {
                itemPane.updateStatus(status);
                break;
            }
        }
    }

    /**
     * Retrieves the current bid value displayed for the active item.
     */
    public double getCurrentBid() {
        return auctionItemPane.get(itemIndex).getCurrentBid();
    }

    /**
     * Returns the name of the currently selected auction item.
     */
    public String getCurrentItem() {
        return auctionItemPane.get(itemIndex).getItem();
    }

    /**
     * Updates the bid value and owner styling for a specific item.
     * @param name The item name.
     * @param bid The current bid amount.
     * @param otherBid True if another agent holds the current bid.
     */
    public void setItemBid(String name, double bid, boolean otherBid) {
        for (AuctionItemGUI itemPane : auctionItemPane) {
            if (itemPane.getItem().equals(name)) {
                itemPane.setCurrentBid(bid, otherBid);
            }
        }
    }

    /**
     * Adds a node to the display.
     * @param node The JavaFX UI node to add.
     */
    public void addToStage(Node node) {
        getChildren().add(node);
    }
}
