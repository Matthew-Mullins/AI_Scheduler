package artificial.intelligence.cpsc;

import java.util.ArrayList;
import java.util.Map;

public class assignTree {
	public Map<Course,TimeSlot> assignNode;
	public float min = 65535;
	public Map<Course,TimeSlot> result = null;
	public TimeSlot[] availableTimeSlots;
	
	//arraylist to remember what has been done and what needs to yet be done
	public ArrayList<Map<Course,TimeSlot>> history = null;
	
	public ArrayList<assignTree> children = new ArrayList<assignTree>();
	
	//getter functions for all above
	
	
	
}
