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
	}
}
