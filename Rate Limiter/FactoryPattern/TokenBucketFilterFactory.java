package FactoryPattern;

public final class TokenBucketFilterFactory {
    
    // Force users to interact with the factory
    // Only thru the static methods
    private TokenBucketFilterFactory() {

    }

    static public TokenBucketFilter makeTokenBucketFilter(int capacity) {
        MultiThreadedTokenBucketFilter tbf = new MultiThreadedTokenBucketFilter(capacity);
        tbf.initialize();
        return tbf;
    }

    private static class MultiThreadedTokenBucketFilter extends TokenBucketFilter {
        private long possibleTokens = 0;
        private final int MAX_TOKENS;
        private final int ONE_SECOND = 1000;

        MultiThreadedTokenBucketFilter(int maxTokens) {
            MAX_TOKENS = maxTokens;
        }

        void initialize() {
            // Never start a thread in a constructor
            Thread dt = new Thread(() -> {
                daemonThread();
            });
            dt.setDaemon(true);
            dt.start();
        }

        private void daemonThread() {
            while (true) {
                synchronized(this) {
                    if (possibleTokens < MAX_TOKENS) {
                        possibleTokens++;
                    }

                    // We need to notify no matter we increment tokens or not
                    // otherwise waited thread will not be woken up
                    this.notify();
                }
                try {
                    Thread.sleep(ONE_SECOND);
                } catch (InterruptedException ie) {
                    // swallow exception
                }                
            }
        }

        public void getToken() throws InterruptedException {

            synchronized (this) {
                while (possibleTokens == 0) {
                    this.wait();
                }
                possibleTokens--;
            }

            System.out.println(
                    "Granting " + Thread.currentThread().getName() + " token at " + System.currentTimeMillis() / 1000);
        }        
    }
}
