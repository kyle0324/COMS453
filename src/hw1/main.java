package hw1;

import java.io.IOException;

public class main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		// Data_table table = new
		// Data_table("C:\\Users\\kyle0\\eclipse-workspace\\COMS453\\src\\hw1\\adult.data");
		Data_table table = new Data_table("src/hw1/adult.data");

		Table_generalizer tg = new Table_generalizer(table);
		System.out.println(tg.group.length);

		table.writeTableFile();
		System.out.println("Success!");

	}

}
