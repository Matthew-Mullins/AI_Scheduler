package artificial.intelligence.cpsc;

public class Main {

	final static String INPUTFILENAME = "TestInput.txt";
	
	//first argument will be input file
	//arguments 2-5 will be penalty values
	//
	
	public static void main(String[] args) {
		Parser p = new Parser(INPUTFILENAME);
		
		System.out.println(p.getCourseSlots().toString());
		System.out.println(p.getLabSlots().toString());
		System.out.println(p.getCourses().toString());
		System.out.println(p.getLabs().toString());
	}
}
