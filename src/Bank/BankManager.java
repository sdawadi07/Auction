package Bank;

/**
 *
 * CS-351L (Distributed Auction)
 * In the Distributed Auction System, BankManager is the main server class. Through the
 * assignment of distinct account numbers, the management of financial transfers, and the
 * maintenance of client account records, it oversees all correspondence between agents and
 * auction houses. For every agent or auction house that connects, the class generates a unique
 * ClientAccount thread and uses a server socket on port 9090 to wait for incoming connections.
 * Additionally, BankManager quickly transfers money between agent and auction house accounts
 * by processing ClientMessage objects that are stored in a shared message queue using a separate
 * handler thread. Effective communication and transaction management throughout the distributed
 * system are made possible by its usage of hash maps to hold active client accounts, client
 * network addresses, and output streams.
 *
 */

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class BankManager implements Runnable{
    private String ipAddress;
    private int portNo;
    private final HashMap<Integer, ClientAccount> clientAccountHashMap = new HashMap<>();
    private final LinkedBlockingQueue<ClientMessage> messageQueue = new LinkedBlockingQueue<>();
    private final HashMap<Integer, ClientAdress> addressHashMap = new HashMap<>();
    private final HashMap<Integer, ObjectOutputStream> clients = new HashMap<>();
    private ServerSocket serverSocket;

    public int getPortNo() {
        return portNo;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(9090);
            portNo = serverSocket.getLocalPort();
//            ipAddress = InetAddress.getLocalHost().getHostAddress();
            ipAddress = "64.106.21.162";
            System.out.println(portNo);
            System.out.println(ipAddress);

            Runnable handler = () ->{
                try{
                    while(true){
                        ClientMessage msg = messageQueue.take();
                        ClientAccount fromAcct = clientAccountHashMap.get(msg.getAgentAcctNo());
                        ClientAccount toAcct = clientAccountHashMap.get(msg.getAucHouseAccNo());
                        int transferAmount = msg.getTransferAmount();

                        fromAcct.withdraw(transferAmount);
                        toAcct.deposit(transferAmount);
                        System.out.println("Amount has been transfered");
                    }
                }catch(NullPointerException | InterruptedException e){
                    e.printStackTrace();
                }
            };
            Thread thread = new Thread(handler);
            thread.start();

            while (true){
                Socket skt;
                skt = serverSocket.accept();
                ClientAccount acct = new ClientAccount(skt, messageQueue, makeClientAccount(), addressHashMap,
                        clients, clientAccountHashMap);
                clientAccountHashMap.put(acct.getAcctNumber(), acct);
                Thread thread1 = new Thread(acct);
                thread1.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int makeClientAccount(){
        Random rand = new Random();
        int accountNumber = -1;
        while (clientAccountHashMap.containsKey(accountNumber) && accountNumber < 0) {
            accountNumber = rand.nextInt(10000, 99999);
        }
        return accountNumber;
    }
}