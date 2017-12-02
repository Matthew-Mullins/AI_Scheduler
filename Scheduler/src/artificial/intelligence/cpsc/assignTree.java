package artificial.intelligence.cpsc;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class assignTree {
	private Map<Classes,TimeSlot> assignNode;
	private float min = Float.MAX_VALUE;
	private Map<Classes,TimeSlot> result = null;
	private ArrayList<Classes> availableCourses;
	
	public assignTree(Map<Classes,TimeSlot> aTree, ArrayList<Classes> available)
	{
		assignNode = aTree;
		availableCourses = available;
	}
	
	//getter functions for all above
	public Map<Classes,TimeSlot> getAssign(){return assignNode;}
	public float getMin(){return min;}
	public ArrayList<Classes> getCourses(){return availableCourses;}
	
	//setter
	public void setMin(float min){this.min = min;}
	
	/**
	 * When createTree is called, a tree is created using the ArrayList of courses passed
	 * into the function.
	 * 
	 * @param available: An array list of courses that need to be assigned
	 * @return a mapping that corresponds to the most optimal solution to the assignment
	 */
	public Map<Classes,TimeSlot> createTree()
	{
		new SlotAssign(null,null,this);
		return result;
	}
	
	
	/**
	 * Function that takes the current assignNode mapping and copies it into result
	 */
	public void copyResult()
	{
		Map<Classes, TimeSlot> copy = new HashMap<Classes, TimeSlot>();
		for(Map.Entry<Classes, TimeSlot> entry : assignNode.entrySet())
		{
			copy.put(entry.getKey(), entry.getValue());
		}
		
		result = copy;
	}
	
	/**
	 * Function that takes in a class and timeslot and evaluates the current assignment
	 * with the added key.
	 * 
	 * @param course: the course being added
	 * @param time: the timeslot of the course being added
	 * @return evaluation, where evaluation < 0 if hard constraints fail or receives a value worse than the min
	 */
	public float evaluateThis(Classes course, TimeSlot time)
	{
		float evaluation = -1;
		assignNode.put(course, time);
		
		// EVALUATE ASSIGNNODE
		if(evaluation > min)
		{
			evaluation = -1;
		}
		
		assignNode.remove(course);
		return evaluation;
	}
	
	/**
	 * function that evaluates the finished assignment. only called when tree has assigned all courses
	 * 
	 * @return evaluation, should not return -1 when this is called
	 */
	public float evaluateCurr()
	{
		float evaluation = -1;
		//EVALUATE ASSIGNNODE AND RETURN
		return evaluation;
	}
	
	
	/**
	 * Assigns a course and a time to the tree
	 * 
	 * @param course: course being assigned
	 * @param time: timeslot of the course being assigned
	 */
	public void assignThis(Classes course, TimeSlot time)
	{
		assignNode.put(course, time);
	}
	
	
	/**
	 * Removes a course from the tree
	 * 
	 * @param course: course being removed from the mapping
	 */
	public void removeThis(Classes course)
	{
		assignNode.remove(course);
	}
}
