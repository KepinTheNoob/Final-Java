package view;

import database.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Pudding;

public class HomePage {
	private Stage stage;
	private BorderPane root = new BorderPane();
	private Scene scene = new Scene(root, 1000, 700);
	
	private Label header_label = new Label("PT Pudding");
	private Label id_label = new Label("Pudding's ID");
	private Label name_label = new Label("Pudding's Name");
	private Label price_label = new Label("Pudding's Price");
	private Label stock_label = new Label("Pudding's Stock");
	
	private TextField id_tf = new TextField();
	private TextField name_tf = new TextField();
	private TextField price_tf = new TextField();
	private TextField stock_tf = new TextField();
	
	private Button add_btn = new Button("Add new Pudding");
	private Button update_btn = new Button("Update Pudding");
	private Button delete_btn = new Button("Delete Pudding");
	
	private HBox button_box = new HBox(update_btn, delete_btn);
	
	private TableView<Pudding> table = new TableView<Pudding>();
	
	private TableColumn<Pudding, String> id_col = new TableColumn<Pudding, String>("Pudding's ID");
	private TableColumn<Pudding, String> name_col = new TableColumn<Pudding, String>("Pudding's name");
	private TableColumn<Pudding, Integer> price_col = new TableColumn<Pudding, Integer>("Pudding's price");
	private TableColumn<Pudding, Integer> stock_col = new TableColumn<Pudding, Integer>("Pudding's stock");
	
	private GridPane gp = new GridPane();
	
	private Pudding selected;
	
	private ObservableList<Pudding> list = FXCollections.observableArrayList();

	private DataBase db = DataBase.getInstance();
	
	public HomePage(Stage stage) {
		this.stage = stage;
		
		this.populate_table();
		this.set_component();
		this.set_style();
		this.set_column();
		this.handle_button();
		this.setListener();
		
	}
	
	@SuppressWarnings("unchecked")
	private void set_component() {
		root.setTop(table);
		root.setCenter(gp);
		
		id_tf.setDisable(true);
		id_tf.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: lightgray; -fx-opacity: 1; -fx-border-radius: 3px;");
		
		gp.add(header_label, 0, 0, 2, 1);
		gp.add(id_label, 0, 1);
		gp.add(name_label, 0, 2);
		gp.add(price_label, 0, 3);
		gp.add(stock_label, 0, 4);
		gp.add(id_tf, 1, 1);
		gp.add(name_tf, 1, 2);
		gp.add(price_tf, 1, 3);
		gp.add(stock_tf, 1, 4);
		gp.add(button_box, 0, 5, 2, 1);
		gp.add(add_btn, 0, 6, 2, 1);
		
		table.getColumns().addAll(id_col, name_col, price_col, stock_col);
		
		stage.setResizable(false);
		stage.setScene(scene);
	}
	
	private void set_style() {
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(15);
		gp.setVgap(15);
		
		GridPane.setHalignment(header_label, HPos.CENTER);
		GridPane.setHalignment(button_box, HPos.CENTER);
		
//		type_cb.setMaxWidth(Double.MAX_VALUE);
		
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		
		add_btn.setMaxWidth(Double.MAX_VALUE);
		update_btn.setMinWidth(160);
		delete_btn.setMinWidth(160);
		
		button_box.setSpacing(15);
	}
	
	private void set_column() {
		id_col.setCellValueFactory(new PropertyValueFactory<Pudding, String>("id"));
		name_col.setCellValueFactory(new PropertyValueFactory<Pudding, String>("name"));
		price_col.setCellValueFactory(new PropertyValueFactory<Pudding, Integer>("price"));
		stock_col.setCellValueFactory(new PropertyValueFactory<Pudding, Integer>("stock"));
	}
	
	private void populate_table() {
		list = DataBase.getAll();
		table.setItems(list);
		
		id_tf.clear();
		name_tf.clear();
		price_tf.clear();
		stock_tf.clear();
	}
	
	private String generateRandomID() {
	    int randomNumber = (int) (Math.random() * 900) + 100; // menghasilkan angka random 100-999
	    return "PD-" + randomNumber;
	}
	
	private void handle_button() {
		add_btn.setOnAction(event -> {
			String id = generateRandomID();
			String name = name_tf.getText();
			String price = price_tf.getText();
			String stock = stock_tf.getText();
			Alert alert = new Alert(AlertType.ERROR);
			
			if (id.isEmpty() || name.isEmpty() || price.isEmpty() || stock.isEmpty()) {
				alert.initOwner(stage);
				alert.setTitle("Error");
				alert.setHeaderText("Validation Error");
				alert.setContentText("All fields must be filled!");
				alert.showAndWait();
				return;
			}
			
			for (Pudding pudding : list) {
				if(pudding.getId().equals(id)) {
					alert.initOwner(stage);
					alert.setTitle("Error");
					alert.setHeaderText("Validation Error");
					alert.setContentText("ID must be Unique!");
					alert.showAndWait();
					return;
				}
			}
			
			if (!id.startsWith("PD")) {
				alert.initOwner(stage);
				alert.setTitle("Error");
				alert.setHeaderText("Validation Error");
				alert.setContentText("ID MUST START WITH PD!");
				alert.showAndWait();
				return;
			}
			
			try {
				Integer.valueOf(price);
			} catch (Exception e) {
				alert.initOwner(stage);
				alert.setTitle("Error");
				alert.setHeaderText("Validation Error");
				alert.setContentText("Price must be Numeric!");
				alert.showAndWait();
				return;
			}
			
			if (Integer.valueOf(price) <= 0) {
				alert.initOwner(stage);
				alert.setTitle("Error");
				alert.setHeaderText("Validation Error");
				alert.setContentText("Price can't be lower than 0!");
				alert.showAndWait();
				return;
			}
			
			if (Integer.valueOf(stock) <= 0) {
				alert.initOwner(stage);
				alert.setTitle("Error");
				alert.setHeaderText("Validation Error");
				alert.setContentText("Stock can't be lower than 0!");
				alert.showAndWait();
				return;
			}
			
			id_tf.setText(generateRandomID());
			DataBase.save(new Pudding(id, name, Integer.valueOf(price), Integer.valueOf(stock)));
			this.populate_table();
			alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(stage);
			alert.setTitle("Success!");
			alert.setHeaderText("Info");
			alert.setContentText("Pudding added successfully!");
			alert.showAndWait();
		});
		
			delete_btn.setOnAction(event -> {
				String id = id_tf.getText();
				String name = name_tf.getText();
				String price = price_tf.getText();
				String stock = stock_tf.getText();
				Alert alert = new Alert(AlertType.ERROR);
				
				if (id.isEmpty() || name.isEmpty() || price.isEmpty() || stock.isEmpty()) {
					alert.initOwner(stage);
					alert.setTitle("Error");
					alert.setHeaderText("Validation Error");
					alert.setContentText("All fields must be filled!");
					alert.showAndWait();
					return;
				}
				
				if (!id.startsWith("PD")) {
					alert.initOwner(stage);
					alert.setTitle("Error");
					alert.setHeaderText("Validation Error");
					alert.setContentText("ID MUST START WITH FR!");
					alert.showAndWait();
					return;
				}
				
				try {
					Integer.valueOf(price);
				} catch (Exception e) {
					alert.initOwner(stage);
					alert.setTitle("Error");
					alert.setHeaderText("Validation Error");
					alert.setContentText("Price must be Numeric!");
					alert.showAndWait();
					return;
				}
				
				if (Integer.valueOf(price) <= 0) {
					alert.initOwner(stage);
					alert.setTitle("Error");
					alert.setHeaderText("Validation Error");
					alert.setContentText("Price can't be lower than 0!");
					alert.showAndWait();
					return;
				}
				
				if (Integer.valueOf(stock) <= 0) {
					alert.initOwner(stage);
					alert.setTitle("Error");
					alert.setHeaderText("Validation Error");
					alert.setContentText("Stock can't be lower than 0!");
					alert.showAndWait();
					return;
				}
				
				DataBase.delete(new Pudding(id, name, Integer.valueOf(price), Integer.valueOf(stock)));
				this.populate_table();
				alert = new Alert(AlertType.INFORMATION);
				alert.initOwner(stage);
				alert.setTitle("Success!");
				alert.setHeaderText("Info");
				alert.setContentText("Pudding successfully deleted!");
				alert.showAndWait();
			});
		
		update_btn.setOnAction(event -> {
			String id = id_tf.getText();
			String name = name_tf.getText();
			String price = price_tf.getText();
			String stock = stock_tf.getText();
			Alert alert = new Alert(AlertType.ERROR);
			
			if (id.isEmpty() || name.isEmpty() || price.isEmpty() || stock.isEmpty()) {
				alert.initOwner(stage);
				alert.setTitle("Error");
				alert.setHeaderText("Validation Error");
				alert.setContentText("All fields must be filled!");
				alert.showAndWait();
				return;
			}
			
			if (!id.startsWith("PD")) {
				alert.initOwner(stage);
				alert.setTitle("Error");
				alert.setHeaderText("Validation Error");
				alert.setContentText("ID MUST START WITH FR!");
				alert.showAndWait();
				return;
			}
			
			try {
				Integer.valueOf(price);
			} catch (Exception e) {
				alert.initOwner(stage);
				alert.setTitle("Error");
				alert.setHeaderText("Validation Error");
				alert.setContentText("Price must be Numeric!");
				alert.showAndWait();
				return;
			}
			
			if (Integer.valueOf(price) <= 0) {
				alert.initOwner(stage);
				alert.setTitle("Error");
				alert.setHeaderText("Validation Error");
				alert.setContentText("Price can't be lower than 0!");
				alert.showAndWait();
				return;
			}
			
			if (Integer.valueOf(stock) <= 0) {
				alert.initOwner(stage);
				alert.setTitle("Error");
				alert.setHeaderText("Validation Error");
				alert.setContentText("Stock can't be lower than 0!");
				alert.showAndWait();
				return;
			}
			
			DataBase.update(new Pudding(id, name, Integer.valueOf(price), Integer.valueOf(stock)));
			this.populate_table();
			alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(stage);
			alert.setTitle("Success!");
			alert.setHeaderText("Info");
			alert.setContentText("Pudding updated successfully!");
			alert.showAndWait();
		});
	}
	
	
	private void setListener() {
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {

			if (selected != null) {
				this.selected = selected;
				id_tf.setText(selected.getId());
				name_tf.setText(selected.getName());
				price_tf.setText(String.valueOf(selected.getPrice()));
				stock_tf.setText(String.valueOf(selected.getStock()));

			}
		});
	}
}
