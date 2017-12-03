package artificial.intelligence.cpsc;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
	
	String line = null;
	
	private ArrayList<CourseSlot> courseSlots = new ArrayList<CourseSlot>();
	private ArrayList<LabSlot> labSlots = new ArrayList<LabSlot>();
	
	private ArrayList<Course> courses = new ArrayList<Course>();
	private ArrayList<Lab> labs = new ArrayList<Lab>();

	private ArrayList<pair<Classes,Classes>> pairs = new ArrayList<pair<Classes,Classes>>();
	private ArrayList<pair<Classes,Classes>> nonCompatible = new ArrayList<pair<Classes,Classes>>();
	private ArrayList<pair<Classes,TimeSlot>> unWanted = new ArrayList<pair<Classes,TimeSlot>>();
	private ArrayList<preferenceTriple> preferences = new ArrayList<preferenceTriple>();
	private ArrayList<pair<Classes,TimeSlot>> partialAssignment = new ArrayList<pair<Classes,TimeSlot>>();
	
	boolean tuesdayMeetingOn = true;
	
	
	int fiveHundredCourseCount = 0;
	int MaximumCourses = 0;
	int MaximumLabs = 0;
	
	int eveningCourses = 0;
	int eveningLabs = 0;
	
	int eveningCourseSlots = 0;
	int eveningLabSlots = 0;
	
	private List<String> headers = new ArrayList<>(Arrays.asList("Course slots:", 
			"Lab slots:",
			"Courses:",
			"Labs:",
			"Not compatible:",
			"Unwanted:",
			"Preferences:", 
			"Pair:",
			"Partial assignments:"));
	
	
	public Parser(String inputFileName){
		try{
			//Read Text File
			FileReader fileReader = new FileReader(inputFileName);
			
			//Wrap FileReader in BufferedReader
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String header = null;
			while((line = bufferedReader.readLine()) != null){
				if(headers.contains(line)){
					header = line;
					System.out.println(header);
				}else{
					parseLine(line,header);
					System.out.println(line);
				}
			}
			
			if(MaximumCourses < courses.size() || MaximumLabs < labs.size()){
				System.out.println("Course Max total: "+ MaximumCourses + ". Lab Max total "+MaximumLabs+". Actual Labs: "+labs.size() +". Actual Courses: "+courses.size());
				System.out.println("Either Courses or Labs have too many to possibly fit in the given slots. Switching Max_Fail_Flag\n");
			}
			if(fiveHundredCourseCount > courseSlots.size()){
				System.out.println("Too many 500 level courses for the given slots. Switching Overload_500_Fail_Flag\n");
			}
			if(tuesdayMeetingOn){
				System.out.println("Checking for the illegal tuesday course Slot \n");
				String[] tuesdayEleven = {"TU","11:00"};
				TimeSlot tuesdaySlot = lookUpCourseSlot(tuesdayEleven);
				if(courseSlots.contains(tuesdaySlot)){
					tuesdaySlot.setMax(0);
					courseSlots.set(courseSlots.indexOf(tuesdaySlot),(CourseSlot) tuesdaySlot);
					System.out.println("Changed the illegal Tuesday slot to zero"+courseSlots.get(courseSlots.indexOf(tuesdaySlot)).getMax());
				}
			}
			if(eveningCourses > eveningCourseSlots || eveningLabs > eveningLabSlots){
				System.out.println("There are either too many labs, or too many Courses in the evening for the possible open evening slots. Switching Evening_Max_Fail_Flag");
			}
			//Close file
			bufferedReader.close();
			
		} catch (FileNotFoundException e){
			System.out.println("Unable to open file '" + inputFileName + "'");
		} catch (IOException e){
			System.out.println("Error reading file '" + inputFileName + "'");
		}
	}

	/**
	 * Processes a line from BufferedReader according to the Header it is under
	 * @param line2: line as read from buffered reader
	 * @param header: last line read that exists as one of the known headers
	 */
	private void parseLine(String line2, String header) {
		if (!line2.isEmpty()) {
			if (header == null) {
				//handle the beginning of file case
			} else if (header.equals(headers.get(0))) {
				parseCourseSlot(line);
			} else if (header.equals(headers.get(1))) {
				parseLabSlot(line);
			} else if (header.equals(headers.get(2))) {
				parseCourse(line);
			} else if (header.equals(headers.get(3))) {
				parseLab(line);
			} else if (header.equals(headers.get(4))) {
				parseNonCompatible(line);
			} else if (header.equals(headers.get(5))) {
				parseUnwanted(line);
			} else if (header.equals(headers.get(6))) {
				parsePreferences(line);
			} else if (header.equals(headers.get(7))) {
				parsePairs(line);
			} else if (header.equals(headers.get(8))) {
				parsePartialAssignments(line);
			}
		}else{
			System.out.println("EMPTY");
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
		
		
		
		String[] partAssignInfo = line2.split(",\\s*");
		String[] classInfo = partAssignInfo[0].split(" +");
		String[] dayInfo = new String[2];
		
		dayInfo[0] = partAssignInfo[1];
		dayInfo[1] = partAssignInfo[2];
		
		dayInfo = clearWhiteSpace(dayInfo);
		classInfo = clearWhiteSpace(classInfo);
		
		if(classInfo[classInfo.length-2].equals("LEC")){
			//its a lecture
			partClass = lookUpCourse(classInfo);
			partSlot = lookUpCourseSlot(dayInfo);

		}else{
			//its a lab
			partClass = lookUpLab(classInfo);
			partSlot = lookUpLabSlot(dayInfo);

		}

		if(partClass != null && partSlot != null){
			partAssignLine = new pair<Classes,TimeSlot>(partClass,partSlot);
			
			partialAssignment.add(partAssignLine);
		}else{
			System.out.println("\n The timeSlot or Class given by the input file does not exist, or is of the wrong format.\n"
					+ "\tSwitching Class_Input_Fail_Flag. Dumping error info:\n"
					+ "\tDay Info- Day: "+dayInfo[0]+" Time: "+dayInfo[1]+
					"\n\tClass Info- Department: "+classInfo[0]+" Course Number: "+classInfo[1]);
			for(int i = 2; i <classInfo.length;i++){
				System.out.println("\t"+ classInfo[i]);
			}
		}
	}
	/**
	 * Function to take a string beneath the Pairs: \n header
	 * Splits the string into the two chunks of class1 and class2, and cleans those up
	 * and then looks them up, depending on if they are lectures or labs
	 * @param line2 ideally of the form "CLASSONEINFO,CLASSTWOINFO"
	 */
	private void parsePairs(String line2) {
		pair<Classes,Classes> pairPair; //dumb name
		
		String[] pairInfo = line2.split(",\\s*");
		String[] firstClass = pairInfo[0].split(" +");
		String[] secondClass = pairInfo[1].split(" +");
		
		Classes leftClass;
		Classes rightClass;
		
		firstClass = clearWhiteSpace(firstClass);
		secondClass = clearWhiteSpace(secondClass);
		if(firstClass[firstClass.length-2].equals("LEC")){
			leftClass = lookUpCourse(firstClass);
			if(secondClass[secondClass.length-2].equals("LEC")){
				rightClass = lookUpCourse(secondClass);
				pairPair = new pair<Classes,Classes>(lookUpCourse(firstClass),lookUpCourse(secondClass));
			}else{
				rightClass = lookUpLab(secondClass);
				pairPair = new pair<Classes,Classes>(lookUpCourse(firstClass),lookUpLab(secondClass));
			}
		}else{
			leftClass = lookUpCourse(firstClass);
			if(secondClass[secondClass.length-2].equals("LEC")){
				rightClass = lookUpCourse(secondClass);
				pairPair = new pair<Classes,Classes>(lookUpLab(firstClass),lookUpCourse(secondClass));
			}else{
				rightClass = lookUpLab(secondClass);
				pairPair = new pair<Classes,Classes>(lookUpLab(firstClass),lookUpLab(secondClass));
			}
		}
		if(leftClass != null && rightClass != null){
			pairs.add(pairPair);
		}else{
			System.out.println("\tOne or more of the courses described in the input file do not exist, or are in the wrong format: \n"
					+ "\tSwitching Pair_Fail_Flag. Dumping error info \n"
					+ "\tLeft Pair Information:\tRight pair Information:");
			for(int i = 0; (i<firstClass.length)||(i<secondClass.length);i++){
				if(i<firstClass.length && i<secondClass.length){
					System.out.println("\t"+firstClass[i]+"\t\t\t"+secondClass[i]);
				}else if(i>firstClass.length){
					System.out.println("\t\t\t\t"+secondClass[i]);
				}else{
					System.out.println("\t"+firstClass[i]);
				}
			}
		}
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
		String[] preferenceInfo = line2.split(",\\s*");
		
		for(String p: preferenceInfo){
			System.out.println(p);
		}
		
		String[] timeSlotInfo = {preferenceInfo[0].replace("\\s+", ""),preferenceInfo[1].replace("\\s+", "")};
		String[] classInfo = preferenceInfo[2].split(" +");
		String penalty = preferenceInfo[3];
		if(classInfo[classInfo.length-2].equals("LEC")){
			preference = new preferenceTriple(lookUpCourseSlot(timeSlotInfo),lookUpCourse(classInfo),Float.parseFloat(penalty));
		}else{
			preference = new preferenceTriple(lookUpLabSlot(timeSlotInfo),lookUpLab(classInfo),Float.parseFloat(penalty));
		}
		if(!preference.hasNullEntries()){
			System.out.println("The preference was made correctly");
			preferences.add(preference);
		}else{
			System.out.println("The preferences are not made correctly: slot or class does not exist");
		}
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
		
		String[] unwantedInfo = line2.split(",\\s*");
		String[] classInfo = unwantedInfo[0].split(" +");
		String[] dayInfo = new String[2];
		
		
		dayInfo[0] = unwantedInfo[1];
		dayInfo[1] = unwantedInfo[2];
		

		
		dayInfo = clearWhiteSpace(dayInfo);
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
		if(l.section.charAt(0) == '9'){
			System.out.println("Found evening Lab \n");
			eveningLabs += 1;
		}
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
		}else {
			workingLabInfo = labInfo;
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
		if(c.section.charAt(0) == '9'){
			System.out.println("Found evening Course \n");
			eveningCourses += 1;
		}
		if(600 > Integer.parseInt(c.classNumber) && Integer.parseInt(c.classNumber) >= 500){
			System.out.println("Five hundred level course found");
			fiveHundredCourseCount += 1; 
		}
	}

	/**
	 * Function which will perform the functionality of splitting an identified LabSlot input line
	 * into an appropriate variable
	 * @param line2: read from the file, following the header Lab slots: \n
	 */
	private void parseLabSlot(String line2) {
		String[] info = new String[4];
		info = line2.split(",\\s*");
		LabSlot ls = new LabSlot(info[0].replaceAll("\\s+", ""), 
										info[1].replaceAll("\\s+", ""), 
										Integer.parseInt(info[2].replaceAll("\\s+", "")), 
										Integer.parseInt(info[3].replaceAll("\\s+", "")));
		labSlots.add(ls);	
		MaximumLabs += ls.getMax();
		if(isEvening(ls.startTime)){
			System.out.println("Found an evening labSlot");
			eveningLabSlots += ls.getMax();
		}
	}


	/**
	 * Function which will perform the functionality of splitting an identified CourseSlot input line
	 * into an appropriate variable
	 * @param line2: read from the file, following the header Course slots: \n
	 */
	private void parseCourseSlot(String line2) {
		String[] info = new String[4];
		info = line2.split(",\\s*");
		CourseSlot cs = new CourseSlot(info[0].replaceAll("\\s+", ""), 
										info[1].replaceAll("\\s+", ""), 
										Integer.parseInt(info[2].replaceAll("\\s+", "")), 
										Integer.parseInt(info[3].replaceAll("\\s+", "")));
		courseSlots.add(cs);	
		MaximumCourses += cs.getMax();
		if(isEvening(cs.startTime)){
			System.out.println("Found an evening courseSlot");
			eveningCourseSlots += cs.getMax();
		}
	}



	
	private boolean isEvening(String startTime) {
		if(startTime.length() == 5){
			String hoursPlace = startTime.substring(0,1);
			int hoursInt = Integer.parseInt(hoursPlace);
			if(hoursInt>=16){
				return true;
			}
		}
		return false;
	}

	public ArrayList<pair<Classes,Classes>> getPairs() {
		return this.pairs;
	}
	public ArrayList<pair<Classes,Classes>> getNonCompatible() {
		return this.nonCompatible;
	}
	public ArrayList<pair<Classes,TimeSlot>> getUnwanted() {
		return this.unWanted;
	}
	public ArrayList<preferenceTriple> getPreferences() {
		return this.preferences;
	}
	
	public ArrayList<pair<Classes,TimeSlot>> getPartialAssignments() {
		return this.partialAssignment;
	}
	
	public ArrayList<CourseSlot> getCourseSlots(){
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
	
	//Creates a list of a list of courses for evalCheck.
	public ArrayList<ArrayList<Course>> getCourseSections() {
		ArrayList<ArrayList<Course>> sectionList = new ArrayList<ArrayList<Course>>();
		boolean foundCourse = false;
		for(Course nextCourse : courses) {
			foundCourse = false;
			for(ArrayList<Course> secSquared : sectionList) {
				if((nextCourse.getDepartment().equals(secSquared.get(0).getDepartment())) && (nextCourse.getClassNumber().equals(secSquared.get(0).getClassNumber()))){
					secSquared.add(nextCourse);
					foundCourse = true;
					break;
				}
			}
			if(!foundCourse) {
				ArrayList<Course> temp = new ArrayList<Course>();
				temp.add(nextCourse);
				sectionList.add(temp);
			}
		}
		return sectionList;
	}
	
	//Creates a list of a list of labs for evalCheck.
	public ArrayList<ArrayList<Lab>> getLabSections() {
		ArrayList<ArrayList<Lab>> sectionList = new ArrayList<ArrayList<Lab>>();
		boolean foundLab = false;
		for(Lab nextLab : labs) {
			foundLab = false;
			for(ArrayList<Lab> secSquared : sectionList) {
				if((nextLab.getDepartment() == secSquared.get(0).getDepartment()) && (nextLab.getClassNumber() == secSquared.get(0).getClassNumber())){
					secSquared.add(nextLab);
					foundLab = true;
					break;
				}
			}
			if(!foundLab) {
				ArrayList<Lab> temp = new ArrayList<Lab>();
				temp.add(nextLab);
				sectionList.add(temp);
			}
		}
		return sectionList;
	}

}
//TODO: Things Parser should catch before passing things to the AI:
//Some combination of Lectures and labs such that there must be overlap (Highly improbable, but could be an edge case)
//unwanted: if a course has no slot that isn't unwanted (Probably rare)
//
