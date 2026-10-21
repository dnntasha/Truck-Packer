package rw344.truckpacker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class InputLoaderTest {

    private Path resource(String name) throws URISyntaxException {
        return Paths.get(getClass().getClassLoader().getResource(name).toURI());
    }

    /**
     * Confirms YAML input is parsed correctly, not silently mishandled
     * Reuses the same known-optimal answer as the Json test on equivalent data.
     */
    @Test
    void yamlInput_parsesAndPacksCorrectly() throws IOException, URISyntaxException {
        TruckConfig config = new InputLoader().load(resource("valid_packing.yaml").toString());
        Packer.PackResult result = new Packer().pack(config);

        assertEquals(50, result.totalCost());
        assertEquals(2200, result.totalValue());
        assertEquals(130, result.totalVolume());
        assertEquals(8, result.totalCount());
    }

    /**
     * Malformed YAML should fail the same clean way malformed JSON does.
     */
    @Test
    void malformedYaml_reportedAsIOException() throws URISyntaxException {
        InputLoader loader = new InputLoader();
        String path = resource("malformed.yaml").toString();

        assertThrows(IOException.class, () -> loader.load(path));
    }

    /**
     * Unsupported extensions must fail with a clear, explicit error rather
     * than being misinterpreted as JSON or crashing with a Jackson parse exception.
     */
    @Test
    void unsupportedExtension_reportedAsIOException() throws URISyntaxException {
        InputLoader loader = new InputLoader();
        String path = resource("random.txt").toString();

        assertThrows(IOException.class, () -> loader.load(path));
    }

    /**
     * The shorter .yml extension must be accepted as YAML, just like .yaml.
     */
    @Test
    void ymlExtension_parsedAsYaml() throws IOException, URISyntaxException {
        TruckConfig config = new InputLoader().load(resource("valid_packing.yml").toString());

        assertEquals(1, config.getTrucks().size());
        assertEquals("T1", config.getTrucks().get(0).getId());
        assertEquals(3, config.getGoods().size());
    }
}
