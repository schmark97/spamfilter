import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class SemiSupervisedLearning {
    public static void main(String[] args) throws Exception {
        double c_ = 0;
        double p = 0;
        int ssize = 0;
        int hsize = 0;
        int iter = 0;
        HashMap<String, Integer> spamWords = new HashMap<String, Integer>();
        HashMap<String, Integer> hamWords = new HashMap<String, Integer>();
        int spamNum = 0;
        int hamNum = 0;
        boolean added = true;
        while(added){
            iter++;
            added = false;
            File f = new File(args[0]);
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
            String fname = sc.nextLine();
            String[] tr = fname.split("\\.");
            String fileName;
            if (tr[1].equals("spam") || tr[1].equals("ham")) {
                fileName = "./nssl/";
                fileName = fileName.concat(tr[0]).concat(".txt");
            } else {
                if (tr[3].equals("ham")) {
                    fileName = "./enron7/ham/";
                } else {
                    fileName = "./enron7/spam/";
                }
                fileName = fileName.concat(fname);
            }
            String[] words;
            String lines = "";

            File g = new File(fileName);
            Scanner s = new Scanner(g);

            while (s.hasNextLine()) {
                lines = lines.concat(s.nextLine());
                lines = lines.concat(" ");
            }
            s.close();

            words = lines.split("\\s+");
            if (tr[1].equals("spam") || tr[1].equals("ham")) {
                if (!tr[1].equals("spam")) {
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
            } else {
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
        }
        hamWords.remove("");
        spamWords.remove("");
        ssize = 0;
        hsize = 0;
        for (String a : spamWords.keySet()) {
            ssize += spamWords.get(a);
        }
        for (String a : hamWords.keySet()) {
            hsize += hamWords.get(a);
        }
        sc.close();

        File g = new File("cimkezetlen.txt");
        Scanner test = new Scanner(g);

        HashMap<String, Integer> testWords = new HashMap<String, Integer>();
        while (test.hasNextLine()) {
            String fname = test.nextLine();
            String fileName = "./nssl/";
            fileName = fileName.concat(fname);
            String[] words;
            String lines = "";

            File k = new File(fileName);
            Scanner s = new Scanner(k);

            while (s.hasNextLine()) {
                lines = lines.concat(s.nextLine());
                lines = lines.concat(" ");
            }
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
                if (spamWords.containsKey(i)) {
                    sp = (spamWords.get(i) * 1.0) / ssize;
                }
                if (hamWords.containsKey(i)) {
                    ha = (hamWords.get(i) * 1.0) / hsize;
                }
                if (spamWords.containsKey(i) && hamWords.containsKey(i))
                    c_ += Math.log(sp) - Math.log(ha);
            }

            c_ += Math.log(spamNum * 1.0 / hamNum);
            testWords.clear();
            if (c_ >= Math.log(5)) {//spam
                FileWriter fileWriter = new FileWriter("newdata.txt", true);
                FileWriter traWriter = new FileWriter(args[0], true);

                String[] st = fname.split("\\.");
                String newFileName = st[0].concat(".spam").concat(".txt");
                PrintWriter printWriter = new PrintWriter(fileWriter);
                PrintWriter tWriter = new PrintWriter(traWriter);

                printWriter.println(newFileName);
                tWriter.println(newFileName);
                fileWriter.close();
                File j = new File("cimkezetlen.txt");
                Scanner jr = new Scanner(j);
                lines = "";
                while (jr.hasNextLine()) {
                    lines = lines.concat(jr.nextLine());
                    lines = lines.concat(" ");
                }
                words = lines.split(" ");
                FileWriter fw = new FileWriter("cimkezetlen.txt");
                PrintWriter pW = new PrintWriter(fw);

                for (String a : words) {
                    if (!fname.equals(a)) {
                        pW.println(a);
                    }
                }

                traWriter.close();
                fw.close();
                jr.close();
                added = true;

            } else if (c_ <= -Math.log(5)) {//ham
                FileWriter fileWriter = new FileWriter("newdata.txt", true);
                FileWriter traWriter = new FileWriter(args[0], true);

                String[] st = fname.split("\\.");
                String newFileName = st[0].concat(".ham").concat(".txt");
                PrintWriter printWriter = new PrintWriter(fileWriter);
                PrintWriter tWriter = new PrintWriter(traWriter);
                tWriter.println(newFileName);
                printWriter.println(newFileName);
                fileWriter.close();
                File j = new File("cimkezetlen.txt");
                Scanner jr = new Scanner(j);
                lines = "";
                while (jr.hasNextLine()) {
                    lines = lines.concat(jr.nextLine());
                    lines = lines.concat(" ");
                }
                words = lines.split(" ");
                FileWriter fw = new FileWriter("cimkezetlen.txt");
                PrintWriter pW = new PrintWriter(fw);

                for (String a : words) {
                    if (!fname.equals(a)) {
                        pW.println(a);

                    }
                }
                fw.close();
                jr.close();
                traWriter.close();
                added = true;
            }

        }
            System.out.println("iteraciok szama: "+iter);
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
