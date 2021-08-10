package com.monash;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Idf {

    public Idf()
    {

    }

    public double idf(String term, ArrayList<ArrayList<String>> allDocs)
    {
        double n = 0;
        for(ArrayList<String> doc: allDocs)
        {
            for(String tempTerm: doc)
            {
                if(tempTerm.equalsIgnoreCase(term))
                {
                    n++;
                    break;
                }
            }
        }

        double idf = Math.log((double)allDocs.size() / (n + 1));
        double input = new BigDecimal(idf).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

        return input;
    }

}
