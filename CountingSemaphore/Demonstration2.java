public class Demonstration2 {
    public static void main(String[] args) throws InterruptedException {
        final CountingSemaphore cs = new CountingSemaphore(1);

        Thread t1 = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 5; i++) {
                        cs.acquire();
                        System.out.println("Ping " + i);
                    }
                } catch (InterruptedException e) {

                }
            }
        });

        Thread t2 = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 5; i++) {
                        cs.release();
                        System.out.println("Pong " + i);
                    }
                } catch (InterruptedException e) {

                }
            }
        });

        t2.start();
        t1.start();
        t1.join();
        t2.join();
    }
}

// class CountingSemaphore {

//     int usedPermits = 0;
//     int maxCount;

//     public CountingSemaphore(int count) {
//         this.maxCount = count;

//     }

//     public synchronized void acquire() throws InterruptedException {

//         while (usedPermits == maxCount)
//             wait();

//         notify();
//         usedPermits++;

//     }

//     public synchronized void release() throws InterruptedException {

//         while (usedPermits == 0)
//             wait();

//         usedPermits--;
//         notify();
//     }
// }