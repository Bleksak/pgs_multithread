public class Scheduler extends Talk implements Runnable {

    private String[] blocks;
    private int currentBlockIndex = 0;

    public static int blockSum = 0;

    private final int workerCount;
    private Thread[] workers;

    public synchronized String getBlock(Worker worker) {

        worker.talk("trying to get a block");

        if(currentBlockIndex >= blocks.length) {
            return null;
        }

        return blocks[currentBlockIndex++];
    }

    public Scheduler(String input, int workerCount) {
        super("Scheduler");

        this.blocks = input.split("\s+");
        this.workerCount = workerCount;

        for (String block : blocks) {
            blockSum += block.length();
        }

        workers = new Thread[workerCount];

        talk("initialized");
    }

    public void run() {

        Worker[] localWorkers = new Worker[workerCount];

        for(int i = 0; i < workerCount; ++i) {
            Worker worker = new Worker("Worker#" + i, this, Arguments.workerTime());

            localWorkers[i] = worker;
            workers[i] = new Thread(worker);
        }

        for(int i = 0; i < workerCount; ++i) {
            workers[i].start();
        }

        for(int i = 0; i < workerCount; ++i) {
            try {
                workers[i].join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
