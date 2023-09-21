package hw1;

import java.io.IOException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) throws IOException {
		Scanner scnr = new Scanner(System.in);
		// Data_table table = new
		// Data_table("C:\\Users\\kyle0\\eclipse-workspace\\COMS453\\src\\hw1\\adult.data");
		Data_table table = new Data_table("src/hw1/adult.data");

		System.out.println("Please select which privacy algorithm you'd like to use:");
		System.out.println("1 - k-Anonymity");
		System.out.println("2 - l-Diversity");
		int choice = scnr.nextInt();

		// Ensure valid choice
		while (choice != 1 && choice != 2) {
			System.out.println("Invalid selection");
			System.out.println("Please select which privacy algorithm you'd like to use:");
			System.out.println("1 - k-Anonymity");
			System.out.println("2 - l-Diversity");
			choice = scnr.nextInt();
		}

		Table_generalizer tg = new Table_generalizer(table, choice);

		System.out.println("distortion: " + tg.distortion);
		System.out.println("percision: " + tg.percision);

		// table.writeTableFile();
		System.out.println("Success!");
		scnr.close();
	}

}
