package artificial.intelligence.cpsc;

import java.util.HashMap;
import java.util.Map;

public class evalCheck {
	Map<Course,TimeSlot> assign;
	float pen_coursemin;
	float pen_labsmin;
	float pen_notpaired;
	float pen_section;
	
	
	public evalCheck(Map<Course,TimeSlot> input,float coursemin,float labmin, float notpaired, float section){
		assign = input;
		pen_coursemin = coursemin;
		pen_labsmin = labmin;
		pen_notpaired = notpaired;
		pen_section = section;
	}
	
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
	
	
	//TODO scan through the preferences of the professors, a collection of course/slot/triple combinations
	//check at each loop head if the Course from the last loop is still the course in question (as every preference
	//for a course needs to be violated before the penalty is incurred)
	
	//ASSUMED: The list of preferences parsed is grouped into courses, in other words, after a new Course value in the triple
	//is reached in the list, the courses already seen earlier in the list will not occur later, after a break of other courses
	//or labs

	public float preferenceCheck(preferenceTriple[] preferences){
		Classes currentClass = null;
		float penaltyTotal  = 0;
		float loopPenalty = 0;
		boolean failedAllFlag = false;
		for(int i = 0; i< preferences.length; i++){
			if((currentClass != preferences[i].getClasses())){
				if (failedAllFlag = false) {
					penaltyTotal += loopPenalty;
				}
				currentClass = preferences[i].getClasses();
				loopPenalty = 0;
				failedAllFlag = false;
			}
			
			if(assign.get(currentClass) == preferences[i].getTime()) {
				failedAllFlag = true;
			} else {
				loopPenalty = preferences[i].getPenalty();
			}
			
			
		}
		return penaltyTotal;
	}
	
	//TODO scan through the list of proposed pairs, checking if the timeSlots for them in the assign 
	//are the same, incrementing the penalty if not
	public float pairCheck(pair<Course,Course>[] pairs){
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
	
	//TODO determine if same-numbered courses are in different time slots
	//Thoughts: penalty is applied for each pair, so does that mean 3 courses gets 3 penalties? (a-b, a-c, b-c) (assuming yes)
	
	//Assuming: a list of lists (or something similar) with the outer list being Courses 
	//and the inner being sections of that course.
	public float sectionCheck(Course[][]courseSections) {
		float sectionPenalty = 0;

		for(int i = 0; i < courseSections.length; i++){
			
			for(int j = 0; j < courseSections[i].length; j++) {
				
				for(int k = (j+1); k < courseSections[i].length; k++) {
					if(assign.get(courseSections[i][j]) == assign.get(courseSections[i][k])){
						sectionPenalty += pen_section;
					}
				}
			}
			
		}
		
		return sectionPenalty;
	}
}