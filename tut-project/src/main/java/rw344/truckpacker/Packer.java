package rw344.truckpacker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Packer {

    public record PackedItem(String name, int quantity, int volume, int value) {
    }

    public record PackResult(List<PackedItem> items, int totalValue, int totalVolume, int totalCount) {
        public boolean isEmpty() {
            return items.isEmpty();
        }
    }

    public PackResult pack(TruckConfig config) {
        List<Item> units = flatten(config.getGoods());

        int n = units.size();
        int maxItems = config.getMaxItems();
        int truckVolume = config.getTruckVolume();

        int[][][] dp = new int[n + 1][maxItems + 1][truckVolume + 1];
        boolean[][][] take = new boolean[n + 1][maxItems + 1][truckVolume + 1];

        for (int i = 1; i <= n; i++) {
            Item u = units.get(i - 1);
            for (int k = 0; k <= maxItems; k++) {
                for (int v = 0; v <= truckVolume; v++) {
                    /**
                     * Default is not to take unit i.
                     */
                    dp[i][k][v] = dp[i - 1][k][v];

                    // Option: take unit i, if there's item-count and volume budget left.
                    if (k >= 1 && v >= u.getVolume()) {
                        int candidate = dp[i - 1][k - 1][v - u.getVolume()] + u.getValue();
                        if (candidate > dp[i][k][v]) {
                            dp[i][k][v] = candidate;
                            take[i][k][v] = true;
                        }
                    }
                }
            }
        }

        /**
         * The optimum need not use the full item or volume budget, so scan for it.
         */
        int bestK = 0, bestV = 0, bestValue = 0;
        for (int k = 0; k <= maxItems; k++) {
            for (int v = 0; v <= truckVolume; v++) {
                if (dp[n][k][v] > bestValue) {
                    bestValue = dp[n][k][v];
                    bestK = k;
                    bestV = v;
                }
            }
        }

        return reconstruct(units, take, n, bestK, bestV, bestValue);
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
    
    private static PackResult reconstruct(List<Item> units, boolean[][][] take,
                                           int startI, int startK, int startV, int bestValue) {
        /**
         * name -> {count, unitVolume, unitValue} and aided by claude.ai for the logic of a 3d 'table'
         */
        Map<String, int[]> selection = new LinkedHashMap<>();

        int i = startI, k = startK, v = startV;
        while (i > 0) {
            if (take[i][k][v]) {
                Item u = units.get(i - 1);
                selection.computeIfAbsent(u.getName(),
                        name -> new int[]{0, u.getVolume(), u.getValue()})[0]++;
                k--;
                v -= u.getVolume();
            }
            i--;
        }

        List<PackedItem> result = new ArrayList<>();
        int totalVolume = 0, totalCount = 0;
        for (Map.Entry<String, int[]> e : selection.entrySet()) {
            int count = e.getValue()[0];
            int unitVolume = e.getValue()[1];
            int unitValue = e.getValue()[2];
            result.add(new PackedItem(e.getKey(), count, unitVolume * count, unitValue * count));
            totalVolume += unitVolume * count;
            totalCount += count;
        }

        return new PackResult(result, bestValue, totalVolume, totalCount);
    }
}