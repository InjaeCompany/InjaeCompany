package com.oopclass.breadapp.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.oopclass.breadapp.config.StageManager;
import com.oopclass.breadapp.models.Customer;
import com.oopclass.breadapp.services.impl.CustomerService;
import com.oopclass.breadapp.services.impl.UserService;
import com.oopclass.breadapp.views.FxmlView;
import java.awt.event.MouseEvent;

//import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * OOP Class 20-21
 *
 * @author Gerald Villaran
 */
@Controller
public class CustomerController implements Initializable {

    @FXML
    private Label customerId;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;
    
    @FXML
    private TextField age;
    
//    @FXML
//    private ToggleGroup gender;
//
//    @FXML
//    private DatePicker dob;
//
//    @FXML
//    private DatePicker dob1;
//
//    @FXML
//    private RadioButton rbFemale;
//
//    @FXML
//    private RadioButton rbMale;

    @FXML
    private Button reset;
    
//    @FXML
//    private Button openCustomer;

    @FXML
    private Button saveCustomer;
    
    @FXML
    private Button deleteCustomer;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Long> colCustomerId;

    @FXML
    private TableColumn<Customer, String> colFirstName;

    @FXML
    private TableColumn<Customer, String> colLastName;
    
    @FXML
    private TableColumn<Customer, String> colAge;

//    @FXML
//    private TableColumn<User, String> colGender;
//    
//    @FXML
//    private TableColumn<User, LocalDate> colDOB;
//    
//    @FXML
//    private TableColumn<User, LocalDate> colDOB2;

    @FXML
    private TableColumn<Customer, Boolean> colEdit;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private CustomerService customerService;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

////    @FXML
////    private void exit(ActionEvent event) {
////        Platform.exit();
////    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
    }
    
//    @FXML
//    void openCustomer(ActionEvent event) {
//        stageManager.switchScene(FxmlView.CUSTOMER);
//    }

    @FXML
    private void saveCustomer(ActionEvent event) {

        if (validate("First Name", getFirstName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Last Name", getLastName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate ("Last Name", getAge(), "([0-9]{2}\\s*+"))
                /*&& emptyValidation("DOB", dob.getEditor().getText().isEmpty())
                && emptyValidation1("DOB1", dob1.getEditor().getText().isEmpty()))*/ {

            if (customerId.getText() == null || "".equals(customerId.getText())) {
                if (true) {

                    Customer customer = new Customer();
                    customer.setFirstName(getFirstName());
                    customer.setLastName(getLastName());
                    customer.setAge(getAge());
//                    user.setGender(getGender());
//                    user.setDob(getDob());
//                    user.setDob1(getDob1());

                    Customer newCustomer = customerService.save(customer);

                    saveAlert(newCustomer);
                }

            } else {
                Customer customer = customerService.find(Long.parseLong(customerId.getText()));
                customer.setFirstName(getFirstName());
                customer.setLastName(getLastName());
                customer.setAge(getAge());
//                user.setDob(getDob());
//                user.setDob1(getDob1());
//                user.setGender(getGender());
                Customer updatedCustomer = customerService.update(customer);
                updateAlert(updatedCustomer);
            }

            clearFields();
            loadUserDetails();
        }

    }

    @FXML
    private void deleteCustomer(ActionEvent event) {
        List<Customer> customer = customerTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            customerService.deleteInBatch(customer);
        }

        loadUserDetails();
    }

    private void clearFields() {
        customerId.setText(null);
        firstName.clear();
        lastName.clear();
        age.clear();
//        rbMale.setSelected(true);
//        rbFemale.setSelected(false);
//        dob.getEditor().clear();
//        dob1.getEditor().clear();
    }

    private void saveAlert(Customer customer) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Customer saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user " + customer.getFirstName() + " " + customer.getLastName() + " has been created and \n" + customer.getAge() + " id is " + customer.getId() + ".");
        alert.showAndWait();
    }

    private void updateAlert(Customer customer) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Customer updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user " + customer.getFirstName() + " " + customer.getLastName() + " has been updated.");
        alert.showAndWait();
    }

//    private String getGenderTitle(String gender) {
//        return (gender.equals("Male")) ? "his" : "her";
//    }

    public String getFirstName() {
        return firstName.getText();
    }

    public String getLastName() {
        return lastName.getText();
    }
    
    public String getAge() {
        return age.getText();
    }

//    public String getGender() {
//        return rbMale.isSelected() ? "Cash" : "Card";
//    }
//    
//    public LocalDate getDob() {
//        return dob.getValue();
//    }
//    
//    public LocalDate getDob1() {
//        return dob1.getValue();
//    }


    /*
	 *  Set All userTable column properties
     */
    private void setColumnProperties() {
        /* Override date format in table
		 * colDOB.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
			 String pattern = "dd/MM/yyyy";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		     @Override 
		     public String toString(LocalDate date) {
		         if (date != null) {
		             return dateFormatter.format(date);
		         } else {
		             return "";
		         }
		     }

		     @Override 
		     public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateFormatter);
		         } else {
		             return null;
		         }
		     }
		 }));*/

        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
//        colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
//        colDOB2.setCellValueFactory(new PropertyValueFactory<>("dob1"));
//        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Customer, Boolean>, TableCell<Customer, Boolean>> cellFactory
            = new Callback<TableColumn<Customer, Boolean>, TableCell<Customer, Boolean>>() {
        @Override
        public TableCell<Customer, Boolean> call(final TableColumn<Customer, Boolean> param) {
            final TableCell<Customer, Boolean> cell = new TableCell<Customer, Boolean>() {
                Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
                final Button btnEdit = new Button();

                @Override
                public void updateItem(Boolean check, boolean empty) {
                    super.updateItem(check, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btnEdit.setOnAction(e -> {
                            Customer customer = getTableView().getItems().get(getIndex());
                            updateCustomer(customer);
                        });

                        btnEdit.setStyle("-fx-background-color: transparent;");
                        ImageView iv = new ImageView();
                        iv.setImage(imgEdit);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(true);
                        iv.setCache(true);
                        btnEdit.setGraphic(iv);

                        setGraphic(btnEdit);
                        setAlignment(Pos.CENTER);
                        setText(null);
                    }
                }

                private void updateCustomer(Customer customer) {
                    customerId.setText(Long.toString(customer.getId()));
                    firstName.setText(customer.getFirstName());
                    lastName.setText(customer.getLastName());
                    age.setText(customer.getAge());
//                    dob.setValue(user.getDob());
//                    dob1.setValue(user.getDob1());
//                    if (user.getGender().equals("Cash")) {
//                        rbMale.setSelected(true);
//                    } else {
//                        rbFemale.setSelected(true);
//                    }
                }
            };
            return cell;
        }
    };

    /*
	 *  Add All users to observable list and update table
     */
    private void loadUserDetails() {
        customerList.clear();
        customerList.addAll(customerService.findAll());

        customerTable.setItems(customerList);
    }

    /*
	 * Validations
     */
    private boolean validate(String field, String value, String pattern) {
        if (!value.isEmpty()) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if (m.find() && m.group().equals(value)) {
                return true;
            } else {
                validationAlert(field, false);
                return false;
            }
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private void validationAlert(String field, boolean empty) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if (field.equals("Role")) {
            alert.setContentText("Please Select " + field);
        } else {
            if (empty) {
                alert.setContentText("Please Enter " + field);
            } else {
                alert.setContentText("Please Enter Valid " + field);
            }
        }
        alert.showAndWait();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        customerTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

        // Add all users into table
        loadUserDetails();
    }
}