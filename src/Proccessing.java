import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.*;

public class Proccessing {
    public static void main(String[] args) throws Exception {
        File f = new File(args[0]);
        Scanner sc = new Scanner(f);
        File h = new File("ustopwords.txt");
        Scanner scanner = new Scanner(h);

        //Stop szavak
        String[] stopWords;
        String sw="";
        while (scanner.hasNextLine()){
            sw = sw.concat(scanner.nextLine());
            sw = sw.concat("/ /");
        }
        scanner.close();
        stopWords = sw.split("/ /");

        while (sc.hasNextLine()){
            String fname = sc.nextLine();
            String[] tr = fname.split("\\.");
            String fileName;
            String newFile;
//            if(tr[3].equals("ham")){
//                fileName = "./enron6/ham/";
//                newFile = "./enron7/ham/";
//            }else if(tr[3].equals("spam")){
//                fileName = "./enron6/spam/";
//                newFile = "./enron7/spam/";
//            }else{
                fileName = "./ssl/";
                newFile = "./nssl/";
//            }
            fileName = fileName.concat(fname);
            newFile = newFile.concat(fname);
            String[] words;
            String lines="";

            File g = new File(fileName);
            Scanner s = new Scanner(g);

            while (s.hasNextLine()){
                lines = lines.concat(s.nextLine());
                lines = lines.concat(" ");
            }
            lines = lines.replaceAll("[^A-Za-z0-9]", " ").toLowerCase();
            words = lines.split("\\s+");


            FileWriter fileWriter = new FileWriter(newFile);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            boolean b;
            for (int i = 0; i < words.length; i++){
                b = false;
                for (int j = 0; j < stopWords.length; j++){
                    if(words[i].equals(stopWords[j])){
                        b = true;
                        break;
                    }
                }
                if(!b)
                    printWriter.println(words[i]);
            }
            fileWriter.close();
            s.close();

        }
    }

}