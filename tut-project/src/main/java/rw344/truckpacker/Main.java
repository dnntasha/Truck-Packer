package rw344.truckpacker;

import java.io.IOException;

public class Main {
    /**
     * This runs the program and exits with a non-zero code on failure
     * @param args input file path, and an optional output file path
     */

    public static void main(String[] args) {
        int exitCode = run(args);
        if(exitCode != 0) {
            System.exit(exitCode);
        }
    }

    /**
     * Loads the input, packs the trucks and writes the output
     * they are kept separate from the main so it can be treated
     * without using the JVM
     * 
     * @param args inout file and an optional output file
     * @return 0 on success, 1 in an error
     */
    static int run(String[] args) {

        if (args.length < 1) {
            System.err.println("Usage: java -jar truck-packer-1.0.jar <input.json> [output.txt]");
            System.exit(1);
            return 1;
        }

        String inputPath = args[0];
        String outputPath = args.length > 1 ? args[1] : "output.txt";

        TruckConfig config;
        try {
            config = new InputLoader().load(inputPath);
        } catch (IOException e) {
            System.err.println("Error: could not read input file '" + inputPath + "': " + e.getMessage());
            return 1;
        }

        Packer.PackResult result = new Packer().pack(config);
        // error checker if no the truck combination couldn't do the job
        if (result.isEmpty()) {
            System.err.println("Error: no valid packing could be found for the given goods, "
                    + "and the available fleet of " + config.getTrucks().size() + " truck(s).");
            return 1;
        }

        try {
            new OutputWriter().write(outputPath, result);
            System.out.println("Packing written to " + outputPath);
        } catch (IOException e) {
            System.err.println("Error: could not write output file '" + outputPath + "': " + e.getMessage());
        }
    }
}