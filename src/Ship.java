public class Ship extends Talk implements Runnable {

    private boolean sleep = true;

    private final int maxCapacity;
    private int loaded = 0;

    private static Ship ship;

    private int loadedTotal;

    public Ship(int maxCapacity) {
        super("Ship");
        this.maxCapacity = maxCapacity;

        talk("initialized");
    }

    public static Ship getInstance() {
        if(ship == null) {
            ship = new Ship(Arguments.ferryCapacity());
        }

        return ship;
    }

    public synchronized void load(Truck truck) throws InterruptedException {
        truck.talk("loading to ship");

        loaded += 1;

        loadedTotal += truck.getLoaded();

        System.err.println(Scheduler.blockSum);
        System.err.println("vs");
        System.err.println(loadedTotal);

        if(loaded == maxCapacity || loadedTotal == Scheduler.blockSum) {
            talk("full, releasing trucks");

            loaded = 0;

            notifyAll();
        } else {
            wait();
        }
    }

    @Override
    public void run() {

    }
}
