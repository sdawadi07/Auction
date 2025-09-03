package Bank;

/**
 * CS-351L (Distributed Auction)
 * In distributed auction, this class represents a client's network address. The IP address
 * and port number * that are used to uniquely identify the client on the network are stored
 * in the ClientAdress object. In order to facilitate communication between the bank and other
 * components, it uses Serializable, which makes it simple to send over the network.
 */

import java.io.Serializable;

public class ClientAdress implements Serializable {
    private final String ipAdress;
    private final int portNumber;

    /**
     * Creates a ClientAdress with the given IP address and port number.
     *
     * @param ipAdress the IP address of the client
     * @param portNumber the port number used by the client
     */
    public ClientAdress(String ipAdress, int portNumber){
        this.ipAdress = ipAdress;
        this.portNumber = portNumber;
    }

    /**
     * Returns the client’s port number.
     *
     * @return the port number
     */
    public int getPortNumber(){
        return portNumber;
    }

    /**
     * Returns the client’s IP address.
     *
     * @return the IP address
     */
    public String getipAdress(){
        return ipAdress;
    }
}