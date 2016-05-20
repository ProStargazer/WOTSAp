package wots_System;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DBConnect {
	private Connection con;
	private Statement st;
	private ResultSet rs;
	private ResultSet rs2;
	private ResultSet rs3;
	private ResultSet rs4;
	private ResultSetMetaData rsmd;
	private int orderLine;
	ArrayList<String> productLocList = new ArrayList<String>();
	ArrayList<String> orderPick = new ArrayList<String>();

	public DBConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/wots", "root", "root");
			st = con.createStatement();

		} catch (Exception ex) {
			System.out.println("Error in connection: " + ex);
		}
	}

	public void getProductList() {
		try {
			String query = "SELECT * FROM wots.wots_pdb;";
			rs = st.executeQuery(query);
			System.out.println("Records from Product Database:");
			while (rs.next()) {
				String productID = rs.getString("productID");
				String productName = rs.getString("productName");
				String productLocation = rs.getString("productLocation");
				String productPrice = rs.getString("productPrice");
				String productStock = rs.getString("productStock");
				String productWeight = rs.getString("productWeight");
				System.out.println("Product ID: " + productID + "\n"
						+ "Product Name: " + productName + "\n"
						+ "Product Location: " + productLocation + "\n"
						+ "Price/unit: " + productPrice + "\n" + "Stock: "
						+ productStock + "\n" + "Weight: " + productWeight
						+ "\n");
			}

			rs.close();
			st.close();
			con.close();
		} catch (Exception ex) {
			System.out.println("Error in retrieve product list: " + ex);
		}
	}

	public void getOrder() throws SQLException {
		Scanner inputCollect = new Scanner(System.in);

		try {
			st = con.createStatement();
			System.out
					.println("Please enter the order number you wish to recall:"
							+ "\n");

			int Collect = inputCollect.nextInt();
			orderLine = Collect;
			String query = "SELECT * FROM wots.wots_odb WHERE orderID ="
					+ Collect + "";
			rs2 = st.executeQuery(query);
			rsmd = rs2.getMetaData();
			int columnsNumber = rsmd.getColumnCount();

			while (rs2.next()) {
				String orderProc = rs2.getString("orderProcessed");
				for (int i = 1; i <= columnsNumber; i++) {
					if ((rs2.getString(i) != null)) {
						System.out.println(rsmd.getColumnName(i));
						System.out.println(rs2.getString(i) + "\n");
					}
				}
				System.out.println(orderProc);
				if (orderProc.equals("Y")) {
					System.out.println("The order is already accepted.");

				} else if (orderProc.equals("N")) {
					System.out
							.println("The order is open, would you like to take on the order?  (Y/N) ");
					String orderClaim = inputCollect.next();
					if (orderClaim.equals("Y")) {
						retrievedOrderClaim();

					} else if (orderClaim.equals("N")) {
						System.out.println("Closing query.");

					}
				}
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			rs2.close();
			st.close();
			con.close();

		}

	}

	public void retrievedOrderClaim() {
		try {
			st = con.createStatement();
			String updateQuery = "UPDATE wots.wots_odb SET orderProcessed = 'Y"
					+ " ' WHERE orderID = " + (orderLine) + "";
			st.executeUpdate(updateQuery);
			System.out
					.println("Order claimed by User, Go to these locations in order to collect order:");
			String locationQuery = "SELECT orderLine1,orderLine2,orderLine3,orderLine4,orderLine5 FROM wots.wots_odb WHERE orderID ="
					+ orderLine + "";
			rs3 = st.executeQuery(locationQuery);
			while (rs3.next()) {
				for (int i = 1; i < 6; i++) {
					if ((rs3.getString(i) != null)) {
						orderPick.add(rs3.getString(i));
					}
				}
			}	
			rs3.close();
			
			
			System.out.println(orderPick);
			String productOrderList = "SELECT productID,productName,productLocation FROM wots.wots_pdb";
			rs4 = st.executeQuery(productOrderList);
			//int COUNT = 0;
			
			while (rs4.next()) {
				//System.out.println(COUNT);
				//COUNT++;
				String productID = rs4.getString("productID");
				String productName = rs4.getString("productName");
				String productLocation = rs4.getString("productLocation");
				for (int j = 0; j <= orderPick.size(); j++) {
				
					if (productID.equals(orderPick.get(j))) {
						System.out.println("|Warehouse Location: Zone "
									+ productLocation + "| productID : "
									+ productID + "| Product Name: "
									+ productName + " |");
					//SORRY THIS DOESN'T WORK GOD DAMN IT SHOULD
					} else {
						//System.out
								//.println("|Warehouse Location: Zone or product don't exist mate");
					}

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
