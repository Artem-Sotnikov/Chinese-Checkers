
public class CheckerMain {
	public static void main(String[] args) {
		Display disp = new Display();
		disp.refresh();
		System.out.println("executed");
		while (true) {
			disp.refresh();
		}
	}
}
