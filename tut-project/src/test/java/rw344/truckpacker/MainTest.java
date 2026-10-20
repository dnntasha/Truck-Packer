package rw344.truckpacker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the command-line flow in Main: argument handling, every error
 * path, and successful runs with and without an output path.
 */
class MainTest {

    @TempDir
    Path tempDir;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private PrintStream originalErr;

    /** Redirect System.out/err so tests can check what was printed. */
    @BeforeEach
    void captureConsole() {
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    /** Put the real console back so later tests print normally. */
    @AfterEach
    void restoreConsole() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    private String resource(String name) throws URISyntaxException {
        return Paths.get(getClass().getClassLoader().getResource(name).toURI()).toString();
    }

    @Test
    void run_noArguments_printsUsageAndFails() {
        assertEquals(1, Main.run(new String[]{}));
        assertTrue(err.toString().contains("Usage:"));
    }

    @Test
    void run_inputFileMissing_reportsReadErrorAndFails() {
        assertEquals(1, Main.run(new String[]{"does_not_exist.json"}));
        assertTrue(err.toString().contains("could not read input file"));
    }

    @Test
    void run_itemsTooLarge_reportsNoPackingAndFails() throws URISyntaxException {
        String input = resource("items_too_large_no_packing.json");
        String output = tempDir.resolve("out.txt").toString();

        assertEquals(1, Main.run(new String[]{input, output}));
        assertTrue(err.toString().contains("no valid packing"));
        assertTrue(err.toString().contains("2 truck(s)"));
    }

    @Test
    void run_outputPathIsDirectory_reportsWriteErrorAndFails() throws URISyntaxException {
        String input = resource("single_truck_sufficient.json");

        assertEquals(1, Main.run(new String[]{input, tempDir.toString()}));
        assertTrue(err.toString().contains("could not write output file"));
    }

    @Test
    void run_validInputAndOutputPath_writesFileAndSucceeds() throws Exception {
        String input = resource("single_truck_sufficient.json");
        Path output = tempDir.resolve("out.txt");

        assertEquals(0, Main.run(new String[]{input, output.toString()}));
        assertEquals("Packed items:", Files.readAllLines(output).get(0));
        assertTrue(out.toString().contains("Packing written to " + output));
    }

    @Test
    void run_noOutputPath_writesDefaultOutputFile() throws Exception {
        Path defaultOutput = Path.of("output.txt");
        try {
            assertEquals(0, Main.run(new String[]{resource("single_truck_sufficient.json")}));
            assertTrue(Files.exists(defaultOutput));
        } finally {
            Files.deleteIfExists(defaultOutput);   // clean up even if an assert fails
        }
    }

    @Test
    void main_validInput_writesOutputWithoutExiting() throws Exception {
        Path output = tempDir.resolve("out.txt");
        Main.main(new String[]{resource("single_truck_sufficient.json"), output.toString()});
        assertTrue(Files.exists(output));
    }
}