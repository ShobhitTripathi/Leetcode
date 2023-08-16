public class Main
{
	public static void main(String[] args) {
	    int n = 7;
	    
		System.out.println("Print 1 to " + n);
		print1toN(n);
		System.out.println();
		System.out.println("Done");
		
		System.out.println();
		
		System.out.println("Print " + n + " to 1");
		printNto1(n);
		System.out.println();
		System.out.println("Done");
	}
	
	private static void print1toN(int n) {
	    if (n == 1) {
	        System.out.print(1 + " ");
	        return;
	    }
	    print1toN(n - 1);
	    System.out.print(n + " ");
	    return;
	}
	
	private static void printNto1(int n) {
	    if (n == 1) {
	        System.out.print(1 + " ");
	        return;
	    }
	    System.out.print(n + " ");
	    printNto1(n - 1);
	    return;
	}
}
