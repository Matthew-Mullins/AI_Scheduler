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
	public boolean maxCheck() {
		for (Map.Entry<Classes, TimeSlot> entry : assign.entrySet()) {
			TimeSlot curSlot = entry.getValue();
			if (curSlot.curNumAssigned > curSlot.getMin()) {
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
			if(!courseTimeSlot.isDollarSign()){
				String courseTime = courseTimeSlot.getDayTime();
				for(int j =0; i<labs.size(); j++) {
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
			System.out.println(unwantedTimeSlot.toString());
			if (assign.containsKey(unwantedTimeSlot)){
				System.out.println("Unwanted: "+unwanted.get(i).getRight().toString()+" Actual: "+assign.get(unwantedTimeSlot).toString());
				if (unwanted.get(i).getRight().equals(assign.get(unwantedTimeSlot))) {
					return false;
				}
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
