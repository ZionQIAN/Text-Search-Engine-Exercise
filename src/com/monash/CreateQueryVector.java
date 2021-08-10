package com.monash;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateQueryVector {


    ArrayList<String> query;

    ArrayList<String> nonRepeatedQuery;

    HashMap<String, Double> queryVector;

    ArrayList<String> appearedInAllDoc;

    HashMap<String, ArrayList<String>> appearedInDocAndAppearedInQuery;

    public CreateQueryVector()
    {
        query = new ArrayList<>();
        nonRepeatedQuery = new ArrayList<>();
        queryVector = new HashMap<>();
        appearedInAllDoc = new ArrayList<>();
        appearedInDocAndAppearedInQuery = new HashMap<>();
    }

    public HashMap<String, Double> createQueryVector(ArrayList<String> queryList, ArrayList<String> allTerms, HashMap<String, Double> allIdfs)
    {
        query = queryList;
        recordAllNonRepeatedTermOfOneQuery();
        deleteTermsDidNotAppearedInAllDocVocabulary(allTerms);
        recordQueryVector(allIdfs);

        return queryVector;

    }


    public void recordAllNonRepeatedTermOfOneQuery()
    {
        for(String term : query)
        {
            if(!isExistedTermOrNot(term, nonRepeatedQuery))
            {
                nonRepeatedQuery.add(term);
            }
        }
    }

    public boolean isExistedTermOrNot(String term, ArrayList<String> list)
    {
        for(String ExistedTerm: list)
        {
            if(term.equalsIgnoreCase(ExistedTerm))
            {
                return true;
            }
        }

        return false;
    }

    public int calculateTheNoOfTermOfAllTermsInOneQuery(String term)
    {
        // 根据输入的all term 的term找到query中相对应的term
        // 由于包含的存在，所以可能一个allTerm中的term对应多个query中的term
        // 所以对于一个allterm中的term， 需要计算所有对应的term的count
        // 再把TF加起来，就是这个all term 中的term的count

        int count = 0;

        ArrayList<String> tempQueryTermList = appearedInDocAndAppearedInQuery.get(term);

        for(String queryTerm: tempQueryTermList)
        {
            count = count + calculateTheNoOfTermInOneQuery(queryTerm);
        }

        return count;

    }

    public int calculateTheNoOfTermInOneQuery(String term)
    {
        int count = 0;

        for(String TempTerm: query)
        {
            if(term.equalsIgnoreCase(TempTerm))
            {
                count++;
            }
        }

        return count;
    }

    public int calculateAllTerm()
    {
        int count = 0;
        for(String term: query)
        {
            count++;
        }
        return count;
    }

    public double getSpecificIdf(String term, HashMap<String, Double> allIdfs)
    {
        double idf = 0;
        for(String termName: allIdfs.keySet())
        {
            if(term.equalsIgnoreCase(termName))
            {
                idf = allIdfs.get(termName);
                return idf;
            }
        }

        return idf;
    }

    public double calculateTf(int termCount, int allCount)
    {
        Tf tf = new Tf();
        return tf.calculateTf(termCount, allCount);
    }

    public double calculateTfIdf(double tf, double idf)
    {
        TfIdf tfIdf = new TfIdf();
        return tfIdf.calculateTfIdf(tf, idf);
    }

    public double calculateOneTermTfIdfValue(String term, HashMap<String, Double> allIdfs)
    {
        int allOfTerm = calculateAllTerm();
        int countOfTerm = calculateTheNoOfTermOfAllTermsInOneQuery(term);
        double tf = calculateTf(countOfTerm, allOfTerm);
        double idf = getSpecificIdf(term, allIdfs);
        double TfIdf = calculateTfIdf(tf, idf);

        return TfIdf;
    }

    public void deleteTermsDidNotAppearedInAllDocVocabulary(ArrayList<String> allTerms)
    {
        // 当query的词，出现在词库中（等于或者包含）
        // 那么这个词可以进行query

        // 这样可能会出现一个情况，同一个Allterms，可能包含多个query 词
        // 那么需要添加记录增加tf

        // appearedInAllDoc 包含的term，是全部词汇中，等于或者包含query词
        // 这些词可以用来生成vector
        // 但是这些词不能用来计算query 中的 term 的 TF

        ArrayList<String> tempNonRepeatedQuery = new ArrayList<>();

        for(String term: nonRepeatedQuery)
        {
            for(String allTerm: allTerms)
            {
                if(allTerm.toLowerCase().contains(term.toLowerCase()))
                {
                    if(!isExistedTermOrNot(allTerm, appearedInAllDoc))
                    {
                        appearedInAllDoc.add(allTerm);
                    }

                    if(!isExistedTermOrNot(term, nonRepeatedQuery))
                    {
                        tempNonRepeatedQuery.add(term);
                    }

                    ArrayList<String> tempRecordAllTermForQueryTerm = new ArrayList<>();

                    if(!isExistedTermOrNot(allTerm, appearedInAllDoc))
                    {
                        tempRecordAllTermForQueryTerm = appearedInDocAndAppearedInQuery.get(allTerm);
                    }

                    tempRecordAllTermForQueryTerm.add(term);

                    appearedInDocAndAppearedInQuery.put(allTerm, tempRecordAllTermForQueryTerm);

                }
            }
        }

        nonRepeatedQuery = tempNonRepeatedQuery;
    }


    public void recordQueryVector(HashMap<String, Double> allIdfs)
    {
        for(String term: appearedInAllDoc)
        {
            double tfIdf = calculateOneTermTfIdfValue(term, allIdfs);
            queryVector.put(term, tfIdf);
        }
    }


}
