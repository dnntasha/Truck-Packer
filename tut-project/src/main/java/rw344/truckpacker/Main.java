package rw344.truckpacker;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar truck-packer-1.0.jar <input.json> [output.txt]");
            System.exit(1);
            return;
        }

        String inputPath = args[0];
        String outputPath = args.length > 1 ? args[1] : "output.txt";

        TruckConfig config;
        try {
            config = new InputLoader().load(inputPath);
        } catch (IOException e) {
            System.err.println("Error: could not read input file '" + inputPath + "': " + e.getMessage());
            return;
        }

        Packer.PackResult result = new Packer().pack(config);
        /**
         * error checker
         */
        if (result.isEmpty()) {
            System.err.println("Error: no valid packing could be found for the given goods, " + "and the available fleet of " + config.getTrucks().size() + "truck(s).");
            return;
        }

        try {
            new OutputWriter().write(outputPath, result);
            System.out.println("Packing written to " + outputPath);
        } catch (IOException e) {
            System.err.println("Error: could not write output file '" + outputPath + "': " + e.getMessage());
        }
    }
}