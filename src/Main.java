
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting!");
		int MAX_DEXNUM = 2;
		
		//loop through all pokemon numbers and get their info
		for(int i = 1 ; i < MAX_DEXNUM ; i++){
			Ripper r = new Ripper(i);
			try {
				r.ripData();
				r.outputStats();
			}
			catch(Exception e){
				System.out.println("Caught exception!");
			}
			
			//TODO output data in js format to a file		
			
		}
	}

}
