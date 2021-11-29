/*
Brandon Northrup
Student ID #001177877
Software I - Java - C482
*/

package inventory.management;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// This class covers the changes made to the data for parts that are to be associated with a product
// Many of the methods used in this class are similar or exactly the same as those in AddPartController
public class AddProductPartController implements Initializable {

    // Declare elements to be included in the Product Part window
    @FXML private TextField companyNameOrMachinePartIDField, partPriceField, partMinField,  partMaxField, partNameTextField, partStockField, partIDTextField;
    @FXML private RadioButton madeInHouseButton, outsourcedButton;
    @FXML private Label companyNamePartLabel, machinePartIDLabel;
    @FXML Label addPartLabel, modifyPartLabel;

    // editData must be true before something will be changed
    Boolean editData = false;
    
    // As opposed to Outsourced
    Boolean madeInHouse = true;

    // Reference the Part class
    Part currentPart;

    // Reference the Product controller - note that this class does not reference the primary (Inventory) controller
    private AddProductController addProductController;

    // No parts need to be associated with a product - the user chooses what they want to have associated
    int associatedProductID = -1;

    // Action method for the Outsourced radio button
    // Uses visibility to show or hide different elements
    @FXML
    void outsourcedButtonAction(ActionEvent event) {
        if (currentPart instanceof InHouse){
            Alert notification = new Alert(Alert.AlertType.ERROR);
            notification.setTitle("Error");
            notification.setHeaderText("An in house part cannot be changed to an outsourced part. Please create a new part.");
            notification.setContentText(null);
            notification.showAndWait();
            madeInHouseButton.setSelected(true);
            outsourcedButton.setSelected(false);
        }
        else {
            companyNamePartLabel.setVisible(true);
            machinePartIDLabel.setVisible(false);
            madeInHouseButton.setSelected(false);
            madeInHouse = false;
            outsourcedButton.setSelected(true);
        }
    }

    // Action method for the In House radio button
    // Uses visibility to show or hide different elements
    @FXML
    void madeInHouseButtonAction(ActionEvent event) {
        if (currentPart instanceof Outsourced){
            Alert notification = new Alert(Alert.AlertType.ERROR);
            notification.setTitle("Error");
            notification.setHeaderText("An outsourced part cannot be changed to an in house part. Please create a new part.");
            notification.setContentText(null);
            notification.showAndWait();
            madeInHouseButton.setSelected(false);
            outsourcedButton.setSelected(true);
        }
        else {
            companyNamePartLabel.setVisible(false);
            machinePartIDLabel.setVisible(true);
            outsourcedButton.setSelected(false);
            madeInHouse = true;
            madeInHouseButton.setSelected(true);
        }
    }

    // Action method for the Save button in the Part windows
    @FXML
    void savePartButtonAction(ActionEvent event){
        if (!partNameTextField.getText().isEmpty() && !partStockField.getText().isEmpty() && !partPriceField.getText().isEmpty() && !partMinField.getText().isEmpty() && !partMaxField.getText().isEmpty()){
            if (Integer.parseInt(partMaxField.getText()) >= Integer.parseInt(partMinField.getText())) {
                if (Integer.parseInt(partMaxField.getText()) >= Integer.parseInt(partStockField.getText())) {
                    if(!companyNameOrMachinePartIDField.getText().isEmpty()){
                        if (editData == false) {
                            if(madeInHouse){
                                addProductController.madeInHouse = true;
                            }
                            else {
                                addProductController.madeInHouse = false;
                            }
                            addProductController.addToTempProductTableView(
                            Integer.parseInt(partIDTextField.getText()),
                            partNameTextField.getText(),
                            Integer.parseInt(partStockField.getText()),
                            Double.parseDouble(partPriceField.getText()),
                            Integer.parseInt(partMinField.getText()),
                            Integer.parseInt(partMaxField.getText()),
                            companyNameOrMachinePartIDField.getText()
                            );
                
                            Alert notification = new Alert(Alert.AlertType.INFORMATION);
                            notification.setTitle("Done");
                            notification.setHeaderText("Part successfully added.");
                            notification.setContentText(null);
                            notification.showAndWait();
                        }
                        else {
                            addProductController.updatePart(
                            Integer.parseInt(partIDTextField.getText()),
                            partNameTextField.getText(),
                            Integer.parseInt(partStockField.getText()),
                            Double.parseDouble(partPriceField.getText()),
                            currentPart,
                            Integer.parseInt(partMinField.getText()),
                            Integer.parseInt(partMaxField.getText()),
                            companyNameOrMachinePartIDField.getText()
                            );
                        
                            Alert notification = new Alert(Alert.AlertType.INFORMATION);
                            notification.setTitle("Done");
                            notification.setHeaderText("Part updated successfully.");
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
                        notification.setHeaderText("All fields must be filled out.");
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
                notification.setHeaderText("Max value cannot be lower than min value.");
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

    // Standard initialization
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    // Set the Product controller to be the parent of the productpart controller
    void setParentController(AddProductController addProductController) {
        this.addProductController = addProductController;
    }

    // Adds or changes the data for a part after saving - sets everything to a text value
    void setPart(Part selectedPart) {
        partIDTextField.setText(String.valueOf(selectedPart.getPartID()));
        partNameTextField.setText("" + selectedPart.getPartName());
        partStockField.setText("" + selectedPart.getPartStock());
        partPriceField.setText("" + selectedPart.getPartPrice());
        editData = true;
        partMinField.setText("" + selectedPart.getPartMin());
        partMaxField.setText("" + selectedPart.getPartMax());
        if (selectedPart instanceof InHouse) {
            madeInHouseButtonAction(null);
            InHouse inHouse = (InHouse) selectedPart;
            companyNameOrMachinePartIDField.setText("" + inHouse.getMachineID());
        }
        else if (selectedPart instanceof Outsourced){
            outsourcedButtonAction(null);
            Outsourced outsourced = (Outsourced) selectedPart;
            companyNameOrMachinePartIDField.setText("" + outsourced.getCompanyName());
        }
        currentPart = selectedPart;
    }

    // Retrieves an incrementally-generated ID and applies it to the part
    void setID(int generateID) {
        partIDTextField.setText(String.valueOf(generateID));
    }

    // Retrieves the ID of a part and product chosen by the user so an association can be made
    void setAssociatedPart(int associatedProductID) {
        this.associatedProductID = associatedProductID;
    }

    // Ask for confirmation before closing the Product Part window
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