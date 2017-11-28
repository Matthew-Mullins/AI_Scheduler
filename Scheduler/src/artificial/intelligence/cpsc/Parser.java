package artificial.intelligence.cpsc;

import java.io.*;
import java.util.ArrayList;

public class Parser {
	
	String line = null;
	
	private ArrayList<CourseSlot> courseSlots = new ArrayList<CourseSlot>();
	private ArrayList<LabSlot> labSlots = new ArrayList<LabSlot>();
	
	private ArrayList<Course> courses = new ArrayList<Course>();
	private ArrayList<Lab> labs = new ArrayList<Lab>();

	private ArrayList<pair<Classes,Classes>> pairs;
	private ArrayList<pair<Classes,Classes>> nonCompatible = new ArrayList<pair<Classes,Classes>>();
	private ArrayList<pair<Classes,TimeSlot>> unWanted;
	private ArrayList<preferenceTriple> preferences;
	
	private final String[] headers = {
			"Course slots:\n" + 
			"Lab slots:\n" + 
			"Courses:\n" + 
			"Labs:\n" + 
			"Not compatible:\n" + 
			"Unwanted:\n" + 
			"Preferences:\n" + 
			"Pair:\n" + 
			"Partial assignments:\n"};
	
	public Parser(String inputFileName){
		try{
			//Read Text File
			FileReader fileReader = new FileReader(inputFileName);
			
			//Wrap FileReader in BufferedReader
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			//Print first block of input file, expected to be of the form:
			//Name:
			//EXAMPLENAME
			//
			while(!(line = bufferedReader.readLine()).isEmpty()){
				System.out.println(line);
			}
			
			
			
			//Course Slots
			/**
			 * Parse through the block of text expected to be CourseSlots, dividing each line up into 
			 * a new CourseSlot object
			 */
			//First cut through any empty lines
			while((line = bufferedReader.readLine()).isEmpty()){
				System.out.println("EMPTY");
			}
			System.out.println("\n" + line);
			
			while(!(line = bufferedReader.readLine()).isEmpty()){
				parseCourseSlot(line);				
			}
			
			
			
			//Lab Slots
			/**
			 * Parse through the block of text expected to be Lab slots, dividing each line up into 
			 * a new LabSlot object
			 */
			//First cut through any empty lines
			while((line = bufferedReader.readLine()).isEmpty()){
				System.out.println("EMPTY");
			}
			System.out.println("\n" + line);
			
			while(!(line = bufferedReader.readLine()).isEmpty()){
				parseLabSlot(line);
			}
			
			
			
			//Courses
			//Much the same as above
			while((line = bufferedReader.readLine()).isEmpty()) {
				System.out.println("EMPTY");
			}
			System.out.println("\n" + line);
			while(!(line = bufferedReader.readLine()).isEmpty()) {
				parseCourse(line);
			}
			
			//Labs
			//Rinsing and repeating; nothing new here
			while((line = bufferedReader.readLine()).isEmpty()) {
				System.out.println("EMPTY");
			}
			while(!(line = bufferedReader.readLine()).isEmpty()) {
				parseLab(line);
			}
			
			//Not Compatible
			while((line = bufferedReader.readLine()).isEmpty()){
				System.out.println("EMPTY");
			}
			while(!(line = bufferedReader.readLine()).isEmpty()) {
				parseNonCompatible(line);
			}
			
			//Unwanted
			
			//Preference
			
			//Pair
			
			//Partial Assignments
			
			//Close file
			bufferedReader.close();
			
		} catch (FileNotFoundException e){
			System.out.println("Unable to open file '" + inputFileName + "'");
		} catch (IOException e){
			System.out.println("Error reading file '" + inputFileName + "'");
		}
	}
	private void parseNonCompatible(String line2) {
		String[] pairString;
		pair<Classes,Classes> nonCompat;
		pairString = line2.split(",");
		String[] firstArg = pairString[0].split(" ");
		for(int i = 0; i <firstArg.length;i++){
			firstArg[i] = firstArg[i].replace("\\s+", "");
		}
		String[] secondArg = pairString[1].split(" ");
		for(int i = 0; i <secondArg.length;i++){
			secondArg[i] = secondArg[i].replace("\\s+", "");
		}
		if(firstArg[firstArg.length-2].equals("LEC")){
			//first arg lec
			Course left = lookUpCourse(firstArg);
			if(secondArg[secondArg.length-2].equals("LEC")){
				//second arg lec
				Course right = lookUpCourse(secondArg);
				nonCompat = new pair(left,right);
			}else{
				//second arg tut or lab
				Lab right = lookUpLab(secondArg);
				nonCompat = new pair(left,right);
			}
		}else{
			Lab left = lookUpLab(firstArg);
			///first arg tut or Lab
			if(secondArg[secondArg.length-2].equals("LEC")){
				//second arg lec
				Course right = lookUpCourse(secondArg);
				nonCompat = new pair(left,right);
			}else{
				//second arg tut or lab
				Lab right = lookUpLab(secondArg);
				nonCompat = new pair(left,right);
			}
		}
	}
	
	/**
	 * Function that takes a line that comes from the Labs: block and parses it
	 * into relevant Lab info, like Department, ClassNumber, Section, and 
	 * its parent Course which is looked up with the BASIC FOR NOW lookupCourse function
	 * Will also add the relevant 
	 * @param line2
	 */
	private void parseLab(String line2) {
		String[] info;
		Lab l;
		int numTerms = line2.split(" ").length;
		info = line2.split(" ");
		l = new Lab(info[0].replaceAll("\\s+", ""),
				info[1].replaceAll("\\s+", ""),
				info[info.length - 1].replaceAll("\\s+", ""));
		String[] courseInfo = new String[4];
		if(numTerms ==4){
			for(int i=0;i<2;i++){
				courseInfo[i] = info[i].replaceAll("\\s+", "");
			}
			courseInfo[2] = "LEC";
			courseInfo[3] = "01";
		}else{
			for(int i = 0;i<4;i++){
				courseInfo[i] = info[i].replaceAll("\\s+","");
			}
		}
		if(lookUpCourse(courseInfo) == null){
			System.out.println("Something bad happened: the requested Course described by the lab does not exist, or Rhys is bad at coding.");
		}
		l.setBelongsTo(lookUpCourse(courseInfo));
		lookUpCourse(courseInfo).addLab(l);
		labs.add(l);		
	}
	
	/**
	 * Function iterates through the lab list, and finds class that matches the string of info
	 * given
	 * @param labInfo 0-Department, 1-CourseNumber, 3-CourseSection,5-LabSection
	 * @return
	 */
	private Lab lookUpLab(String[] labInfo){
		String[] workingLabInfo = new String[6];
		if(labInfo.length == 4){
			workingLabInfo[0] = labInfo[0];
			workingLabInfo[1] = labInfo[1];
			workingLabInfo[2] = "LEC";
			workingLabInfo[3] = "01";
			workingLabInfo[4] = labInfo[2];
			workingLabInfo[5] = labInfo[3];
		}
		for(int i =0; i < labs.size();i++){
			Lab tempLab = labs.get(i);
			if((tempLab.getDepartment().equals(workingLabInfo[0]))&&
				(tempLab.getClassNumber().equals(workingLabInfo[1]) &&
				(tempLab.getBelongsTo().getSection().equals(workingLabInfo[3])) &&
				(tempLab.getSection().equals(workingLabInfo[5])))){
				return labs.get(i);
			}
			
		}
		
		return null;
	}
	
	/**
	 * Simple function iterates through the course list and finds the class that
	 * matches all the info given to it in courseInfo. 
	 * @param courseInfo Array of size 4, first element is DEPARTMENT, second is NUMBER, third is always LEC, and fourth is SECTION
	 * @return a course from the ArrayList which matches the given course
	 */
	private Course lookUpCourse(String[] courseInfo) {
		for(int i =0; i < courses.size();i++){
			Course tempCourse = courses.get(i);
			if(tempCourse.getClassNumber().equals(courseInfo[1]) &&
			   tempCourse.getDepartment().equals(courseInfo[0])  &&
			   tempCourse.getSection().equals(courseInfo[3])){
				return courses.get(i);
			}
		}
		return null;
	}
	/**
	 * Function that splits an identified Course information input line
	 * into its appropriate variable
	 * @param line2: read from the file, course information following Course: \n Header
	 */
	private void parseCourse(String line2) {
		String[] info = new String[4];
		info = line2.split(" ");
		Course c = new Course(info[0].replaceAll("\\s+", ""), 
								info[1].replaceAll("\\s+", ""), 
								info[3].replaceAll("\\s+", ""));
		courses.add(c);		
	}

	/**
	 * Function which will perform the functionality of splitting an identified LabSlot input line
	 * into an appropriate variable
	 * @param line2: read from the file, following the header Lab slots: \n
	 */
	private void parseLabSlot(String line2) {
		String[] info = new String[4];
		info = line2.split(",");
		LabSlot ls = new LabSlot(info[0].replaceAll("\\s+", ""), 
										info[1].replaceAll("\\s+", ""), 
										Integer.parseInt(info[2].replaceAll("\\s+", "")), 
										Integer.parseInt(info[3].replaceAll("\\s+", "")));
		labSlots.add(ls);		
	}


	/**
	 * Function which will perform the functionality of splitting an identified CourseSlot input line
	 * into an appropriate variable
	 * @param line2: read from the file, following the header Course slots: \n
	 */
	private void parseCourseSlot(String line2) {
		String[] info = new String[4];
		info = line2.split(",");
		CourseSlot cs = new CourseSlot(info[0].replaceAll("\\s+", ""), 
										info[1].replaceAll("\\s+", ""), 
										Integer.parseInt(info[2].replaceAll("\\s+", "")), 
										Integer.parseInt(info[3].replaceAll("\\s+", "")));
		courseSlots.add(cs);		
	}


	/**
	 * Simple function to check if a String representing a read line
	 * from the file is equal to a header of the input file, and if so, 
	 * what one via its place in the headers list
	 * Returns -1 if its not a header
	 */
	private int equalHeader(String line2) {
		for(int i = 0; i < headers.length;i++) {
			if(line2.equals(headers[i])) {
				return i;
			}
		}
		return -1;
	}
	
	
	
	public ArrayList<CourseSlot> getCourseSlots() {
		return this.courseSlots;
	}
	
	public ArrayList<LabSlot> getLabSlots() {
		return this.labSlots;
	}
	
	public ArrayList<Course> getCourses() {
		return this.courses;
	}
	
	public ArrayList<Lab> getLabs() {
		return this.labs;
	}
}
