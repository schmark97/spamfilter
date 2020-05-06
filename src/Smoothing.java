import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;




public class Smoothing {
    private void makeData(int i) throws Exception{
        File f = new File("train.txt");
        Scanner sc = new Scanner(f);
        FileWriter fileWriter1 = new FileWriter("crosstrain.txt");
        PrintWriter trainWriter = new PrintWriter(fileWriter1);
        FileWriter fileWriter2 = new FileWriter("crosstest.txt");
        PrintWriter testWriter = new PrintWriter(fileWriter2);
        int ind = 0;
        switch (i){
            case 0:{
                String line="";
                while(ind < 835){
                    testWriter.println(sc.nextLine());
                    ind++;
                }
                while (sc.hasNextLine()){
                    trainWriter.println(sc.nextLine());
                }
                sc.close();
                trainWriter.close();
                testWriter.close();
                break;
            }
            case 1:{
                while(ind < 835){
                    trainWriter.println(sc.nextLine());
                    ind++;
                }
                ind = 0;
                while(ind < 835){
                    testWriter.println(sc.nextLine());
                    ind++;
                }

                while (sc.hasNextLine()){
                    trainWriter.println(sc.nextLine());
                }
                sc.close();
                trainWriter.close();
                testWriter.close();
                break;
            }
            case 2:{
                while(ind < 1670){
                    trainWriter.println(sc.nextLine());
                    ind++;
                }
                ind  = 0;
                while(ind < 835){
                    testWriter.println(sc.nextLine());
                    ind++;
                }
                while (sc.hasNextLine()){
                    trainWriter.println(sc.nextLine());
                }
                sc.close();
                trainWriter.close();
                testWriter.close();
                break;
            }
            case 3:{
                while(ind < 2505){
                    trainWriter.println(sc.nextLine());
                    ind++;
                }
                ind  = 0;
                while(ind < 835){
                    testWriter.println(sc.nextLine());
                    ind++;
                }
                while (sc.hasNextLine()){
                    trainWriter.println(sc.nextLine());
                }
                sc.close();
                trainWriter.close();
                testWriter.close();
                break;
            }
            case 4:
                while(ind < 3340){
                    trainWriter.println(sc.nextLine());
                    ind++;
                }

                while (sc.hasNextLine()){
                    testWriter.println(sc.nextLine());
                }
                sc.close();
                trainWriter.close();
                testWriter.close();
                break;
        }
    }

    public double cross(String trainfile,String testfile,double alpha) throws Exception{
        File f = new File(trainfile);
        Scanner sc = new Scanner(f);
        double c_ = 0;
        double p  = 0;
        int differentWords = 0;
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
            s.close();
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
        sc.close();

        hamWords.remove("");
        spamWords.remove("");
        int ssize = 0;
        int hsize = 0;
        differentWords = spamWords.size() + hamWords.size();
        for (String a : spamWords.keySet()) {
            ssize += spamWords.get(a);
        }
        for (String a : hamWords.keySet()) {
            hsize += hamWords.get(a);
            if(spamWords.containsKey(a)) differentWords--;
        }

        File g = new File(testfile);
        Scanner test = new Scanner(g);
        int testNum = 0;
        HashMap<String, Integer> testWords = new HashMap<String, Integer>();
        while (test.hasNextLine()){
            testNum++;
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
            s.close();
           // words = lines.split("[ \n@/.,?>:()#$-_<\"\'*]");
            words = lines.split("\\s+");
            for (String a : words) {
                if (!testWords.containsKey(a)) {
                    testWords.put(a, 1);
                } else {
                    testWords.put(a, testWords.get(a) + 1);
                }
            }

            testWords.remove("");
            double e = 0;
            double d = 0;
            c_ = 0;

            for (String i : testWords.keySet()) {
                if(spamWords.containsKey(i)){
                    e = (spamWords.get(i)* 1.0 + alpha)/(alpha*differentWords + ssize);
                }else
                    e = (alpha)/(alpha*differentWords+ssize);
                if(hamWords.containsKey(i)){
                    d = (hamWords.get(i)*1.0+alpha)/(alpha*differentWords+hsize);
                }else
                    d = (alpha)/(alpha*differentWords+hsize);
                if(spamWords.containsKey(i) && hamWords.containsKey(i))
                    c_ += Math.log(e) - Math.log(d);
            }
            c_ += Math.log(spamNum*1.0/hamNum);
            testWords.clear();
            if(c_ > 0 && tr[3].equals("spam")){
                p++;
            }else if(c_ < 0 && tr[3].equals("ham")){
                p++;
            }

        }
        test.close();
        return  1-p*1.0/testNum;
    }

    public static void main(String[] args) throws Exception {
        Smoothing smoothing = new Smoothing();
        HashMap<Double, Double> alphas = new HashMap<Double, Double>();
        alphas.put(0.1,0.0);
        alphas.put(0.3,0.0);
        alphas.put(0.5,0.0);
        alphas.put(0.8,0.0);
        alphas.put(1.0,0.0);
        double average = 0;
        double sum;
        for (Double a : alphas.keySet()) {
            sum = 0;
            for (int i = 0; i < 5; i++) {
                smoothing.makeData(i);
                sum += smoothing.cross("crosstrain.txt", "crosstest.txt", a);
            }
            alphas.put(a,sum*1.0/5);
        }

        double min = Double.MAX_VALUE;
        double minAlph = Double.MAX_VALUE;
        for (Double a : alphas.keySet()) {
            System.out.println(a+": "+alphas.get(a));
            if (alphas.get(a) < min){
                min = alphas.get(a);
                minAlph = a;
            }
        }
        System.out.println(minAlph);
    }
}