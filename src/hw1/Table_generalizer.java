package hw1;

import java.util.ArrayList;

public class Table_generalizer {
	Data_table og;
	Data_table manip;
	
	int[] group;
	
	boolean isRich; //this is keeping track of the groups so we can know if we are finding k = 10 or k = 5
	
	//we use this to increment when counting k factor
	int matching = 0;
	
	int[] max_level_identifier;
	int[] curr_level_identifier;
	
	boolean found_solution;
	int[] solution;
	
	double distortion;
	double percision;
	
	double entropy;
	int L = 3;
	
	
	public Table_generalizer(Data_table dt) {
		distortion = 0;
		percision = 0;
		og = dt;
		manip = new Data_table(dt);
		group = new int[dt.age.size()];
		resetGroup();	
		curr_level_identifier = new int[4];
		
		for(int i = 0; i < curr_level_identifier.length; i++) {
			curr_level_identifier[i] = 0;
		}
		
		max_level_identifier = new int[4];
		
		// TODO set the max identifiers. they go in order of (age, marital, education, race)
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
		for(int i = 0; i < group.length; i++) {
			group[i] = 0;
		}
	}
	
	private void manipTable_k() {
		//TODO
	}
	
	private void manipAge(int index, int level) {
		//TODO
	}
	
	private void manipMarital(int index, int level) {
		//TODO
	}
	
	private void manipEducation(int index, int level) {
		//TODO
	}
	
	private void manipRace(int index, int level) {
		//TODO
	}
	private double calculate_distortion() {
		double x = 0.0;
		for(int i = 0; i < curr_level_identifier.length; i++) {
			x += curr_level_identifier[i] * 1.00 / max_level_identifier[i];
		}
		return x / 4.0;
	}
	
	private double calculate_percision() { //don't know man.  This might work
		
		double x = 0.0;
		double y = 0.0;
		for(int i = 0; i <max_level_identifier.length; i++) {
			for(int j = 0; j < curr_level_identifier.length; j++) {
				y += curr_level_identifier[j]/ max_level_identifier[i];
			}
			x += y;
			y = 0.0;
		}
		x = 1 - x/(og.age.size() * curr_level_identifier.length);
		return x;
	}
	
	//The parts below this are for problem two and we will need to clarify this
	
	private void manipTable_l() {
		//TODO
	}
	
	private boolean calculate_entropy() { //to be done after we have successfully found a k = 5, groups should still be active
		int index = 0;
		double x = 0.0;
		ArrayList<String> uniques = new ArrayList<String>();
		ArrayList<Integer> numUniques = new ArrayList<Integer>();
		
		
		while(index < og.age.size()- 9) { //run through the list of groups
			if(group[index] != 0) {  //checks to see if the row we are on has been visited
				int groupNum = group[index];
				uniques.add(manip.occupation.get(index));
				numUniques.add(1);
				group[index] = 0;
				String rich = manip.income.get(index);
				
				//now we grabbed the group# and if they were rich. set a second iterator to check the rest of the list for this group
				
				for(int i = index + 1; i < og.occupation.size(); i++) {
					if(group[i] == groupNum && manip.income.get(i) == rich) { //if we find a match
						if(uniques.contains(manip.occupation.get(i))) {
							numUniques.set(uniques.indexOf(manip.occupation.get(i)), numUniques.get(uniques.indexOf(manip.occupation.get(i))) + 1);
							//for when we find this occupation already.  I know its hard to read.  The first part gives the index and so we have to use it twice. Once to aim with and the second to grab the number for addition
						}
						else {
							uniques.add(manip.occupation.get(i));
							numUniques.add(1);
						}
						group[i] = 0; //remember to set group to 0 to indicate we visited it
					}
				}
				
				//now all uniques should be found and we have the counts.
				int numSens = 0;
				for(int i = 0; i < numUniques.size(); i++) {
					numSens += numUniques.get(i);
				}
				
				//now to run the formula
				
				double total = 0.00;
				
				for(int i = 0; i < numUniques.size(); i++) {
					double y = 1.00 * numUniques.get(i)/numSens;
					y = y * Math.log(y)/ Math.log(2); //change of base formula... java doesn't have a log base 2
					total += y;
				}
				
				total = total * -1; // to fix the negative after words
				if (total <= (Math.log(3)/Math.log(2))) {
					return false;
				}
			}
			index++;
		}
		//run the list of groups.
		//if unique value, add to unique and add a 1 to numUniques
		//else find index on uniques to add 1 to the corresponding index to numUniques
		
		return true;
		
	}
	
	/*private void calculate_L() {
		ArrayList<String> uniques = new ArrayList<String>();
		uniques.add(og.occupation.get(0));
		
		for(int i = 1; i < og.occupation.size(); i++) {
			if(!uniques.contains(og.occupation.get(i))) {
				uniques.add(og.occupation.get(i));
			}
		}
		L = uniques.size();
	}*/
	
}
