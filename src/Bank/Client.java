package Bank;

/**
 * CS-351L (Distributed Auction)
 * The Client class is used to store a reference to either an AuctionHouse or an Agent.
 * This helps the Bank system identify and manage different types of connected clients.
 */

import Agent.Agent;
import Auctionhouse.AuctionHouse;

public class Client {
    AuctionHouse auctionHouse;
    Agent agent;

    public Client(AuctionHouse auctionHouse){
        this.auctionHouse = auctionHouse;
    }

    public Client(Agent agent){
        this.agent = agent;
    }
}