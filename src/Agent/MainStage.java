package Agent;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

/**
 * MainStage is the primary layout container for the Agent's JavaFX GUI.
 * It hosts the auction house tabs, the bidding controls, and the wallet display.
 */
public class MainStage extends VBox {
    TabPane auctionHouses = new TabPane();
    LinkedList<AuctionGUI> tabs = new LinkedList<>();
    private int ahNumber = 1;

    /**
     * Constructs the main visual layout for the Agent interface.
     * @param wallet The pane showing total and available balance.
     * @param interactionPane The pane containing the bid slider and buttons.
     */
    public MainStage(AmountPane wallet, InteractionHandler interactionPane) {
        setSpacing(20);
        setStyle("-fx-padding: 20; -fx-background-color: #ffffff;");

        auctionHouses.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        auctionHouses.setTabMinWidth(80);
        auctionHouses.setStyle("-fx-font-size: 14px; -fx-font-family: 'SansSerif'; -fx-font-weight: bold;");

        addToStage(auctionHouses);
        addToStage(interactionPane);
        addToStage(wallet);
    }

    /**
     * Adds a new auction house tab to the UI.
     * @param auctionPane The AuctionGUI representing the house's items.
     */
    public void addAuctionHouse(AuctionGUI auctionPane) {
        Tab tab = new Tab("Auction House " + ahNumber, auctionPane);
        tab.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");
        auctionHouses.getTabs().add(tab);
        tabs.add(auctionPane);
        ahNumber++;
    }

    /**
     * Navigates to the next item in the current auction tab.
     */
    public void navigateNext() {
        if (!tabs.isEmpty()) {
            tabs.get(getCurrentAH()).getNextItem();
        }
    }

    /**
     * Navigates to the previous item in the current auction tab.
     */
    public void navigatePrev() {
        if (!tabs.isEmpty()) {
            tabs.get(getCurrentAH()).getPrevItem();
        }
    }

    /**
     * Retrieves the currently selected auction house tab index.
     */
    public int getCurrentAH() {
        return auctionHouses.getSelectionModel().getSelectedIndex();
    }

    /**
     * Gets the current bid amount of the selected item in the active auction tab.
     */
    public double getCurrentBid() {
        return tabs.get(getCurrentAH()).getCurrentBid();
    }

    /**
     * Gets the name of the currently selected item in the active auction tab.
     */
    public String getCurrentItem() {
        return tabs.get(getCurrentAH()).getCurrentItem();
    }

    /**
     * Utility method to add a JavaFX node to this layout.
     * @param node The UI component to add.
     */
    public void addToStage(Node node) {
        getChildren().add(node);
    }
}