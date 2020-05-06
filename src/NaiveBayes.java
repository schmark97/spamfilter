import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class NaiveBayes {

    public static void main(String[] args) throws Exception {
        File f = new File(args[0]);
        Scanner sc = new Scanner(f);
        double c_ = 0;
        double p  = 0;
        HashMap<String, Integer> spamWords = new HashMap<String, Integer>();
        HashMap<String, Integer> hamWords = new HashMap<String, Integer>();
        int spamNum = 0;
        int hamNum = 0;
        while (sc.hasNextLine()) {
        String fname = sc.nextLine();
        String[] tr = fname.split("\\.");
        String fileName;
        if (tr[3].equals("ham")) {
            fileName = "./enron7/ham/";
        } else {
            fileName = "./enron7/spam/";
        }
        fileName = fileName.concat(fname);
        String[] words;
        String lines = "";

        File g = new File(fileName);
        Scanner s = new Scanner(g);

        while (s.hasNextLine()) {
            lines = lines.concat(s.nextLine());
            lines = lines.concat(" ");
        }
        //words = lines.split("[ \n@/.,?>:()#$-_<\"\'*]");
            words = lines.split("\\s+");
            if (!tr[3].equals("spam")) {
            hamNum += 1;
            for (String a : words) {
                if (!hamWords.containsKey(a)) {
                    hamWords.put(a, 1);
                } else {
                    hamWords.put(a, hamWords.get(a) + 1);
                }
            }
        } else {
            spamNum += 1;
            for (String a : words) {
                if (!spamWords.containsKey(a)) {
                    spamWords.put(a, 1);
                } else {
                    spamWords.put(a, spamWords.get(a) + 1);
                }
            }

            }
        }
        hamWords.remove("");
        spamWords.remove("");
        int ssize = 0;
        int hsize = 0;
        for (String a : spamWords.keySet()) {
            ssize += spamWords.get(a);
        }
        for (String a : hamWords.keySet()) {
            hsize += hamWords.get(a);
        }

        File g = new File("test.txt");
        Scanner test = new Scanner(g);

        HashMap<String, Integer> testWords = new HashMap<String, Integer>();
        while (test.hasNextLine()){
            String fname = test.nextLine();
            String[] tr = fname.split("\\.");
            String fileName;
            if (tr[3].equals("ham")) {
                fileName = "./enron7/ham/";
            } else {
                fileName = "./enron7/spam/";
            }
            fileName = fileName.concat(fname);
            String[] words;
            String lines = "";

            File k = new File(fileName);
            Scanner s = new Scanner(k);

            while (s.hasNextLine()) {
                lines = lines.concat(s.nextLine());
                lines = lines.concat(" ");
            }
            //words = lines.split("[ \n@/.,?>:()#$-_<\"\'*]");
            words = lines.split("\\s+");
            for (String a : words) {
                    if (!testWords.containsKey(a)) {
                        testWords.put(a, 1);
                    } else {
                        testWords.put(a, testWords.get(a) + 1);
                    }
            }

            testWords.remove("");
            double sp = 0;
            double ha = 0;
            c_ = 0;

            for (String i : testWords.keySet()) {
                if(spamWords.containsKey(i)){
                    sp = (spamWords.get(i)* 1.0)/ssize;
                }
                if(hamWords.containsKey(i)){
                    ha = (hamWords.get(i)*1.0)/hsize;
                }
                if(spamWords.containsKey(i) && hamWords.containsKey(i))
                    c_ += Math.log(sp) - Math.log(ha);
            }

            c_ += Math.log(spamNum*1.0/hamNum);
            testWords.clear();
            if(c_ > 0 && tr[3].equals("spam")){
                p++;
            }else if(c_ < 0 && tr[3].equals("ham")){
                p++;
            }

        }

        System.out.println( 1 - p*1.0/1826);
    }
}