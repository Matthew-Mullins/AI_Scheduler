
/*
package artificial.intelligence.cpsc;

import java.util.ArrayList;

/**
 * 
 * Function that will take in tree with no children, and create all possible 
 * legal children that are a a result of a class pulled from a global list or queue of classes 
 * remaining
 *
 *

public class branchMaker {
	
	public branchMaker(assignTree tree){
		ArrayList<pair<Classes,TimeSlot>> returnChildren;
		Map<Classes,TimeSlot> nodeAssignments = tree.getAssignments();
		SlotAssign slotMachine = new SlotAssign();
		Classes nextClass = remainingClasses.getNext();
		
		pair<Classes,Timeslot> returnChild;
		if(nextClass.IsLab){
			for(int i = 0; i<remainingLabSlots;i++){
				returnChild = slotAssign(nextClass,remainingLabSlot[i]);
				if(returnChild != null){
					returnChildren.add(returnChild);
				}
			}
		}else{
			for(int i = 0; i<remainingCourseSlots;i++){
				returnChild = slotAssign(nextClass,remainingCourseSlot[i]);
				if(returnChild != null){
					returnChildren.add(returnChild);
				}
			}
		}
		return returnChildren;
	}
}
*/