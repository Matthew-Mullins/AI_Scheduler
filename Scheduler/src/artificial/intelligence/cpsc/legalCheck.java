package artificial.intelligence.cpsc;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class legalCheck {

	Map<Classes,TimeSlot> assign;
	
	/**
	  * Constructor.
	  * 
	  * @param passed (required) a collection of the form Map, consisting of Courses or Labs and their
	  * associated TimeSlot
	  */
	public legalCheck(Map<Classes,TimeSlot> passed) {
		assign = passed;
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
	public boolean courseLabCheck(Course course) {
		List<Lab> labs = course.getLabs();
		TimeSlot courseTimeSlot = assign.get(course);
		String courseTime = courseTimeSlot.getTime();
		for(int i =0; i<labs.size(); i++) {
			Lab lab = labs.get(i);
			TimeSlot labTimeSlot = assign.get(lab);
			String labTime = labTimeSlot.getTime();
			if(!timeCheck(labTime,courseTime)) {
				return false;
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
	public boolean compatibleCheck(Classes classOne, Classes classTwo) {
		TimeSlot classOneTimeSlot = assign.getValue(classOne);
		TimeSlot classTwoTimeSlot = assign.getValue(classTwo);
		return timeCheck(classTwoTimeSlot.getTime(),classOneTimeSlot.getTime());
	}
	/**
	 * Check a course and a timeSlot, returning whether or not the classes timeSlot
	 * and the given slot are the same
	 * @param a : the class or lab to be looked at
	 * @param slot : the timeslot to compare to the priors assigned slot
	 * @return <tt>true</tt> in the case that the given slot is equal to the classes assigned slot
	 */
	public boolean unwantedCheck(Classes a, TimeSlot slot) {
		return slot.equals(assign.getValue(a));
	}

	/**
	 * Check if two simple strings representing times from the timeSlot object will 'step on each others toes'
	 * @param timeOne 
	 * @param timeTwo
	 * @return <tt>true</tt> true if the string representation of the times will occupy the same time at any point.
	 */
	//TODO this function will likely end up needing to take two TimeSlots rather than just Strings, to account for the fact
	//that labs and courses have different lengths, and treat them differently when checking to see if they will be concurrent
	//at any point
	private boolean timeCheck(String timeOne, String timeTwo) {
		// TODO Auto-generated method stub
		return false;
	}
	
}

