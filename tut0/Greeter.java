<<<<<<< HEAD
//<<<<<<< HEAD
import java.util.Scanner;
public class Greeter {
	public static String getName() {
	    Scanner scanner = new Scanner(System.in);
	    System.out.print("Enter your name:");
	    String name = scanner.nextLine();
            return name;
	}
//	public static void main(String[] args)
//	{
//	    String userName = getName();
//	    System.out.println("Name received: " + userName);
//=======
//class Greeter {
=======
import java.util.Scanner;

public class Greeter {
>>>>>>> 93cf753 (added import)

	public static void greeter() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            System.out.println("Hello, " + name + "! Welcome.");
            scanner.close();
    	}
	public static void main(String[] args)
	{ 
	    greeter();
//>>>>>>> 65ad70c (Reattempt at 3 [#3])
	}
}
