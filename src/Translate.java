

import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;


public class Translate {

    private static final String INVALID = "invalid sentence";


    // hashmap for simple pronouns
    private static final HashMap<String, String>  pronounMap = new HashMap<String, String>();
    static {
        pronounMap.put("i", "au");
        pronounMap.put("he", "ia");
        pronounMap.put("she", "ia");
        pronounMap.put("you", "koe"); // default is 1 inclusive
        pronounMap.put("they", "rāua"); // default is 2 exclusive
        pronounMap.put("we", "māua");  // default is 2 exclusive
    }

    // hashmap for simple pronouns incl/excl specified
    private static final HashMap<String, String> complexPronounMap = new HashMap<String, String>();
    static {
        complexPronounMap.put("you1 incl", "koe");
        complexPronounMap.put("you2 incl", "kōrua");
        complexPronounMap.put("you3 incl", "koutou");

        complexPronounMap.put("they2 excl", "rāua");
        complexPronounMap.put("they3 excl", "rātou");

        complexPronounMap.put("we2 excl", "māua");
        complexPronounMap.put("we3 excl", "mātou");
        complexPronounMap.put("we2 incl", "tāua");
        complexPronounMap.put("we3 incl", "tātou");
    }

    // hashmap accessed if auxiliary verb is "will"
    private static final HashMap<String, String> futureAuxiliaryVerbMap = new HashMap<String, String>();
    static {
        futureAuxiliaryVerbMap.put("go", "Ka haere");
        futureAuxiliaryVerbMap.put("make", "Ka hanga");
        futureAuxiliaryVerbMap.put("see", "Ka kite");
        futureAuxiliaryVerbMap.put("want", "Ka hiaia");
        futureAuxiliaryVerbMap.put("call", "Ka karanga");
        futureAuxiliaryVerbMap.put("ask", "Ka pātai");
        futureAuxiliaryVerbMap.put("read", "Ka pānui");
        futureAuxiliaryVerbMap.put("learn", "Ka ako");
    }

    // hashmap accesssed if no auxiliary verb
    private static final HashMap<String, String> noAuxiliaryVerbMap = new HashMap<String, String>();
    static {
        noAuxiliaryVerbMap.put("go", "Kei te haere");
        noAuxiliaryVerbMap.put("make", "Kei te hanga");
        noAuxiliaryVerbMap.put("see", "Kei te kite");
        noAuxiliaryVerbMap.put("want", "Kei te hiaia");
        noAuxiliaryVerbMap.put("call", "Kei te karanga");
        noAuxiliaryVerbMap.put("ask", "Kei te pātai");
        noAuxiliaryVerbMap.put("learn", "Kei te ako");
        noAuxiliaryVerbMap.put("went", "I haere");
        noAuxiliaryVerbMap.put("made", "I hanga");
        noAuxiliaryVerbMap.put("saw", "I kite");
        noAuxiliaryVerbMap.put("wanted", "I hiaia");
        noAuxiliaryVerbMap.put("called", "I karanga");
        noAuxiliaryVerbMap.put("asked", "I pātai");
        noAuxiliaryVerbMap.put("read", "I pānui"); // read defaults to past tense
        noAuxiliaryVerbMap.put("learned", "I ako");
    }  //read defaults to past tense

    // hashmap if auxiliary verb is "are", "is", "am", "were", or "was"
    private static final HashMap<String, String> pastPresentAuxiliaryVerbMap = new HashMap<String, String>();
    static {
        pastPresentAuxiliaryVerbMap.put("going", "Kei te haere");
        pastPresentAuxiliaryVerbMap.put("making", "Kei te hanga");
        pastPresentAuxiliaryVerbMap.put("seeing", "Kei te kite");
        pastPresentAuxiliaryVerbMap.put("wanting", "Kei te hiaia");
        pastPresentAuxiliaryVerbMap.put("calling", "Kei te karanga");
        pastPresentAuxiliaryVerbMap.put("asking", "Kei te pātai");
        pastPresentAuxiliaryVerbMap.put("reading", "Kei te pānui");
        pastPresentAuxiliaryVerbMap.put("learning", "Kei te ako");
    }

    // main method simply handles input and output
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        while (scan.hasNextLine()) {
            System.out.println(processLine(scan.nextLine()));
        }


    }

    /**
     * Translates an simple english sentence into a maori sentence
     *
     * @param line a potential English sentence read from stdin
     * @return     a translation of the english sentence into Maori or an invalid message
     */
    private static String processLine(String line) {
        String englishLine = line.toLowerCase();
        String pronounModifier;
        String maoriPronoun;
        String maoriVerb;

        // if the list contains brackets, it is extracted as a pronoun incl/excl modifier otherwise it is blank
        if (englishLine.matches(".*\\(.*?\\).*")) {
            pronounModifier = englishLine.replaceAll(".*\\((.*?)\\).*", "$1");
            englishLine = englishLine.replaceAll("\\((.*?)\\)", "");

        } else {
            pronounModifier = "";
        }

        // extract just the words removing punctuation
        String[] englishWords = englishLine.split("\\W+");

        // check that sentence size is valid between 2-3
        if (englishWords.length >= 2 && englishWords.length <= 3) {

            // check if there is a putative pronoun modifier
            if (pronounModifier != "") {

                // check if pronoun and modifier are valid before retrieving
                if (complexPronounMap.containsKey(englishWords[0] + pronounModifier)) {
                    maoriPronoun = complexPronounMap.get(englishWords[0] + pronounModifier);
                } else {
                    return INVALID;
                }

                // check if simple pronoun is valid before retrieving
            } else if (pronounMap.containsKey(englishWords[0])) {
                maoriPronoun = pronounMap.get(englishWords[0]);
            } else {
                return INVALID;
            }

            // check length to see if there is an auxiliary verb
            if (englishWords.length == 3) {

                // check for present tense auxiliary verbs, implies that keys will be in format "*ing"
                if (englishWords[1].contentEquals("are") || englishWords[1].contentEquals("am")
                        || englishWords[1].contentEquals("is") || englishWords[1].contentEquals("were") ||
                        englishWords[1].contentEquals("was")){

                    // check if verb is valid before retrieving
                    if (pastPresentAuxiliaryVerbMap.containsKey(englishWords[englishWords.length - 1])) {
                        maoriVerb = pastPresentAuxiliaryVerbMap.get(englishWords[englishWords.length - 1]);
                    } else {
                        return "invalid verb \"" + englishWords[englishWords.length - 1] + "\"";
                    }
                // check for future tense auxiliary verb, this implies simple versions of verbs should be used as keys
                } else if (englishWords[1].contentEquals("will")) {

                    // check if verb is valid before retrieving
                    if (futureAuxiliaryVerbMap.containsKey(englishWords[englishWords.length - 1])) {
                        maoriVerb = futureAuxiliaryVerbMap.get(englishWords[englishWords.length - 1]);
                    } else {
                        return "invalid verb \"" + englishWords[englishWords.length - 1] + "\"";
                    }
                } else {
                    return INVALID; // sentence is invalid because auxiliary verb there but is invalid
                }

            // otherwise no auxiliary verb so could be past or present tense
            } else {

                // check if verb is valid before retrieving
                if (noAuxiliaryVerbMap.containsKey(englishWords[englishWords.length - 1])) {
                    maoriVerb = noAuxiliaryVerbMap.get(englishWords[englishWords.length - 1]);
                } else {
                    return "invalid verb \"" + englishWords[englishWords.length - 1] + "\"";
                }

            }

            return maoriVerb + " " + maoriPronoun;
        } else {
            return INVALID;
        }



    }




}
