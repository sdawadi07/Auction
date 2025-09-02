package Auctionhouse;
/**
 * Represents a notification that a bid has been rejected by a bank.
 * This class encapsulates the identifier of the bank that issued the rejection,
 * the explanatory message, and the name of the item for which the bid was placed.
 */

import java.io.Serializable;

public class BidRejection implements Serializable {
    private final Integer bankId;
    private final String message;
    private final String itemName;

    public BidRejection(Integer bankId, String message, String itemName) {
        this.bankId = bankId;
        this.message = message;
        this.itemName = itemName;
    }

    public Integer getBankId() {
        return bankId;
    }

    public String getMessage() {
        return message;
    }

    public String getItemName() {
        return itemName;
    }
}