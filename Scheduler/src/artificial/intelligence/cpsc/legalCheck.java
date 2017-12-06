package artificial.intelligence.cpsc;

import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



public class legalCheck {

	Map<String, ArrayList<String>> conflictMap = new HashMap<String, ArrayList<String>>();
	
	Map<Classes,TimeSlot> assign;
		
	/**
	  * Constructor.
	  * 
	  * @param passed (required) a collection of the form Map, consisting of Courses or Labs and their
	  * associated TimeSlot
	  */
	public legalCheck(Map<Classes,TimeSlot> passed) {
		assign = passed;
		conflictMap.put("MO 8:00", new ArrayList<String>(Arrays.asList("MO 8:00", "Fri 8:00")));
		conflictMap.put("MO 9:00", new ArrayList<String>(Arrays.asList("MO 9:00", "Fri 8:00")));
		conflictMap.put("MO 10:00", new ArrayList<String>(Arrays.asList("MO 10:00", "Fri 10:00")));
		conflictMap.put("MO 11:00", new ArrayList<String>(Arrays.asList("MO 11:00", "Fri 10:00")));
		conflictMap.put("MO 12:00", new ArrayList<String>(Arrays.asList("MO 12:00", "Fri 12:00")));
		conflictMap.put("MO 13:00", new ArrayList<String>(Arrays.asList("MO 13:00", "Fri 12:00")));
		conflictMap.put("MO 14:00", new ArrayList<String>(Arrays.asList("MO 14:00", "Fri 14:00")));
		conflictMap.put("MO 15:00", new ArrayList<String>(Arrays.asList("MO 15:00", "Fri 14:00")));
		conflictMap.put("MO 16:00", new ArrayList<String>(Arrays.asList("MO 16:00", "Fri 16:00")));
		conflictMap.put("MO 17:00", new ArrayList<String>(Arrays.asList("MO 17:00", "Fri 16:00")));
		conflictMap.put("MO 18:00", new ArrayList<String>(Arrays.asList("MO 18:00", "Fri 18:00")));
		conflictMap.put("MO 19:00", new ArrayList<String>(Arrays.asList("MO 19:00", "Fri 18:00")));
		conflictMap.put("MO 20:00", new ArrayList<String>(Arrays.asList("MO 20:00")));

		//TUsday conflics
		conflictMap.put("TU 8:00", new ArrayList<String>(Arrays.asList("TU 8:00", "TU 9:00")));
		conflictMap.put("TU 9:30", new ArrayList<String>(Arrays.asList("TU 9:00", "TU 10:00")));
		conflictMap.put("TU 11:00", new ArrayList<String>(Arrays.asList("TU 11:00", "TU 12:00")));
		conflictMap.put("TU 12:30", new ArrayList<String>(Arrays.asList("TU 12:00", "TU 13:00")));
		conflictMap.put("TU 14:00", new ArrayList<String>(Arrays.asList("TU 14:00", "TU 15:00")));
		conflictMap.put("TU 15:30", new ArrayList<String>(Arrays.asList("TU 15:00", "TU 16:00")));
		conflictMap.put("TU 17:00", new ArrayList<String>(Arrays.asList("TU 17:00", "TU 18:00")));
		conflictMap.put("TU 18:30", new ArrayList<String>(Arrays.asList("TU 18:00", "TU 19:00")));


	}
	
	/** Check if any timeslot has any more than the timeslot.max
	 * number of courses assigned to it via quick pass over the Map
	 * @return <tt>true</tt>  true if no timeslots are found to contain 
	 * more than their max in the pass
	 * simply have hardcoded 10 in here for testing purposes however 
	 * it will test against the courseSlot.Coursemax eventually*/
	
	public boolean doAllChecks(List<Course> Courses, List<pair<Classes,Classes>> nonCompatible, ArrayList<pair<Classes,TimeSlot>> unwanted,ArrayList<Course> fiveHundreds) {
			if (maxCheck()) {
				if (courseLabCheck(Courses)) {
					if (compatibleCheck(nonCompatible)) {
						if (unwantedCheck(unwanted)) {
							//if(fiveHundredCheck(fiveHundreds)){
									return true;
							//}
							
						}
					}
				}
			}
			return false;
	
	}
	
	public boolean maxCheck() {
		for (Map.Entry<Classes, TimeSlot> entry : assign.entrySet()) {
			TimeSlot curSlot = entry.getValue();
			if (curSlot.curNumAssigned > curSlot.getMax()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if all labs for a given input course are at different times 
	 * @param course to be checked against its associated labs
	 * @return <tt>true</tt> if none of the labs contain the same timeslot
	 * as the course. False else.
	 */
	public boolean courseLabCheck(List<Course> Courses) {
		for (int i = 0; i <Courses.size(); i++) {
			Course temp = Courses.get(i);
			List<Lab> labs = temp.getLabs();
			TimeSlot courseTimeSlot = assign.get(temp);
			//System.out.println("The assignment time is: "+ assign.get(temp).toString());
			if(!courseTimeSlot.isDollarSign()){
				String courseTime = courseTimeSlot.getDayTime();
				for(int j =0; j<labs.size(); j++) {
					Lab lab = labs.get(j);
					TimeSlot labTimeSlot = assign.get(lab);
					String labTime = labTimeSlot.getDayTime();
					if(labTime.equals(courseTime)) {
						return false;
					}	
				
				}
			}
		}
		return true;
	}
		
	/**
	 * Check two courses which are noncompatible, and review whether they collide with 
	 * one another where timeslot is concerned
	 * @param classOne First class to be compared
	 * @param classTwo Second class to be compared
	 * @return the value of timeCheck when passed the classes assigned TimeSlot
	 */
	public boolean compatibleCheck(List<pair<Classes,Classes>> nonCompatible) {
		for (int i = 0; i <nonCompatible.size(); i++) {
		Classes classOneTimeSlot = nonCompatible.get(i).getLeft(); //get left value
		Classes classTwoTimeSlot = nonCompatible.get(i).getRight(); //get right value
			if  ((!assign.get(classOneTimeSlot).equals(null)) & (!assign.get(classTwoTimeSlot).equals(null))){
				if(!assign.get(classOneTimeSlot).isDollarSign() && !assign.get(classTwoTimeSlot).isDollarSign()){
					if ((assign.get(classOneTimeSlot)).equals(assign.get(classTwoTimeSlot))){
						return false;
					}
				}
			}
		}
		return true;
	}
	/**
	 * Check a course and a timeSlot, returning whether or not the classes timeSlot
	 * and the given slot are the same
	 * @param a : the class or lab to be looked at
	 * @param slot : the timeslot to compare to the priors assigned slot
	 * @return <tt>true</tt> in the case that the given slot is equal to the classes assigned slot
	 */
	public boolean unwantedCheck(ArrayList<pair<Classes,TimeSlot>> unwanted) {
		for (int i = 0; i <unwanted.size(); i++) {
			
			Classes unwantedTimeSlot = unwanted.get(i).getLeft();
			//System.out.println(unwantedTimeSlot.toString());
			if (assign.containsKey(unwantedTimeSlot)){
				//System.out.println("Unwanted: "+unwanted.get(i).getRight().toString()+" Actual: "+assign.get(unwantedTimeSlot).toString());
				if (unwanted.get(i).getRight().equals(assign.get(unwantedTimeSlot))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks to see if all of the classes that are of fiveHundred Level
	 * are in the different slots
	 * @param fiveHundredCourses; list of all 500 level courrses
	 * @return true if all classes are in different slots, false else
	 */
	public boolean fiveHundredCheck(ArrayList<Course> fiveHundredCourses){
		int i;
		int j;
		for(i =0; i < fiveHundredCourses.size();i++){
			Course temp = fiveHundredCourses.get(i);
			if(!assign.get(temp).isDollarSign()){
				for(j = i+1;j<fiveHundredCourses.size();j++){
					Course temp2 = fiveHundredCourses.get(j);
					if(!assign.get(temp2).isDollarSign()){
						if(assign.get(temp).equals(assign.get(temp2))){
							return false;
						}
					}	
				}
			}
		}
		return true;
	}
	
	public boolean eveningCheck(ArrayList<Course> eveningCourses){
		for(int i =0;i<eveningCourses.size();i++){
			Course temp = eveningCourses.get(i);
			if(!assign.get(temp).isDollarSign()){
				if(!isEvening(assign.get(temp).getTime())){
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean isEvening(String startTime) {
		if(startTime.length() == 5){
			String hoursPlace = startTime.substring(0,1);
			int hoursInt = Integer.parseInt(hoursPlace);
			if(hoursInt>=16){
				return true;
			}
		}
		return false;
	}

	public boolean nullCheck() {
		for(Map.Entry<Classes,TimeSlot> entry: assign.entrySet()){
			//System.out.println("Currenty Value: "+entry.getValue() +" Current Key: "+entry.getKey());
			if(entry.getValue().isDollarSign() || entry.getValue() == null || entry.getValue().day == null){
				return false;
			}
		}
		return true;
	}

}

	

	/**
	 * Check if two simple strings representing times from the timeSlot object will 'step on each others toes'
	 * @param timeSlot 
	 * @param timeSlot2
	 * @return <tt>true</tt> true if the string representation of the times will occupy the same time at any point.
	 */
	//this function checked that the first string is not a conflict of the second string, might have to also 
	//check second string
/**	private boolean timeCheck(String timeslot1, String timeSlot2) {
		if (conflictMap.containsKey(timeSlot)) {
			ArrayList<String> current = conflictMap.get(timeSlot);
			if (current.contains(timeSlot2)) {
				return true;	
			}
		}		
		return false;
	}
	
	
}*/
