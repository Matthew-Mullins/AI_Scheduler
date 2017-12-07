package artificial.intelligence.cpsc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



public class Main {

	final static String INPUTFILENAME = "TestInput.txt";
	static long runUntil;
	static float coursemin;
	static float labmin;
	static float pairpen;
	static float sectionpen;
	
	public Map<Classes,TimeSlot> winnerKid = null;
	public float absoluteMin = Float.MAX_VALUE; 

	
	//first argument will be input file
	//second argument will be time to run program(in minutes) (if 1 arg, set to 1)
	//arguments 3-6 will be penalty values (if only 2 args, set all to 1)
	//
	
	public static void main(String[] args) {
		Parser p = new Parser(INPUTFILENAME);		

		if(args.length > 1) {
			long minutes = (new Long(args[1]) * 60000); //get milliseconds
			runUntil = System.currentTimeMillis() + minutes; //this is the time where the program should stop running and give the best answer it found.
			if(args.length == 6) {
				coursemin = new Float(args[2]);
				labmin = new Float(args[3]);
				pairpen = new Float(args[4]);
				sectionpen = new Float(args[5]);
			} else {
				coursemin = 1;
				labmin = 1;
				pairpen = 1;
				sectionpen = 1;
			}
		} else {
			runUntil = System.currentTimeMillis() + 60000;//1 minute
			coursemin = 1;
			labmin = 1;
			pairpen = 1;
			sectionpen = 1;
		}
		Map<Classes,TimeSlot> partAssign = createPartAssign(p);
	
		
		evalCheck eval = new evalCheck(partAssign,coursemin,labmin,pairpen,sectionpen);
		
		//System.out.println("The minimum penalty is: "+ eval.minCheck(p.getCourseSlots(),p.getLabSlots()));
		
		//System.out.println("The preference penalty is: "+ eval.preferenceCheck(p.getPreferences()));
		
		//System.out.println("The pair penalty is: "+eval.pairCheck(p.getPairs()));
		
	//	System.out.println("The lab section penalty is: "+eval.sectionLabCheck(p.getLabSections()));
		
		
		//System.out.println("The course section penalty is: "+eval.sectionCourseCheck(p.getCourseSections()));
		
		//System.out.println(p.getLabSlots().toString());
		//System.out.println(p.getCourseSlots().toString());
		
//		legalCheck lcheck = new legalCheck(partAssign);
//		if(lcheck.doAllChecks(p.getCourses(),p.getNonCompatible(),p.getUnwanted(),p.getFiveHundredCourses())){
//			System.out.println("The check passed \n");
//		}else{
//			System.out.println("The check failed\n");
//		}
//		if(lcheck.courseLabCheck(p.getCourses())){
//			System.out.println("Check courselab passed. Shoudn't happen");
//		}
		
		nodeData headNodeData = new nodeData(partAssign);
		
		//Map<Classes, TimeSlot> newthing = deepCopy(partAssign,p.getCourses().get(0),p.getCourseSlots().get(0));

		Tree<nodeData> andTree = new Tree<nodeData>(headNodeData);

		andTreeRecurse(p, andTree);
		
		
		
		assignTree tree = new assignTree(p,partAssign,eval);
		
		Map<Classes,TimeSlot> newTree = tree.createTree();
		
		for(Map.Entry<Classes, TimeSlot> entry: newTree.entrySet()){
			System.out.println("Class: "+entry.getKey().toString());
			System.out.println("Assignment: "+entry.getValue().toString());
		}
		
		
		
		
		
		
		
		/*
		System.out.println("BEGINNING OF PART ASSIGN: \n");
		for(Map.Entry<Classes,TimeSlot> entry: partAssign.entrySet()){
			System.out.println("The class: "+entry.getKey());
			System.out.println("The Assignment: "+entry.getValue().toString());
		}
		System.out.println("\n\nBEGINNING OF MADE TREE: \n");
		for(Map.Entry<Classes,TimeSlot> entry: newTree.entrySet()){
			System.out.println("The class: "+entry.getKey());
			System.out.println("The Assignment: "+entry.getValue());
		}
		*/
		/*
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
		*/
		
		
		
	}
	private static void andTreeRecurse(Parser p, Tree<nodeData> andTree) {
		if(andTree.getData().amDone()){
			evalCheck finalCheck = new evalCheck(andTree.getData().getCurrentAssignment(), coursemin , labmin, pairpen, sectionpen);
			//finalCheck.eval(p, newAssign);
		}
		
		ArrayList<Tree<nodeData>> children = treeFiller(andTree,p);
	
		andTree.addChildren(children);
		
		cullIllegalChildren(andTree,p);
		if(andTree.getChildren().size() == 0){
			System.out.println("No legal choices");
		}
		for(int i = 0; i <= andTree.getChildren().size();i++){
			Tree nextTree = andTree.getChildren().get(i);
			//recurse on nextTree
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
	
	private static void cullIllegalChildren(Tree<nodeData> t, Parser p){
		legalCheck gestapo = new legalCheck(null);
		for(int i = 0; i<t.getChildren().size();i++){
			Tree<nodeData> child = t.getChildren().get(i);
			gestapo.setAssign(child.getData().getCurrentAssignment());
			if(!gestapo.doAllChecks(p.getCourses(), p.getNonCompatible(), p.getUnwanted(), p.getFiveHundredCourses())){
				t.removeChild(child);
			}
		}
	}
	
	private static Map<Classes,TimeSlot> deepCopy(Map<Classes,TimeSlot> assign,Classes c, TimeSlot t){
		Map<Classes,TimeSlot> newAssign = new HashMap<Classes,TimeSlot>();
		for(Map.Entry<Classes,TimeSlot> entry: assign.entrySet()){
			newAssign.put(entry.getKey(), entry.getValue());
		}
		newAssign.put(c,t);
		return newAssign;
	}
	private static ArrayList<Map<Classes,TimeSlot>> deepCopy(Map<Classes,TimeSlot> assign,Classes c, ArrayList<TimeSlot> t){
		ArrayList<Map<Classes,TimeSlot>> newAssign = new ArrayList<Map<Classes,TimeSlot>>();
		for(int i = 0; i < t.size();i++){
			for(Map.Entry<Classes, TimeSlot> entry:assign.entrySet()){
				newAssign.get(i).put(entry.getKey(), entry.getValue());
			}
			newAssign.get(i).put(c, t.get(i));
		}
		return newAssign;
	}
	public static ArrayList<Tree<nodeData>> treeFiller(Tree <nodeData> t, Parser p){
        Map<Classes, TimeSlot> map = t.getData().getCurrentAssignment();
        for (Map.Entry<Classes, TimeSlot> entry : map.entrySet()) {
            TimeSlot curSlot = entry.getValue();
            ArrayList<Map<Classes,TimeSlot>> mapOfNodes = null;
            if (curSlot.isDollarSign() && (curSlot.trueIfCourseFalseIfLab())){
                mapOfNodes = deepCopy(t.getData().getCurrentAssignment(), entry.getKey(), p.getCourseSlots());
                
            }else if (curSlot.isDollarSign() && (!curSlot.trueIfCourseFalseIfLab())){
                mapOfNodes = deepCopy(t.getData().getCurrentAssignment(), entry.getKey(), p.getLabSlots());
            }
            if(curSlot.isDollarSign()){
	            ArrayList<Tree<nodeData>> childrenReturn = new ArrayList<Tree<nodeData>>();
	            for(int i = 0; i < mapOfNodes.size();i++){
	            	nodeData childData = new nodeData(mapOfNodes.get(i));
	            	Tree<nodeData> newChild = new Tree<nodeData>(childData);
	            	childrenReturn.add(newChild);
	            }
	            return childrenReturn;
            }
        }
        return null;

    }

}

