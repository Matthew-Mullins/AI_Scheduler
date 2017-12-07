package artificial.intelligence.cpsc;

public class TimeSlot {
	public String day;
	public String startTime;
	public int curNumAssigned;		//Current number of Labs/Courses assigned to a slot
	private boolean dollarSign;
	private boolean courseOrLab;
	
	public int max;
	public int min;
	
	public TimeSlot(){
	}
	public TimeSlot(String day_, String startTime_, int labMax_, int labMin_, boolean labOrCourse){
		day = day_;
		startTime = startTime_;
		max = labMax_;
		min = labMin_;
		courseOrLab = labOrCourse;
	}
	
	public void addAssigned(){
		curNumAssigned++;
	}
	public void removedAssigned(){
		curNumAssigned--;
	}
	
	public void setMax(int newMax){}
	public int getMin() {
		// TODO Auto-generated method stub
		return min;
	}

	public String getDayTime(){
		return day+" "+startTime;
	}
	
	public String getTime() {
		// TODO Auto-generated method stub
		return startTime;
	}

	public boolean isDollarSign(){
		return dollarSign;
	}
	
	//Initial partAssign will create a full list of classes just assigned to dollarSign set slots
	public void setDollarSign(boolean b){
		dollarSign = b;
	}
	public String toString(){
		return day+" "+startTime+", "+max+", "+min+", assigned: "+curNumAssigned;
	}

	public int getMax() {
		return max;
	}
	public boolean trueIfCourseFalseIfLab(){
		return courseOrLab;
	}
}
