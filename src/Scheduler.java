import java.io.FileNotFoundException;

public class Scheduler extends Talk implements Runnable {

    private final Arguments arguments;
    private final Worker[] workers;
    private final Thread[] workerThreads;
    private final Logger logger;
    private final Ship ship;

    private final TruckManager truckManager;
    private final String[] blocks;
    private int currentBlockIndex = 0;
    private int materialCount = 0;

    /**
     * creates: array with worker threads, initial truck, ship, logger which logs into the output file
     * splits the input string into blocks with only "x/X" characters
     * counts the total amount of material in the input string
     *
     * @param input input string - contains only x/X, spaces and newline characters (\n or \r\n)
     * @param arguments
     * @throws FileNotFoundException thrown only when the user doesn't have permissions to write
     */
    public Scheduler(String input, Arguments arguments) throws FileNotFoundException {
        super("Scheduler");

        this.arguments = arguments;
        this.blocks = input.split("[(\\n|\\r\\n)\\s]+");
        this.workers = new Worker[arguments.workerCount()];
        this.workerThreads = new Thread[arguments.workerCount()];
        this.ship = new Ship(this, arguments.ferryCapacity());
        this.logger = new Logger(arguments.outputFile());
        this.truckManager = new TruckManager(this, ship, arguments.lorryCapacity(), arguments.lorryTime());

        for (String block : blocks) {
            materialCount += block.length();
        }

        talk("Input file: " + arguments.inputFile());
        talk("Output file: " + arguments.outputFile());
        talk("Worker count: " + arguments.workerCount());
        talk("Worker time: " + arguments.workerTime());
        talk("Truck capacity: " + arguments.lorryCapacity());
        talk("Truck travel time: " + arguments.lorryTime());
        talk("Ship capacity: " + arguments.ferryCapacity());

        talk("Block count: " + blocks.length);
        talk("Material count: " + materialCount);

        logger.log(this, String.format("Finished file analysis - block_count: %d - material_count: %d", blocks.length, materialCount));

        talk("initialized");
    }

    /**
     * creates worker threads with names: Worker#1, Worker#2, Worker#3, etc...
     */
    private void startThreads() {
        for (int i = 0; i < arguments.workerCount(); ++i) {
            workers[i] = new Worker("Worker#" + (i + 1), this, arguments.workerTime());
            workerThreads[i] = new Thread(workers[i]);
            workerThreads[i].start();
        }
    }

    private void joinThreads() throws InterruptedException {
        for (Thread t : workerThreads) {
            t.join();
        }

        truckManager.joinThreads();
    }

    @Override
    public void run() {

        startThreads();

        try {
            joinThreads();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ship.talk("Material transported: " + ship.getLoadedTotal());

        for(Worker worker : workers) {
            worker.talk("Total mined: " + worker.getWorkDone() + " material");
        }

        logger.destroy();
    }

    /**
     * assigns a block to a worker
     *
     * @param worker worker which the block is assigned to
     * @return string containing "x/X" characters == block
     *           or null == all blocks have been processed
     */
    public synchronized String getBlock(Worker worker) {
        worker.talk("trying to get a block");

        if (currentBlockIndex >= blocks.length) {
            worker.talk("done with work");
            return null;
        }

        return blocks[currentBlockIndex++];
    }

    public int getMaterialCount() {
        return materialCount;
    }

    public Logger getLogger() {
        return logger;
    }

    public TruckManager getTruckManager() {
        return truckManager;
    }
}
