package Bank;

/**
 *
 *  CS-351L (Distributed Auction)
 * The Distributed Auction System's bank side uses ClientAccount to represent a single
 * connected client (an agent or an auction house). Account setup, balance updates, and money
 * transfer requests are all handled by this class on behalf of the client in their interactions
 * with the bank. Along with cleaning up when clients disconnect, it also handles updating
 * agents about new auction houses.
 *
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientAccount implements Runnable{
    private final Integer acctNumber;
    private double totalBalance;
    private Socket socket;
    private HashMap<Integer, ClientAdress> auctionHouseAddresses;
    private final LinkedBlockingQueue<ClientMessage> messageQueue;
    private final HashMap<Integer, ObjectOutputStream> clients;
    private final HashMap<Integer, ClientAccount> accounts;
    private ObjectOutputStream writer;
    private ObjectInputStream inputStream;


    /**
     *  Constructor to set up the ClientAccount with the required references.
     */

    public ClientAccount(Socket socket,
                         LinkedBlockingQueue<ClientMessage> messageQueue, Integer acctNumber,
                         HashMap<Integer, ClientAdress> auctionHouseAddresses,
                         HashMap<Integer, ObjectOutputStream> clients,
                         HashMap<Integer, ClientAccount> accounts){
        this.acctNumber = acctNumber;
        this.socket = socket;
        this.messageQueue = messageQueue;
        this.auctionHouseAddresses = auctionHouseAddresses;
        this.clients = clients;
        this.accounts = accounts;
    }

    /**
     * Run method for the main thread. communicates with the associated agent or auction
     * house, handles balance updates, registers the auction house, and sends transfer
     * requests to the message queue.
     *
     */

    @Override
    public void run() {
        try {
            writer = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            ClientMessage tmp;

            String msg = (String) inputStream.readObject();

            if (msg.equals("auction")){
                System.out.println("Connected to Auction House");
                ClientAdress clientAdress = (ClientAdress) inputStream.readObject();
                System.out.println(clientAdress.getipAdress() + " " + clientAdress.getPortNumber());
                auctionHouseAddresses.put(acctNumber, clientAdress);
                writer.writeObject(acctNumber);

                for (ObjectOutputStream client : clients.values()){
                    client.writeObject(clientAdress);
                }

                while(socket.isConnected()){
                    tmp = (ClientMessage) inputStream.readObject();
                    if (tmp != null){
                        messageQueue.add(tmp);
                    }
                }
            }else if (msg.equals("Agent")){
                System.out.println("Connected to Agent");
                totalBalance = (Double) inputStream.readObject();
                writer.writeObject(acctNumber);
                System.out.println("Agent Account Number: " + acctNumber);
                System.out.println("Agent Bank Balance: " + totalBalance);
                clients.put(acctNumber, writer);

                for (ClientAdress address : auctionHouseAddresses.values()){
                    writer.writeObject(address);
                }

                while (socket.isConnected()){
                    Object step  = inputStream.readObject();
                    if (step instanceof String){
                        writer.writeObject(totalBalance);
                    }else{
                        ClientMessage tmpMsg = (ClientMessage) step;
                        ClientMessage clientmsg =
                                new ClientMessage(tmpMsg.getAgentAcctNo(), tmpMsg.getAucHouseAccNo(), acctNumber);
                        messageQueue.add(clientmsg);
                    }
                }
            }
        }catch(SocketException | EOFException e){
            System.out.println("User #" + acctNumber + " has closed.");
            auctionHouseAddresses.remove(acctNumber);
            clients.remove(acctNumber);
            accounts.remove(acctNumber);
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public Integer getAcctNumber(){
        return acctNumber;
    }

    public void deposit(int amount){
        totalBalance += amount;
    }

    public void withdraw(int amount){
        try {
            totalBalance -= amount;
            writer.writeObject(totalBalance);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}