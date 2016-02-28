package lamp.admin.utils;

public class ThroughputThrottler {

    private static final long NS_PER_MS = 1000000L;
    private static final long NS_PER_SEC = 1000 * NS_PER_MS;
    private static final long MIN_SLEEP_NS = 2 * NS_PER_MS;

    long sleepTimeNs;
    long sleepDeficitNs = 0;
    long targetThroughput = -1;
    long startMs;

    /**
     * @param targetThroughput Can be messages/sec or bytes/sec
     * @param startMs          When the very first message is sent
     */
    public ThroughputThrottler(long targetThroughput, long startMs) {
        this.startMs = startMs;
        this.targetThroughput = targetThroughput;
        this.sleepTimeNs = targetThroughput > 0 ?
                NS_PER_SEC / targetThroughput :
                Long.MAX_VALUE;
    }

    /**
     * @param amountSoFar bytes produced so far if you want to throttle data throughput, or
     *                    messages produced so far if you want to throttle message throughput.
     * @param sendStartMs timestamp of the most recently sent message
     * @return
     */
    public boolean shouldThrottle(long amountSoFar, long sendStartMs) {
        if (this.targetThroughput < 0) {
            // No throttling in this case
            return false;
        }

        float elapsedMs = (sendStartMs - startMs) / 1000.f;
        return elapsedMs > 0 && (amountSoFar / elapsedMs) > this.targetThroughput;
    }

    /**
     * Occasionally blocks for small amounts of time to achieve targetThroughput.
     *
     * Note that if targetThroughput is 0, this will block extremely aggressively.
     */
    public void throttle() {
        if (targetThroughput == 0) {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                // do nothing
            }
            return;
        }

        // throttle throughput by sleeping, on average,
        // (1 / this.throughput) seconds between "things sent"
        sleepDeficitNs += sleepTimeNs;

        // If enough sleep deficit has accumulated, sleep a little
        if (sleepDeficitNs >= MIN_SLEEP_NS) {
            long sleepMs = sleepDeficitNs / 1000000;
            long sleepNs = sleepDeficitNs - sleepMs * 1000000;

            long sleepStartNs = System.nanoTime();
            try {
                Thread.sleep(sleepMs, (int) sleepNs);
                sleepDeficitNs = 0;
            } catch (InterruptedException e) {
                // If sleep is cut short, reduce deficit by the amount of
                // time we actually spent sleeping
                long sleepElapsedNs = System.nanoTime() - sleepStartNs;
                if (sleepElapsedNs <= sleepDeficitNs) {
                    sleepDeficitNs -= sleepElapsedNs;
                }
            }
        }
    }
}
