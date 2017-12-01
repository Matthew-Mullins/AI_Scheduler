package artificial.intelligence.cpsc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	final static String INPUTFILENAME = "TestInput.txt";
	
	//first argument will be input file
	//second argument will be time to run program(in minutes)
	//arguments 3-6 will be penalty values (if only 2 args, set all to 1)
	//
	
	public static void main(String[] args) {
		Parser p = new Parser(INPUTFILENAME);
		
		System.out.println("COURSE SLOTS: \n");
		System.out.println(p.getCourseSlots().toString());
		System.out.println("LAB SLOTS: \n");
		System.out.println(p.getLabSlots().toString());
		System.out.println("COURSES: \n");
		System.out.println(p.getCourses().toString());
		System.out.println("LABS: \n");
		System.out.println(p.getLabs().toString());
		
		System.out.println("UNWANTED: \n");
		System.out.println(p.getUnwanted().toString());
		System.out.println("NONCOMPATIBLE: \n");
		System.out.println(p.getNonCompatible().toString());
		
		System.out.println("PAIRS: \n");
		System.out.println(p.getPairs().toString());

		System.out.println("PREFERENCES: \n");
		System.out.println(p.getPreferences().toString());
		System.out.println("PART ASSIGNMENTS: \n");
		System.out.println(p.getPartialAssignments().toString());
		
	}
}
