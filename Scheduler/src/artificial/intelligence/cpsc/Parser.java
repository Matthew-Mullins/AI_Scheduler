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
	private ArrayList<pair<Classes,Classes>> nonCompatible;
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
			while(!(line = bufferedReader.readLine()).isEmpty()||(-1 == equalHeader(line))){
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
			
			while(!(line = bufferedReader.readLine()).isEmpty()||(-1 == equalHeader(line))){
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
			
			while(!(line = bufferedReader.readLine()).isEmpty()||(-1 == equalHeader(line))){
				parseLabSlot(line);
			}
			
			
			
			//Courses
			//Much the same as above
			while((line = bufferedReader.readLine()).isEmpty()) {
				System.out.println("EMPTY");
			}
			System.out.println("\n" + line);
			while(!(line = bufferedReader.readLine()).isEmpty()||(-1 == equalHeader(line))) {
				parseCourse(line);
			}
			
			//Labs
			//Rinsing and repeating; nothing new here
			while((line = bufferedReader.readLine()).isEmpty()) {
				System.out.println("EMPTY");
			}
			while(!(line = bufferedReader.readLine()).isEmpty()||(-1 == equalHeader(line))) {
				String[] info;
				Lab l;
				int numTerms = line.split(" ").length;
				info = line.split(" ");
				l = new Lab(info[0].replaceAll("\\s+", ""),
						info[1].replaceAll("\\s+", ""),
						info[info.length - 1].replaceAll("\\s+", ""));
				if(numTerms == 4) {
					l.setBelongsTo(null);
				} else {
					//NOT WORKING PROPERLY
					for(Course c : courses) {
						if(c.getDepartment().equals(info[0].replaceAll("\\s+", "")) && 
								c.getClassNumber().equals(info[1].replaceAll("\\s+", "")) && 
								c.getSection().equals(info[3].replaceAll("\\s+", ""))) {
							l.setBelongsTo(c);
							c.addLab(l);
						} else {
							l.setBelongsTo(null);
						}
					}
				}
				labs.add(l);
			}
			
			//Not Compatible
			
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
	/**
	 * Function that splits an identified Course information input line
	 * into its appropriate variable
	 * @param line2: read from the file, course information following Course: \n Header
	 */
	private void parseCourse(String line2) {
		String[] info = new String[4];
		info = line.split(" ");
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
		info = line.split(",");
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
		info = line.split(",");
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
