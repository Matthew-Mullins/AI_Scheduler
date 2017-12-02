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
		conflictMap.put("Mon 08:00", new ArrayList<String>(Arrays.asList("Mon 08:00", "Fri 08:00")));
		conflictMap.put("Mon 09:00", new ArrayList<String>(Arrays.asList("Mon 09:00", "Fri 08:00")));
		conflictMap.put("Mon 10:00", new ArrayList<String>(Arrays.asList("Mon 10:00", "Fri 10:00")));
		conflictMap.put("Mon 11:00", new ArrayList<String>(Arrays.asList("Mon 11:00", "Fri 10:00")));
		conflictMap.put("Mon 12:00", new ArrayList<String>(Arrays.asList("Mon 12:00", "Fri 12:00")));
		conflictMap.put("Mon 13:00", new ArrayList<String>(Arrays.asList("Mon 13:00", "Fri 12:00")));
		conflictMap.put("Mon 14:00", new ArrayList<String>(Arrays.asList("Mon 14:00", "Fri 14:00")));
		conflictMap.put("Mon 15:00", new ArrayList<String>(Arrays.asList("Mon 15:00", "Fri 14:00")));
		conflictMap.put("Mon 16:00", new ArrayList<String>(Arrays.asList("Mon 16:00", "Fri 16:00")));
		conflictMap.put("Mon 17:00", new ArrayList<String>(Arrays.asList("Mon 17:00", "Fri 16:00")));
		conflictMap.put("Mon 18:00", new ArrayList<String>(Arrays.asList("Mon 18:00", "Fri 18:00")));
		conflictMap.put("Mon 19:00", new ArrayList<String>(Arrays.asList("Mon 19:00", "Fri 18:00")));
		conflictMap.put("Mon 20:00", new ArrayList<String>(Arrays.asList("Mon 20:00")));

		//Tuesday conflics
		conflictMap.put("Tue 08:00", new ArrayList<String>(Arrays.asList("Tue 08:00", "Tue 9:00")));
		conflictMap.put("Tue 09:30", new ArrayList<String>(Arrays.asList("Tue 09:00", "Tue 10:00")));
		conflictMap.put("Tue 11:00", new ArrayList<String>(Arrays.asList("Tue 11:00", "Tue 12:00")));
		conflictMap.put("Tue 12:30", new ArrayList<String>(Arrays.asList("Tue 12:00", "Tue 13:00")));
		conflictMap.put("Tue 14:00", new ArrayList<String>(Arrays.asList("Tue 14:00", "Tue 15:00")));
		conflictMap.put("Tue 15:30", new ArrayList<String>(Arrays.asList("Tue 15:00", "Tue 16:00")));
		conflictMap.put("Tue 17:00", new ArrayList<String>(Arrays.asList("Tue 17:00", "Tue 18:00")));
		conflictMap.put("Tue 18:30", new ArrayList<String>(Arrays.asList("Tue 18:00", "Tue 19:00")));

		if (maxCheck()) {
			if (courseLabCheck()) {
				
			}
		}
	}
	
	/** Check if any timeslot has any more than the timeslot.max
	 * number of courses assigned to it via quick pass over the Map
	 * @return <tt>true</tt>  true if no timeslots are found to contain 
	 * more than their max in the pass
	 * simply have hardcoded 10 in here for testing purposes however 
	 * it will test against the courseSlot.Coursemax eventually*/
	public boolean maxCheck() {
		for (Map.Entry<Classes, TimeSlot> entry : assign.entrySet()) {
			TimeSlot curSlot = entry.getValue();
			if (curSlot.curNumAssigned > 10) {
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
		String courseTime = courseTimeSlot.getTime();
			for(int j =0; i<labs.size(); j++) {
				Lab lab = labs.get(j);
				TimeSlot labTimeSlot = assign.get(lab);
				String labTime = labTimeSlot.getTime();
				if(!timeCheck(labTime,courseTime)) {
					return false;
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
		Classes classOneTimeSlot = nonCompatible.getLeft(i); //get left value
		Classes classTwoTimeSlot = nonCompatible.getRight(i); //get right value
			if  ((!assign.get(classOneTimeSlot).equals(null)) & (!assign.get(classTwoTimeSlot).equals(null))){
			
				if ((assign.get(classOneTimeSlot)).equals(assign.get(classTwoTimeSlot))){
					return false;
				}
			}
		}
	}
	/**
	 * Check a course and a timeSlot, returning whether or not the classes timeSlot
	 * and the given slot are the same
	 * @param a : the class or lab to be looked at
	 * @param slot : the timeslot to compare to the priors assigned slot
	 * @return <tt>true</tt> in the case that the given slot is equal to the classes assigned slot
	 */
	public boolean unwantedCheck(List<pair<Classes,TimeSlot>> unwanted) {
		for (int i = 0; i <unwanted.size(); i++) {
			Classes unwantedTimeSlot = unwanted.getLeft(i);
			if (assign.containsKey(unwantedTimeSlot)){
				if (unwanted.getRight(i).equals(assign.get(unwantedTimeSlot))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check if two simple strings representing times from the timeSlot object will 'step on each others toes'
	 * @param timeSlot 
	 * @param timeSlot2
	 * @return <tt>true</tt> true if the string representation of the times will occupy the same time at any point.
	 */
	//this function checked that the first string is not a conflict of the second string, might have to also 
	//check second string
	private boolean timeCheck(TimeSlot timeSlot, TimeSlot timeSlot2) {
		if (conflictMap.containsKey(timeSlot)) {
			ArrayList<String> current = conflictMap.get(timeSlot);
			if (current.contains(timeSlot2)) {
				return true;	
			}
		}		
		return false;
	}
	
	
}
