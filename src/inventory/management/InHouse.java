/*
Brandon Northrup
Student ID #001177877
Software I - Java - C482
*/

package inventory.management;

public class InHouse extends Part {
    
    private int machineID;
    
    public InHouse(int partID, String partName, int partStock, double partPrice, int partMin, int partMax, int machineID) {
        super(partID, partName, partStock, partPrice, partMin, partMax);
        this.machineID = machineID;
    }
    
    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}