package com.monash;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Rank {

    int displayDoc;
    Integer[] docName;
    Double[] cosineValue;

    public Rank(HashMap<Integer, Double> cosineResultOfQuery)
    {
        displayDoc = 10;
        docName = new Integer[cosineResultOfQuery.size()];
        cosineValue = new Double[cosineResultOfQuery.size()];
    }

    public void rank(HashMap<Integer, Double> cosineResultOfQuery)
    {
        receiveDocNumber();
        splitHashMapToTwoArray(cosineResultOfQuery);
        sortForBiggestCosineAndDocName();
        displayDocToUser();

    }

    public void receiveDocNumber()
    {
        Scanner sc = new Scanner(System.in);
        Validation validation = new Validation();
        String inputInt = "How many documents you want to see?";
        displayDoc = validation.getInt(sc, inputInt, 1);
    }

    public void splitHashMapToTwoArray(HashMap<Integer, Double> cosineResultOfQuery)
    {
        int i = 0;
        for(Integer tempDocName: cosineResultOfQuery.keySet())
        {
            docName[i] = tempDocName;
            cosineValue[i] = cosineResultOfQuery.get(tempDocName);
            i++;
        }
    }

    public void sortForBiggestCosineAndDocName()
    {
        double tempCosine;
        int tempDocName;

        for(int i = 0; i < cosineValue.length; i++)
        {
            for(int j = i + 1; j < cosineValue.length; j++)
            {
                if(cosineValue[i] < cosineValue[j])
                {
                    // 交换cosine 值的位置
                    tempCosine = cosineValue[i];
                    cosineValue[i] = cosineValue[j];
                    cosineValue[j] = tempCosine;

                    // 根据序号，交换doc name的名字
                    tempDocName = docName[i];
                    docName[i] = docName[j];
                    docName[j] = tempDocName;

                }
            }
        }
    }

    public void displayDocToUser()
    {
        for(int i = 0; i < displayDoc; i++)
        {
            System.out.println(docName[i] + "," + keepThreeDecimal(cosineValue[i]));
        }
    }

    public Double keepThreeDecimal(double cosine)
    {
        BigDecimal bd = new BigDecimal(cosine);
        double newCosine = bd.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

        return newCosine;
    }
}
