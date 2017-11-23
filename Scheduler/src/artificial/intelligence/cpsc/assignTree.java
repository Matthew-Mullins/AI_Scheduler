package artificial.intelligence.cpsc;

public class assignTree {
	public Map<Course,Timeslot> assignNode;
	public float min = MAX_VALUE;
	public Map<Course,TimeSlot> result = null;
	public TimeSlot[] availableTimeSlots;
	
	//arraylist to remember what has been done and what needs to yet be done
	public ArrayList<Map<Course,TimeSlot>> history = null;
	
	public ArrayList<assignTree> children = new ArrayList<assignTree>();
	
	//getter functions for all above
	
	
	
}
