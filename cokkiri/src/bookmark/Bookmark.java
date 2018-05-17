package bookmark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import coin.Coin;

public class Bookmark {
	protected String name;
	protected String address;
	
	private Bookmark(String name, String address) {
		this.name=name;
		this.address=address;
	}
	
	private static boolean checkInput(String name, String address) {
		if(address.length()!=28) {
			System.out.println("wrong address");
			return false;
		}
		if(Coin.bookmark.containsKey(name)) {
			System.out.println("name already exist");
			return false;
		}
		return true;
	}
	
	public static boolean addBookmark(String name, String address) {
		if(checkInput(name, address)) {
			System.out.println("wrong input");
			return false;
		}
		Bookmark bookmark=new Bookmark(name, address);
		if(addtoFile(bookmark)) {
			Coin.bookmark.put(name, address);
			return true;
		}
		return false;
	}

	public static void loadBookmark() {
		String filename = Coin.pathDir+"/"+Coin.id+"/bookmark";
		File file=new File(filename);
		//if(!file.exists()) file.mkdirs();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String s;
			while ((s = in.readLine()) != null) {
				String[] tmp = s.split("\\s");
				Coin.bookmark.put(tmp[0], tmp[1]);
			}
			in.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	private static boolean addtoFile(Bookmark newbookmark) {
		String filename = Coin.pathDir+"/"+Coin.id+"/bookmark.txt";
		File file=new File(filename);
		if(!file.exists()) {
			System.out.println("error bookmark file open");
			return false;
		}
		 try {
			 BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
			 out.append(newbookmark.name+" "+newbookmark.address+"\n"); 
			 out.close();
			 return true;
		 } catch (IOException e) {
			 System.err.println(e);
		 }
		 return false;
	}
}
