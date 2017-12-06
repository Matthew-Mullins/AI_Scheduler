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
	static long runUntil;
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
			long minutes = 1;//(new Long(args[1]) * 60000); //get milliseconds
			runUntil = System.currentTimeMillis() + minutes; //this is the time where the program should stop running and give the best answer it found.
			if(args.length == 10) {
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
			runUntil = 1;//System.currentTimeMillis() + 60000;//1 minute
			coursemin = 100;
			labmin = 100;
			pairpen = 1;
			sectionpen = 10;
			wMin = 1;
			wPref = 0;
			wPair = 1;
			wSec = 0;
		}
		Map<Classes,TimeSlot> partAssign = createPartAssign(p);
	
		
		evalCheck eval = new evalCheck(partAssign,coursemin,labmin,pairpen,sectionpen,wMin,wPref,wPair,wSec);
		
//		System.out.println("The minimum penalty is: "+ eval.minCheck(p.getCourseSlots(),p.getLabSlots()));
//		
//		System.out.println("The preference penalty is: "+ eval.preferenceCheck(p.getPreferences()));
//		
//		System.out.println("The pair penalty is: "+eval.pairCheck(p.getPairs()));
//		
//	//	System.out.println("The lab section penalty is: "+eval.sectionLabCheck(p.getLabSections()));
//		
//		
//		System.out.println("The course section penalty is: "+eval.sectionCourseCheck(p.getCourseSections()));
//		
//		System.out.println(p.getLabSlots().toString());
//		System.out.println(p.getCourseSlots().toString());
//		
//		System.out.println(p.getFiveHundredCourses().toString());
//		
//		System.out.println(p.get413NonCompatible());
//
//
//		System.out.println(p.get413NonCompatible());

		
//		legalCheck lcheck = new legalCheck(partAssign);
//		if(lcheck.doAllChecks(p.getCourses(),p.getLabs(),p.getNonCompatible(),p.getUnwanted(),p.getFiveHundredCourses(),p.getEveningCourses(),p.getEveningLabs())){
//			System.out.println("The check passed \n");
//		}else{
//			System.out.println("The check failed\n");
//		}

		
		
		
		assignTree tree = new assignTree(p,partAssign,eval);
		
		Map<Classes,TimeSlot> newTree = tree.createTree();
		
		if(newTree !=null) {
		System.out.println(p.getName() + "\t Final eval: "+tree.getMin());
		printAssignments(newTree,p);
		}else {
			System.out.println("No Solution Found");
		}

	
		
		
		
		
		
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
	
	private static void printAssignments(Map<Classes,TimeSlot> assign,Parser p) {
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
