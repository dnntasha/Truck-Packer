package rw344.truckpacker;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * This writes a {@link Packer.PackResult} to a txt file 
 * with the list of trucks and what each delivered and the totals for the delivery
 */

public class OutputWriter {

    /**
     * Writes the packing result to the file at {@code path}.
     * @param path to write the output file
     * @param result of th epakcing
     * @throws IOException is the file cant be written
     */
    public void write(String path, Packer.PackResult result) throws IOException {
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println("Packed items:");
            for (Packer.TruckLoad load : result.truckLoads()) {
                writer.printf("Truck %s (volume=%d, Items=%d):%n",
                        load.truckId(), load.volume(), load.itemCount());
                for (Packer.PackedItem item : load.items()) {
                    writer.printf("- %s x%d (volume = %d, values =%d)%n", item.name(), item.quantity(), item.volume(), item.value());
                }
                writer.println();

            }
            //outputs the plain text of the totals to the output file

            writer.printf("Total cost: %d%n", result.totalCost());
            writer.printf("Total items: %d%n", result.totalCount());
            writer.printf("Total volume used: %d%n", result.totalVolume());
            writer.printf("Total value: %d%n", result.totalValue());
        }
    }
}