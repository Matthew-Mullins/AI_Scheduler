package artificial.intelligence.cpsc;

public class CourseSlot extends TimeSlot{
	private int courseMax;
	private int courseMin;
	
	public CourseSlot(String day_, String startTime_, int courseMax_, int courseMin_){
		this.day = day_;
		this.startTime = startTime_;
		this.courseMax = courseMax_;
		this.courseMin = courseMin_;
	}
	
	public String toString(){
		return ("Day: " + this.day + "\t\tStart Time: " + this.startTime + "\tCourse Max: " + this.courseMax + "\tCourse Min: " + this.courseMin);
	}
}
