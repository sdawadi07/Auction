package Auctionhouse;
/**
 * Manages auction items, timing their bidding periods and dispatching expired items to the Client.
 * Retrieves and registers the next item to sell from the available Items list.
 * Periodically checks each active bid’s remaining time and signals when bidding ends.
 */
import java.util.Collection;
import java.util.HashMap;

public class Salesman extends HashMap<String, AuctionBidManager> implements Runnable {
    private final Client client;
    private double bidPrice;
    private int bidPeriod;
    private AuctionBidManager bidItem;
    private Items items;


    public Salesman(Client client, Items items){
        this.client = client;
        this.items = items;
    }

    public synchronized AuctionBidManager getSelleableItems(){
        AuctionBidManager selleableItems = items.remove(0);
        this.put(selleableItems.getAuctionItemName(), selleableItems);
        return selleableItems;
    }

    public synchronized Collection<AuctionBidManager> getValues(){
        return values();
    }

    public synchronized AuctionBidManager getItem(Object key){
        return super.get(key);
    }
    @Override
    public void run() {
        while (true){
            try{
                synchronized (this){
                    wait(1000);
                    for (AuctionBidManager item : getValues()){
                        item.timeOver();
                        if (item.getRemainingTime() == 0){
                            item.setBidOver(true);
                            client.add(item);
                        }
                    }
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public boolean itemLeft(){
        return !items.isEmpty();
    }
}