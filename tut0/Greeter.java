import java.util.Scanner;

public class Greeter {
	public static String getName() {
	    Scanner scanner = new Scanner(System.in);
	    System.out.print("Enter your name:");
	    String name = scanner.nextLine();
            return name;
	}
	
	public static void goodbye() {
	    System.out.println("Toodles, have a great day!");
	}
	
	public static void greeter(String name) {
	    System.out.println("Wuddup, " + name + " I am supposed to give you a longer greeting message this time round... so here it is");
	}
	public static void main(String[] args)
	{
	    String uname = getName();
	    greeter(uname);
	}

}
