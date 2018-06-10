/** 
 * Project 2 for CS 1181 
 * This program is a user friendly collection of items which can be manipulated 
 * through input from the user.
 */

package javafxapplication2;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Tristan Armbrister
 * CS 1181
 * Instructor:  M. Cheatham
 * Project 2
 */
public class JavaFXApplication2 extends Application {

    //create new TableView and List to store and show items in the collection
    private TableView<MediaItem> table = new TableView<MediaItem>();
    private final ObservableList<MediaItem> items
            = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //Create BorderPane for embedding other layouts
        BorderPane root = new BorderPane();

        //Create columns for TableView
        TableColumn titleCol = new TableColumn("Title");
        titleCol.setMinWidth(250);
        titleCol.setCellValueFactory(
                new PropertyValueFactory<MediaItem, String>("title"));

        TableColumn formatCol = new TableColumn("Format");
        formatCol.setMinWidth(100);
        formatCol.setCellValueFactory(
                new PropertyValueFactory<MediaItem, String>("format"));

        TableColumn loanedToCol = new TableColumn("Loaned To");
        loanedToCol.setMinWidth(150);
        loanedToCol.setCellValueFactory(
                new PropertyValueFactory<MediaItem, String>("loanedTo"));

        TableColumn dateLoanedCol = new TableColumn("Date Loaned");
        dateLoanedCol.setMinWidth(100);
        dateLoanedCol.setCellValueFactory(
                new PropertyValueFactory<MediaItem, String>("dateLoaned"));

        //Set up the Table
        table.setItems(items);
        table.setEditable(true);
        table.setMaxWidth(600);
        table.getColumns().addAll(titleCol, formatCol, loanedToCol, dateLoanedCol);

        //Create right side of the window using a Vbox containing several GridPanes
        VBox rightBox = new VBox();
        rightBox.setPadding(new Insets(80, 50, 80, 50));
        rightBox.setSpacing(80);

        //First GridPane for adding/removing/returning items
        GridPane topPane = new GridPane();
        topPane.setPadding(new Insets(15));
        topPane.setVgap(20);
        topPane.setHgap(10);

        //Add controls for topPane
        Button addBtn = new Button("Add");
        Label titleLabel = new Label("Title:");
        Label formatLabel = new Label("Format:");
        TextField titleText = new TextField();
        titleText.setPromptText("Enter Title");
        TextField formatText = new TextField();
        formatText.setPromptText("Enter Format");
        Button removeBtn = new Button("Remove");
        Button returnBtn = new Button("Return");

        //Add buttons to an HBox
        HBox buttonContainer = new HBox();
        buttonContainer.getChildren().addAll(addBtn, removeBtn, returnBtn);
        buttonContainer.setSpacing(10);

        //Add all to topPane
        topPane.addColumn(0, titleLabel, formatLabel);
        topPane.addColumn(1, titleText, formatText);
        topPane.add(buttonContainer, 0, 2, 2, 2);

        //Create fancy border
        topPane.setBorder(new Border(new BorderStroke(Color.GRAY,
                BorderStrokeStyle.SOLID, new CornerRadii(10.0), new BorderWidths(1))));

        //Second GridPane for Loaning items 
        GridPane loanPane = new GridPane();
        loanPane.setPadding(new Insets(15));
        loanPane.setVgap(20);
        loanPane.setHgap(10);

        //Add controls for loanPane
        Label loanedLabel = new Label("Loaned To:");
        TextField loanedText = new TextField();
        loanedText.setPromptText("Name");
        Label loanDateLabel = new Label("Loaned On: ");
        DatePicker dp = new DatePicker();
        dp.setPromptText("Select a Date");
        Button loanBtn = new Button("Loan");

        //Add all to loanPane
        loanPane.addColumn(0, loanedLabel);
        loanPane.add(loanBtn, 0, 2);
        loanPane.add(loanDateLabel, 0, 1, 1, 1);
        loanPane.addColumn(1, loanedText, dp);

        //Create fancy border 2
        loanPane.setBorder(new Border(new BorderStroke(Color.GRAY,
                BorderStrokeStyle.SOLID, new CornerRadii(10.0), new BorderWidths(1))));

        //Create bottom pane for sorting
        

        //Create event handlers for the controls
        //Add button event handler
        addBtn.setOnAction(e -> {
            MediaItem item = new MediaItem(titleText.getText(),
                    formatText.getText());
            //Validate if item exists
            if (!items.contains(item)) {
                if (!item.getTitle().isEmpty() && !item.getFormat().isEmpty()) {
                    items.add(item);
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText("You did not add any input to the table");
                    alert.setContentText("To add an item, enter the item's title"
                            + " and format, then press add.");
                    alert.showAndWait();
                }
            } else {
                //Allow user to add more of the same item for whatever reason
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Duplicate Item");
                alert.setHeaderText("This item Already exists in your collection");
                alert.setContentText("Would you like to add the item anyways?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    items.add(item);
                }
            }
            //Clear TextFields
            titleText.clear();
            formatText.clear();
        });
        //Remove Button event  handler
        removeBtn.setOnAction(e -> {
            //Get selected row
            int index = table.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                table.getItems().remove(index);
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Choice");
                alert.setHeaderText("You did not make a selection");
                alert.setContentText("To remove an item, select it in the"
                        + " table and click remove");
                alert.showAndWait();
            }
        });
        //Loan button event handler
        loanBtn.setOnAction(e -> {
            //Get selected row
            int index = table.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                MediaItem item = table.getItems().get(index);
                try {
                    //Validate input
                    if (item.getLoanedTo().equals("Not Loaned") && item.getDateLoaned().equals("N/A")) {
                        if (!(loanedText.getText().isEmpty() || dp.getValue().toString().isEmpty())) {
                            //Change item to loaned
                            item.setLoanedTo(loanedText.getText());
                            item.setDateLoaned(dp.getValue().format(DateTimeFormatter.ISO_DATE));
                            //Refresh table because it won't update by itself smh
                            //This seriously took like 2 hours to figure out
                            table.getColumns().get(index).setVisible(false);
                            table.getColumns().get(index).setVisible(true);
                        }
                    } else {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Loan Error");
                        alert.setHeaderText("There was an error loaning the item");
                        alert.setContentText("To loan an item, select an item that"
                                + " is not currently on loan and fill out the loan "
                                + "information.");

                        alert.showAndWait();
                    }
                } catch (Exception e2) {
                    //Catch Exception for when the date picker has invalid input
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Loan Error");
                    alert.setHeaderText("There was an error loaning the item");
                    alert.setContentText("To loan an item, select an item that"
                            + " is not currently on loan and fill out the loan "
                            + "information.");

                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Choice");
                alert.setHeaderText("You did not make a selection");
                alert.setContentText("Please select an item and"
                        + " enter the name of the person you loaned it to, "
                        + "and select a date.");
                alert.showAndWait();
            }
            
            //Reset fields
            loanedText.clear();
            dp.setValue(null);
        });
        //Return Button event handler
        returnBtn.setOnAction(e -> {
            //Get selected row
            int index = table.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                MediaItem item = table.getItems().get(index);
                //Validate input
                if (item.getLoanedTo().equals("Not Loaned") && item.getDateLoaned().equals("N/A")) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Return Error");
                    alert.setHeaderText("There was an error returning the item");
                    alert.setContentText("Please make sure the selected item is "
                            + "currently loaned in order to return it.");
                    alert.showAndWait();
                } else {
                    //Return the item
                    item.setLoanedTo("Not Loaned");
                    item.setDateLoaned("N/A");
                    //And refresh the table
                    table.getColumns().get(index).setVisible(false);
                    table.getColumns().get(index).setVisible(true);
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Choice");
                alert.setHeaderText("You did not make a selection");
                alert.setContentText("Please select an item and"
                        + " enter the name of the person you loaned it to, "
                        + "and select a date.");
                alert.showAndWait();
            }
            //Clear fields
            loanedText.clear();
            dp.setValue(null);
        });
        
        //Add panes to right box
        rightBox.getChildren().addAll(topPane, loanPane);
        
        //Set the rightBox to the right border
        root.setRight(rightBox);
        
        //Set the table to the left border
        root.setLeft(table);
        
        Scene scene = new Scene(root, 1100, 800);
        primaryStage.setTitle("Media Collection");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    
    /**
     * This class contains the methods and fields which define a media item
     */
    public static class MediaItem {

        private final SimpleStringProperty title;
        private final SimpleStringProperty format;
        private final SimpleStringProperty loanedTo;
        private final SimpleStringProperty dateLoaned;
        
        /**
         * The constructor method for the MediaItem class
         * @param t - the title of the item
         * @param f - the format of the item
         */
        private MediaItem(String t, String f) {
            this.title = new SimpleStringProperty(t);
            this.format = new SimpleStringProperty(f);
            this.loanedTo = new SimpleStringProperty("Not Loaned");
            this.dateLoaned = new SimpleStringProperty("N/A");
        }

        public String getTitle() {
            return title.get();
        }

        public void setTitle(String t) {
            title.set(t);
        }

        public String getFormat() {
            return format.get();
        }

        public void setFormat(String f) {
            format.set(f);
        }

        public String getLoanedTo() {
            return loanedTo.get();
        }

        public void setLoanedTo(String l) {
            loanedTo.set(l);
        }

        public String getDateLoaned() {
            return dateLoaned.get();
        }

        public void setDateLoaned(String d) {
            dateLoaned.set(d);
        }
        
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + Objects.hashCode(this.title);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final MediaItem other = (MediaItem) obj;
            if (!Objects.equals(this.title.toString(), other.title.toString())) {
                return false;
            }
            return true;
        }
    }
}
