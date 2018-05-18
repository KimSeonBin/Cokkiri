package coin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Cash {
   private static double cash;
   private final static double INIT = 10000;

   public static void loadCash() {
      String filename = Constant.pathDir+"/"+Coin.id+"/cash.txt";
      File file=new File(filename);
      if(!file.exists()) {
         try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.append(String.valueOf(INIT)); 
            out.close();
            cash=INIT;
            return;
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      
      try {
         BufferedReader in = new BufferedReader(new FileReader(filename));
         String s;
         s=in.readLine();
         in.close();
         
         cash=Double.parseDouble(s);
         
      } catch (IOException e) {
         System.err.println(e);
      }
   }
   
   public void plus(double value) {
      String filename = Constant.pathDir+"/"+Coin.id+"/cash.txt";
   
      try {
         BufferedWriter out = new BufferedWriter(new FileWriter(filename));
         out.append(String.valueOf(cash)); 
         out.close();
         cash+=value;

      } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
      }
   
   }
   public static void minus(double value) {
      String filename = Constant.pathDir+"/"+Coin.id+"/cash.txt";
      
      try {
         BufferedWriter out = new BufferedWriter(new FileWriter(filename));
         out.append(String.valueOf(cash)); 
         out.close();
         cash-=value;

      } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
      }
   }
   public static double getCash() {
      return cash;
   }
}