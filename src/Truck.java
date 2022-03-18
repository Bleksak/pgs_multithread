import java.util.concurrent.ThreadLocalRandom;

public class Truck extends Talk implements Runnable {

    private final int maxWaitTime;

    private final int maxCapacity;
    private int loaded = 0;

    private static int n = 0;

    private static Truck truck;

    public Truck(int maxCapacity, int maxWaitTime) {
        super("Truck#" + ++n);

        this.maxCapacity = maxCapacity;
        this.maxWaitTime = maxWaitTime;

        talk("initialized");
    }

    public int getLoaded() {
        return loaded;
    }

    private synchronized void leave() throws InterruptedException {
        Ship.getInstance().load(this);
        talk("Released from ship");

        int waitingTime = ThreadLocalRandom.current().nextInt(maxWaitTime) + 1;
//        Thread.sleep(waitingTime);

        talk("Done with job");
    }

    public synchronized static Truck getTruck() {
        if(truck == null) {
            replaceTruck();
        }

        return truck;
    }

    public synchronized static void replaceTruck() {
        truck = new Truck(Arguments.lorryCapacity(), Arguments.lorryTime());
    }

    public synchronized void load(Worker worker) {
        worker.talk("loading");
        loaded += 1;

        if (loaded == maxCapacity) {
            talk("full");
            worker.talk("replacing truck");

            replaceTruck();

            new Thread(this).start();
        }
    }

    @Override
    public void run() {
        try {
            leave();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
