import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Ripper {
	private int dexNum;
	private String baseUrl = "http://www.serebii.net/pokedex-xy/";
	private String ext = ".shtml";
	private BaseStats stats;
	private List<MovePair> moves;
		
	public Ripper(int dn){
		dexNum = dn;
		stats = new BaseStats();
		moves = new LinkedList<MovePair>();
	}
	
	public Ripper(int dn, String b, String e){
		dexNum = dn;
		baseUrl = b;
		ext = e;
		stats = new BaseStats();
		moves = new LinkedList<MovePair>();
	}
	
	public String outputJSMoves(){
		//output move data
		String moveData = "";
		
		//sample:
		//var bulbasaurLS = [new levelMove("Tackle",1),new levelMove("Growl",3),new levelMove("Leech Seed",7),new levelMove("Vine Whip",9),new levelMove("Poison Powder",13),new levelMove("Sleep Powder",13),new levelMove("Take Down",15),new levelMove("Razor Leaf",19),new levelMove("Sweet Scent",21),new levelMove("Growth",25),new levelMove("Worry Seed",33),new levelMove("Seed Bomb",37)];
		
		moveData += "var ";
		moveData += stats.name.toLowerCase();
		moveData += "LS [";
		
		//iterate over moves and add them to the array
		Iterator<MovePair> m = moves.iterator();
		boolean first = true;
		while(m.hasNext()){
			MovePair mp = m.next();
			if(!first) { moveData += ", ";}
			else {first = false;}
			moveData += "new levelMove(\"";
			moveData += mp.name;
			moveData += "\",";
			moveData += Integer.toString(mp.level);
			moveData += ")";
		}
		moveData += "];";
		
		return moveData;
	}
	
	public String outputJSStats(){
		//output move data
		String statData = "";
		
		//var bulbasaur = new pokemonSpecies(1,"Bulbasaur","grass","poison",45,49,49,65,65,45,bulbasaurLS);
		statData += "var ";
		statData += stats.name.toLowerCase();
		statData += " = new pokemonSpecies(";
		statData += Integer.toString(stats.dexNum);
		statData += ",\"";
		statData += stats.name;
		statData += "\",";
		if(stats.type != null){statData += "\""; statData += stats.type; statData += "\"";}
		else{statData += "null";}
		statData += ",";
		if(stats.type2 != null){statData += "\""; statData += stats.type2; statData += "\"";}
		else{statData += "null";}
		statData += ",";
		//hp
		statData += stats.hp + ",";
		//attack;
		statData += stats.attack + ",";
		//defense;
		statData += stats.defense + ",";
		//spAttack;
		statData += stats.spAttack + ",";
		//spDefense;
		statData += stats.spDefense + ",";
		//speed;
		statData += stats.speed + ",";
		
		statData += stats.name.toLowerCase() + "LS);";
		
		//allPokemon.push(bulbasaur);
		statData += " allPokemon.push(" + stats.name.toLowerCase() + "); ";
		
		return statData;
	}
	
	private String assembleUrl(){
		String out = baseUrl;
		if(dexNum < 10){
			out += "0";
		}
		if(dexNum < 100){
			out += "0";
		}
		out += Integer.toString(dexNum);
		out += ext;
		return out;
	}
	
	public void outputStats(){
		System.out.println(stats.name);
		System.out.println("type: " + stats.type);
		System.out.println("type2: " + stats.type2);
		System.out.println("hp: " + stats.hp);
		System.out.println("att: " + stats.attack);
		System.out.println("def: " + stats.defense);
		System.out.println("sAtt: " + stats.spAttack);
		System.out.println("sDef: " + stats.spDefense);
		System.out.println("spd: " + stats.speed);
	}
	
	private void addMoves(BufferedReader br) throws IOException{
		while(true){
			//read first two lines
			String level = parseMoveLine(br);
			String name = parseMoveLineWithLink(br);
			
			//add the move to the list
			MovePair mp;
			if(level.equals("&#8212;")){
				mp = new MovePair(1, name);
			}
			else {
				mp = new MovePair(Integer.parseInt(level), name);
			}
			mp.output();
			moves.add(mp);
			
			//skip next 7 lines
			for(int i = 0; i < 6; i++){
				br.readLine();
			}
			//check last line to make sure we haven't hit tm/hm
			String line = br.readLine();
			if(line.contains("TM/HM")){
				break;
			}
		}
	}
	
	private String parseMoveLine(BufferedReader br) throws IOException{
		String line = br.readLine();
		if(line.contains("<td rowspan=\"2\" class=\"fooinfo\">")){
			line = line.substring(line.indexOf(">") + 1);
			line = line.substring(0, line.indexOf("<"));
			//System.out.println("Move detail: " + line);
			return line;
		}
		else {
			System.out.println("ERROR parsing move! Dex#:" + dexNum);
		}
		return null;
	}
	
	private String parseMoveLineWithLink(BufferedReader br) throws IOException{
		String line = br.readLine();
		if(line.contains("<td rowspan=\"2\" class=\"fooinfo\">")){
			line = line.substring(line.indexOf(">") + 1);
			line = line.substring(line.indexOf(">") + 1);
			line = line.substring(0, line.indexOf("<"));
			//System.out.println("Move detail: " + line);
			return line;
		}
		else {
			System.out.println("ERROR parsing move! Dex#:" + dexNum);
		}
		return null;
	}
	
	private void addName(String l){
		String line = l;
		line = line.substring(line.indexOf(">") + 1);
		line = line.substring(0, line.indexOf("#") - 3);
		stats.name = line;
	}
	
	private int parseStatLine(BufferedReader br) throws IOException{
		String line = br.readLine();
		if(line.contains("<td align=\"center\" class=\"fooinfo\">")){
			line = line.substring(line.indexOf(">") + 1);
			line = line.substring(0, line.indexOf("<"));
			return Integer.parseInt(line);
		}
		else {
			System.out.println("ERROR parsing stat!");
		}
		return -1;
	}
	
	private void addStats(BufferedReader br) throws IOException{
		stats.dexNum = dexNum;
		//skip forward 8 lines
		for(int i = 0; i < 8; i++){
			br.readLine();
		}
		//parse and add stats
		
		//HP
		stats.hp = parseStatLine(br);
		//Attack
		stats.attack = parseStatLine(br);
		//Defense
		stats.defense = parseStatLine(br);
		//Sp. Attack
		stats.spAttack = parseStatLine(br);
		//Sp. Defense
		stats.spDefense = parseStatLine(br);
		//Speed
		stats.speed = parseStatLine(br);
	}
	
	private void addType(BufferedReader br) throws IOException{
		String line = br.readLine();
		line = line.substring(line.indexOf("pokedex-bw/type/") + 16);
		stats.type = line.substring(0, line.indexOf("."));
		if(line.indexOf("pokedex-bw/type/") > 0){
		line = line.substring(line.indexOf("pokedex-bw/type/") + 16);
		stats.type2 = line.substring(0, line.indexOf("."));
		}
		else{stats.type2 = null;}
	}
	
	public void ripData() throws Exception{
		//set up variables
		BufferedReader br;
		InputStreamReader inStream;
		
		//establish connection
		URL url = new URL(assembleUrl());
		URLConnection urlConnection = (URLConnection)url.openConnection();
		
		//set up stream and buffering
		inStream = new InputStreamReader(urlConnection.getInputStream());
        br = new BufferedReader(inStream);
        
        //read html data
		String line = null;
		boolean foundType = false;
		while((line = br.readLine()) != null){
			if(line.contains("<title>")){
				//System.out.println("Found header");
				addName(line);
			}
			
			if(line.contains("Female") && !foundType){
				foundType = true;
				//System.out.println("Found header");
				addType(br);
			}
			
			if(line.contains("X / Y Level Up")){
				//System.out.println("Found learn set table");
				addMoves(br);
			}
			
			if(line.contains("<b>Stats</b>")){
				//System.out.println("Found base stats table");
				addStats(br);
			}
			
		}
        
	}
	
}
