package artificial.intelligence.cpsc;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class assignTree {
	private static Parser parser;
	private Map<Classes,TimeSlot> assignNode;
	private static float min = Float.MAX_VALUE;
	private static float partMin = Float.MAX_VALUE;
	private static Map<Classes,TimeSlot> result = null;
	private ArrayList<Classes> availableCourses = new ArrayList<Classes>();
	private TimeSlot dollarSign = new TimeSlot();
	private evalCheck evaluater;
	
	public assignTree(Parser p, Map<Classes,TimeSlot> aTree,evalCheck eval)
	{
		dollarSign.setDollarSign(true);
		parser = p;
		assignNode = aTree;
		for(Map.Entry<Classes,TimeSlot> entry: assignNode.entrySet()){
			if(entry.getValue().isDollarSign()){
				availableCourses.add(entry.getKey());
			}
		}
		evaluater = eval;
	}
	
	//getter functions for all above
	public Map<Classes,TimeSlot> getAssign(){return assignNode;}
	public float getMin(){return min;}
	public float getPartMin(){return partMin;}
	public ArrayList<Classes> getCourses(){return availableCourses;}
	public static Map<Classes,TimeSlot> getResult(){return result;}
	public static Parser getParser() {return parser;}
	
	//setter
	public void setMin(float givenMin){min = givenMin;}
	public void setPartMin(float givenMin){partMin = givenMin;}
	
	/**
	 * Function that creates an And Tree and returns the best possible solution.
	 * 
	 * @return Map<Classes,TimeSlot> that corresponds to the best solution, or null if no solution was found.
	 */
	public Map<Classes,TimeSlot> createTree()
	{
		new SlotAssign(null,null,this);
		return result;
	}
	
	/**
	 * Function that takes the current assignNode mapping and copies it into result
	 */
	public void copyResult()
	{
		Map<Classes, TimeSlot> copy = new HashMap<Classes, TimeSlot>();
		for(Map.Entry<Classes, TimeSlot> entry : assignNode.entrySet())
		{
			copy.put(entry.getKey(), entry.getValue());
		}
		
		result = copy;
	}
	
	/**
	 * Function that takes in a class and timeslot and evaluates the current assignment
	 * with the added key.
	 * 
	 * @param course: the course being added
	 * @param time: the timeslot of the course being added
	 * @return evaluation, where evaluation < 0 if hard constraints fail or receives a value worse than the min
	 */
	public float evaluateThis(Classes course, TimeSlot time)
	{
		float evaluation = 0;
		assignNode.put(course, time);
		time.addAssigned();
		//System.out.println("Evaluating:" +course.toString()+" In Slot: "+time.toString());
		
		legalCheck lc = new legalCheck(assignNode);
		if(!lc.doAllChecks(parser.getCourses(),parser.getLabs(),parser.getNonCompatible(),parser.getUnwanted(),parser.getFiveHundredCourses(),parser.getEveningCourses(),parser.getEveningLabs(),parser)){
			//System.out.println("It failed\n");
			assignNode.put(course, dollarSign);
			time.removedAssigned();
			return -1;
		}
	//	evaluation = evaluateCurr();
		evaluater.setAssign(assignNode);
		evaluation += evaluater.wPenMinFilled * evaluater.minCheck(parser.getCourseSlots(),parser.getLabSlots());
		evaluation += evaluater.wPenNotPaired * evaluater.pairCheck(parser.getPairs());
		evaluation += evaluater.wPenSection * evaluater.sectionCourseCheck(parser.getCourseSections());
//		evaluation += evaluater.sectionLabCheck(parser.getLabSections());
		if(evaluation > partMin)
		{
			evaluation = -1;
		}
		//System.out.println("It passed\n");
		assignNode.put(course,dollarSign);
		time.removedAssigned();
		return evaluation;
	}
	
	/**
	 * function that evaluates the finished assignment. only called when tree has assigned all courses
	 * Only soft constraints need to be tested as if a hard constraint was broken, it should have been caught in the
	 * previous node.
	 * 
	 * @return evaluation, should not return -1 when this is called
	 */
	public float evaluateCurr()
	{
		evaluater.setAssign(assignNode);
		float evaluation = 0;
		evaluation += evaluater.wPenMinFilled * evaluater.minCheck(parser.getCourseSlots(),parser.getLabSlots());
		evaluation += evaluater.wPenPref * evaluater.preferenceCheck(parser.getPreferences());
		evaluation += evaluater.wPenNotPaired * evaluater.pairCheck(parser.getPairs());
		evaluation += evaluater.wPenSection * evaluater.sectionCourseCheck(parser.getCourseSections());
		return evaluation;
		 
	}
	
	public float evaluatePart() {
		evaluater.setAssign(assignNode);
		float evaluation = 0;
		evaluation += evaluater.wPenNotPaired * evaluater.pairCheck(parser.getPairs());
		evaluation += evaluater.wPenSection * evaluater.sectionCourseCheck(parser.getCourseSections());
		return evaluation;
	}
	
	/**
	 * Assigns a course and a time to the tree
	 * 
	 * @param course: course being assigned
	 * @param time: timeslot of the course being assigned
	 */
	public void assignThis(Classes course, TimeSlot time)
	{

		assignNode.put(course, time);
		assignNode.get(course).addAssigned();

	}
		
	/**
	 * Removes a course from the tree
	 * 
	 * @param course: course being removed from the mapping
	 */
	public void removeThis(Classes course)
	{
		//System.out.println("Before removing: Course: "+parser.getCourseSlots().get(0).curNumAssigned+"Lab: "+parser.getLabSlots().get(0).curNumAssigned);
		assignNode.get(course).removedAssigned();
		assignNode.put(course,dollarSign);
		//System.out.println("After removing: Course: "+parser.getCourseSlots().get(0).curNumAssigned+"Lab: "+parser.getLabSlots().get(0).curNumAssigned);
	}
	
	/**
	 * Function that takes in a class and returns a list of possible timeslots it can
	 * go into
	 * 
	 * @param aClass: the class we want timeslots for
	 * @return ArrayList<TimeSlot> of times
	 */
	public ArrayList<TimeSlot> getTimes(Classes aClass)
	{
		ArrayList<TimeSlot> times = new ArrayList<TimeSlot>();
		if(aClass instanceof Course)
		{
			ArrayList<CourseSlot> cTimes = parser.getCourseSlots();
			times.addAll(cTimes);
			return times;
		}
		else if(aClass instanceof Lab)
		{
			ArrayList<LabSlot> lTimes = parser.getLabSlots();
			times.addAll(lTimes);
			return times;
		}
		
		
		
		else return null;
	}
}
