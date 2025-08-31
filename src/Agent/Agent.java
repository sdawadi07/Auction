package Agent;

import Bank.ClientAdress;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Represents a user-controlled Agent in the distributed auction system.
 * The Agent connects to the Bank to create an account and discover Auction Houses,
 * then communicates with each Auction House to view and bid on items.
 */
public class Agent implements Runnable {
    // Network and IO components
    private Socket socket = null;
    private ObjectInputStream inMessage = null;
    private ObjectOutputStream outMessage = null;

    // Agent account and balance tracking
    private int accountId;
    private String name;
    private double bankBalance;
    private double availableBalance;
    private final String bankAddress;
    private final int bankPort;

    // Auction house tracking
    private final HashMap<Integer, ItemWatcher> ah = new HashMap<>();
    private int i = 0;
    private double bidAmount;

    public boolean hasBankId = false;

    /**
     * Constructs an Agent with the bank's IP address and port number.
     */
    public Agent(String bankAddress, int bankPort) {
        this.bankAddress = bankAddress;
        this.bankPort = bankPort;
    }

    /**
     * Connects to the Bank, creates an account, and listens for AuctionHouse updates.
     */
    @Override
    public void run() {
        try {
            socket = new Socket(bankAddress, bankPort);
            System.out.println("Connected to Bank");
            inMessage = new ObjectInputStream(socket.getInputStream());
            outMessage = new ObjectOutputStream(socket.getOutputStream());

            outMessage.writeObject("Agent");
            outMessage.writeObject(bankBalance);
            accountId = (int) inMessage.readObject();
            System.out.println("Account number: " + accountId);

            while (socket.isConnected()) {
                Object object = inMessage.readObject();
                if (object instanceof ClientAdress client) {
                    Socket auctionSocket = new Socket(client.getipAdress(), client.getPortNumber());
                    System.out.println("Connected to Auction House" + (i + 1) + ".");
                    ItemWatcher itemWatcher = new ItemWatcher(auctionSocket, i, accountId);
                    i++;
                    ah.put(itemWatcher.getItemNo(), itemWatcher);
                    (new Thread(itemWatcher)).start();
                } else {
                    setTotalBalance((double) object);
                }
            }
        } catch (SocketException | EOFException e) {
            System.out.println("Connection Closed");
            System.exit(0);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Sends a bid for a specific item to the corresponding AuctionHouse. */
    public void sendBid(String item, int i) throws IOException {
        availableBalance -= bidAmount;
        ah.get(i).makeABid(item, (int) bidAmount, accountId);
    }

    /** Checks whether this agent owns the current bid on an item. */
    public boolean checkBidOwner(int i, String item) {
        return ah.get(i).getAuctionIsOtherBid(item, accountId);
    }

    /** Retrieves the current bid value for an item from a specific auction. */
    public double getBidStatus(int index, String item) {
        return ah.get(index).getItemBid(item);
    }

    /** Gets the latest status message (Accepted, Rejected, Winner, etc.) for a bid. */
    public String receiveMessage(int i, String item) {
        return ah.get(i).getStatusMessage(item);
    }

    /** Manually sets a rejection message for a given item. */
    public void setMessage(int i, String item) {
        ah.get(i).updateAuctionUpdate(item, BidMessage.REJECTED.getMessage());
    }

    /** Returns the set of item names being tracked in a given auction. */
    public Set<String> getAuctionItemKeys(int i) {
        return ah.get(i).getAuctionItems().keySet();
    }

    /** Returns all item names in a given auction as an array. */
    public String[] getListOfItem(int i) {
        return ah.get(i).getItems();
    }

    /** Returns sorted indexes of all connected auction houses. */
    public Integer[] getIndex() {
        Integer[] itemArray = ah.keySet().toArray(new Integer[0]);
        Arrays.sort(itemArray);
        return itemArray;
    }

    public void setTotalBalance(double bankBalance) {
        this.bankBalance = bankBalance;
    }

    public double getTotalBalance() {
        return bankBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setBidAmount(double bid) {
        this.bidAmount = bid;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public int getAccountId() {
        return accountId;
    }

    /** Checks whether there are currently funds blocked due to active bids. */
    public boolean checkActiveBids() {
        return availableBalance != bankBalance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Determines if a given item in an auction is still actively being bid on. */
    public boolean isItemActive(String item, int i) {
        return ah.get(i).isItemActive(item);
    }

    /** Gracefully closes socket and associated streams. */
    public void closeSocket() {
        try {
            if (socket != null) {
                inMessage.close();
                outMessage.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Checks all auctions for newly registered items and resets flags. */
    public boolean getAHUpdate() {
        boolean check = false;
        for (ItemWatcher aucHo : ah.values()) {
            if (aucHo.isNewItemsRegistered()) {
                aucHo.resetItems();
                check = true;
            }
        }
        return check;
    }

    /** Returns the amount from rejected or refunded bids back to available balance. */
    public void returnRejectedBid() {
        for (ItemWatcher itemWatcher : ah.values()) {
            double amount = itemWatcher.getAmountReturn();
            availableBalance += amount;
        }
    }

    /** Clears the stored account ID flag (used for UI reset). */
    public void clearAccountId() {
        hasBankId = false;
    }
}