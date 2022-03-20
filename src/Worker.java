import java.util.concurrent.ThreadLocalRandom;

public class Worker extends Talk implements Runnable {

    private final static int LOADING_TIME = 10;
    private static int totalLoaded = 0;
    private final Scheduler scheduler;

    private final int maxWaitingTime;

    private int workDone = 0;

    public Worker(String name, Scheduler scheduler, int maxWaitingTime) {
        super(name);

        this.scheduler = scheduler;
        this.maxWaitingTime = maxWaitingTime;

        talk("initialized");
    }

    private static synchronized int incrementAndGetTotalLoaded() {
        totalLoaded += 1;
        return totalLoaded;
    }

    private void work(String block) throws InterruptedException {
        talk("got block, working on it");

        int waitTimeSum = 0;

        for (int i = 0; i < block.length(); ++i) {
            int waitTime = ThreadLocalRandom.current().nextInt(maxWaitingTime) + 1;
            Thread.sleep(waitTime);
            workDone += 1;
            scheduler.getLogger().log(this, "finished mining material - it took: " + waitTime + "ms");

            waitTimeSum += waitTime;
        }

        scheduler.getLogger().log(this, "finished mining a block - it took: " + waitTimeSum + "ms");
    }

    private void doneCheck() {
        synchronized (Worker.class) {
            if (totalLoaded == scheduler.getMaterialCount()) {

//              invalidate the result
                totalLoaded += 1;
                scheduler.getTruckManager().sendTruck();
            }
        }
    }

    private void load(String block) throws InterruptedException {
        talk("started loading");
        talk("loading " + block.length() + " material");

        boolean lastCycleSent = false;

        for (int i = 0; i < block.length(); ++i) {
            Thread.sleep(LOADING_TIME);
            boolean sent = scheduler.getTruckManager().loadTruck();
            if(incrementAndGetTotalLoaded() == scheduler.getMaterialCount() && sent) {
                lastCycleSent = true;
            }
        }

        if(!lastCycleSent) {
            doneCheck();
        }
    }

    public void run() {
        talk("thread started");

        String block;

        while ((block = scheduler.getBlock(this)) != null) {
            try {
                work(block);
                load(block);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        talk("Total mined: " + workDone + " sources");
    }

    public int getWorkDone() {
        return workDone;
    }
}
