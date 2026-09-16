package rw344.truckpacker;

import java.io.IOException;
import java.io.PrintWriter;

public class OutputWriter {

    public void write(String path, Packer.PackResult result) throws IOException {
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println("Packed items:");
            for (Packer.PackedItem item : result.items()) {
                writer.printf("- %s x%d (volume=%d, value=%d)%n",
                        item.name(), item.quantity(), item.volume(), item.value());
            }
            writer.println();
            /**
             * outputs the plain text to the output file
             */
            writer.printf("Total items: %d%n", result.totalCount());
            writer.printf("Total volume used: %d%n", result.totalVolume());
            writer.printf("Total value: %d%n", result.totalValue());
        }
    }
}