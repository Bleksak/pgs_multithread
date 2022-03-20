public class Arguments {
    private String inputFile;
    private String outputFile;
    private int workerCount;
    private int workerTime;
    private int lorryCapacity;
    private int lorryTime;
    private int ferryCapacity;

    public Arguments(String... args) {
        if (args.length != 14) {
            throw new RuntimeException("Not enough launch arguments");
        }

        try {
            for (int i = 0; i < args.length; ++i) {
                switch (args[i].toLowerCase().trim()) {
                    case "-i": {
                        inputFile = args[++i];
                    }
                    break;

                    case "-o": {
                        outputFile = args[++i];
                    }
                    break;

                    case "-cworker": {
                        workerCount = Integer.parseInt(args[++i]);
                    }
                    break;

                    case "-tworker": {
                        workerTime = Integer.parseInt(args[++i]);
                    }
                    break;

                    case "-caplorry": {
                        lorryCapacity = Integer.parseInt(args[++i]);
                    }
                    break;

                    case "-tlorry": {
                        lorryTime = Integer.parseInt(args[++i]);
                    }
                    break;

                    case "-capferry": {
                        ferryCapacity = Integer.parseInt(args[++i]);
                    }
                    break;

                    default: {
                        throw new RuntimeException("Invalid argument provided");
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new RuntimeException("Not enough launch arguments");
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Integer expected");
        }
    }

    public String inputFile() {
        return inputFile;
    }

    public String outputFile() {
        return outputFile;
    }

    public int workerCount() {
        return workerCount;
    }

    public int workerTime() {
        return workerTime;
    }

    public int lorryCapacity() {
        return lorryCapacity;
    }

    public int lorryTime() {
        return lorryTime;
    }

    public int ferryCapacity() {
        return ferryCapacity;
    }
}
