package Agent;

import Auctionhouse.AuctionBidManager;
import Auctionhouse.BidRejection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * ItemWatcher manages the socket connection between the Agent and a specific Auction House.
 * It listens for incoming item updates or bid results and maintains the auction state for this house.
 */
public class ItemWatcher implements Runnable {
    private final Socket socket;
    private final int itemNo;
    private final int accountNumber;
    private final HashMap<String, AuctionBidManager> itemsForAuction = new HashMap<>();
    private final HashMap<String, String> auctionUpdate = new HashMap<>();
    private ObjectOutputStream outMessage;
    private ObjectInputStream inMessage;
    private double amountReturn;
    private boolean newItemsRegistered, returnApproved;

    /**
     * Constructs a watcher for one AuctionHouse socket.
     */
    public ItemWatcher(Socket socket, int itemNo, int accountNumber) {
        this.socket = socket;
        this.itemNo = itemNo;
        this.accountNumber = accountNumber;
    }

    /**
     * Starts listening for auction updates from the AuctionHouse.
     * Handles new bid states, item updates, and rejections.
     */
    @Override
    public void run() {
        try {
            inMessage = new ObjectInputStream(socket.getInputStream());
            outMessage = new ObjectOutputStream(socket.getOutputStream());
            outMessage.writeObject(accountNumber);

            while (socket.isConnected()) {
                Object object = inMessage.readObject();
                if (object instanceof AuctionBidManager item) {
                    updateItemList(item);
                    auctionUpdate.put(item.getAuctionItemName(), item.getMessage(accountNumber));
                    if (!item.isBidOver()) {
                        amountReturn += item.refundBid(accountNumber);
                        returnApproved = amountReturn != 0;
                    }
                } else {
                    BidRejection rejection = (BidRejection) object;
                    updateAuctionUpdate(rejection.getItemName(), rejection.getMessage());
                }
            }
        } catch(SocketException | EOFException e) {
            System.out.println("AuctionHouse Closed");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the bid status message for an item.
     */
    public void updateAuctionUpdate (String auctionItem, String message) {
        auctionUpdate.put(auctionItem, message);
    }

    /**
     * Adds or replaces an item in the internal item list.
     */
    private void updateItemList(AuctionBidManager item) {
        itemsForAuction.put(item.getAuctionItemName(), item);
        newItemsRegistered = true;
    }

    /**
     * Marks all items as closed and refunds the agent if it was the high bidder.
     */
    public void closeAuction(){
        for (AuctionBidManager items : itemsForAuction.values()){
            if (!items.getMessage(accountNumber).equals("Auction won.") &&
                    !items.getMessage(accountNumber).equals("Auction lost.")) {
                auctionUpdate.put(items.getAuctionItemName(), "Auction closed");
                items.setBidOver(true);
                if (items.getCurrentBidderID() == accountNumber) {
                    amountReturn += items.getCurrentBid();
                    returnApproved = true;
                }
            }
        }
    }

    /**
     * Retrieves the most recent status message for a given item.
     */
    public String getStatusMessage(String item) {
        return auctionUpdate.get(item);
    }

    /**
     * Gets the latest bid amount for a given item.
     */
    public double getItemBid(String item) {
        return itemsForAuction.get(item).getCurrentBid();
    }

    /**
     * Checks if the current high bidder is someone else.
     */
    public boolean getAuctionIsOtherBid(String item, int accountNumber) {
        return itemsForAuction.get(item).isOtherBidder(accountNumber);
    }

    /**
     * Returns all item names currently tracked in this auction.
     */
    public String[] getItems() {
        String[] itemList = itemsForAuction.keySet().toArray(new String[0]);
        Arrays.sort(itemList);
        return itemList;
    }

    /**
     * Sends a bid to the Auction House.
     */
    public void makeABid(String item, int amount, int accountNumber) throws IOException {
        outMessage.writeObject(new AuctionBidManager(itemsForAuction.get(item), amount, accountNumber, false));
    }

    /**
     * Returns refund amount if a bid was outbid or auction was lost.
     */
    public double getAmountReturn() {
        if (returnApproved) {
            double temp = amountReturn;
            amountReturn = 0;
            returnApproved = false;
            return temp;
        }
        return 0;
    }

    /** Returns the index of this auction house (assigned by the Agent). */
    public int getItemNo() { return itemNo; }

    /** True if new items were recently added. */
    public boolean isNewItemsRegistered() {
        return newItemsRegistered;
    }

    /** Resets the new-items flag after GUI update. */
    public void resetItems() {
        newItemsRegistered = false;
    }

    /** Exposes all current auction items to the Agent. */
    public HashMap<String, AuctionBidManager> getAuctionItems() {
        return itemsForAuction;
    }

    /** Checks if the given item is still active (not bid over). */
    public boolean isItemActive(String item) {
        return !itemsForAuction.get(item).isBidOver();
    }
}