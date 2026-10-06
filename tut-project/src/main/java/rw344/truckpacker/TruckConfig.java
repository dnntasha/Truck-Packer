package rw344.truckpacker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Holds the trucks that are avaiable and the goods to be delivered
 */
public class TruckConfig {
    private final List<Truck> trucks;
    private final List<Item> goods;

    /**
     * reads from the input file
     * the capacity, maximum no. allowed on board and available goods
     * 
     * @param trucks the fleet of the trucks available for the delivery
     * @param goods  that need to be packed and transported
     */
    @JsonCreator
    public TruckConfig(@JsonProperty("trucks") List<Truck> trucks,
            @JsonProperty("goods") List<Item> goods) {
        this.trucks = trucks;
        this.goods = goods;
    }

    /**
     * @return the trucks available to deliver
     */
    public List<Truck> getTrucks() {
        return trucks;
    }

    /**
     * @return the goods that need to be packed and transported
     */
    public List<Item> getGoods() {
        return goods;
    }
}
