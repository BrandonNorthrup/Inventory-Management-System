/*
Brandon Northrup
Student ID #001177877
Software I - Java - C482
*/

package inventory.management;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


// This class covers the initial page that you get after running the program - the actual inventory
public class Inventory implements Initializable {

    // Declare elements for the products table
    @FXML
    TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableColumn<Product, Integer> productID, productStock;

    @FXML
    private TableColumn<Product, Double> productPrice;

    @FXML
    private TextField productFilter;

    // Declare elements for the parts table
    @FXML
    TableView<Part>  partTable;

    @FXML
    private TableColumn<Part, String> partName;

    @FXML
    private TableColumn<Part, Integer> partID, partStock;

    @FXML
    private TableColumn<Part, Double> partPrice;

    @FXML
    private TextField partFilter;

    // Declare an array for parts
    ObservableList<Part> parts = FXCollections.observableArrayList();
    // Declare an array for products
    ObservableList<Product> products = FXCollections.observableArrayList();
    
    // As opposed to Outsourced
    Boolean madeInHouse;

    // Don't confuse this with currentPart - used for updating information
    Part selectedPart = null;

    Product selectedProduct = null;

    SortedList<Part> sortedData;

    // Initialize part and product tables upon loading the application
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productTable.getItems().setAll(products());
        productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productStock.setCellValueFactory(new PropertyValueFactory<>("productStock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        productTable.setOnMousePressed((MouseEvent event) -> {
            selectedProduct = productTable.getSelectionModel().getSelectedItem();
        });

        partTable.getItems().setAll(parts());
        partID.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partName.setCellValueFactory(new PropertyValueFactory<>("partName"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("partStock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        partTable.setOnMousePressed((MouseEvent event) -> {
            selectedPart = partTable.getSelectionModel().getSelectedItem();
        });
    }

    // Generate a part ID incrementally as they are added
    int generatePartID(){
        int i = 1;
        for(Part p: parts){
            if(p.getPartID() >= i){
                i = p.getPartID() + 1;
            }
        }
        return i;
    }

    // Action method for the ADD button in the Parts table
    @FXML
    void addPartButtonAction(ActionEvent event) {
        try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPart.fxml"));
                    Parent root = (Parent) loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Add Part");
                    stage.setScene(new Scene(root));

                    loader.<AddPartController>getController().setParentController(this);
                    AddPartController partInterface = loader.getController();
                    partInterface.setID(generatePartID());
                    partInterface.addPartLabel.setVisible(true);
                    partInterface.modifyPartLabel.setVisible(false);
                    stage.show();
        }
        catch (IOException e) {
                    Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // Action method for the MODIFY button in the Parts table
    @FXML
    void modifyPartButtonAction(ActionEvent event) {
        if (selectedPart != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPart.fxml"));
                Parent root = (Parent) loader.load();
                Stage stage = new Stage();
                stage.setTitle("Modify Part");
                stage.setScene(new Scene(root));

                loader.<AddPartController>getController().setParentController(this);
                AddPartController partInterface = loader.getController();
                partInterface.setData(selectedPart);
                partInterface.addPartLabel.setVisible(false);
                partInterface.modifyPartLabel.setVisible(true);
                stage.show();
            }
            catch (IOException e) {
                Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        else
        {
            Alert notification = new Alert(AlertType.ERROR);
            notification.setTitle("Error");
            notification.setHeaderText("Select a part to modify.");
            notification.setContentText(null);
            notification.showAndWait();
        }
    }

    // Action method for the DELETE button in the Parts table
    // Also takes care of any parts deleted through the Products table
    @FXML
    void deletePartAction(ActionEvent event) {
        if (selectedPart != null && selectedPart.getAssociatedPartID() == -1) {
            Alert notification = new Alert(AlertType.CONFIRMATION);
            notification.setTitle("Confirmation");
            notification.setHeaderText("Are you sure you want to delete part " + selectedPart.getPartID() + "?");
            notification.setContentText(null);
            Optional<ButtonType> result = notification.showAndWait();
            if (result.get() == ButtonType.OK) {
                    partTable.getItems().remove(selectedPart);
                    parts.remove(selectedPart);
            }
        }
        else if (selectedPart != null && selectedPart.getAssociatedPartID() != -1) {
            Alert notification = new Alert(AlertType.ERROR);
            notification.setTitle("Error");
            notification.setHeaderText("This part is associated with product " + selectedPart.associatedPartID + "\nThis item cannot be deleted before having the association removed.");
            notification.setContentText(null);
            Optional<ButtonType> result = notification.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
                    Parent root = (Parent) loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Modify Product");
                    stage.setScene(new Scene(root));

                    loader.<AddProductController>getController().setParentController(this);

                    products.stream().filter((Product product) -> selectedPart.associatedPartID == product.getProductID()).forEachOrdered((Product product) -> {
                        selectedProduct = product;
                    });

                    AddProductController productInterface = loader.getController();
                    productInterface.setData(selectedProduct);

                    parts.stream().filter((Part product) -> selectedProduct.getProductID() == product.getAssociatedPartID()).forEachOrdered((Part product) -> {
                        productInterface.currentProductParts.add(product);
                    });

                    productInterface.partTable.getItems().setAll(productInterface.currentProductParts);
                    stage.show();
                }
                catch (IOException e) {
                    Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            else {
                // Do nothing
            }
        }
        else {
            Alert notification = new Alert(AlertType.ERROR);
            notification.setTitle("Error");
            notification.setHeaderText("Select a part to delete.");
            notification.setContentText(null);
            notification.showAndWait();
        }
    }

    // Action method for the SEARCH button in the Parts table
    @FXML
    void searchPartButtonAction(ActionEvent event) {
        FilteredList<Part> filteredData = new FilteredList<>(parts, p -> true);
                    filteredData.setPredicate(Part -> {
                        if (partFilter.getText() == null || partFilter.getText().isEmpty()) {
                            return true;
                        }
                        String caseFilter = partFilter.getText().toLowerCase();
                            return Part.getPartName().toLowerCase().contains(caseFilter);
                        });
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(partTable.comparatorProperty());
        partTable.getItems().clear();
        partTable.getItems().setAll(sortedData);
    }

    // Generate a product ID incrementally as they are added
    int generateProductID(){
        int i = 1;
        for(Product p: products){
            if(p.getProductID() >= i){
                i = p.getProductID() + 1;
            }
        }
        return i;
    }

    // Action method for the ADD button in the Products table
    @FXML
    void addProductButtonAction(ActionEvent event){
        try {
                    FXMLLoader loader2 = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
                    Parent root2 = (Parent) loader2.load();
                    Stage stage2 = new Stage();
                    stage2.setTitle("Add Product");
                    stage2.setScene(new Scene(root2));

                    loader2.<AddProductController>getController().setParentController(this);
                    AddProductController productInterface = loader2.getController();
                    productInterface.setData(generateProductID());
                    productInterface.partTable2.getItems().addAll(parts);
                    productInterface.addProductLabel.setVisible(true);
                    productInterface.modifyProductLabel.setVisible(false);
                    stage2.show();
        }
        catch (IOException e) {
                    Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // Action method for the MODIFY button in the Products table
    @FXML
    void modifyProductButtonAction(ActionEvent event) {
        if (selectedProduct != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
                Parent root = (Parent) loader.load();
                Stage stage = new Stage();
                stage.setTitle("Modify Product");
                stage.setScene(new Scene(root));

                loader.<AddProductController>getController().setParentController(this);
                AddProductController productInterface = loader.getController();
                productInterface.setData(selectedProduct);

                parts.stream().filter((Part product) -> selectedProduct.getProductID() == product.getAssociatedPartID()).forEachOrdered((Part product) -> {
                    productInterface.currentProductParts.add(product);
                });

                productInterface.partTable.getItems().setAll(productInterface.currentProductParts);
                productInterface.partTable2.getItems().addAll(parts);
                productInterface.addProductLabel.setVisible(false);
                productInterface.modifyProductLabel.setVisible(true);
                productInterface.tempProductParts = productInterface.currentProductParts;
                stage.show();
            }
            catch (IOException e) {
                Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        else {
            Alert notification = new Alert(AlertType.ERROR);
            notification.setTitle("Error");
            notification.setHeaderText("Select a product to modify.");
            notification.setContentText(null);
            notification.showAndWait();
        }
    }

    // Action method for the DELETE button in the Products table
    @FXML
    void deleteProductSelected(ActionEvent event) {
        if (selectedProduct != null) {
            Alert notification = new Alert(AlertType.CONFIRMATION);
            notification.setTitle("Confirmation");
            notification.setHeaderText("Are you sure you want to delete product " + selectedProduct.getProductID() + "?");
            String warning = "Deleting this product will also delete all associated parts:";
                warning = parts.stream().filter((Part product) -> product.getAssociatedPartID() == selectedProduct.getProductID()).map((Part product) -> "\nProduct ID: " + product.getPartID()).reduce(warning, String::concat);
                notification.setContentText(warning);
            Optional<ButtonType> result = notification.showAndWait();
            if (result.get() == ButtonType.OK) {
                for (Iterator<Part> part2 = parts.iterator(); part2.hasNext();) {
                    Part product = part2.next();
                    if (product.getAssociatedPartID() == selectedProduct.getProductID()) {
                        part2.remove();
                        partTable.getItems().remove(product);
                    }
                }
                productTable.getItems().remove(selectedProduct);
                products.remove(selectedProduct);

                Alert notification2 = new Alert(AlertType.INFORMATION);
                notification2.setTitle("Done");
                notification2.setHeaderText("Product successfully deleted.");
                notification2.setContentText(null);
                notification2.showAndWait();
            }
        }
        else {
            Alert notification2 = new Alert(AlertType.ERROR);
            notification2.setTitle("Error");
            notification2.setHeaderText("Select a product to delete.");
            notification2.setContentText(null);
            notification2.showAndWait();
        }
    }

    // Action method for the SEARCH button in the Products table - uses a second SortedList so that both tables can be sorted at once
    @FXML
    void searchProductButtonAction(ActionEvent event) {
        FilteredList<Product> filteredData = new FilteredList<>(products, p -> true);
            filteredData.setPredicate(Product -> {
                if (productFilter.getText() == null || productFilter.getText().isEmpty()) {
                    return true;
                }
                String caseFilter = productFilter.getText().toLowerCase();
                return Product.getProductName().toLowerCase().contains(caseFilter);
        });
        SortedList<Product> sortedData2 = new SortedList<>(filteredData);
        sortedData2.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.getItems().clear();
        productTable.getItems().setAll(sortedData2);
    }

    // Ask for confirmation before closing the application
    @FXML
    void exitButtonAction(ActionEvent event){
            Alert notification = new Alert(AlertType.CONFIRMATION);
            notification.setTitle("Confirmation");
            notification.setHeaderText("Are you sure you want to exit?");
            notification.setContentText(null);
            Optional<ButtonType> result = notification.showAndWait();
            if(result.get() == ButtonType.OK){
                final Node source = (Node) event.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
            else if(result.get() == ButtonType.CANCEL){
                // Do nothing
            }
    }

    // Set the attributes of the parts that are loaded upon starting the application
    public ObservableList<Part> parts(){
        parts.add(new InHouse(1, "Part 1", 3, 61.99, 1, 10, 122) {});
        parts.add(new InHouse(2, "Part 2", 5, 24.82, 1, 10, 296) {});
        parts.add(new InHouse(3, "Part 3", 4, 96.87, 1, 10, 38) {});
        parts.add(new InHouse(4, "Part 4", 9, 54.56, 1, 10, 4029) {});
        parts.add(new Outsourced(5, "Part 5", 10, 13.98, 1, 10, "Tron") {});
        parts.add(new Outsourced(6, "Part 6", 1, 14.99, 1, 10, "Galaga") {});
        parts.add(new Outsourced(7, "Part 7", 1, 74.49, 1, 10, "Starfleet") {});
        parts.add(new Outsourced(8, "Part 8", 2, 16.49, 1, 10, "Veeshan") {});
        return parts;
    }

    // Set the attributes of the products that are added to the list
    public ObservableList<Product> products(){
        return products;
    }

    // Called by other methods to add a new part using the proper arguments
    public void addNewPart(int partID, String partName, int partStock, double partPrice, int partMin, int partMax, String companyNameOrMachineID){
        if(madeInHouse){
            int machineID = Integer.parseInt(companyNameOrMachineID);
            parts.add(new InHouse(partID, partName, partStock, partPrice, partMin, partMax, machineID) {});
        }
        else {
            parts.add(new Outsourced(partID, partName, partStock, partPrice, partMin, partMax, companyNameOrMachineID) {});
        }
        partTable.getItems().clear();
        partTable.getItems().setAll(parts);
    }

    // Called by other methods to add a new product using the proper arguments
    public void addNewProduct(int productID, String productName, int productStock, double productPrice, int productMin, int productMax){
        products.add(new Product(productID, productName, productStock, productPrice, productMin, productMax));
        productTable.getItems().clear();
        productTable.getItems().setAll(products);
    }
    
    // Called by other methods to update a part using the proper arguments
    void updatePart(int partID, String partName, int partStock, double partPrice, Part currentPart, int partMin, int partMax, String companyNameOrMachineID) {   
        if(currentPart instanceof InHouse){
            InHouse inHouse = (InHouse) currentPart;
            inHouse.setPartID(partID);
            inHouse.setPartName(partName);
            inHouse.setPartStock(partStock);
            inHouse.setPartPrice(partPrice);
            inHouse.setPartMin(partMin);
            inHouse.setPartMax(partMax);
            int machineID = Integer.parseInt(companyNameOrMachineID);
            inHouse.setMachineID(machineID);
        }
        else if(currentPart instanceof Outsourced){
            Outsourced outsourced = (Outsourced) currentPart;
            outsourced.setPartID(partID);
            outsourced.setPartName(partName);
            outsourced.setPartStock(partStock);
            outsourced.setPartPrice(partPrice);
            outsourced.setPartMin(partMin);
            outsourced.setPartMax(partMax);
            outsourced.setCompanyName(companyNameOrMachineID);
        }
        partTable.getItems().clear();
        partTable.getItems().setAll(parts);
    }

    // Called by other methods to update a product using the proper arguments
    void updateProduct(int productID, String productName, int productStock, double productPrice, Product selectedProduct, int productMin, int productMax) {
         selectedProduct.setProductID(productID);
         selectedProduct.setProductName(productName);
         selectedProduct.setProductPrice(productPrice);
         selectedProduct.setProductStock(productStock);
         selectedProduct.setProductMin(productMin);
         selectedProduct.setProductMax(productMax);
         productTable.getItems().clear();
         productTable.getItems().setAll(products);
    }
}