package artificial.intelligence.cpsc;

public class Main {

	final static String INPUTFILENAME = "TestInput.txt";
	static long runUntil;
	static float coursemin;
	static float labmin;
	static float pairpen;
	static float sectionpen;
	
	//first argument will be input file
	//second argument will be time to run program(in minutes) (if 1 arg, set to 1)
	//arguments 3-6 will be penalty values (if only 2 args, set all to 1)
	//
	
	public static void main(String[] args) {
		Parser p = new Parser(INPUTFILENAME);
		


		if(args.length > 1) {
			long minutes = (new Long(args[1]) * 60000); //get milliseconds
			runUntil = System.currentTimeMillis() + minutes; //this is the time where the program should stop running and give the best answer it found.
			if(args.length == 6) {
				coursemin = new Float(args[2]);
				labmin = new Float(args[3]);
				pairpen = new Float(args[4]);
				sectionpen = new Float(args[5]);
			} else {
				coursemin = 1;
				labmin = 1;
				pairpen = 1;
				sectionpen = 1;
			}
		} else {
			runUntil = System.currentTimeMillis() + 60000;
			coursemin = 1;
			labmin = 1;
			pairpen = 1;
			sectionpen = 1;
		}
		
		System.out.println("COURSE SLOTS: \n");
		System.out.println(p.getCourseSlots().toString());
		System.out.println("LAB SLOTS: \n");
		System.out.println(p.getLabSlots().toString());
		System.out.println("COURSES: \n");
		System.out.println(p.getCourses().toString());
		System.out.println("LABS: \n");
		System.out.println(p.getLabs().toString());
		
		System.out.println("UNWANTED: \n");
		System.out.println(p.getUnwanted().toString());
		System.out.println("NONCOMPATIBLE: \n");
		System.out.println(p.getNonCompatible().toString());
		
		System.out.println("PAIRS: \n");
		System.out.println(p.getPairs().toString());

		System.out.println("PREFERENCES: \n");
		System.out.println(p.getPreferences().toString());
		System.out.println("PART ASSIGNMENTS: \n");
		System.out.println(p.getPartialAssignments().toString());
		
	}
}


	/*
	parser p = new parser(args[0]);

	
	parser does its thing
	runUntil gets passed to the tree.
	penalties can be public. Tree should be able to access them for passing to evalcheck
	


*/