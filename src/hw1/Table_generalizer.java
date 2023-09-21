package hw1;

import java.io.IOException;
import java.util.ArrayList;

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
	double percision;

	double entropy;
	int L = 3;

	public Table_generalizer(Data_table dt) throws IOException {
		distortion = 0;
		percision = 0;
		og = dt;
		manip = new Data_table(dt);
		group = new int[dt.age.size()];
		resetGroup();
		curr_level_identifier = new int[4];

		for (int i = 0; i < curr_level_identifier.length; i++) {
			curr_level_identifier[i] = 0;
		}

		max_level_identifier = new int[4];
		// max identifiers. they go in order of (age, marital, education, race)
		max_level_identifier[0] = 4;
		max_level_identifier[1] = 3;
		max_level_identifier[2] = 2;
		max_level_identifier[3] = 1;
		
		
		manipTable_k();
	}

	private void resetAll() {
		resetAge();
		resetMarital();
		resetEducation();
		resetRace();
		resetGroup();
	}

	private void resetAge() {
		manip.age = og.age;
	}

	private void resetMarital() {
		manip.marital = og.marital;
	}

	private void resetEducation() {
		manip.education = og.education;
	}

	private void resetRace() {
		manip.race = og.race;
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
		for(int i = 0; i < manip.age.size() - 1; i++) {
			if(group[i] == 0 && found_solution) {
				group[i] = group_counter;
				String richness = manip.income.get(i);
				matching++;
				for(int j = i +1; j < manip.age.size(); j++) {
					if(group[j] == 0 && compRecord(i,j) && manip.income.get(j).contains(richness)) {
						group[j] = group_counter;
						matching++;
					}
				}
				if(richness.contains("<=50K")) { //set variable for rich
					isRich = false;
				}
				else {
					isRich = true;
				}
				
				if((isRich && matching < 5) || (!isRich && matching < 10)) {
					found_solution = false;
				}
				matching = 0;
				group_counter++;
			}
		} //this is what one test looks like
		
		if(!found_solution) {
			resetAll();
			for(int i = 0; i <= max_level_identifier[3]; i++) {
				for(int j = 0; j <= max_level_identifier[2]; j++) {
					for(int k = 0; k <= max_level_identifier[1]; k++) {
						curr_level_identifier[1] = k;
						curr_level_identifier[2] = j;
						curr_level_identifier[3] = i;
						manipAge(curr_level_identifier[0]);
						manipMarital(curr_level_identifier[1]);
						manipEducation(curr_level_identifier[2]);
						manipRace(curr_level_identifier[3]);
						
						found_solution = manipGroup_hw1(); //this will also test to make sure
						
						
						if(found_solution) {
							solution = curr_level_identifier;
							distortion = calculate_distortion();
							percision = calculate_precision();
							break;
						}
						resetAll();
					}
					if(found_solution) {
						break;
					}
				}
				if(found_solution) {
					break;
				}
			}
		} //solution should be found
		if(found_solution) { //now to look at all other combinations, skipping the ones that have higher distortion
			resetAll();
			for(int i = 3; i >= 0 ; i--) {
				curr_level_identifier[0] = i;
				for(int j = 0; j <= max_level_identifier[3]; j++) {
					curr_level_identifier[3] = j;
					for(int k = 0; k <= max_level_identifier[2]; k++) {
						curr_level_identifier[2] = k;
						for(int l = 0; l <= max_level_identifier[1]; l++) {
							curr_level_identifier[1] = l;
							if(calculate_distortion() < distortion) {
								manipAge(curr_level_identifier[0]);
								manipMarital(curr_level_identifier[1]);
								manipEducation(curr_level_identifier[2]);
								manipRace(curr_level_identifier[3]);
								
								if(manipGroup_hw1()) {
									solution = curr_level_identifier;
									distortion = calculate_distortion();
									percision = calculate_precision();
								}
								resetAll();
							}
						}
					}
				}
			}
		}
		//finally bring table to generalized point
		
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
		for(int i = 0; i < manip.age.size()-1; i++) {
			if(group[i] == 0 && works) {
				group[i] = group_counter;
				matching++;
				for(int j = i+1; j < manip.age.size(); j++) {
					if(group[j] == 0 && compRecord(i, j) && manip.income.get(i).contains(manip.income.get(j))) {
						group[j] = group_counter;
						matching++;
					}
				}
				//now set isRich
				if(manip.income.get(i).contains("<=50K")) {
					isRich = false;
				}
				else {
					isRich = true;
				}
				
				if((isRich && matching < 5) ||  (!isRich && matching < 10)) {
					works = false;
				}
			}
		}
		return works;
	}

	private void manipAge(int level) {
		// TODO change entire table
	}

	private void manipMarital(int level) {
		// TODO change entire table
	}

	private void manipEducation(int level) {
		// TODO change entire table
	}

	private void manipRace(int level) {
		// TODO change entire table
	}

	private double calculate_distortion() {
		double x = 0.0;
		for (int i = 0; i < curr_level_identifier.length; i++) {
			x += curr_level_identifier[i] * 1.00 / max_level_identifier[i];
		}
		return x / 4.0;
	}

	private double calculate_precision() { // don't know man. This might work

		double x = 0.0;
		double y = 0.0;
		for (int i = 0; i < max_level_identifier.length; i++) {
			for (int j = 0; j < curr_level_identifier.length; j++) {
				y += curr_level_identifier[j] / max_level_identifier[i];
			}
			x += y;
			y = 0.0;
		}
		x = 1 - x / (og.age.size() * curr_level_identifier.length);
		return x;
	}

	// The parts below this are for problem two and we will need to clarify this

	private void manipTable_l() {
		// TODO
	}

	private boolean calculate_entropy() { // to be done after we have successfully found a k = 5, groups should still be
																				// active
		int index = 0;
		double x = 0.0;
		ArrayList<String> uniques = new ArrayList<String>();
		ArrayList<Integer> numUniques = new ArrayList<Integer>();

		while (index < og.age.size() - 9) { // run through the list of groups
			if (group[index] != 0) { // checks to see if the row we are on has been visited
				int groupNum = group[index];
				uniques.add(manip.occupation.get(index));
				numUniques.add(1);
				group[index] = 0;
				String rich = manip.income.get(index);

				// now we grabbed the group# and if they were rich. set a second iterator to
				// check the rest of the list for this group

				for (int i = index + 1; i < og.occupation.size(); i++) {
					if (group[i] == groupNum && manip.income.get(i) == rich) { // if we find a match
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

	/*
	 * private void calculate_L() {
	 * ArrayList<String> uniques = new ArrayList<String>();
	 * uniques.add(og.occupation.get(0));
	 * 
	 * for(int i = 1; i < og.occupation.size(); i++) {
	 * if(!uniques.contains(og.occupation.get(i))) {
	 * uniques.add(og.occupation.get(i));
	 * }
	 * }
	 * L = uniques.size();
	 * }
	 */

}
