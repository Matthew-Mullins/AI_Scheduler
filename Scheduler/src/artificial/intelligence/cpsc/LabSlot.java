package artificial.intelligence.cpsc;

public class LabSlot extends TimeSlot{
	private int labMax;
	private int labMin;
	
	public LabSlot(String day_, String startTime_, int labMax_, int labMin_){
		this.day = day_;
		this.startTime = startTime_;
		this.labMax = labMax_;
		this.labMin = labMin_;
	}

	public String toString(){
		return ("Day: " + this.day + "\tStart Time: " + this.startTime + "\tLab Max: " + this.labMax + "\tLab Min: " + this.labMin+"\n");
	}
}
