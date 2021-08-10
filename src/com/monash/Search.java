package com.monash;

import java.util.ArrayList;
import java.util.HashMap;

public class Search {

    HashMap<Integer, HashMap<String, Double>> allDocsVector;
    HashMap<String, Double> oneQueryVector;
    HashMap<Integer, Double> cosineResultOfQuery;

    public Search(ArrayList<ArrayList<String>> allDocs, HashMap<String, Double> allIdfs)
    {
        allDocsVector = generateAllDocsVector(allDocs, allIdfs);
        oneQueryVector = new HashMap<>();
        cosineResultOfQuery = new HashMap<>();
    }

    public HashMap<Integer, Double> search(ArrayList<ArrayList<String>> allDocs, ArrayList<String> allTerms, HashMap<String, Double> allIdfs)
    {
        ArrayList<String> queryList = dealWithQuery();
        generateQueryVector(queryList, allTerms, allIdfs);
        generateAllDocsVector(allDocs, allIdfs);

        // 对Query vector进行转码
        Double[] queryVector = transferDifferentVectorToSameNumberOfAllTerms(allTerms, oneQueryVector);

        generateAllCosineAndDocName(allTerms, queryVector);

        return cosineResultOfQuery;
    }


    public ArrayList<String> dealWithQuery()
    {
        GenerateProcessedQuery generateProcessedQuery = new GenerateProcessedQuery();
        ArrayList<String> queryList = generateProcessedQuery.dealWithQuery();

        return queryList;
    }

    public void generateQueryVector(ArrayList<String> queryList, ArrayList<String> allTerms, HashMap<String, Double> allIdfs)
    {
        CreateQueryVector createQueryVector = new CreateQueryVector();
        oneQueryVector =  createQueryVector.createQueryVector(queryList, allTerms, allIdfs);
    }

    public HashMap<Integer, HashMap<String, Double>> generateAllDocsVector(ArrayList<ArrayList<String>> allDocs, HashMap<String, Double> allIdfs)
    {
        CreateAllDocVector createAllDocVector = new CreateAllDocVector();
        HashMap<Integer, HashMap<String, Double>> temp = createAllDocVector.createAllDocVector(allDocs, allIdfs);

        return temp;
    }

    public Double[] transferDifferentVectorToSameNumberOfAllTerms(ArrayList<String> allTerms, HashMap<String, Double> vector)
    {
        //ArrayList<Double> finalVector = new ArrayList<>();

        //HashMap<String, Double> tempVectorContainer = new HashMap<>();

        //for(String allTerm: allTerms)
        //{
         //   tempVectorContainer.put(allTerm, 0.0);
        //}

        Double[] finalVector = new Double[allTerms.size()];

        for(int i = 0 ; i < allTerms.size(); i++)
        {
            for(String vectorString: vector.keySet())
            {
                if(allTerms.get(i).equals(vectorString))
                {
                    //tempVectorContainer.put(allTerm, vector.get(vectorString));
                    finalVector[i] = vector.get(vectorString);
                }
            }
        }

        for(int a = 0; a < finalVector.length; a++)
        {
            if(finalVector[a] == null )
            {
                finalVector[a] = 0.0;
            }
        }

        //for(String temp: tempVectorContainer.keySet())
        //{
        //    finalVector.add(tempVectorContainer.get(temp));
        //}

        return finalVector;
    }

    public double generateCosineScore(Double[] docVector, Double[] queryVector)
    {
        CosineScore cosineScore = new CosineScore();
        double cosine = cosineScore.cosineSimilarity(docVector, queryVector);

        return cosine;
    }

    public void generateAllCosineAndDocName(ArrayList<String> allTerms, Double[] queryVector)
    {
        for(Integer docName: allDocsVector.keySet())
        {
            // 得到一个doc的vector（term，TfIdf）
            HashMap<String, Double> tempDocVector = allDocsVector.get(docName);
            // 进行转码，生成符合向量数量的vector
            Double[] oneDocVector = transferDifferentVectorToSameNumberOfAllTerms(allTerms, tempDocVector);
            // 生成Cosine
            double cosine = generateCosineScore(oneDocVector,queryVector);
            // 将doc的代号和cosine放进hashMap中
            cosineResultOfQuery.put(docName, cosine);
        }
    }
}
