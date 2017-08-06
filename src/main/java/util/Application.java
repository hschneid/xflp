package util;
import xf.xflp.XFLP;

public class Application {

	public static void main(String[] args) {
		try {
			new XFLP().executeLoadPlanning();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
