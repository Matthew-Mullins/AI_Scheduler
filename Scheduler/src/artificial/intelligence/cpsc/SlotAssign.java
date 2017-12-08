package artificial.intelligence.cpsc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SlotAssign {

	private static assignTree tree;
	private ArrayList<pair<TimeSlot,Float>> nextAssign;
	private Classes nextClass;
	private SlotAssign nextNode;
	private final Classes aClass;
	private final TimeSlot aTime;
	
	public SlotAssign(Classes someClass, TimeSlot someTime, assignTree aTree)
	{
		aClass = someClass;
		aTime = someTime;
		tree = aTree;
		this.getNextNode();
	}
	
	public synchronized void getNextNode()
	{
		if(!tree.getCourses().isEmpty())
		{
			//System.out.println("Courses remain to be assigned. There are: "+tree.getCourses().size()+" left.\n");
			// If the tree is not empty we know that more branches are likely available
			// take the next class in the list and remove it from the list
			//System.out.println("\n COURSES TO ASSIGN: " + tree.getCourses().toString());
			nextClass = tree.getCourses().get(tree.getCourses().size() - 1);
			tree.getCourses().remove(tree.getCourses().size() - 1);
			
			//System.out.println("\n\nCLASS INFORMATION: "+nextClass.toString());
			
			// We get the list of timeslots available for the next class
			ArrayList<TimeSlot> times = tree.getTimes(nextClass);
			int timeSize = times.size();
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
			
			if(nextAssign.size() <= 0)
			{
				tree.getCourses().add(nextClass);
				//System.out.println("NO TIMES");
			}
			// While the list of 	TimeSlots is not empty
			
			else
			{
				int size = nextAssign.size();
				while(!nextAssign.isEmpty())
				{
					int index = nextAssign.size() - 1;
					float best = Float.MAX_VALUE;
//					// We choose the current best TimeSlot
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
					//System.out.println("ASSIGNING - " + nextClass + " : " + nextAssign.get(index).getLeft().getTime());
					nextNode = new SlotAssign(nextClass, nextAssign.get(index).getLeft(), tree);
					// Once it returns, we remove the TimeSlot that has been explored along with its mapping
					//nextAssign.get(index).getLeft().removedAssigned();
					tree.removeThis(nextClass);
					nextAssign.remove(index);
				}
				tree.getCourses().add(nextClass);
				//System.out.println(nextClass.toString() + " is BACKTRACKING");
			}
		}
		else
		{
			// Should reach this part once all courses have been assigned
			// We evaluate the tree as a solution and compare it with the current best solution
			float finalEval = tree.evaluateCurr();
			//System.out.println("No more courses to assign");	
			
			if(finalEval < tree.getMin())
			{
				System.out.println("Found a solution:");
				float partEval = tree.evaluatePart();
				tree.setPartMin(partEval);
				tree.setMin(finalEval);
				tree.copyResult();
			}
		}
	}
}
	

