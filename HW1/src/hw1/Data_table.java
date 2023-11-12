package hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Data_table { // lists things in columns. To get number of records do size of an Array
	ArrayList<String> age;
	ArrayList<String> education;
	ArrayList<String> marital;
	ArrayList<String> race;
	ArrayList<String> occupation;
	ArrayList<String> income;

	public Data_table(String filename) throws FileNotFoundException {
		age = new ArrayList<String>();
		education = new ArrayList<String>();
		marital = new ArrayList<String>();
		race = new ArrayList<String>();
		occupation = new ArrayList<String>();
		income = new ArrayList<String>();
		read(filename);
	}

	public Data_table(Data_table dt) {
		age = (ArrayList<String>) dt.age.clone();
		education = (ArrayList<String>) dt.education.clone();
		marital = (ArrayList<String>) dt.marital.clone();
		race = (ArrayList<String>) dt.race.clone();
		occupation = (ArrayList<String>) dt.occupation.clone();
		income = (ArrayList<String>) dt.income.clone();

		Collections.copy(age, dt.age);
		Collections.copy(education, dt.education);
		Collections.copy(marital, dt.marital);
		Collections.copy(race, dt.race);
		Collections.copy(occupation, dt.occupation);
		Collections.copy(income, dt.income);
	}

	/**
	 * Reads in file data for generalized table
	 * 
	 * @param filename - name of file to read
	 * @throws FileNotFoundException
	 */
	private void read(String filename) throws FileNotFoundException {
		File f = new File(filename);
		Scanner scn = new Scanner(f);
		int i = 1;
		while (scn.hasNextLine()) {
			Scanner line = new Scanner(scn.nextLine());
			if (!line.hasNext()) {

			} else {
				line.useDelimiter(",");

				age.add(line.next());
				line.next();
				line.next();
				education.add(line.next());
				line.next();
				marital.add(line.next());
				occupation.add(line.next());
				line.next();
				race.add(line.next());
				line.next();
				line.next();
				line.next();
				line.next();
				line.next();
				income.add(line.next());
				// System.out.println("read line : " + i);
				i++;
				line.close();
			}

		}
		scn.close();

	}

	/**
	 * Writes generalized table to new file
	 * 
	 * @throws IOException
	 */
	public void writeTableFile() throws IOException {
		// If have a different desired output, or file structure, can respecify here
		File f = new File("src/hw1/Generalized_table.csv");
		f.delete();
		f.createNewFile();
		FileWriter fw = new FileWriter(f);

		fw.write("age, education, marital, race, occupation \n");
		for (int i = 0; i < age.size(); i++) {
			fw.write(age.get(i) + ", " + education.get(i) + ", " + marital.get(i) + ", " + race.get(i) + ", "
					+ occupation.get(i) + "\n");
		}

		fw.close();
	}
}
