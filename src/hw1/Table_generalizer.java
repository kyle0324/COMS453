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
	
	private void calculate_entropy() {
		//TODO
		
		
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
