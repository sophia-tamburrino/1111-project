import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import org.ejml.simple.SimpleMatrix;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.sql.Timestamp;
import java.util.Date;

public class Sentence {
        String text;
        String author;
        String timestamp;

        public Sentence(String text, String author, String timestamp) {
                this.text = text;
                this.author = author;
                this.timestamp = timestamp;
        }
        public static Sentence convertLine(String line) {
                ArrayList<String> pieces = new ArrayList<String>(8);
                String current = "";
                Boolean quote = false;
                for (int i = 0; i < line.length() - 1; i++) {
                        if (quote == false && line.substring(i, i + 1).equals("\"")) {
                                quote = true;
                        }
                        else if (quote == true && line.substring(i, i + 1).equals("\"")) {
                                quote = false;
                        }
                        
                        if (quote == true && line.substring(i, i + 1).equals(",")) {
                                current += line.substring(i, i + 1);
                        }
                        else if (quote == false && line.substring(i, i + 1).equals(",")) {
                                pieces.add(current);
                                current = "";
                        }
                        else if (quote == false && (i == line.length() - 2)) {
                                current += line.substring(i, i + 2);
                                pieces.add(current);
                        }
                        else {
                                current += line.substring(i, i + 1);
                        }

                } 

                if (pieces.size() <= 7) {
                        int remaining = 8 - pieces.size();
                        for(int i = 0; i < remaining; i++) {
                                pieces.add("");
                        }
                }
                //month converter 4-6
                String month = "";
                String day = "";
                String year = "2020";
                if(pieces.get(2).length() != 0) {
                        if (pieces.get(2).substring(0,1).equals("4")) {
                        month = "April";
                        for(int i = 2; i < 4; i++) {
                                if (pieces.get(2).substring(i, i+1).equals("/")) {
                                        break;
                                }
                                else {
                                        day += pieces.get(2).substring(i, i+1);
                                }
                        }
                                String result = month + " " + day + " " + year;
                                pieces.set(2, result);
                        }
                        else if (pieces.get(2).substring(0,1).equals("5")) {
                                month = "May";
                                for(int i = 2; i < 4; i++) {
                                        if (pieces.get(2).substring(i, i+1).equals("/")) {
                                                break;
                                        }
                                        else {
                                                day += pieces.get(2).substring(i, i+1);
                                        }
                                }
                                String result = month + " " + day + " " + year;
                                pieces.set(2, result);
                        }
                        else {
                                month = "June";
                                for(int i = 2; i < 4; i++) {
                                        if (pieces.get(2).substring(i, i+1).equals("/")) {
                                                break;
                                        }
                                        else {
                                                day += pieces.get(2).substring(i, i+1);
                                        }
                                }
                                String result = month + " " + day + " " + year;
                                pieces.set(2, result);
                        }
                }
                
                //System.out.println(pieces);
                Sentence result = new Sentence(pieces.get(7), pieces.get(4), pieces.get(2));
                return result;
        }

        public ArrayList<String> splitSentence() {
                String current = "";
                ArrayList<String> result = new ArrayList<>();
                String[] stopwords = {"a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves"};
                for (int i = 0; i < text.length() - 1; i++) {
                        if (text.substring(i, i+1).equals(" ")) {
                                result.add(current);
                                current = "";
                        }
                        else if (i == text.length() - 2) {
                                current += text.substring(i, i+2);
                                result.add(current);
                        }
                        else {
                                current += text.substring(i, i+1);
                        }
                }

                ArrayList<String> newResult = new ArrayList<>();
                for(int i = 0; i < result.size(); i++) {
                        Boolean check = false; 
                        for(int j = 0; j < stopwords.length; j++) {
                                if (result.get(i).toLowerCase().equals(stopwords[j])) {
                                        check = true;
                                }
                        }
                        if(check == false) {
                                newResult.add(result.get(i).toLowerCase());
                        }
                        
                }

                return newResult;
        }

        public String getText() {
                return text;
        }

        public void setText(String theText) {
                text = theText;
        } 

        public String getAuthor() {
                return author;
        }

        public void setAuthor(String theAuthor) {
                author = theAuthor;
        }

        public String getTimestamp() {
                return timestamp;
        }

        public void setTimestamp(String theTime) {
                timestamp = theTime;
        }
        
        public String toString() {
                return "{author:" + author + ", sentence:\"" +  text + "\", timestamp:\"" + timestamp + "\"}";
        }

        public int getSentiment() {
                try {
                        Properties props = new Properties();
                        props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
                        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
                        Annotation annotation = pipeline.process(text);
                        System.out.println(annotation.toString());
                        System.out.println(author);
                        CoreMap sentence = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
                        Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                        return RNNCoreAnnotations.getPredictedClass(tree);
                } catch (Exception e){
                        return 0;
                }
        }

        public boolean keep(String temporalRange) {
                //temporal range is the current date of tweet
                String theRange = "Apr 19 2020-Apr 22 2020"; 
                String[] spl = theRange.split("-");

                try {
                        DateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
                        // you can change format of date
                        Date dateOne = formatter.parse(spl[0]);
                        Date dateTwo = formatter.parse(spl[1]);
                        DateFormat formatter2 = new SimpleDateFormat("MMMMM dd yyyy");
                        Date tempRange = formatter2.parse(temporalRange);

                        if ((dateOne.getTime() <= tempRange.getTime()) && (tempRange.getTime() <= dateTwo.getTime())) {
                                //System.out.println("working");
                                return true;   
                        }
                        else {
                                return false;
                        }

                } catch (ParseException e) {
                        System.out.println("Exception :" + e);
                }

                return false;

        }

        public boolean keepNegative (Sentence str) {
                int sent = str.getSentiment();
                if (sent == 1) {
                        return true;
                }

                return false;
        }

}