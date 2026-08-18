package rw344.truckpacker;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

class Item {
    private final String name;
    private final int volume;
    private final int value;
    private final int quantity;

    @JsonCreator 
    public Item(@JsonProperty("name") String name, @JsonProperty("volume") int volume, @JsonProperty("value") int value, @JsonProperty("quantity") int quantity) {
                    this.name = name;
                    this.volume = volume;
                    this.value = value;
                    this.quantity = quantity;
    }
    //return various findings
    public String getName() {
        return name;
    }
    public int getVolume() {
        return volume;
    }
    public int getValue() {
        return value;
    }
    public int getQuantity() {
        return quantity;
    }
}