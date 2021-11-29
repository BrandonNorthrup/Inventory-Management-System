/*
Brandon Northrup
Student ID #001177877
Software I - Java - C482
*/

package inventory.management;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

// This class covers the changes made to product data
public class AddProductController implements Initializable {

    // Declare elements to be included in the Product windows
    @FXML private TextField productPriceField, productMinField,  productMaxField, productNameTextField, productStockField, productIDTextField;
    @FXML Label addProductLabel, modifyProductLabel;

    // Declare elements to be included in the two tables (one temporary, one not) that are within the Product window
    @FXML
    TableView<Part>  partTable, partTable2;

    @FXML
    private TableColumn<Part, Integer> partID, partStock, partID2, partStock2;

    @FXML
    private TableColumn<Part, Double> partPrice, partPrice2;

    @FXML
    private TableColumn<Part, String> partName, partName2;

    // Reference the primary controller
    private Inventory mainInterfaceController;

    // Reference the Product class
    Product selectedProduct;

    // Reference the Part class
    Part selectedPart;

    // editData must be true before something will be changed
    Boolean editData = false;
    
    // As opposed to Outsourced
    Boolean madeInHouse;
    
    @FXML
    private TextField existingPartFilter;
    
    SortedList<Part> sortedExistingParts;

    // Declare an array for parts of a product that are to be added after saving, as well as parts that have already been added
    ObservableList<Part> productParts = FXCollections.observableArrayList();
    ObservableList<Part> currentProductParts = FXCollections.observableArrayList();
    
    // Used for search method in Add/Modify Product windows
    ObservableList<Part> existingParts = FXCollections.observableArrayList();
    
    // Used to store changes made to a product's part list before saving
    ObservableList<Part> tempProductParts = FXCollections.observableArrayList();

    // Action method for the Modify button within the Product window
    @FXML
    void productPartModifyAction(ActionEvent event) {
        if (selectedPart != null) {
            if (selectedPart == partTable2.getSelectionModel().getSelectedItem()){
                Alert notification = new Alert(Alert.AlertType.ERROR);
                notification.setTitle("Error");
                notification.setHeaderText("Please select a part from the associated parts table if you want to modify.");
                notification.setContentText(null);
                notification.showAndWait();
            }
            else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProductPart.fxml"));
                    Parent root;
                    root = (Parent) loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Modify Product Part");
                    stage.setScene(new Scene(root));
                
                    loader.<AddProductPartController>getController().setParentController(this);
                    AddProductPartController productInterface = loader.getController();
                    productInterface.setPart(selectedPart);
                    productInterface.addPartLabel.setVisible(false);
                    productInterface.modifyPartLabel.setVisible(true);
                    stage.show();
                }
                catch (IOException e) {
                    Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        else {
            Alert notification = new Alert(Alert.AlertType.ERROR);
            notification.setTitle("Error");
            notification.setHeaderText("Select a part to modify.");
            notification.setContentText(null);
            notification.showAndWait();
        }
    }

    // Action method for the Delete button within the Product window
    @FXML
    void productPartDeleteAction(ActionEvent event) {
        if (selectedPart != null) {
            Alert notification = new Alert(Alert.AlertType.CONFIRMATION);
            notification.setTitle("Confirmation");
            notification.setHeaderText("Are you sure you want to delete part " + selectedPart.getPartID() + "?");
            notification.setContentText(null);
            Optional<ButtonType> result = notification.showAndWait();

            if (result.get() == ButtonType.OK) {
                partTable.getItems().remove(selectedPart);
                productParts.remove(selectedPart);
                mainInterfaceController.parts.remove(selectedPart);
                mainInterfaceController.partTable.getItems().remove(selectedPart);
            }
        }
        else {
            Alert notification = new Alert(Alert.AlertType.ERROR);
            notification.setTitle("Error");
            notification.setHeaderText("Select a part to delete.");
            notification.setContentText(null);
            notification.showAndWait();
        }
    }

    // Action method for the Remove Association button within the Product window
    @FXML
    void productPartRemoveAssociationAction(ActionEvent event) {
        if (selectedPart != null) {
            Alert notification = new Alert(Alert.AlertType.CONFIRMATION);
            notification.setTitle("Confirmation");
            notification.setHeaderText("Are you sure you want to remove the association of part " + selectedPart.getPartID() + " from this product?");
            notification.setContentText(null);
            Optional<ButtonType> result = notification.showAndWait();
            if (result.get() == ButtonType.OK) {
                    tempProductParts.remove(selectedPart);
                    partTable.getItems().remove(selectedPart);
                    selectedPart.setAssociatedPartID(-1);
            Alert notification2 = new Alert(Alert.AlertType.INFORMATION);
            notification2.setTitle("Information");
            notification2.setHeaderText("If you would like to add this part again, you can find it in the parts list.");
            notification2.setContentText(null);
            notification2.showAndWait();
            }
        }
        else {
            Alert notification = new Alert(Alert.AlertType.ERROR);
            notification.setTitle("Error");
            notification.setHeaderText("Select a part to remove its association to this product.");
            notification.setContentText(null);
            notification.showAndWait();
        }
    }

    // Action method for the CREATE NEW PART button within the Product window
    @FXML
    private void addAssociatedProductPart(ActionEvent event) {
        try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProductPart.fxml"));
                    Parent root = (Parent) loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Add Product Part");
                    stage.setScene(new Scene(root));

                    loader.<AddProductPartController>getController().setParentController(this);
                    AddProductPartController productInterface = loader.getController();
                    productInterface.setID(mainInterfaceController.generatePartID());
                    productInterface.setAssociatedPart(Integer.parseInt(productIDTextField.getText()));
                    productInterface.addPartLabel.setVisible(true);
                    productInterface.modifyPartLabel.setVisible(false);
                    stage.show();
        }
        catch (IOException e) {
                    Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    // Action method for the SEARCH button in the Product window
    @FXML
    void searchExistingPartButton(ActionEvent event) {
        existingParts = mainInterfaceController.parts;
        FilteredList<Part> filteredData = new FilteredList<>(existingParts, p -> true);
                    filteredData.setPredicate(Part -> {
                        if (existingPartFilter.getText() == null || existingPartFilter.getText().isEmpty()) {
                            return true;
                        }
                        String caseFilter = existingPartFilter.getText().toLowerCase();
                            return Part.getPartName().toLowerCase().contains(caseFilter);
                        });
        sortedExistingParts = new SortedList<>(filteredData);
        sortedExistingParts.comparatorProperty().bind(partTable2.comparatorProperty());
        partTable2.getItems().clear();
        partTable2.getItems().setAll(sortedExistingParts);
    }
    
    // Action method for picking a part from the existing parts lists and adding it to the product
    @FXML
    void addExistingProductPartButton(ActionEvent event) {
        if(selectedPart != null) {
            productIDTextField.getText();
            tempProductParts.add(selectedPart);
            partTable.getItems().add(selectedPart);
        }
        else {
            Alert notification = new Alert(Alert.AlertType.ERROR);
            notification.setTitle("Error");
            notification.setHeaderText("Please select a part to add.");
            notification.setContentText(null);
            notification.showAndWait();
        }
    }
    
    // Action method for the Save button within the Product window
    @FXML
    void saveProductButtonAction(ActionEvent event){
        currentProductParts = tempProductParts;
        currentProductParts.forEach((part) -> {
            part.setAssociatedPartID(Integer.parseInt(productIDTextField.getText()));
        });

        if (!productNameTextField.getText().isEmpty() && !productStockField.getText().isEmpty() && !productPriceField.getText().isEmpty() && !productMinField.getText().isEmpty() && !productMaxField.getText().isEmpty()) {
            if(productParts.isEmpty() == false || currentProductParts.isEmpty() == false){
                if (Integer.parseInt(productMaxField.getText()) >= Integer.parseInt(productMinField.getText())) {
                    if (Integer.parseInt(productMaxField.getText()) >= Integer.parseInt(productStockField.getText())) {
                        if (editData == true) {
                            mainInterfaceController.updateProduct(
                                Integer.parseInt(productIDTextField.getText()),
                                productNameTextField.getText(),
                                Integer.parseInt(productStockField.getText()),
                                Double.parseDouble(productPriceField.getText()),
                                selectedProduct,
                                Integer.parseInt(productMinField.getText()),
                                Integer.parseInt(productMaxField.getText())
                                );

                            Alert notification = new Alert(Alert.AlertType.INFORMATION);
                            notification.setTitle("Done");
                            notification.setHeaderText("Updated product successfully.");
                            notification.setContentText(null);
                            notification.showAndWait();
                        }
                        else {
                                mainInterfaceController.addNewProduct(
                                Integer.parseInt(productIDTextField.getText()),
                                productNameTextField.getText(),
                                Integer.parseInt(productStockField.getText()),
                                Double.parseDouble(productPriceField.getText()),
                                Integer.parseInt(productMinField.getText()),
                                Integer.parseInt(productMaxField.getText())
                                );
                            Alert notification = new Alert(Alert.AlertType.INFORMATION);
                            notification.setTitle("Done");
                            notification.setHeaderText("Product added successfully.");
                            notification.setContentText(null);
                            notification.showAndWait();
                        }
                        final Node source = (Node) event.getSource();
                        final Stage stage = (Stage) source.getScene().getWindow();
                        stage.close();
                    }
                    else {
                        Alert notification = new Alert(Alert.AlertType.ERROR);
                        notification.setTitle("Error");
                        notification.setHeaderText("Max value cannot be lower than min value.");
                        notification.setContentText(null);
                        notification.showAndWait();
                    }
                }
                else {
                    Alert notification = new Alert(Alert.AlertType.ERROR);
                    notification.setTitle("Error");
                    notification.setHeaderText("Max value cannot be lower than min value.");
                    notification.setContentText(null);
                    notification.showAndWait();
                }
            }
            else {
                Alert notification = new Alert(Alert.AlertType.ERROR);
                notification.setTitle("Error");
                notification.setHeaderText("Each product must have at least one part.");
                notification.setContentText(null);
                notification.showAndWait();
            }
        }
        else {
            Alert notification = new Alert(Alert.AlertType.ERROR);
            notification.setTitle("Error");
            notification.setHeaderText("All fields must be filled out.");
            notification.setContentText(null);
            notification.showAndWait();
        }
    }

    // Set the primary controller to be the parent of the product controller
    void setParentController(Inventory mainInterfaceController) {
        this.mainInterfaceController = mainInterfaceController;
    }
    
    // Initializes the two tables in the Product window and allows you to select the items inside of them
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partID.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partName.setCellValueFactory(new PropertyValueFactory<>("partName"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("partStock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        partTable.setOnMousePressed((MouseEvent event) -> {
            partTable2.getSelectionModel().clearSelection();
            selectedPart = partTable.getSelectionModel().getSelectedItem();
        });
        
        partID2.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partName2.setCellValueFactory(new PropertyValueFactory<>("partName"));
        partStock2.setCellValueFactory(new PropertyValueFactory<>("partStock"));
        partPrice2.setCellValueFactory(new PropertyValueFactory<>("partPrice"));
        partTable2.setOnMousePressed((MouseEvent event) -> {
            partTable.getSelectionModel().clearSelection();
            selectedPart = partTable2.getSelectionModel().getSelectedItem();
        });
        
        tempProductParts = currentProductParts;
    }    

    // Adds or changes the data for a product after saving - sets everything to a text value
    void setData(Product selectedProduct) {
        productIDTextField.setText("" + selectedProduct.getProductID());
        productNameTextField.setText(selectedProduct.getProductName());
        productStockField.setText("" + selectedProduct.getProductStock());
        productPriceField.setText("" + selectedProduct.getProductPrice());
        productMinField.setText("" + selectedProduct.getProductMin());
        productMaxField.setText("" + selectedProduct.getProductMax());
        this.selectedProduct = selectedProduct;
        editData = true;
    }

    // Retrieves an incrementally-generated ID and applies it to the product
    void setData(int generateProductID) {
        productIDTextField.setText("" + generateProductID);
    }

    // Called by other methods to add a new part to the temporary table using the proper arguments. Also adds the new part to the Inventory.
    void addToTempProductTableView(int partID, String partName, int partStock, double partPrice, int partMin, int partMax, String companyNameOrMachineID){
        if(madeInHouse){
            mainInterfaceController.madeInHouse = true;
            int machineID = Integer.parseInt(companyNameOrMachineID);
            tempProductParts.add(new InHouse(partID, partName, partStock, partPrice, partMin, partMax, machineID) {});
        }
        else {
            mainInterfaceController.madeInHouse = false;
            tempProductParts.add(new Outsourced(partID, partName, partStock, partPrice, partMin, partMax, companyNameOrMachineID) {});
        }
        mainInterfaceController.addNewPart(partID, partName, partStock, partPrice, partMin, partMax, companyNameOrMachineID);
        partTable2.getItems().clear();
        partTable2.getItems().setAll(mainInterfaceController.parts);
    }

    // Called by other methods to update a part using the proper arguments
    void updatePart(int partID, String partName, int partStock, double partPrice, Part currentPart, int partMin, int partMax, String companyNameOrMachineID) {
        if(selectedPart instanceof InHouse){
            madeInHouse = true;
            InHouse inHouse = (InHouse) selectedPart;
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
            madeInHouse = false;
            Outsourced outsourced = (Outsourced) selectedPart;
            outsourced.setPartID(partID);
            outsourced.setPartName(partName);
            outsourced.setPartStock(partStock);
            outsourced.setPartPrice(partPrice);
            outsourced.setPartMin(partMin);
            outsourced.setPartMax(partMax);
            outsourced.setCompanyName(companyNameOrMachineID);
        }
        mainInterfaceController.updatePart(partID, partName, partStock, partPrice, currentPart, partMin, partMax, companyNameOrMachineID);
        partTable2.getItems().clear();
        partTable2.getItems().setAll(mainInterfaceController.parts);
        partTable.getItems().clear();
        partTable.getItems().setAll(tempProductParts);
    }

    // Ask for confirmation before closing the Product window
    @FXML
    void closeButtonAction(ActionEvent event){
            Alert notification = new Alert(Alert.AlertType.CONFIRMATION);
            notification.setTitle("Confirmation");
            notification.setHeaderText("Are you sure you want to cancel?");
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
}