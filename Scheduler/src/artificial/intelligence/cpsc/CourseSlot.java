package artificial.intelligence.cpsc;

public class CourseSlot extends TimeSlot{
	
	public CourseSlot(String day_, String startTime_, int courseMax_, int courseMin_){
		this.day = day_;
		this.startTime = startTime_;
		this.max = courseMax_;
		this.min = courseMin_;
	}
	

	public int getMax(){
		return max;
	}
	public int getMin(){
		return min;
	}
	public void setMax(int newMax){
		max = newMax;
	}
}
