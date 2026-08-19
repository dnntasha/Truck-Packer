package rw344.truckpacker;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PackerTest {

    private Path resource(String name) throws URISyntaxException {
        return Paths.get(getClass().getClassLoader().getResource(name).toURI());
    }

    // 1. Well-formed input, valid packing exists.
    @Test
    void wellFormedInput_validPackingFound() throws IOException, URISyntaxException {
        TruckConfig config = new InputLoader().load(resource("valid_packing.json").toString());
        Packer.PackResult result = new Packer().pack(config);

        assertFalse(result.isEmpty(), "Expected at least one item to be packed");
        assertTrue(result.totalVolume() <= config.getTruckVolume(),
                "Packed volume must not exceed the truck's volume");
        assertTrue(result.totalCount() <= config.getMaxItems(),
                "Packed item count must not exceed the item cap");
    }

    // Extra: confirms the DP finds the actual optimum, not just *a* valid packing.
    // Hand-worked answer for valid_packing.json (truckVolume 50, maxItems 3):
    // best combination is 2x TV (20 vol/500 val each) + 1x Radio (10 vol/100 val)
    // = 3 items, 50 volume, 1100 value.
    @Test
    void packsExpectedOptimalValue() throws IOException, URISyntaxException {
        TruckConfig config = new InputLoader().load(resource("valid_packing.json").toString());
        Packer.PackResult result = new Packer().pack(config);

        assertEquals(1100, result.totalValue());
        assertEquals(50, result.totalVolume());
        assertEquals(3, result.totalCount());
    }

    // 2. Well-formed input, but no valid packing can be determined.
    @Test
    void wellFormedInput_noValidPackingFound() throws IOException, URISyntaxException {
        TruckConfig config = new InputLoader().load(resource("no_valid_packing.json").toString());
        Packer.PackResult result = new Packer().pack(config);

        assertTrue(result.isEmpty(), "Expected no items to fit within the truck's volume");
    }

    // 3. Ill-formed input: should fail cleanly (IOException), not crash with an
    //    unhandled runtime exception.
    @Test
    void illFormedInput_reportedAsIOException() throws URISyntaxException {
        InputLoader loader = new InputLoader();
        String path = resource("malformed.json").toString();

        assertThrows(IOException.class, () -> loader.load(path));
    }
}
