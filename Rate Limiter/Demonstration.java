import java.util.*;

public class Demonstration {
    public static void main(String[] args) throws InterruptedException{
        TokenBucketFilter.runTestMaxTokenIsTen();
    }
}

class TokenBucketFilter {
    private int MAX_TOKENS;
    private long lastRequestTime = System.currentTimeMillis();
    long possibleTokens = 0;

    public TokenBucketFilter(int maxTokens) {
        MAX_TOKENS = maxTokens;
    }

    synchronized void getToken() throws InterruptedException {

        possibleTokens += (System.currentTimeMillis() - lastRequestTime) / 1000;

        if (possibleTokens > MAX_TOKENS) {
            possibleTokens = MAX_TOKENS;
        }

        if (possibleTokens == 0) {
            Thread.sleep(1000);
        } else {
            possibleTokens--;
        }
        lastRequestTime = System.currentTimeMillis();

        System.out.println("Granting " + Thread.currentThread().getName() + " token at " + (System.currentTimeMillis() / 1000));
    }
    
    public static void runTestMaxTokenIs1() throws InterruptedException {
        Set<Thread> allThreads = new HashSet<>();
        final TokenBucketFilter filter = new TokenBucketFilter(1);

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable(){
                public void run() {
                    try {
                        filter.getToken();
                    } catch (InterruptedException e) {
                        System.out.println("We have a problem");
                    }
                }
            });

            thread.setName("Thread_" + (i+1));
            allThreads.add(thread);
        }

        for (Thread t : allThreads) {
            t.start();
        }

        for (Thread t : allThreads) {
            t.join();
        }
    }

    public static void runTestMaxTokenIsTen() throws InterruptedException {
        Set<Thread> allThreads = new HashSet<>();
        final TokenBucketFilter filter = new TokenBucketFilter(5);

        Thread.sleep(10000);
        for (int i = 0; i < 12; i++) {
            Thread thread = new Thread(new Runnable(){
                public void run() {
                    try {
                        filter.getToken();
                    } catch (InterruptedException e) {
                        System.out.println("We have a problem");
                    }
                }
            });

            thread.setName("Thread_" + (i+1));
            allThreads.add(thread);            
        }

        for (Thread t : allThreads) {
            t.start();
        }

        for (Thread t : allThreads) {
            t.join();
        }        
    }
}