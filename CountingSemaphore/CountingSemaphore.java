public class CountingSemaphore {
    int usedPermits = 0;
    int maxCount;

    public CountingSemaphore(int count) {
        this.maxCount = count;
    }

    // with synchronized keyword only one thread can call 
    // either of the two methods
    public synchronized void acquire() throws InterruptedException {
        while (usedPermits == maxCount)
            wait();

        usedPermits++;
        notify();
    }

    public synchronized void release() throws InterruptedException {
        while (usedPermits == 0) 
            wait();


        // It DOES NOT MATTER if below two lines are switched order
        // because no other thread can acquire the lock until 
        // this method returns and by then usedPermits is already
        // updated
        usedPermits--;
        notify();
    }
}