import java.util.concurrent.ThreadLocalRandom;

public class Truck extends Talk implements Runnable {

    private final int maxWaitTime;

    private final int maxCapacity;
    private int loaded = 0;

    private static int n = 0;

    private final Scheduler scheduler;

    private final long creationTime = System.currentTimeMillis();

    private final Ship ship;

    public Truck(Scheduler scheduler, Ship ship, int maxCapacity, int maxWaitTime) {
        super("Truck#" + ++n);

        this.scheduler = scheduler;
        this.ship = ship;
        this.maxCapacity = maxCapacity;
        this.maxWaitTime = maxWaitTime;

        talk("initialized");
    }

    public int getLoaded() {
        return loaded;
    }

    private void leave() throws InterruptedException {
        int waitingTime = ThreadLocalRandom.current().nextInt(maxWaitTime) + 1;
        Thread.sleep(waitingTime);

        scheduler.getLogger().log(this, "Got to the ship - it took: " + waitingTime + "ms");

        ship.load(this);
        talk("Released from ship");

        waitingTime = ThreadLocalRandom.current().nextInt(maxWaitTime) + 1;
        Thread.sleep(waitingTime);

        scheduler.getLogger().log(this, "Got to the final destination - it took: " + waitingTime + "ms");
        talk("Done with job");
    }

    public void load() {
        loaded += 1;

        if (full()) {
            talk("full");
        }
    }

    public boolean full() {
        return loaded == maxCapacity;
    }

    @Override
    public void run() {
        try {
            leave();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * sends truck to the ship
     * @return created thread for the truck
     */
    public Thread send() {
        scheduler.getLogger().log(this, "Truck is full - it took: " + (System.currentTimeMillis() - creationTime) + "ms to fill");

        Thread thread = new Thread(this);
        thread.start();

        return thread;
    }
}
