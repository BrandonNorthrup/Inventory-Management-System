/*
Brandon Northrup
Student ID #001177877
Software I - Java - C482
*/

package inventory.management;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    int associatedProductID;
    Part part;
    private int productID; // Auto-incremented
    private String productName;
    private int productStock; // Amount of items available - must be greater than or equal to productMin and lower than or equal to productMax
    private double productPrice;
    private int productMin;
    private int productMax;
    

    public Product(int productID, String productName, int productStock, double productPrice, int productMin, int productMax){
        this.productID = productID;
        this.productName = productName;
        this.productStock = productStock;
        this.productPrice = productPrice;
        this.productMin = productMin;
        this.productMax = productMax;
    }

    /*
    Prepare all of these methods for calling in other classes
    */

    public int getProductMin() {
        return productMin;
    }

    public void setProductMin(int productMin) {
        this.productMin = productMin;
    }

    public int getProductMax() {
        return productMax;
    }

    public void setProductMax(int productMax) {
        this.productMax = productMax;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
    
    public void addAssociatedPart(Part part) {
        this.part = part;
    }
    
    void setAssociatedPart(int associatedProductID) {
        this.associatedProductID = associatedProductID;
    }
    
    public boolean deleteAssociatedPart(Part selectedAsPart) {
        return false;
    }
    
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}