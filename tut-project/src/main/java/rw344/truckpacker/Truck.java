package rw344.truckpacker;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Truck {
    private final String id;
    private final int volume;
    private final int maxItems;
    private final int cost;

    /**
     * Create the truck from the input-file fields
     * @param id unique truck identofyer
     * @param volume volume of the truck
     * @param maxItems maximum number of items the truck can carry
     * @param cost cost of using the truck for delivery
     */
    public Truck(@JsonProperty("id") String id, @JsonProperty("volume") int volume, @JsonProperty("maxItems") int maxItems, @JsonProperty("cost") int cost) {
        this.id = id;
        this.volume = volume;
        this.maxItems = maxItems;
        this.cost = cost;
    }

    /**
     * @return the truck identifier
     */
    public String getId() {
        return id;
    }

    /**
     * @return the capaacity of the truck
     */
    public String getVolume() {
        return volume;
    }

    /**
     * @return the maximum no. of items this truck can carry
     */
    public String getMaxItems() {
        return maxItems;
    }

    /**
     * @return the cost if this truck is used
     */
    public String getCost() {
        return cost;
    }

}