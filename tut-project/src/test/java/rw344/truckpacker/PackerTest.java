package rw344.truckpacker;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the multi packing behaviour to minimise cost
 * and ensure all goods are delivered
 */
class PackerTest {

    private Path resource(String name) throws URISyntaxException {
        return Paths.get(getClass().getClassLoader().getResource(name).toURI());
    }

    /**
     * when 1 reuck is able to hold and deliver all the goods
     */
    @Test
    void singleTruckSufficient_allGoodsPackedInOneTruck() throws IOException, URISyntaxException {
        TruckConfig config = new InputLoader().load(resource("single_truck_sufficient.json").toString());
        Packer.PackResult result = new Packer().pack(config);

        assertFalse(result.isEmpty(), "Expected a valid packing to be found");
        assertEquals(1, result.truckLoads().size(), "Expected only one truck to be used");
        assertEquals(50, result.totalCost());
        assertEquals(1300, result.totalValue());
        assertEquals(70, result.totalVolume());
        assertEquals(5, result.totalCount());
    }

    /**
     * When goods don't fit in any single truck, they should be split
     * across multiple trucks, and the total cost should include every
     * truck actually used.
     */
    @Test
    void multipleTrucksRequired_goodsSplitAcrossTrucks() throws IOException, URISyntaxException {
        TruckConfig config = new InputLoader().load(resource("multi_truck_required.json").toString());
        Packer.PackResult result = new Packer().pack(config);

        assertFalse(result.isEmpty(), "Expected a valid packing to be found");
        assertEquals(2, result.truckLoads().size(), "Expected both trucks to be used");
        assertEquals(180, result.totalCost(), "Expected the cost of both trucks (80 + 100)");
        assertEquals(1300, result.totalValue());
        assertEquals(110, result.totalVolume());
        assertEquals(2, result.totalCount());

        for (Packer.TruckLoad load : result.truckLoads()) {
            assertTrue(load.volume() <= 60, "No truck should exceed its own volume limit");
            assertTrue(load.itemCount() <= 2, "No truck should exceed its own item limit");
        }
    }

    /**
     * Cheaper trucks should be preferred when they're capable of carrying
     * everything alone, confirming the packer minimises cost rather than
     * just finding any valid combination.
     */
    @Test
    void cheaperTruckPreferred_overMoreExpensiveAlternative() throws IOException, URISyntaxException {
        TruckConfig config = new InputLoader().load(resource("cheapest_truck_preferred.json").toString());
        Packer.PackResult result = new Packer().pack(config);

        assertFalse(result.isEmpty(), "Expected a valid packing to be found");
        assertEquals(1, result.truckLoads().size(), "Expected only the cheaper truck to be used");
        assertEquals("Cheaper", result.truckLoads().get(0).truckId());
        assertEquals(30, result.totalCost(), "Expected only the cheaper truck's cost");
    }

    /**
     * An item larger than every available truck's volume can never be
     * packed, regardless of how many trucks are available.
     */
    @Test
    void itemTooLargeForAnyTruck_noValidPackingFound() throws IOException, URISyntaxException {
        TruckConfig config = new InputLoader().load(resource("items_too_large_no_packing.json").toString());
        Packer.PackResult result = new Packer().pack(config);

        assertTrue(result.isEmpty(), "Expected no valid packing when no truck can fit the item");
    }

    // 3. Ill-formed input: should fail cleanly (IOException), not crash with an
    // unhandled runtime exception.
    @Test
    void illFormedInput_reportedAsIOException() throws URISyntaxException {
        InputLoader loader = new InputLoader();
        String path = resource("malformed.json").toString();

        assertThrows(IOException.class, () -> loader.load(path));
    }

    /**
     * Each packed item line should report the volume and the value for its whole
     * guantity
     */
    @Test
    void packedItems_reportVolumeAndValueForWholeQuantity() throws IOException, URISyntaxException {
        TruckConfig config = new InputLoader().load(resource("single_truck_sufficient.json").toString());
        Packer.PackResult result = new Packer().pack(config);

        List<Packer.PackedItem> expected = List.of(
                new Packer.PackedItem("TV", 2, 40, 1000),
                new Packer.PackedItem("Radio", 3, 30, 300));
        assertEquals(expected, result.truckLoads().get(0).items());
    }
}
