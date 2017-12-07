package artificial.intelligence.cpsc;

public class LabSlot extends TimeSlot{
	
	public LabSlot(String day_, String startTime_, int labMax_, int labMin_){
		this.day = day_;
		this.startTime = startTime_;
		this.max = labMax_;
		this.min = labMin_;
	}

	public int getMax(){
		return max;
	}
	public int getMin(){
		return min;
	}
}
