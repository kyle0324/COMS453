package hw1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Table_generalizer {
	Data_table og;
	Data_table manip;

	int[] group;

	boolean isRich; // this is keeping track of the groups so we can know if we are finding k = 10
									// or k = 5

	// we use this to increment when counting k factor
	int matching = 0;

	int[] max_level_identifier;
	int[] curr_level_identifier;

	boolean found_solution;
	int[] solution;

	double distortion;
	double precision;

	double entropy;
	int L = 3;

	public Table_generalizer(Data_table dt, int algoChoice) throws IOException {
		distortion = 0;
		precision = 0;
		og = dt;
		manip = new Data_table(dt);
		group = new int[dt.age.size()];
		resetGroup();
		curr_level_identifier = new int[4];
		solution = new int[4];

		for (int i = 0; i < curr_level_identifier.length; i++) {
			curr_level_identifier[i] = 0;
			solution[i] = 0;
		}

		max_level_identifier = new int[4];
		// max identifiers. they go in order of (age, marital, education, race)
		max_level_identifier[0] = 4;
		max_level_identifier[1] = 3;
		max_level_identifier[2] = 2;
		max_level_identifier[3] = 1;

		if (algoChoice == 1) {
			manipTable_k();
		} else if (algoChoice == 2) {
			manipTable_l();
		}
	}

	private void resetAll() {
		resetAge();
		resetMarital();
		resetEducation();
		resetRace();
		resetGroup();
	}

	private void resetAge() {
		Collections.copy(manip.age, og.age);
		// manip.age = new ArrayList<>(og.age);
	}

	private void resetMarital() {
		Collections.copy(manip.marital, og.marital);
		// manip.marital = new ArrayList<>(og.marital);
	}

	private void resetEducation() {
		Collections.copy(manip.education, og.education);
		// manip.education = new ArrayList<>(og.education);
	}

	private void resetRace() {
		Collections.copy(manip.race, og.race);
		// manip.race = new ArrayList<>(og.race);
	}

	private void resetGroup() {
		for (int i = 0; i < group.length; i++) {
			group[i] = 0;
		}
	}

	private boolean compRecord(int r1, int r2) {
		if (manip.age.get(r1).equals(manip.age.get(r2))) {
			// Continue checking
			if (manip.marital.get(r1).equals(manip.marital.get(r2))) {
				// Cont
				if (manip.education.get(r1).equals(manip.education.get(r2))) {
					// Cont
					if (manip.race.get(r1).equals(manip.race.get(r2))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void manipTable_k() throws IOException {
		curr_level_identifier[0] = 4;
		manipAge(curr_level_identifier[0]);
		found_solution = true;
		int group_counter = 1;
		for (int i = 0; i < manip.age.size() - 1; i++) {
			if (group[i] == 0 && found_solution) {
				group[i] = group_counter;
				String richness = manip.income.get(i);
				matching++;
				for (int j = i + 1; j < manip.age.size(); j++) {
					if (group[j] == 0 && compRecord(i, j) && manip.income.get(j).contains(richness)) {
						group[j] = group_counter;
						matching++;
					}
				}
				if (richness.contains("<=50K")) { // set variable for rich
					isRich = false;
				} else {
					isRich = true;
				}

				if ((isRich && matching < 5) || (!isRich && matching < 10)) {
					found_solution = false;
				}
				matching = 0;
				group_counter++;
			}
		} // this is what one test looks like

		if (!found_solution) {
			resetAll();
			for (int i = 0; i <= max_level_identifier[3]; i++) {
				for (int j = 0; j <= max_level_identifier[2]; j++) {
					for (int k = 0; k <= max_level_identifier[1]; k++) {
						curr_level_identifier[1] = k;
						curr_level_identifier[2] = j;
						curr_level_identifier[3] = i;
						manipAge(curr_level_identifier[0]);
						manipMarital(curr_level_identifier[1]);
						manipEducation(curr_level_identifier[2]);
						manipRace(curr_level_identifier[3]);

						found_solution = manipGroup_hw1(); // this will also test to make sure

						if (found_solution) {
							// solution = curr_level_identifier;
							solution[0] = curr_level_identifier[0];
							solution[1] = curr_level_identifier[1];
							solution[2] = curr_level_identifier[2];
							solution[3] = curr_level_identifier[3];

							distortion = calculate_distortion();
							precision = calculate_precision();
							break;
						}
						resetAll();
					}
					if (found_solution) {
						break;
					}
				}
				if (found_solution) {
					break;
				}
			}
		} // solution should be found
		if (found_solution) { // now to look at all other combinations, skipping the ones that have higher
													// distortion
			resetAll();
			for (int i = 3; i >= 0; i--) {
				curr_level_identifier[0] = i;
				for (int j = 0; j <= max_level_identifier[3]; j++) {
					curr_level_identifier[3] = j;
					for (int k = 0; k <= max_level_identifier[2]; k++) {
						curr_level_identifier[2] = k;
						for (int l = 0; l <= max_level_identifier[1]; l++) {
							curr_level_identifier[1] = l;
							if (calculate_distortion() < distortion) {
								manipAge(curr_level_identifier[0]);
								manipMarital(curr_level_identifier[1]);
								manipEducation(curr_level_identifier[2]);
								manipRace(curr_level_identifier[3]);

								if (manipGroup_hw1()) {
									// solution = curr_level_identifier;
									solution[0] = curr_level_identifier[0];
									solution[1] = curr_level_identifier[1];
									solution[2] = curr_level_identifier[2];
									solution[3] = curr_level_identifier[3];
									distortion = calculate_distortion();
									precision = calculate_precision();
								}
								resetAll();
							}
						}
					}
				}
			}
		}
		// finally bring table to generalized point
		resetAll();

		manipAge(solution[0]);
		manipMarital(solution[1]);
		manipEducation(solution[2]);
		manipRace(solution[3]);
		manip.writeTableFile();
	}

	private boolean manipGroup_hw1() {
		matching = 0;
		int group_counter = 1;
		boolean works = true;
		for (int i = 0; i < manip.age.size() - 1; i++) {
			if (group[i] == 0 && works) {
				group[i] = group_counter;
				matching++;
				for (int j = i + 1; j < manip.age.size(); j++) {
					if (group[j] == 0 && compRecord(i, j) && manip.income.get(i).contains(manip.income.get(j))) {
						group[j] = group_counter;
						matching++;
					}
				}
				// now set isRich
				if (manip.income.get(i).contains("<=50K")) {
					isRich = false;
				} else {
					isRich = true;
				}

				if ((isRich && matching < 5) || (!isRich && matching < 10)) {
					works = false;
				}
				matching = 0;
			}
		}
		return works;
	}

	/**
	 * Generalizes age attribute of the entire table
	 * 
	 * @param level - generalization level to set to
	 */
	private void manipAge(int level) {
		// Resetting to original
		if (level == 0) {
			resetAge();
		} else if (level == 4) { // Set to highest generalization
			for (int i = 0; i < manip.age.size(); i++) {
				manip.age.set(i, "*");
			}
		} else if (level == 3) { // Set to <40 and >=40
			for (int i = 0; i < manip.age.size(); i++) {
				int elem = Integer.parseInt(manip.age.get(i));
				if (elem < 40) {
					manip.age.set(i, "<40");
				} else {
					manip.age.set(i, ">=40");
				}
			}
		} else if (level == 2) { // Set to intervals of 10
			for (int i = 0; i < manip.age.size(); i++) {
				int elem = Integer.parseInt(manip.age.get(i));
				if (elem < 28) {
					manip.age.set(i, "<28");
				} else if (elem < 38) {
					manip.age.set(i, "<38");
				} else if (elem < 48) {
					manip.age.set(i, "<48");
				} else if (elem < 58) {
					manip.age.set(i, "<58");
				} else {
					manip.age.set(i, ">=58");
				}
			}
		} else if (level == 1) { // Set to intervals of 5
			for (int i = 0; i < manip.age.size(); i++) {
				int elem = Integer.parseInt(manip.age.get(i));
				if (elem < 23) {
					manip.age.set(i, "<23");
				} else if (elem < 28) {
					manip.age.set(i, "<28");
				} else if (elem < 33) {
					manip.age.set(i, "<33");
				} else if (elem < 38) {
					manip.age.set(i, "<38");
				} else if (elem < 43) {
					manip.age.set(i, "<43");
				} else if (elem < 48) {
					manip.age.set(i, "<48");
				} else if (elem < 53) {
					manip.age.set(i, "<53");
				} else if (elem < 58) {
					manip.age.set(i, "<58");
				} else {
					manip.age.set(i, ">=58");
				}
			}
		}
		return;
	}

	/**
	 * Generalizes marital status attribute of the entire table, max level of 2
	 * 
	 * @param level - generalization level to set to
	 */
	private void manipMarital(int level) {
		if (level == 0) { // Reset to original
			resetMarital();
		} else if (level == 2) { // Set to highest generalization level
			for (int i = 0; i < manip.marital.size(); i++) {
				manip.marital.set(i, "*");
			}
		} else if (level == 1) {
			for (int i = 0; i < manip.marital.size(); i++) {
				String elem = manip.marital.get(i).toLowerCase();
				if (elem.contains("married")) {
					manip.marital.set(i, "Married");
				} else {
					manip.marital.set(i, "Single");
				}
			}
		}
		return;
	}

	/**
	 * Generalizes education attribute of the entire table, max level of 3
	 * 
	 * @param level - generalization level to set to
	 */
	private void manipEducation(int level) {
		if (level == 0) { // Reset to original
			resetEducation();
		} else if (level == 3) { // Set to highest generalization level
			for (int i = 0; i < manip.education.size(); i++) {
				manip.education.set(i, "*");
			}
		} else if (level == 2) { // Set to higher learning or none
			for (int i = 0; i < manip.education.size(); i++) {
				String elem = manip.education.get(i).toLowerCase();
				if (elem.contains("th") || elem.equals("preschool") || elem.contains("grad")) {
					manip.education.set(i, "No Higher Learning");
				} else if (elem.contains("assoc") || elem.contains("college") || elem.equals("bachelors")
						|| elem.equals("masters") || elem.equals("doctorate")
						|| elem.contains("prof")) {
					manip.education.set(i, "Higher Learning");
				}
			}
		} else if (level == 1) { // Set to no highschool diploma, < Bachelors, or >= Bachelors
			for (int i = 0; i < manip.education.size(); i++) {
				String elem = manip.education.get(i).toLowerCase();
				if (elem.contains("th") || elem.equals("preschool")) {
					manip.education.set(i, "No Highschool Diploma");
				} else if (elem.contains("assoc") || elem.contains("grad") || elem.contains("college")) {
					manip.education.set(i, "< Bachelors");
				} else if (elem.equals("bachelors") || elem.equals("masters") || elem.equals("doctorate")
						|| elem.contains("prof")) {
					manip.education.set(i, ">= Bachelors");
				}
			}
		}
		return;
	}

	/**
	 * Generalizes race attribute of the entire table, max level of 1
	 * 
	 * @param level - generalization level to set to
	 */
	private void manipRace(int level) {
		if (level == 0) { // Reset to original
			resetRace();
		} else if (level == 1) { // Set to max generalization
			for (int i = 0; i < manip.race.size(); i++) {
				manip.race.set(i, "*");
			}
		}
		return;
	}

	private double calculate_distortion() {
		double x = 0.0;
		for (int i = 0; i < curr_level_identifier.length; i++) {
			x += curr_level_identifier[i] * 1.00 / max_level_identifier[i];
		}
		return x / 4.0;
	}

	private double calculate_precision() {

		double x = 0.0;
		double y = 0.0;
		for (int i = 0; i < max_level_identifier.length; i++) { // i think N stands for the number of entries effected
			y = 1.00 * manip.age.size() * curr_level_identifier[i] / max_level_identifier[i];
			x += y;
			y = 0.0;
		}
		x = 1 - x / (og.age.size() * curr_level_identifier.length);
		return x;
	}

	// The parts below this are for problem two and we will need to clarify this

	private void manipTable_l() throws IOException {
		curr_level_identifier[0] = 4;
		manipAge(curr_level_identifier[0]);
		found_solution = true;
		int group_counter = 1;

		for (int i = 0; i < manip.age.size() - 1; i++) { // runs through the table to give values in groups
			if (group[i] == 0 && found_solution) {
				group[i] = group_counter;
				matching++;
				for (int j = i + 1; j < manip.age.size(); j++) {
					if (group[j] == 0 && compRecord(i, j)) {
						group[j] = group_counter;
						matching++;
					}
				} // we don't need to worry about rich this time. Keep track of entropy

				if ((matching < 5)) {
					found_solution = false;
				}
				matching = 0;
				group_counter++;
			}
		} // this is what one test looks like
		
		if(found_solution) {
			found_solution = calculate_entropy();
		}

		if (!found_solution) {
			resetAll();
			for (int i = 0; i <= max_level_identifier[3]; i++) {
				for (int j = 0; j <= max_level_identifier[2]; j++) {
					for (int k = 0; k <= max_level_identifier[1]; k++) {
						curr_level_identifier[1] = k;
						curr_level_identifier[2] = j;
						curr_level_identifier[3] = i;
						manipAge(curr_level_identifier[0]);
						manipMarital(curr_level_identifier[1]);
						manipEducation(curr_level_identifier[2]);
						manipRace(curr_level_identifier[3]);

						found_solution = manipGroup_hw2() && calculate_entropy();

						if (found_solution) {
							// solution = curr_level_identifier;
							solution[0] = curr_level_identifier[0];
							solution[1] = curr_level_identifier[1];
							solution[2] = curr_level_identifier[2];
							solution[3] = curr_level_identifier[3];
							distortion = calculate_distortion();
							precision = calculate_precision();
							break;
						}
						resetAll();
					}
					if (found_solution) {
						break;
					}
				}
				if (found_solution) {
					break;
				}
			}
		} // solution should be found
		if (found_solution) { // now to look at all other combinations, skipping the ones that have higher
													// distortion
			resetAll();
			for (int i = 3; i >= 0; i--) {
				curr_level_identifier[0] = i;
				for (int j = 0; j <= max_level_identifier[3]; j++) {
					curr_level_identifier[3] = j;
					for (int k = 0; k <= max_level_identifier[2]; k++) {
						curr_level_identifier[2] = k;
						for (int l = 0; l <= max_level_identifier[1]; l++) {
							curr_level_identifier[1] = l;
							if (calculate_distortion() < distortion) {
								manipAge(curr_level_identifier[0]);
								manipMarital(curr_level_identifier[1]);
								manipEducation(curr_level_identifier[2]);
								manipRace(curr_level_identifier[3]);

								if (manipGroup_hw2() && calculate_entropy()) {
									// solution = curr_level_identifier;
									solution[0] = curr_level_identifier[0];
									solution[1] = curr_level_identifier[1];
									solution[2] = curr_level_identifier[2];
									solution[3] = curr_level_identifier[3];
									distortion = calculate_distortion();
									precision = calculate_precision();
								}
								resetAll();
							}
						}
					}
				}
			}
		}
		resetAll();
		manipAge(solution[0]);
		manipMarital(solution[1]);
		manipEducation(solution[2]);
		manipRace(solution[3]);
		manip.writeTableFile();

	}

	private boolean manipGroup_hw2() {

		matching = 0;
		int group_counter = 1;
		boolean works = true;
		for (int i = 0; i < manip.age.size() - 1; i++) {
			if (group[i] == 0 && works) {
				group[i] = group_counter;
				matching++;
				for (int j = i + 1; j < manip.age.size(); j++) {
					if (group[j] == 0 && compRecord(i, j)) {
						group[j] = group_counter;
						matching++;
					}
				}

				if (matching < 5) {
					works = false;
				}
				matching = 0;
				group_counter++;
			}
		}
		return works;
	}

	private boolean calculate_entropy() { // to be done after we have successfully found a k = 5, groups should still be
																				// active
		int index = 0;
		double x = 0.0;
		ArrayList<String> uniques = new ArrayList<String>();
		ArrayList<Integer> numUniques = new ArrayList<Integer>();

		while (index < og.age.size() - 2) { // run through the list of groups
			if (group[index] != 0) { // checks to see if the row we are on has been visited
				int groupNum = group[index];
				uniques.add(manip.occupation.get(index));
				numUniques.add(1);
				group[index] = 0;

				// now we grabbed the group# and if they were rich. set a second iterator to
				// check the rest of the list for this group

				for (int i = index + 1; i < og.occupation.size(); i++) {
					if (group[i] == groupNum) { // if we find a match
						if (uniques.contains(manip.occupation.get(i))) {
							numUniques.set(uniques.indexOf(manip.occupation.get(i)),
									numUniques.get(uniques.indexOf(manip.occupation.get(i))) + 1);
							// for when we find this occupation already. I know its hard to read. The first
							// part gives the index and so we have to use it twice. Once to aim with and the
							// second to grab the number for addition
						} else {
							uniques.add(manip.occupation.get(i));
							numUniques.add(1);
						}
						group[i] = 0; // remember to set group to 0 to indicate we visited it
					}
				}

				// now all uniques should be found and we have the counts.
				int numSens = 0;
				for (int i = 0; i < numUniques.size(); i++) {
					numSens += numUniques.get(i);
				}

				// now to run the formula

				double total = 0.00;

				for (int i = 0; i < numUniques.size(); i++) {
					double y = 1.00 * numUniques.get(i) / numSens;
					y = y * Math.log(y) / Math.log(2); // change of base formula... java doesn't have a log base 2
					total += y;
				}

				total = total * -1; // to fix the negative after words
				if (total <= (Math.log(3) / Math.log(2))) {
					return false;
				}
			}
			index++;
		}
		// run the list of groups.
		// if unique value, add to unique and add a 1 to numUniques
		// else find index on uniques to add 1 to the corresponding index to numUniques

		return true;

	}

}
