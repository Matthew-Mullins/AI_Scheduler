package artificial.intelligence.cpsc;

import java.util.ArrayList;
import java.util.HashMap;

public class SlotAssign {

	private assignTree tree;
	private ArrayList<pair<TimeSlot,Float>> nextAssign;
	private Classes nextClass;
	private Classes aClass;
	private TimeSlot aTime;
	
	public SlotAssign(Classes someClass, TimeSlot someTime, assignTree aTree)
	{
		aClass = someClass;
		aTime = someTime;
		tree = aTree;
		
		if(!tree.getCourses().isEmpty())
		{
			nextClass = tree.getCourses().get(tree.getCourses().size() - 1);
			tree.getCourses().remove(tree.getCourses().size() - 1);
			
			// BLOCK THAT GETS TIMESLOTS OF nextClass
			// FOR EACH TIMESLOT, CALL tree.evaluateThis(nextClass, TIME)
			// CREATE PAIR OF TIMESLOT AND EVALUATION IF EVALUATION IS <=0
			
			while(!nextAssign.isEmpty())
			{
				int index = 0;
				float best = Float.MAX_VALUE;
				for(int i = 0; i < nextAssign.size(); i++)
				{
					if(nextAssign.get(i).getRight() < best)
					{
						index = i;
						best = nextAssign.get(i).getRight();
					}
				}
				tree.assignThis(nextClass, nextAssign.get(index).getLeft());
				new SlotAssign(nextClass, nextAssign.get(index).getLeft(), tree);
				nextAssign.remove(index);
				tree.removeThis(nextClass);
			}
		}
		else
		{
			float finalEval = tree.evaluateCurr();
			if(finalEval < tree.getMin())
			{
				//LOGICCHECK
				if(finalEval < 0)
					System.out.println("ERROR: SHOULD NOT PRINT");
				
				tree.setMin(finalEval);
				tree.copyResult();
			}
		}
		
		tree.getCourses().add(aClass);
	}
	
}
