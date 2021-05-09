public class TokenBucketFilter {
    private int MAX_TOKENS;
    private long lastRequestTime = System.currentTimeMillis();
    long possibleTokens = 0;

    public TokenBucketFilter(int maxTokens) {
        MAX_TOKENS = maxTokens;
    }

    synchronized void getToken() throws InterruptedException {

        // calculate the total tokens generated since last requested time
        possibleTokens += (System.currentTimeMillis() - lastRequestTime) / 1000;

        possibleTokens = Math.min(possibleTokens, MAX_TOKENS);

        if (possibleTokens == 0) {
            Thread.sleep(1000);
        } else {
            possibleTokens--;
        }
        lastRequestTime = System.currentTimeMillis();

        System.out.println("Granting " + Thread.currentThread().getName() + " token at " + (lastRequestTime / 1000));
    }
}