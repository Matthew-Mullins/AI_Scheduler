package artificial.intelligence.cpsc;

import java.io.*;
import java.util.ArrayList;

//push Testing...

public class Parser {
	
	String line = null;
	
	private ArrayList<CourseSlot> courseSlots = new ArrayList<CourseSlot>();
	private ArrayList<LabSlot> labSlots = new ArrayList<LabSlot>();
	private ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
	
	private ArrayList<Course> courses = new ArrayList<Course>();
	private ArrayList<Lab> labs = new ArrayList<Lab>();

	private ArrayList<pair<Classes,Classes>> pairs = new ArrayList<pair<Classes,Classes>>();
	private ArrayList<pair<Classes,Classes>> nonCompatible = new ArrayList<pair<Classes,Classes>>();
	private ArrayList<pair<Classes,TimeSlot>> unWanted = new ArrayList<pair<Classes,TimeSlot>>();
	private ArrayList<preferenceTriple> preferences = new ArrayList<preferenceTriple>();
	private ArrayList<pair<Classes,TimeSlot>> partialAssignment = new ArrayList<pair<Classes,TimeSlot>>();
	
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
			
			
			timeSlots.addAll(courseSlots);
			timeSlots.addAll(labSlots);
			
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
			//This one necessitates the lookUpLab function too
			while((line = bufferedReader.readLine()).isEmpty()){
				System.out.println("EMPTY");
			}
			while(!(line = bufferedReader.readLine()).isEmpty()) {
				parseNonCompatible(line);
			}
			
			/*
			//Unwanted
			while((line = bufferedReader.readLine()).isEmpty()){
				System.out.println("EMPTY");
			}
			while(!(line = bufferedReader.readLine()).isEmpty()) {
				parseUnwanted(line);
			}
			
			//Preference
			while((line = bufferedReader.readLine()).isEmpty()){
				System.out.println("EMPTY");
			}
			while(!(line = bufferedReader.readLine()).isEmpty()) {
				parsePreferences(line);
			}
			
			//Pair
			while((line = bufferedReader.readLine()).isEmpty()){
				System.out.println("EMPTY");
			}
			while(!(line = bufferedReader.readLine()).isEmpty()) {
				parsePairs(line);
			}
			
			//Partial Assignments
			while((line = bufferedReader.readLine()).isEmpty()){
				System.out.println("EMPTY");
			}
			while(!(line = bufferedReader.readLine()).isEmpty()) {
				parsePartialAssignments(line);
			}
			*/
			
			//Close file
			bufferedReader.close();
			
		} catch (FileNotFoundException e){
			System.out.println("Unable to open file '" + inputFileName + "'");
		} catch (IOException e){
			System.out.println("Error reading file '" + inputFileName + "'");
		}
	}
	/**
	 * TODO generally worried that the pulling of a thing from the ArrayList won't create a true ref to that object, might need to check into more about this and test what the effects of it are. Errors might be hard to find in the general workings of the parser
	 * 
	 * this one functions almost exactly the same as parseUnwanted, save where
	 * it saves the final result. DOes the general song and dance of cleaning up the line,
	 * and looking up the appropriate lecture/lab/slot... with the appropriate function
	 * and created the final product out of the returned results. 
	 * @param line2 Ideally of the form "CLASSINFO, DAY, TIME"
	 */
	private void parsePartialAssignments(String line2) {
		pair<Classes,TimeSlot> partAssignLine;
		Classes partClass;
		TimeSlot partSlot;
		
		
		
		String[] partAssignInfo = line2.split(",");
		String[] classInfo = partAssignInfo[0].split(" +");
		String[] dayInfo = new String[2];
		dayInfo[0] = partAssignInfo[1].replace("\\s+", "");
		dayInfo[1] = partAssignInfo[2].replace("\\s+", "");
		
		classInfo = clearWhiteSpace(classInfo);
		
		if(classInfo[classInfo.length-1].equals("LEC")){
			//its a lecture
			partClass = lookUpCourse(classInfo);
			partSlot = lookUpLabSlot(dayInfo);

		}else{
			//its a lab
			partClass = lookUpLab(classInfo);
			partSlot = lookUpCourseSlot(dayInfo);

		}

		partAssignLine = new pair<Classes,TimeSlot>(partClass,partSlot);
		
		partialAssignment.add(partAssignLine);
	}
	/**
	 * Function to take a string beneath the Pairs: \n header
	 * Splits the string into the two chunks of class1 and class2, and cleans those up
	 * and then looks them up, depending on if they are lectures or labs
	 * @param line2 ideally of the form "CLASSONEINFO,CLASSTWOINFO"
	 */
	private void parsePairs(String line2) {
		pair<Classes,Classes> pairPair; //dumb name
		
		String[] pairInfo = line2.split(",");
		String[] firstClass = pairInfo[0].split(" +");
		String[] secondClass = pairInfo[1].split(" +");
		
		firstClass = clearWhiteSpace(firstClass);
		secondClass = clearWhiteSpace(secondClass);
		if(firstClass[firstClass.length-2].equals("LEC")){
			if(secondClass[secondClass.length-2].equals("LEC")){
				pairPair = new pair<Classes,Classes>(lookUpCourse(firstClass),lookUpCourse(secondClass));
			}else{
				pairPair = new pair<Classes,Classes>(lookUpCourse(firstClass),lookUpLab(secondClass));
			}
		}else{
			if(secondClass[secondClass.length-2].equals("LEC")){
				pairPair = new pair<Classes,Classes>(lookUpLab(firstClass),lookUpCourse(secondClass));
			}else{
				pairPair = new pair<Classes,Classes>(lookUpLab(firstClass),lookUpLab(secondClass));
			}
		}
		pairs.add(pairPair);
	}
	/**
	 * Function that I found I was using a lot so created method. Iterates through
	 * list of strings, removing any whitespace so they are pure useable information
	 * @param stringArray array of strings from the input or somewhere else
	 * @return the array but any whitespace is removed.
	 * 
	 * e.g. {"Hello   ","  there."} becomes
	 * {"Hello","there."}
	 */
	private String[] clearWhiteSpace(String[] stringArray) {
		for(int i =0;i<stringArray.length;i++){
			stringArray[i] = stringArray[i].replace("\\s+", "");
			stringArray[i] = stringArray[i].replace("\\n+", "");
		}
		return stringArray;
	}
	/**
	 * Function which takes the line under Preference header, ideally of the form:
	 * "DAY,TIME, CLASSINFO, PENALTYNUMBER"
	 * splitting them by those ','s and finding the corresponding lab or course slot
	 * and corresponding lab or course, creating a preferenceTriple with those values
	 * @param line2 line found beneath the preference Header, form found above
	 */
	private void parsePreferences(String line2) {
		preferenceTriple preference;
		String[] preferenceInfo = line2.split(",");
		String[] timeSlotInfo = {preferenceInfo[0].replace("\\s+", ""),preferenceInfo[1].replace("\\s+", "")};
		String[] classInfo = preferenceInfo[2].split(" +");
		String penalty = preferenceInfo[3];
		if(classInfo[classInfo.length-2].equals("LEC")){
			preference = new preferenceTriple(lookUpCourseSlot(timeSlotInfo),lookUpCourse(classInfo),Float.parseFloat(penalty));
		}else{
			preference = new preferenceTriple(lookUpLabSlot(timeSlotInfo),lookUpLab(classInfo),Float.parseFloat(penalty));
		}
		preferences.add(preference);
	}
	/**
	 * parse the unwanted section to the input file, following header Unwanted: \n
	 * Split line into the class info, day and start time, and combine the latter two into timeInfo string
	 * and the former into classInfo, looking up each in the appropriate timeSlot and Classes ArrayList
	 * and add the unwantedPair created from the two into the main List
	 * @param line2 found under the header above, ideally of the form: "LECTUREINFORMATION,DAY,STARTTIME"
	 */
	private void parseUnwanted(String line2) {
		pair<Classes,TimeSlot> unwantedPair;
		Classes unwantedClass;
		TimeSlot unwantedTimeSlot;
		
		String[] unwantedInfo = line2.split(",");
		String[] classInfo = unwantedInfo[0].split(" +");
		String[] dayInfo = new String[2];
		dayInfo[0] = unwantedInfo[1].replace("\\s+", "");
		dayInfo[1] = unwantedInfo[2].replace("\\s+", "");
		
		classInfo = clearWhiteSpace(classInfo);
		
		if(classInfo[classInfo.length-1].equals("LEC")){
			//its a lecture
			unwantedClass = lookUpCourse(classInfo);
			unwantedTimeSlot = lookUpLabSlot(dayInfo);

		}else{
			//its a lab
			unwantedClass = lookUpLab(classInfo);
			unwantedTimeSlot = lookUpCourseSlot(dayInfo);

		}
		unwantedPair = new pair<Classes,TimeSlot>(unwantedClass,unwantedTimeSlot);
		
		unWanted.add(unwantedPair);
	}
	/**
	 * Specific cases for the lookUpSlot general function
	 * TODO see the lookUpSlot todo entry
	 * @param dayInfo
	 * @return
	 */
	private TimeSlot lookUpCourseSlot(String[] dayInfo) {
		for(int i =0; i < courseSlots.size();i++){
			CourseSlot tempSlot = courseSlots.get(i);
			if(tempSlot.day.equals(dayInfo[0]) && (tempSlot.startTime.equals(dayInfo[1]))){
				return courseSlots.get(i);
			}
		}
		//TODO create a handler for null cases for this function
		return null;
	}
	private TimeSlot lookUpLabSlot(String[] dayInfo) {
		for(int i =0; i < labSlots.size();i++){
			LabSlot tempSlot = labSlots.get(i);
			if(tempSlot.day.equals(dayInfo[0]) && (tempSlot.startTime.equals(dayInfo[1]))){
				return labSlots.get(i);
			}
		}
		//TODO create a handler for null cases for this function
		return null;
	}
	/**
	 * Simple method to parse through the ArrayList of Timeslots given
	 * some information in an array of strings
	 * TODO make this a general function to use instead of checking the type of class everytime. Maybe group this and lookUpCourse/Lab together into one main function, taking both the courseInfo and timeInfo in one method?
	 * @param dayInfo an array where 1-DAYSTRING(e.g. 'MO', 'TU'...) 2-TIMESTRING(e.g. 8:00...)
	 * @return the appropriate day from the ArrayList, if it exists
	 */
	private TimeSlot lookUpTimeSlot(String[] dayInfo) {
		for(int i =0; i < timeSlots.size();i++){
			TimeSlot tempSlot = timeSlots.get(i);
			if(tempSlot.day.equals(dayInfo[0]) && (tempSlot.startTime.equals(dayInfo[1]))){
				return timeSlots.get(i);
			}
		}
		return null;
	}
	/**
	 * Function to do the job of parsing lines identified beneath a NonCompatible: header
	 * Will basically split the function into two halves, as identified by the comma. Each half ideally
	 * describes a course or lab. Branching if tree to identify the type of each (assuming the second last 
	 * word in each line is either LEC or LAB or TUT)
	 * Then simply look up the lab or course's object in the appropriate ArrayList, and create a new pair
	 * using those found objects
	 * Doesn't handle the null exceptions yet TODO 
	 * 
	 * @param line2 Line of the form "String of Class","String of second Class" under header NonCompatible:\n
	 */
	private void parseNonCompatible(String line2) {
		String[] pairString;
		pair<Classes,Classes> nonCompat;
		pairString = line2.split(",\\s*");
		String[] firstArg = pairString[0].split(" +");
		String[] secondArg = pairString[1].split(" +");

		System.out.println("First Half of the thing "+pairString[0]);
		System.out.println("Second half of the string, after the , " + pairString[1]);
		
		firstArg = clearWhiteSpace(firstArg);
		
		secondArg = clearWhiteSpace(secondArg);

		if(firstArg[firstArg.length-2].equals("LEC")){
			Course left = lookUpCourse(firstArg);
			if(secondArg[secondArg.length-2].equals("LEC")){
				Course right = lookUpCourse(secondArg);
				nonCompat = new pair<Classes,Classes>(left,right);
			}else{
				Lab right = lookUpLab(secondArg);
				nonCompat = new pair<Classes,Classes>(left,right);
			}
		}else{
			Lab left = lookUpLab(firstArg);
			if(secondArg[secondArg.length-2].equals("LEC")){
				Course right = lookUpCourse(secondArg);
				nonCompat = new pair<Classes,Classes>(left,right);
			}else{
				Lab right = lookUpLab(secondArg);
				nonCompat = new pair<Classes,Classes>(left,right);
			}
		}
		nonCompatible.add(nonCompat);
	}
	
	/**
	 * Function that takes a line that comes from the Labs: block and parses it
	 * into relevant Lab info, like Department, ClassNumber, Section, and 
	 * its parent Course which is looked up with the BASIC FOR NOW lookupCourse function
	 * Will also add the relevant lab to the children of the looked up Course
	 * @param line2 as recognized under the header
	 */
	private void parseLab(String line2) {
		String[] info;
		Lab l;
		
		info = line2.split(" +");
		int numTerms = info.length;
		info = clearWhiteSpace(info);
				
		l = new Lab(info[0],
				info[1],
				info[info.length - 1]);
		String[] courseInfo = new String[4];
		if(numTerms ==4){
			for(int i=0;i<2;i++){
				courseInfo[i] = info[i];
			}
			courseInfo[2] = "LEC";
			courseInfo[3] = "01";
		}else{
			for(int i = 0;i<4;i++){
				courseInfo[i] = info[i];
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
		System.out.println("Length: "+labInfo.length);
		for(int i = 0;i<labInfo.length;i++) {
			System.out.println(i+ " place "+ labInfo[i]);
		}
		if(labInfo.length == 4){
			
			workingLabInfo[0] = labInfo[0];
			workingLabInfo[1] = labInfo[1];
			workingLabInfo[2] = "LEC";
			workingLabInfo[3] = "01";
			workingLabInfo[4] = labInfo[2];
			workingLabInfo[5] = labInfo[3];
		}else {
			workingLabInfo = labInfo;
		}
		
		System.out.println("Dep "+workingLabInfo[0]+" Coursenumber "+workingLabInfo[1]+" coursesection "+workingLabInfo[3]+" labsection "+workingLabInfo[5]);
		
		for(int i =0; i < labs.size();i++){
			Lab tempLab = labs.get(i);
			if((tempLab.getDepartment().equals(workingLabInfo[0]))&&
				(tempLab.getClassNumber().equals(workingLabInfo[1]) &&
				(tempLab.getBelongsTo().getSection().equals(workingLabInfo[3])) &&
				(tempLab.getSection().equals(workingLabInfo[5])))){
				return labs.get(i);
			}
			
		}
		//@TODO TODO
		System.out.println("This should never happen, Lab not found. THROW EXCEPTION HERE");
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
		//@TODO TODO
		System.out.println("This should never happen, Course not found. THROW EXCEPTION HERE");

		return null;
	}
	/**
	 * Function that splits an identified Course information input line
	 * into its appropriate variable
	 * @param line2: read from the file, course information following Course: \n Header
	 */
	private void parseCourse(String line2) {
		String[] info = new String[4];
		info = line2.split(" +");
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
	 * TODO add this functionality to check each header before enacting whatever function fits. General decision on what to parse based off return from this function
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
