import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		System.out.println("Starting!");
		int MAX_DEXNUM = 100;
		
		File m = new File("moves.js");
		File s = new File("stats.js");
		// if file doesnt exists, then create it
		if (!m.exists()) {
			try {
				m.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!s.exists()) {
			try {
				s.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileWriter mfw;
		FileWriter sfw;
		BufferedWriter mbw = null;
		BufferedWriter sbw = null;
		try {
			sfw = new FileWriter(s.getAbsoluteFile());
			mfw = new FileWriter(m.getAbsoluteFile());
			mbw = new BufferedWriter(mfw);
			sbw = new BufferedWriter(sfw);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
					
		//loop through all pokedex numbers and get their info
		for(int i = 1 ; i < MAX_DEXNUM ; i++){
			Ripper r = new Ripper(i);
			try {
				r.ripData();
				//r.outputStats();
			}
			catch(Exception e){
				System.out.println("Caught exception! Dex#:" + i);
			}
			
			//TODO output data in js format to a file
			String moves = r.outputJSMoves();
			//System.out.println(moves);
			String stats = r.outputJSStats();
			//System.out.println(stats);
			
			try {
				mbw.append(moves);
				sbw.append(stats);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			mbw.close();
			sbw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
