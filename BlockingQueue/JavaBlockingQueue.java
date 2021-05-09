import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class JavaBlockingQueue {
    public static void main(String[] args) {
        BlockingQueue<Item> queue = new ArrayBlockingQueue<>(10);
        
        final Runnable producer = () -> {
            while (true) {
                try {
                    queue.put(createItem()); // thread blocks if queue full
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        new Thread(producer).start();
        new Thread(producer).start();

        final Runnable consumer = () -> {
            while (true) {
                Item i = null;
                try {
                    i = queue.take(); // thread blocks if queue empty
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                process(i);
            }
        };

        new Thread(consumer).start();
        new Thread(consumer).start();
    }

    private static void process(Item i) {

    }

    private static Item createItem() {
        return new Item();
    }
}

class Item {
    
}