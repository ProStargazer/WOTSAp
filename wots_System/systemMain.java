package wots_System;

import java.sql.SQLException;
import java.util.Scanner;

public class systemMain {

	public static void main(String[] args) throws SQLException {
		boolean Running = true;

		do {
			Scanner input = new Scanner(System.in);
			System.out
					.println("\n"
							+ "Please type the number to select from the following options:");
			System.out.println("1. Retrieve Total Item Database.");
			System.out.println("2. Recall an order on the system.");
			System.out.println("3. Close connection.");
			int userInput = input.nextInt();
			if (userInput == 1) {
				callProductList();
			} else if (userInput == 2) {
				callOrder();
			} else if (userInput == 3) {
				System.out.println("Closing system, thanks for using me!");
				Running = false;
				System.exit(0);

			}
		} while (Running);
	}

	public static void callProductList() {
		DBConnect connect = new DBConnect();
		connect.getProductList();
	}

	public static void callOrder() throws SQLException {
		DBConnect connect = new DBConnect();
		connect.getOrder();
	}

}
