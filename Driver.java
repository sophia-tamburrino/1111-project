import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map; //place with imports
import java.util.Collections; //place with imports

public class Driver {

private static final String FILENAME = "C:\\Users\\sophi\\OneDrive\\Documents\\Code\\NLPProject\\covid_10K.csv";

public static void main(String[] args) {

        ArrayList<Sentence> result = new ArrayList<Sentence>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
                String strCurrentLine;
                while ((strCurrentLine = br.readLine()) != null) {
                        result.add(Sentence.convertLine(strCurrentLine));
                        //int sentimentScore = Sentence.convertLine(strCurrentLine).getSentiment();
                        //System.out.println(sentimentScore);
                        //System.out.println(result);
                }

                // for(int i = 0; i < result.size(); i++) {
                //         System.out.println(result.get(i).getTimestamp());
                //         System.out.println(result.get(i).getAuthor());
                //         System.out.println(result.get(i).getText());
                // }

        } catch (IOException stackTrace) {
                stackTrace.printStackTrace();
        }

                //pt 5
                ArrayList<Sentence> modResult = new ArrayList<Sentence>();
                for (int i = 0; i < result.size(); i++) {
                        if(result.get(i).keep(result.get(i).getTimestamp())) {
                                modResult.add(result.get(i));
                        }
                }
                //System.out.println(modResult);
        
                //pt 4 modified for part 5 data
                // for (int i = 0; i < modResult.size(); i++) {
                //         System.out.println(modResult.get(i).toString()); 
                //         if (modResult.get(i).getText().length() > 0) {
                //                 System.out.println(modResult.get(i).getSentiment()); 
                //                 //System.out.println(modResult.get(i).getTimestamp()); 
                //         }
                // }

                ArrayList<Sentence> masks = new ArrayList<Sentence>();
                String[] maskLanguage = {"mask", "masks", "PPE", "face coverings", "filter", "reusable", "disposable", "cloth"};
                
                for (int i = 0; i < result.size(); i++) {
                        String[] temp = result.get(i).getText().split(" ");
                        for (int j = 0; j < temp.length; j++) {
                                for (int c = 0; c < maskLanguage.length; c++) {
                                        if (temp[j].contains(maskLanguage[c])) {
                                                masks.add(result.get(i));
                                                break;
                                        }
                                }
                               
                        }
                }
                
                ArrayList<Sentence> negativeMasks = new ArrayList<Sentence>();
                for (int i = 0; i < masks.size(); i++) {
                        if (masks.get(i).keepNegative(masks.get(i))) {
                                negativeMasks.add(masks.get(i));
                        }
                }

                double percent = 100*((negativeMasks.size() + 0.0) / (masks.size() + 0.0));
                System.out.println("Result: " + percent + "%");

                //pt 3
                Map.Entry<String, Integer> maxEntry = null;
                HashMap<String, Integer> YOUR_HASH_MAP = printTopWords(result);
                
                for (Map.Entry<String, Integer> entry : YOUR_HASH_MAP.entrySet())
                        if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
                                maxEntry = entry;

                int maxValueLen = maxEntry.getValue().toString().length();
                ArrayList <String> results = new ArrayList<String>();
                for (Map.Entry set : YOUR_HASH_MAP.entrySet()){
                        String value = set.getValue().toString();
                        while(value.length() < maxValueLen)
                                value = " " + value;
                        results.add(value + " of " + set.getKey());
                }
                Collections.sort(results);
                Collections.reverse(results);

        }
              


        public static HashMap<String,Integer> printTopWords(ArrayList<Sentence> list) {
                ArrayList<String> temp = new ArrayList<String>();
                HashMap<String, Integer> result = new HashMap<String, Integer>();

                for (int i = 0; i < list.size(); i++) {
                        temp = list.get(i).splitSentence();
                        //put words into hashmap if not already in
                        //if already in, increase number
                        //iterates thru current temp array
                        for (int j = 0; j < temp.size(); j++) {
                                if (result.containsKey(temp.get(j))) {
                                        int num = result.get(temp.get(j));
                                        num += 1;
                                        result.put(temp.get(j), num);
                                }
                                else {
                                        result.put(temp.get(j), 1);
                                }
                        }
                
                }                
                
                return result;
        }

}