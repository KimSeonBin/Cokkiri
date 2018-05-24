package login;

import java.util.ArrayList;
import java.util.Iterator;

public class Login {

	static ArrayList<userInfo> users=new ArrayList<userInfo>();
	/*public Login() {
		users.add(new userInfo("aaa", "aaa", "201520907"));
		users.add(new userInfo("abc", "abc", "201520908"));
		users.add(new userInfo("bbb", "bbb", "201520909"));
	}*/
	
	public static String login(String id, String pw) {
		if(id.equals(pw)) {
			if(id.length()==9&&id.charAt(0)=='2'&&id.charAt(1)=='0') {
				
				return id;
			}
		}
		return null;
		/*
		users.add(new userInfo("aaa", "aaa", "201520907"));
		users.add(new userInfo("abc", "abc", "201520908"));
		users.add(new userInfo("bbb", "bbb", "201520909"));
		users.add(new userInfo("xxx", "xxx", "201520000"));
		
		Iterator it = users.iterator();
		
		while(it.hasNext()) {
		
			userInfo tmp = (userInfo) it.next();
			System.out.println("check "+tmp.Id+" "+tmp.Pw);
			System.out.println("input "+id+" "+pw);
			if(id.equals(tmp.Id)&&pw.equals(tmp.Pw)) return tmp.identifier;
		}
		return null;*/
	}

	static class userInfo{
		private String Id;
		private String Pw;
		private String identifier;
		
		private userInfo(String id, String pw, String id2) {
			Id=id;
			Pw=pw;
			identifier=id2;
		}
	}
}
