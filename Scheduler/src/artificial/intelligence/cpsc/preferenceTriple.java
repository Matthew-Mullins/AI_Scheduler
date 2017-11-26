package artificial.intelligence.cpsc;

//
//Simple structure to hold a TimeSlot, Course and Penalty if that course is not in
//the given timeslot. Parser should order these into like courses and create a list of them
//from the input file

public class preferenceTriple {
	private final TimeSlot slot;
	private final Classes courseOrLab;
	private final float penalty;
	
	public preferenceTriple(TimeSlot givenSlot, Classes givenCourseOrLab, float pen){
		slot = givenSlot;
		courseOrLab = givenCourseOrLab;
		penalty = pen;
	}
	
	public TimeSlot getTime(){return slot; }
	public Classes getClasses(){return courseOrLab; }
	public float getPenalty(){return penalty; }

}
