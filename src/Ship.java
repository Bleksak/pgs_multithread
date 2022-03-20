public class Ship extends Talk implements Runnable {

    private final Scheduler scheduler;
    private boolean sleep = true;

    private final int maxCapacity;
    private int loaded;
    private int loadedTotal;

    private long creationTime = System.currentTimeMillis();

    public Ship(Scheduler scheduler, int maxCapacity) {
        super("Ship");

        this.scheduler = scheduler;
        this.maxCapacity = maxCapacity;

        talk("initialized");
    }

    public int getLoadedTotal() {
        return loadedTotal;
    }

    /**
     * synchronization barrier
     *
     * @param truck
     * @throws InterruptedException
     */
    public synchronized void load(Truck truck) throws InterruptedException {
        truck.talk("loading to ship");

        while(!sleep) {
            wait();
        }

        loaded += 1;
        loadedTotal += truck.getLoaded();

        if(loaded == maxCapacity) {
            talk("full, releasing trucks");

            long currentTime = System.currentTimeMillis();

            scheduler.getLogger().log(this, "The Ship is on the way - it took: " + (currentTime - creationTime) + "ms");

            this.creationTime = currentTime;

            sleep = false;

            notifyAll();
        }

        while(sleep) {
            wait();
        }

        loaded -= 1;

        if(loaded == 0) {
            sleep = true;
            notifyAll();
        }
    }

    @Override
    public void run() {

    }
}
