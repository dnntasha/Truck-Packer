import java.util.Scanner;

/**
 *These are for the user messages
 */

public class Greeter {
	/**
	 *This reads the name from the input by the user
	 */
	
	public static String getName() {
	    Scanner scanner = new Scanner(System.in);
	    System.out.print("Enter your name:");
	    String name = scanner.nextLine();
            return name;
	}
	
//	    String userName = getName();
//      System.out.println("Name received: " + userName);
//	public static void greeter() {
//	    Scanner scanner = new Scanner(System.in);
//	    System.out.print("Enter your name: ");
  //          String name = scanner.nextLine();
    //        System.out.println("Hello, " + name + "! Welcome.");
      //      scanner.close();

	//public static void main(String[] args)
        //{
//	    greeter();
	  //  greeter(args[0]);
        //}
	/**
	 *Prints a greeting message using the input name
	 */
	public static void goodbye() {
	    System.out.println("Toodles, have a great day!");
	}
	public static void greeter(String name) {
	    System.out.println("Wuddup, " + name + " I am supposed to give you a longer greeting message this time round... so here it is");
	}

	/**
	 *calls the greet and the goodbye message
	 */
	public static void main(String[] args)
	{
	    String uname = getName();
	    greeter(uname);
	    goodbye();
	}

}
