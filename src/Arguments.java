public class Arguments {
    private static String inputFile;
    private static String outputFile;
    private static int workerCount;
    private static int workerTime;
    private static int lorryCapacity;
    private static int lorryTime;
    private static int ferryCapacity;

    public static void load(String... args) {
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

    public static String inputFile() {
        return inputFile;
    }

    public static String outputFile() {
        return outputFile;
    }

    public static int workerCount() {
        return workerCount;
    }

    public static int workerTime() {
        return workerTime;
    }

    public static int lorryCapacity() {
        return lorryCapacity;
    }

    public static int lorryTime() {
        return lorryTime;
    }

    public static int ferryCapacity() {
        return ferryCapacity;
    }

    @Override
    public String toString() {
        return "Arguments[" +
                "inputFile=" + inputFile + ", " +
                "outputFile=" + outputFile + ", " +
                "workerCount=" + workerCount + ", " +
                "workerTime=" + workerTime + ", " +
                "lorryCapacity=" + lorryCapacity + ", " +
                "lorryTime=" + lorryTime + ", " +
                "ferryCapacity=" + ferryCapacity + ']';
    }
}
