package com.monash;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateAllDocVector {

    HashMap<Integer, HashMap<String, Double>> allDocVector;

    public CreateAllDocVector()
    {
        allDocVector = new HashMap<>();
    }

    public HashMap<Integer, HashMap<String, Double>> createAllDocVector(ArrayList<ArrayList<String>> allDocs, HashMap<String, Double> allIdfs)
    {

        for(int i = 0; i < allDocs.size(); i++)
        {
            CreateOneDocVector createOneDocVector = new CreateOneDocVector();
            HashMap<String, Double> oneDocVector = createOneDocVector.createOneDocVector(allDocs.get(i), allIdfs);
            recordAllDocVector(i + 1, oneDocVector);
        }

        return allDocVector;
    }

    public void recordAllDocVector(int docName, HashMap<String, Double> oneVector)
    {
        allDocVector.put(docName, oneVector);
    }
}
