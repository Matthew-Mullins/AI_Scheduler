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
	
	float Actuapen_minFilled;
	float Actualpen_notpaired;
	float Actualpen_section;
	float Actualpen_seccdiff;
	float Actualpen_pref;
	
	public evalCheck(Map<Classes,TimeSlot> input,float coursemin,float labmin, float notpaired, float section,float penCoursemin, float penPref, float penNotPaired, float penSection){
		assign = input;
		pen_coursemin = coursemin;
		pen_labsmin = labmin;
		pen_notpaired = notpaired;
		pen_section = section;
		Actuapen_minFilled = penCoursemin;
		Actualpen_notpaired = penNotPaired;
		Actualpen_section = penSection;
		Actualpen_pref = penPref;
	}
	
	public void setAssign(Map<Classes,TimeSlot> newAssign){
		assign = newAssign;
	}
	
	//Checks if there are any timeSlots that are underfilled for an assign.
	//The penalty is applied for each course below the minimum.
	public float minCheck(ArrayList<CourseSlot> cs, ArrayList<LabSlot> ls){
	/*	Map<TimeSlot,Integer> timeSlotOccurs = new HashMap<TimeSlot,Integer>();
		for(TimeSlot slot : assign.values()){
			if(timeSlotOccurs.containsKey(slot)){
				timeSlotOccurs.put(slot, timeSlotOccurs.get(slot)+1);
			}else{
				timeSlotOccurs.put(slot, 1);
			}
		}
		*/
		
		
		float courseMin = 0;
		float labMin = 0;
		int numTimes = 0;
		int labTimes = 0;
		
		
		for(CourseSlot cSlot:cs){
			numTimes = 0;
			if(cSlot.curNumAssigned < cSlot.getMin()) {
				numTimes = cSlot.getMin() - cSlot.curNumAssigned;
			}
			courseMin += pen_coursemin * numTimes;
//			if(!cSlot.isDollarSign()){
//				int numberOfOccurences;
//				if(timeSlotOccurs.containsKey(cSlot)){
//					numberOfOccurences = timeSlotOccurs.get(cSlot);
//				}else{
//					numberOfOccurences = 0;
//				}
//				if(numberOfOccurences <= cSlot.getMin()) {
//					courseMin += ((cSlot.getMin() - numberOfOccurences) * pen_coursemin);
//				}
//			} else {
//				//courseMin += (cSlot.getMin() * pen_coursemin;
//			}
		}
		for(LabSlot lSlot:ls){
			labTimes =0;
			if(lSlot.curNumAssigned < lSlot.getMin()) {
				labTimes = lSlot.getMin() - lSlot.curNumAssigned;
			}
			labMin += pen_labsmin * numTimes;
		}
//		for(LabSlot lSlot:ls){
//			if(!lSlot.isDollarSign()){
//				int numberOfOccurences;
//				if(timeSlotOccurs.containsKey(lSlot)){
//					numberOfOccurences = timeSlotOccurs.get(lSlot);
//				}else{
//					numberOfOccurences = 0;
//				}
//				if(numberOfOccurences <= lSlot.getMin()) {
//					labMin += ((lSlot.getMin() - numberOfOccurences) * pen_labsmin);
//				}
//			} else {
//				//courseMin += (lSlot.getMin() * pen_labsmin;
//			}
//		}
		System.out.println(courseMin + labMin);
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
		
		for(int i = 0; i<preferences.size(); i++){
			//System.out.println("The class we are looking at is: "+preferences.get(i).getClasses().toString());
			//System.out.println("The time that is wants is: "+preferences.get(i).getTime().toString());
			//System.out.println("The Actual assigned slot is: "+assign.get(preferences.get(i).getClasses()).toString());
			
			
			//Assuming dollarsign will evaluate as not equal, so this will work
			if(assign.get(preferences.get(i).getClasses()) != preferences.get(i).getTime()) {
				
				//if(!assign.get(preferences.get(i).getClass()).isDollarSign()){
					penaltyTotal += preferences.get(i).getPenalty();
				//}
			}	
		}
		return penaltyTotal;
	}
	
	//scan through the list of proposed pairs, checking if the timeSlots for them in the assign 
	//are the same, incrementing the penalty if not
	public float pairCheck(ArrayList<pair<Classes,Classes>> pairs){
		float pairPenalty = 0;
		
		for(pair<Classes,Classes> coursePair : pairs) {
			Classes lCourse = coursePair.getLeft();
			Classes rCourse = coursePair.getRight();
			if (!(assign.get(lCourse) == assign.get(rCourse))) {
				//if(assign.get(lCourse).isDollarSign()){
				//	pairPenalty += pen_notpaired;
				//}
				if(!assign.get(lCourse).isDollarSign() && !assign.get(rCourse).isDollarSign()){
					pairPenalty += pen_notpaired;
				}
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
						if((!assign.get(courseSections.get(i).get(j)).isDollarSign())&&(!assign.get(courseSections.get(i).get(k)).isDollarSign())){
							sectionPenalty += pen_section;
						}
						//if(assign.get(courseSections.get(i).get(j)).isDollarSign()){
							//sectionPenalty += pen_section;
						//}
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
					//if(assign.get(labSections.get(i).get(j)).isDollarSign()){
					//	sectionPenalty += pen_section;
					//}
				}
			}
		}
		
		return sectionPenalty;
	}
}