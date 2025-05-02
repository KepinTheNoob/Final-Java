package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Pudding;

public class DataBase {
	private String url = "jdbc:mysql://localhost:3306/pudding";
	private String username = "root";
	private String password = "";
	
	private static Connection con;
	private static DataBase instance;
	
	private static PreparedStatement ps;
	private static ResultSet rs;
	
	private DataBase() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public PreparedStatement preparedStatement (String query) throws SQLException {
		return con.prepareStatement(query);
	}
	
	public static DataBase getInstance() {
		if (instance == null) {
			instance = new DataBase();
		}
		return instance;
	}
	
	public static ObservableList<Pudding> getAll(){
		String query = "SELECT * FROM menu";
		ObservableList<Pudding> puddinglist = FXCollections.observableArrayList();
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				puddinglist.add(new Pudding(rs.getString("id"), rs.getString("name"), rs.getInt("price"), rs.getInt("stock")));
 			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return puddinglist;
	}
	
	public static void save(Pudding pudding) {
		String query = "INSERT INTO menu VALUES (?, ?, ?, ?)";
		
		try {
			ps = con.prepareStatement(query);
			ps.setString(1, pudding.getId());
			ps.setString(2, pudding.getName());
			ps.setInt(3, pudding.getPrice());
			ps.setInt(4, pudding.getStock());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void delete(Pudding pudding) {
		String query = "DELETE FROM menu WHERE id = ?";
		
		try {
			ps = con.prepareStatement(query);
			ps.setString(1, pudding.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void update(Pudding pudding) {
		String query = "UPDATE menu SET name = ?, price = ?, stock = ? WHERE id = ?";
		
		try {
			ps = con.prepareStatement(query);
			ps.setString(4, pudding.getId());
			ps.setString(1, pudding.getName());
			ps.setInt(2, pudding.getPrice());
			ps.setInt(3, pudding.getStock());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
