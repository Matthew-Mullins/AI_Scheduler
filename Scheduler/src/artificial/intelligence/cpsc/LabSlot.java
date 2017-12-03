package artificial.intelligence.cpsc;

public class LabSlot extends TimeSlot{
	
	
	public LabSlot(String day_, String startTime_, int labMax_, int labMin_){
		this.day = day_;
		this.startTime = startTime_;
		this.max = labMax_;
		this.min = labMin_;
	}

	public String toString(){
		return ("Day: " + this.day + "\tStart Time: " + this.startTime + "\tLab Max: " + this.max + "\tLab Min: " + this.min+"\tCurrent Assigned: "+this.curNumAssigned+"\n");
		
	}
	public int getMax(){
		return max;
	}
	public int getMin(){
		return min;
	}
}
