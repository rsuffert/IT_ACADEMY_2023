import java.security.InvalidParameterException;

/**
 * Represents the items that made up a specific transportation.
 */
public class Item {
    private String itemName;
    private double weight;
    private int quantity;

    public Item(String name, double weight, int quantity) throws InvalidParameterException {
        itemName = name;
        if (weight > 0) this.weight = weight;
        else throw new InvalidParameterException("Weight must be positive.");
        if (quantity > 0) this.quantity = quantity;
        else throw new InvalidParameterException("Quantity must be positive.");
    }

    /**
     * Tells the name of this item.
     * @return the name of the item.
     */
    public String getItemName() { return itemName; }

    /**
     * Tells the weight of this item.
     * @return the weight of this item.
     */
    public double getWeight() { return weight; }

    /**
     * Tells how many of this item have been added.
     * @return how many of this item have been added.
     */
    public int getQuantity() { return quantity; }
}