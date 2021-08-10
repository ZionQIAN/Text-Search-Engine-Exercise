package com.monash;

import java.util.ArrayList;

public class Stemming {

    private Stemmer stemmer;
    private ArrayList<String> newDoc;

    public Stemming()
    {
        stemmer = new Stemmer();
        newDoc = new ArrayList<>();
    }

    public ArrayList<String> stemming(ArrayList<String> inputDoc)
    {
        for (String term : inputDoc) {
            for (int i = 0; i < term.length(); i++) {
                stemmer.add(term.charAt(i));
            }

            stemmer.stem();
            String newTerm = stemmer.toString();
            newDoc.add(newTerm);
        }

        return newDoc;
    }
}
