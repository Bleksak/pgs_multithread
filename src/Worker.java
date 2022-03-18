import java.util.concurrent.ThreadLocalRandom;

public class Worker extends Talk implements Runnable {
    private final Scheduler scheduler;
    private final int maxWorkingTime;

    private static int totalWorkDone;

    private final static int LOADING_TIME = 1000;

    private int workDone = 0;

    public Worker(String name, Scheduler scheduler, int maxWorkingTime) {
        super(name);

        this.scheduler = scheduler;
        this.maxWorkingTime = maxWorkingTime;

        talk("initialized");
    }

    private void work(String block) throws InterruptedException {
        talk("got block, working on it");

        for(int i = 0; i < block.length(); ++i) {
            int waitTime = ThreadLocalRandom.current().nextInt(maxWorkingTime) + 1;
//            Thread.sleep(waitTime);
            talk("working on a block");
            workDone += 1;

            synchronized (Worker.class) {
                totalWorkDone += 1;
            }
        }

        talk("I have " + workDone + " material");
    }

    private static synchronized void doneCheck() {
        if(totalWorkDone == Scheduler.blockSum) {

//            invalidate the result
            totalWorkDone += 1;
            new Thread(Truck.getTruck()).start();
        }
    }

    private void load(String block) throws InterruptedException {
        talk("started loading");

        for(int i = 0; i < block.length(); ++i) {
//            Thread.sleep(LOADING_TIME);
            synchronized (Truck.class) {
                Truck.getTruck().load(this);
            }
        }

        doneCheck();
    }

    public void run() {
        talk("thread started");

        String block;

        while((block = scheduler.getBlock(this)) != null) {
            try {
                work(block);
                load(block);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            workDone = 0;
        }
    }
}
