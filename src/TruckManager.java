import java.util.ArrayList;
import java.util.List;

public class TruckManager {

    private final Scheduler scheduler;
    private final Ship ship;

    private final int capacity;
    private final int time;

    private final List<Thread> threads = new ArrayList<>();
    private Truck truck;

    public TruckManager(Scheduler scheduler, Ship ship, int capacity, int time) {
        this.scheduler = scheduler;
        this.ship = ship;
        this.capacity = capacity;
        this.time = time;

        this.replaceTruck();
    }

    private synchronized void replaceTruck() {
        this.truck = new Truck(scheduler, ship, capacity, time);
    }

    public synchronized void sendTruck() {
        threads.add(truck.send());
    }

    public synchronized boolean loadTruck() {
        truck.load();

        boolean full = truck.full();

        if (full) {
            sendTruck();
            replaceTruck();
        }

        return full;
    }

    public void joinThreads() throws InterruptedException {
        for (Thread t : threads) {
            t.join();
        }
    }
}
