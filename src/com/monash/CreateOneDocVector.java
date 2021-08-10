package com.monash;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateOneDocVector {

    // 为一个处理过的文档创建IR 向量
    // 最开始得到一个文档(ArrayList)
    // 计算文档所有词汇的数量
    // 得到这个doc的全部term（不重复）
    // 得到所有term在这个文档中出现的次数
    // 计算每个term的tf值
    // 得到每个term对应的idf值
    // 计算weight(Tf-Idf)
    // 记录下来

    HashMap<String, Double> oneDocVector;

    ArrayList<String> allNonRepeatedTermOfOneDoc;

    public CreateOneDocVector()
    {
        oneDocVector = new HashMap<>();
        allNonRepeatedTermOfOneDoc = new ArrayList<>();
    }

    public HashMap<String, Double> createOneDocVector(ArrayList<String> doc, HashMap<String, Double> allIdfs)
    {
        int allCount = calculateNoOfTermsInOneDoc(doc);
        recordAllNonRepeatedTermOfOneDoc(doc);
        recordAllTheVector(doc, allCount,allIdfs);

        return oneDocVector;
    }

    public void recordAllNonRepeatedTermOfOneDoc(ArrayList<String> doc)
    {
        for(String term : doc)
        {
            if(!isExistedTermOrNot(term))
            {
                allNonRepeatedTermOfOneDoc.add(term);
            }
        }
    }

    public boolean isExistedTermOrNot(String term)
    {
        for(String ExistedTerm: allNonRepeatedTermOfOneDoc)
        {
            if(term.equalsIgnoreCase(ExistedTerm))
            {
                return true;
            }
        }

        return false;
    }

    public int calculateNoOfTermsInOneDoc(ArrayList<String> doc)
    {
        int count = 0;
        for(String term : doc)
        {
            count++;
        }

        return count;
    }

    public int calculateCountOfTermInOneDoc(String term, ArrayList<String> doc)
    {
        int count = 0;

        for(String tempTerm: doc)
        {
            if(term.equalsIgnoreCase(tempTerm))
            {
                count++;
            }
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


    public double calculateOneTermTfIdfValue(String term, ArrayList<String> doc, int allOfTerm, HashMap<String, Double> allIdfs)
    {
        int countOfTerm = calculateCountOfTermInOneDoc(term, doc);
        double tf = calculateTf(countOfTerm, allOfTerm);
        double idf = getSpecificIdf(term, allIdfs);
        double TfIdf = calculateTfIdf(tf, idf);

        return TfIdf;


    }


    public void recordAllTheVector(ArrayList<String> doc, int AllOfTerm, HashMap<String, Double> allIdfs)
    {
        for(String term: allNonRepeatedTermOfOneDoc)
        {
            double tfIdf = calculateOneTermTfIdfValue(term, doc, AllOfTerm, allIdfs);
            recordOneVector(term, tfIdf);
        }
    }

    public void recordOneVector(String term, double tfIdf)
    {
        oneDocVector.put(term, tfIdf);
    }


}
