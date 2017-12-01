package artificial.intelligence.cpsc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class evalCheck {
	Map<Classes,TimeSlot> assign;
	float pen_coursemin;
	float pen_labsmin;
	float pen_notpaired;
	float pen_section;
	
	
	public evalCheck(Map<Classes,TimeSlot> input,float coursemin,float labmin, float notpaired, float section){
		assign = input;
		pen_coursemin = coursemin;
		pen_labsmin = labmin;
		pen_notpaired = notpaired;
		pen_section = section;
	}
	
	//Checks if there are any timeSlots that are underfilled for an assign.
	//The penalty is applied for each course below the minimum.
	public float minCheck(){
		Map<TimeSlot,Integer> timeSlotOccurs = new HashMap<TimeSlot,Integer>();
		for(TimeSlot slot : assign.values()){
			if(timeSlotOccurs.containsKey(slot)){
				timeSlotOccurs.put(slot, timeSlotOccurs.get(slot)+1);
			}else{
				timeSlotOccurs.put(slot, 1);
			}
		}
		
		float courseMin = 0;
		float labMin = 0;
		
		for(TimeSlot slot :timeSlotOccurs.keySet()){
			int times = timeSlotOccurs.get(slot);
			
			if(slot instanceof CourseSlot){
				if(times < slot.getMin()){
					courseMin += ((slot.getMin() - times) * pen_coursemin);
				}
			}else{
				if(times< slot.getMin()){
					labMin += ((slot.getMin() - times) * pen_labsmin);
				}
			}
		}
		return courseMin + labMin;
	}
	
	
	//scan through the preferences of the professors, a collection of course/slot/triple combinations
	//check at each loop head if the Course from the last loop is still the course in question (as every preference
	//for a course needs to be violated before the penalty is incurred)
	
	//ASSUMED: The list of preferences parsed is grouped into courses, in other words, after a new Course value in the triple
	//is reached in the list, the courses already seen earlier in the list will not occur later, after a break of other courses
	//or labs

	public float preferenceCheck(ArrayList<preferenceTriple> preferences){
		Classes currentClass = null;
		float penaltyTotal  = 0;
		float loopPenalty = 0;
		boolean failedAllFlag = false;
		for(int i = 0; i< preferences.size(); i++){
			if((currentClass != preferences.get(i).getClasses())){
				if (failedAllFlag = false) {
					penaltyTotal += loopPenalty;
				}
				currentClass = preferences.get(i).getClasses();
				loopPenalty = 0;
				failedAllFlag = false;
			}
			
			if(assign.get(currentClass) == preferences.get(i).getTime()) {
				failedAllFlag = true;
			} else {
				loopPenalty = preferences.get(i).getPenalty();
			}
			
			
		}
		return penaltyTotal;
	}
	
	//scan through the list of proposed pairs, checking if the timeSlots for them in the assign 
	//are the same, incrementing the penalty if not
	public float pairCheck(ArrayList<pair<Course,Course>> pairs){
		float pairPenalty = 0;
		
		for(pair<Course,Course> coursePair : pairs) {
			Course lCourse = coursePair.getLeft();
			Course rCourse = coursePair.getRight();
			if (!(assign.get(lCourse) == assign.get(rCourse))) {
				pairPenalty += pen_notpaired;
			}
		}
		
		return pairPenalty;
	}
	
	//determine if same-numbered courses are in different time slots
	//Thoughts: penalty is applied for each pair, so does that mean 3 courses gets 3 penalties? (a-b, a-c, b-c) (assuming yes)
	
	//Assuming: a list of lists (or something similar) with the outer list being Courses 
	//and the inner being sections of that course.
	public float sectionCourseCheck(ArrayList<ArrayList<Course>> courseSections) {
		float sectionPenalty = 0;

		for(int i = 0; i < courseSections.size(); i++){
			for(int j = 0; j < courseSections.get(i).size(); j++) {
				for(int k = (j+1); k < courseSections.get(i).size(); k++) {
					if(assign.get(courseSections.get(i).get(j)) == assign.get(courseSections.get(i).get(k))){
						sectionPenalty += pen_section;
					}
				}
			}
		}
		
		return sectionPenalty;
	}
	
	//Same as the above, except for labs.
	//This can be a separate function since a hard constraint does not allow courses and labs to have the same timeslot.
	public float sectionLabCheck(ArrayList<ArrayList<Lab>> labSections) {
		float sectionPenalty = 0;

		for(int i = 0; i < labSections.size(); i++){
			for(int j = 0; j < labSections.get(i).size(); j++) {
				for(int k = (j+1); k < labSections.get(i).size(); k++) {
					if(assign.get(labSections.get(i).get(j)) == assign.get(labSections.get(i).get(k))){
						sectionPenalty += pen_section;
					}
				}
			}
		}
		
		return sectionPenalty;
	}
}