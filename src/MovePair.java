
public class MovePair {
	public int level;
	public String name;
	
	public MovePair(int l, String n){
		level = l;
		name = n;
	}
	
	public void output(){
		System.out.println("Move: " + level + " - " + name);
	}
}
