package rw344.truckpacker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests that OutputWriter writes the packing result in the expected
 * format and reports files that cannot be written.
 */
class OutputWriterTest {

    /** JUnit creates a fresh empty folder for each test and deletes it afterwards. */
    @TempDir
    Path tempDir;

    /**
     * Builds a small, known result by hand: one truck carrying
     * 1 TV and 2 Radios, so every expected number is easy to check.
     * Done with the assistance of Claude
     */
    private Packer.PackResult sampleResult() {
        List<Packer.PackedItem> items = List.of(
                new Packer.PackedItem("TV", 1, 30, 500),
                new Packer.PackedItem("Radio", 2, 20, 200));
        Packer.TruckLoad load = new Packer.TruckLoad("T1", items, 50, 3);
        return new Packer.PackResult(List.of(load), 100, 700, 50, 3);
    }

    /**
     * The output file should list each truck, its items and the totals,
     * line by line in exactly this format.
     */
    @Test
    void write_validResult_writesExpectedFormat() throws IOException {
        // Arrange
        Path output = tempDir.resolve("output.txt");

        // Act
        new OutputWriter().write(output.toString(), sampleResult());

        // Assert
        List<String> expected = List.of(
                "Packed items:",
                "Truck T1 (volume=50, Items=3):",
                "- TV x1 (volume = 30, values =500)",
                "- Radio x2 (volume = 20, values =200)",
                "",
                "Total cost: 100",
                "Total items: 3",
                "Total volume used: 50",
                "Total value: 700");
        assertEquals(expected, Files.readAllLines(output));
    }

    /**
     * Writing to a path that is a directory (not a file) must fail
     * with an IOException rather than crash or silently do nothing.
     */
    @Test
    void write_pathIsDirectory_throwsIOException() {
        assertThrows(IOException.class,
                () -> new OutputWriter().write(tempDir.toString(), sampleResult()));
    }
}