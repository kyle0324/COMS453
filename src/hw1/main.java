package hw1;

import java.io.IOException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) throws IOException {
		Scanner scnr = new Scanner(System.in);
		// Program expects original data in the following path destination
		Data_table table = new Data_table("src/hw1/adult.data");
		// Can just respecify the file path if need to run it

		System.out.println();
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

		// Run algorithm
		Table_generalizer tg = new Table_generalizer(table, choice);

		System.out.println("distortion: " + tg.distortion);
		System.out.println("precision: " + tg.precision);
		System.out.println("Success!");
		scnr.close();
	}

}
