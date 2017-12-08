package artificial.intelligence.cpsc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	final static String INPUTFILENAME = "minnumber.txt";
	final static String[] InputList = {
		"gehtnicht1.txt",
		"gehtnicht2.txt",
		"gehtnicht3.txt",
		"gehtnicht4.txt",
		"gehtnicht5.txt",
		"gehtnicht6.txt",
		"gehtnicht7.txt",
		"gehtnicht8.txt",
		"gehtnicht9.txt",
		"gehtnicht10.txt",
		"gehtnicht11.txt",
		"gehtnicht12.txt",
		"minnumber.txt",
		"pairing.txt",
		"parallelpen.txt",
		"prefexamp.txt"
	};
	public static long runUntil;
	static float coursemin;
	static float labmin;
	static float pairpen;
	static float sectionpen;
	static float wMin;
	static float wPref;
	static float wPair;
	static float wSec;
	
	//first argument will be input file
	//second argument will be time to run program(in minutes) (if 1 arg, set to 1)
	//arguments 3-6 will be penalty values (if only 2 args, set all to 1)
	//arguments 7-10 are the weights of those values (again, default to 1)
	
	public static void main(String[] args) {
		Parser p;
		if(args.length == 0) {
			p = new Parser(INPUTFILENAME);
		} else {
			p = new Parser(args[0]);
		}

		if(args.length > 1) {
			long minutes = (new Long(args[1]) * 60000); //get milliseconds
			runUntil = System.currentTimeMillis() + minutes; //this is the time where the program should stop running and give the best answer it found.
			if(args.length >= 10) {
				coursemin = new Float(args[2]);
				labmin = new Float(args[3]);
				pairpen = new Float(args[4]);
				sectionpen = new Float(args[5]);
				wMin = new Float(args[6]);
				wPref = new Float(args[7]);
				wPair = new Float(args[8]);
				wSec = new Float(args[9]);
			} else {
				coursemin = 1;
				labmin = 1;
				pairpen = 1;
				sectionpen = 1;
				wMin = 1;
				wPref = 1;
				wPair = 1;
				wSec = 1;
			}
		} else {
			runUntil = System.currentTimeMillis() + 60000;//1 minute
			coursemin = 1;
			labmin = 1;
			pairpen = 1;
			sectionpen = 11;
			wMin = 1;
			wPref = 1;
			wPair = 1;
			wSec = 1;
		}
		
		testLoop(9);
		
//		Map<Classes,TimeSlot> partAssign = createPartAssign(p);

//		evalCheck eval = new evalCheck(partAssign,coursemin,labmin,pairpen,sectionpen,wMin,wPref,wPair,wSec);
		
//		assignTree tree = new assignTree(p,partAssign,eval);
		
//		Map<Classes,TimeSlot> newTree = tree.createTree();

//		if(newTree !=null) {
//		System.out.println(p.getName() + "\t Final eval: "+tree.getMin());
//		printAssignments(newTree,p);
//		}else {
//			System.out.println("No Solution Found");
//		}
	}
	private static void testLoop(int i) {
		System.out.println("\n BEGINNING TEST: "+ i);
		Parser testp = new Parser(InputList[i]);
		Map<Classes,TimeSlot> partAssignTest = createPartAssign(testp);
		evalCheck evalTest = new evalCheck(partAssignTest,coursemin,labmin,pairpen,sectionpen,wMin,wPref,wPair,wSec);
		assignTree treeTest = new assignTree(testp,partAssignTest,evalTest);
		Map<Classes,TimeSlot> finalAssignTest = treeTest.createTree();
		if(finalAssignTest != null){
			System.out.println(testp.getName()+ "\t Final eval: "+treeTest.getMin());
			printAssignments(finalAssignTest,testp);
		}else{
			System.out.println("Test "+testp.getName()+" Failed\n\n");
		}
		testp = null;
		partAssignTest = null;
		evalTest = null;
		treeTest = null;
		finalAssignTest = null;
	}
	/**
	 * Basic function that takes in the parser all burgeoning with input file data 
	 * and creates an initial partAssign out of the partAssignment arrayList parsed from the file
	 * It will initialize ALL courses and labs found in the input file to a generic 'dollarSign' timeslot
	 * @param p parser that took in all the input data
	 * @return a Map of Classes to Timeslots, with any classes that exist in partAssign given the value of their appropriate timeslot
	 */
	private static Map<Classes,TimeSlot> createPartAssign(Parser p){
		Map<Classes,TimeSlot> createdPartAssign = new HashMap<Classes,TimeSlot>();
		ArrayList<pair<Classes,TimeSlot>> partAssign = p.getPartialAssignments();
		TimeSlot dollarSign = new TimeSlot();
		dollarSign.setDollarSign(true);
		for(Course c: p.getCourses()){
			createdPartAssign.put(c, dollarSign);
		}
		for(Lab l: p.getLabs()){
			createdPartAssign.put(l, dollarSign);
		}
		for(pair<Classes,TimeSlot> couple: partAssign){
			createdPartAssign.put(couple.getLeft(), couple.getRight());
		}
		return createdPartAssign;
	}
	
	static void printAssignments(Map<Classes,TimeSlot> assign,Parser p) {
		ArrayList<Course> courseHolder = p.getCourses();
		
		while(!courseHolder.isEmpty()) {
			Course earliestCourse = courseHolder.get(0);
			for(int i = 0; i<courseHolder.size();i++) {
				String earliestDepartment = earliestCourse.getDepartment();
				String nextDepartment = courseHolder.get(i).getDepartment();
				if((earliestDepartment.equals("SENG") && nextDepartment.equals("CPSC"))) {
					earliestCourse = courseHolder.get(i);
				}else if(earliestDepartment.equals("CPSC") && nextDepartment.equals("SENG")){
					//stays
				}else {
					int currentNumber = Integer.parseInt(earliestCourse.getClassNumber());
					int compareNumber = Integer.parseInt(courseHolder.get(i).getClassNumber());
					if(currentNumber > compareNumber) {
						int currentSection = Integer.parseInt(earliestCourse.getSection());
						int nextSection = Integer.parseInt(courseHolder.get(i).getSection());
						if(currentSection > nextSection) {
							earliestCourse = courseHolder.get(i);
						}
					}
				}
			}
			System.out.println(earliestCourse.toString()+"\t\t"+assign.get(earliestCourse).toString());
			List<Lab> courseLabs = earliestCourse.getLabs();
			for(int i = 0; i < courseLabs.size();i++) {
				System.out.println(courseLabs.get(i).toString()+ "\t"+assign.get(courseLabs.get(i)).toString());
			}
			courseHolder.remove((courseHolder.indexOf(earliestCourse)));	
		}
	}
	
}
