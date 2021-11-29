/*
Brandon Northrup
Student ID #001177877
Software I - Java - C482
*/

package inventory.management;

public class Outsourced extends Part {

    private String companyName;
    
    public Outsourced(int partID, String partName, int partStock, double partPrice, int partMin, int partMax, String companyName) {
        super(partID, partName, partStock, partPrice, partMin, partMax);
        this.companyName = companyName;
    }
        
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}