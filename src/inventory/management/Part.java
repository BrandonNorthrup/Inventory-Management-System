/*
Brandon Northrup
Student ID #001177877
Software I - Java - C482
*/

package inventory.management;

public abstract class Part {

    private int partID; // Auto-incremented
    private String partName;
    private int partStock; // Amount of items available - must be greater than or equal to partMin and lower than or equal to partMax
    private double partPrice;
    private int partMin;
    private int partMax;
    int associatedPartID = -1; // No parts need to be associated with a product - the user chooses what they want to have associated
    
    public Part(int partID, String partName, int partStock, double partPrice, int partMin, int partMax) {
        this.partID = partID;
        this.partName = partName;
        this.partStock = partStock;
        this.partPrice = partPrice;
        this.partMin = partMin;
        this.partMax = partMax;
    }

    /*
    Prepare all of these methods for calling in other classes
    */

    public int getPartID() {
        return partID;
    }

    public void setPartID(int partID) {
        this.partID = partID;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public int getPartStock() {
        return partStock;
    }

    public void setPartStock(int partStock) {
        this.partStock = partStock;
    }

    public double getPartPrice() {
        return partPrice;
    }

    public void setPartPrice(double partPrice) {
        this.partPrice = partPrice;
    }

    public int getPartMin() {
        return partMin;
    }

    public void setPartMin(int partMin) {
        this.partMin = partMin;
    }

    public int getPartMax() {
        return partMax;
    }

    public void setPartMax(int partMax) {
        this.partMax = partMax;
    }
    
    public int getAssociatedPartID() {
        return associatedPartID;
    }

    public void setAssociatedPartID(int associatedPartID) {
        this.associatedPartID = associatedPartID;
    }
}