package rw344.truckpacker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TruckConfig {
    private final int truckVolume; 
    private final int maxItems;
    private final List<Item> goods;
	//reads from the input file
    @JsonCreator
//the capacity, maximum no. allowed on board and available goods
    public TruckConfig(@JsonProperty("truckVolume") int truckVolume, 
                        @JsonProperty("maxItems") int maxItems,
                        @JsonProperty("goods") List<Item> goods) {
        this.truckVolume = truckVolume;
        this.maxItems = maxItems;
        this.goods = goods;
    }
//returns the values/list
    public int getTruckVolume() {
        return truckVolume;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public List<Item> getGoods() {
        return goods;
    }
}
