<<<<<<< HEAD
import java.util.Scanner;
public class Greeter {
	public static String getName() {
	    Scanner scanner = new Scanner(System.in);
	    System.out.print("Enter your name:");
	    String name = scanner.nextLine();
            return name;
	}
	public static void main(String[] args)
	{
	    String userName = getName();
	    System.out.println("Name received: " + userName);
=======
public class Greeter {
//	public static void greeter() {
//	    Scanner scanner = new Scanner(System.in);
//	    System.out.print("Enter your name: ");
  //          String name = scanner.nextLine();
    //        System.out.println("Hello, " + name + "! Welcome.");
      //      scanner.close();

	public static void main(String[] args)
        {
//	    greeter();
	    greeter(args[0]);
        }
	public static void greeter(String name) {
	    System.out.println("Wuddup, " + name + " I am supposed to give you a longer greeting message this time round... so here it is");
>>>>>>> feature_argument_greeter
	}

}
