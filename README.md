 # Auction
This project simulates a distributed auction platform featuring a central Bank, multiple 
Auction Houses, and multiple Agents — each running as a separate application. Agents can
interact with auction houses to bid on items, and funds are managed securely through the
bank.


 ## Project Structure
* Bank/ – Manages account creation, fund blocking, and transfers.
* Auctionhouse/ – Hosts and manages auction items. Accepts bids from agents.
* Agent/ – JavaFX-based GUI allowing users to view, bid, and track auctions.
* items.txt – List of item names for auction houses to sell.
* README.md – This file.

##[ Running the System
1. Start the Bank (on one machine)
   java Bank.BankMain


2. Start one or more Auction Houses
   java Auctionhouse.AuctionHouseMain <BankIP> <BankPort> items.


Example:
java Auctionhouse.AuctionHouseMain 10.0.0.105 9090 items.txt