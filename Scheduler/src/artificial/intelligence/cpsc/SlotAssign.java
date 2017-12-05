package artificial.intelligence.cpsc;

import java.util.ArrayList;
import java.util.HashMap;

public class SlotAssign {

	private static assignTree tree;
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
			//System.out.println("Courses remain to be assigned. There are: "+tree.getCourses().size()+" left.\n");
			// If the tree is not empty we know that more branches are likely available
			// take the next class in the list and remove it from the list
			
			nextClass = tree.getCourses().get(tree.getCourses().size() - 1);
			tree.getCourses().remove(tree.getCourses().size() - 1);
			
			//System.out.println("\n\nCLASS INFORMATION: "+nextClass.toString());
			
			// We get the list of timeslots available for the next class
			ArrayList<TimeSlot> times = tree.getTimes(nextClass);
			
			//System.out.println("\n\nCOURSES FOR THAT CLASS: "+times.toString());
			
			nextAssign = new ArrayList<pair<TimeSlot,Float>>();
			// For each timeslot, we evaluate it and if it is a valid solution, we add it into
			// nextAssign
			for(TimeSlot x: times)
			{
				float eval = tree.evaluateThis(nextClass, x);
				if(eval >= 0)
				{
					nextAssign.add(new pair<TimeSlot, Float>(x,eval));
				}
			}
			
			// While the list of 	TimeSlots is not empty
			while(!nextAssign.isEmpty())
			{
				int index = 0;
				float best = Float.MAX_VALUE;
				// We choose the current best TimeSlot
				for(int i = 0; i < nextAssign.size(); i++)
				{
					if(nextAssign.get(i).getRight() < best)
					{
						index = i;
						best = nextAssign.get(i).getRight();
					}
				}
				// That TimeSlot is assigned to the current mapping and SlotAssign is recursively called
				//System.out.println("The current class is being assigned to: "+nextAssign.get(index).getLeft().toString());
				//System.out.println("Creating New Node with class: "+nextClass.toString()+" With the timeSlot: " + nextAssign.get(index).getLeft().toString());
				
				tree.assignThis(nextClass, nextAssign.get(index).getLeft());
				
				new SlotAssign(nextClass, nextAssign.get(index).getLeft(), tree);
				// Once it returns, we remove the TimeSlot that has been explored along with its mapping
				//System.out.println("Removing :"+ nextAssign.get(index).toString()+"Whose hashcode is :" +nextAssign.hashCode());
				nextAssign.remove(index);
				tree.removeThis(nextClass);
			}
			tree.getCourses().add(aClass);
		}
		else
		{
			// Should reach this part once all courses have been assigned
			// We evaluate the tree as a solution and compare it with the current best solution
			float finalEval = tree.evaluateCurr();
			System.out.println("No more courses to assign");
			if(finalEval < tree.getMin())
			{
				
				System.out.println("Found a solution:");
				//LOGICCHECK
				//if(finalEval < 0)
					//System.out.println("ERROR: SHOULD NOT PRINT");
				
				// if the new solution is better than the old, replaces the min, and copies the new solution into result
				tree.setMin(finalEval);
				tree.copyResult();
			}
		}
		
		// Finally, we add the course back into the list of courses to be assigned
	}
	
}
