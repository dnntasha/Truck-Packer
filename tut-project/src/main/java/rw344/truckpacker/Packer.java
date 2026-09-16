package rw344.truckpacker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The goods are packed across the available trucks, 
 * also minimising delivery cost and ensures all items are transported
 */

public class Packer {

    public record PackedItem(String name, int quantity, int volume, int value) {
    }
    
    /**
     * items loaded into one truck and its totals(volume and count)
     */
    public record TruckLoad(String truckId, List<PackedItem> items, int volume, int itemCount) {
    }
    /**
     * the overall result of the packing, trucks used, items packed and totals across the deliveries
     */
    public record PackResult(List<TruckLoad> truckLoads, int totalCost, int totalValue, int totalVolume, int totalCount) {
        public boolean isEmpty() {
            return truckLoads.isEmpty();
        }
    }
    /**
     * Aided by Claude AI to design the packing algorithm 
     * a First-Fit-Decreasing bin-packing by sorting 
     * the cost(ascending) and item volume (descending)
     * while minimising cost
     */
    public PackResult pack(TruckConfig config) {
        List<Item> units = flatten(config.getGoods());

	    List<Truck> trucksByCost = new ArrayList<>(config.getTrucks());
	    trucksByCost.sort(Comparator.comparingInt(Truck::getCost));

	    List<Item> unitsByVolumeDesc = new ArrayList<>(units);
	    unitsByVolumeDesc.sort(Comparator.comparingInt(Item::getVolume).reversed());

	    Map<Truck, Integer> remainingVolume = new LinkedHashMap<>();
   	    Map<Truck, Integer> remainingItem = new LinkedHashMap<>();
	    Map<Truck, List<Item>> loaded = new LinkedHashMap<>();
        for(Truck truck : trucksByCost) {
            remainingVolume.put(truck,truck.getVolume());
            remainingItem.put(truck, truck.getMaxItems());
            loaded.put(truck, new ArrayList<>());
        }
        /**
         * Places each Item int the cheapest truck according to cost that still has room 
         * largest stored first
         */
        for (Item unit : unitsByVolumeDesc) {
            Truck chosen = null;
            for (Truck truck : trucksByCost) {
                if (remainingVolume.get(truck) >= unit.getVolume() && remainingItem.get(truck) >= 1) {
                    chosen = truck;
                    break;
                }
            }
            if (chosen == null) {
                return emptyResult();
            }
            loaded.get(chosen).add(unit);
            remainingVolume.put(chosen, remainingVolume.get(chosen) - unit.getVolume());
            remainingItem.put(chosen, remainingItem.get(chosen) - 1);
        }

        return buildResult(trucksByCost, loaded);
    }

    private static List<Item> flatten(List<Item> goods) {
        List<Item> units = new ArrayList<>();
        for (Item good : goods) {
            for (int i = 0; i < good.getQuantity(); i++) {
                units.add(good);
            }
        }
        return units;
    }
    
    private PackResult buildResult(List<Truck> trucksByCost, Map<Truck, List<Item>> loaded) {
                List<TruckLoad> truckLoads = new ArrayList<>();
        int totalCost = 0, totalValue = 0, totalVolume = 0, totalCount = 0;

        for (Truck truck : trucksByCost) {
            List<Item> items = loaded.get(truck);
            if (items.isEmpty()) {
                continue; 
            }

            Map<String, int[]> grouped = new LinkedHashMap<>();
            for (Item unit : items) {
                grouped.computeIfAbsent(unit.getName(),
                        name -> new int[]{0, unit.getVolume(), unit.getValue()})[0]++;
            }

            List<PackedItem> packedItems = new ArrayList<>();
            int truckVolume = 0, truckCount = 0, truckValue = 0;
            for (Map.Entry<String, int[]> entry : grouped.entrySet()) {
                int count = entry.getValue()[0];
                int unitVolume = entry.getValue()[1];
                int unitValue = entry.getValue()[2];
                packedItems.add(new PackedItem(entry.getKey(), count, unitVolume * count, unitValue * count));
                truckVolume += unitVolume * count;
                truckCount += count;
                truckValue += unitValue * count;
            }

            truckLoads.add(new TruckLoad(truck.getId(), packedItems, truckVolume, truckCount));
            totalCost += truck.getCost();
            totalValue += truckValue;
            totalVolume += truckVolume;
            totalCount += truckCount;
        }

        return new PackResult(truckLoads, totalCost, totalValue, totalVolume, totalCount);
    }
    private PackResult emptyResult() {
        return new PackResult(new ArrayList<>(),0,0,0,0);
    }
     
}
