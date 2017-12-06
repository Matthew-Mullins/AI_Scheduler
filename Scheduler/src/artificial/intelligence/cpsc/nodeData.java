package artificial.intelligence.cpsc;

import java.util.Map;

public class nodeData{
	private Map<Classes,TimeSlot> currentAssignment;
	private Classes currentClass;
	private TimeSlot currentTimeSlot;
	
	public nodeData(Map<Classes,TimeSlot> assignment, Classes aClass, TimeSlot timeSlot){
		currentAssignment = assignment;
		currentClass = aClass;
		currentTimeSlot = timeSlot;
	}

	public Map<Classes, TimeSlot> getCurrentAssignment() {
		return currentAssignment;
	}

	public void setCurrentAssignment(Map<Classes, TimeSlot> currentAssignment) {
		this.currentAssignment = currentAssignment;
	}

	public Classes getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(Classes currentClass) {
		this.currentClass = currentClass;
	}

	public TimeSlot getCurrentTimeSlot() {
		return currentTimeSlot;
	}

	public void setCurrentTimeSlot(TimeSlot currentTimeSlot) {
		this.currentTimeSlot = currentTimeSlot;
	}
	
	public boolean amDone(){
		for(Map.Entry<Classes,TimeSlot> entry:currentAssignment.entrySet()){
			if(entry.getValue().isDollarSign()){
				return false;
			}
		}
		return true;
	}
	
}
