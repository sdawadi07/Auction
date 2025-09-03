package Bank;

/**
 * CS-351L (Distributed Auction)
 * A message sent by an agent to the bank in the auction system is represented by this class.
 * In addition to the amount of money to be sent between the two accounts, the ClientMessage object
 * contains the account numbers of the agent and the auction house. The object can be saved to a file
 * or sent over a network during communication between distributed components.
 *
 */


import java.io.Serializable;

public class ClientMessage implements Serializable {
    private int agentAcctNo;
    private int aucHouseAccNo;
    private int transferAmount;

    /**
     * Constructs a ClientMessage with the specified agent account number,
     * auction house account number, and transfer amount.
     */
    public ClientMessage(int agentAcctNo, int aucHouseAccNo, int transferAmount){
        this.agentAcctNo = agentAcctNo;
        this.aucHouseAccNo = aucHouseAccNo;
        this.transferAmount = transferAmount;
    }

    /**
     * Gets the agent's account number.
     */
    public int getAgentAcctNo() {
        return agentAcctNo;
    }

    /**
     * Gets the auction house's account number.
     */
    public int getAucHouseAccNo() {
        return aucHouseAccNo;
    }

    /**
     * Gets the amount of money to transfer.
     */
    public int getTransferAmount() {
        return transferAmount;
    }
}