package artificial.intelligence.cpsc;

import java.util.Map;

public class nodeData{
	private Map<Classes,TimeSlot> currentAssignment;
	
	public nodeData(Map<Classes,TimeSlot> assignment){
		currentAssignment = assignment;
	}

	public Map<Classes, TimeSlot> getCurrentAssignment() {
		return currentAssignment;
	}

	public void setCurrentAssignment(Map<Classes, TimeSlot> currentAssignment) {
		this.currentAssignment = currentAssignment;
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
