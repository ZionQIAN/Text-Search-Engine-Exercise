package com.monash;

import java.util.ArrayList;
import java.util.Scanner;

public class GenerateProcessedQuery {

    // 处理query
    // 首先将query处理成txt
    // 按照空格分开
    // 将query进行文本处理
    // 得到一个处理后的query文档

    private ArrayList<String> queryList;
    private ArrayList<String> specialTerms;


    public GenerateProcessedQuery()
    {
        queryList = new ArrayList<>();
        specialTerms = new ArrayList<>();
    }

    public ArrayList<String> dealWithQuery()
    {
        receiveRawQuery();
        System.out.println("Please wait a moment...");
        processedQuery();

        return queryList;

    }

    public void receiveRawQuery()
    {
        FileIO fileIO = new FileIO();
        String rawQuery = fileIO.acceptString("Please enter what you want to search.");
        queryList = fileIO.wordsReader(rawQuery);

    }

    public void processedQuery()
    {
        StopWord stopWord = new StopWord();
        Tokenization tokenization = new Tokenization();
        Stemming stemming = new Stemming();

        queryList = stopWord.removeStopWordsFromDocument(queryList);

        // 生成不包含特殊term的文档，以便进行下一步的Stemming步骤
        queryList = tokenization.normalTokenization(queryList);
        // 得到不需要经过Stemming处理的special terms
        specialTerms = tokenization.getSpecialTerms();

        // 对普通query进行Stemming步骤
        queryList = stemming.stemming(queryList);

        // 将stemming处理后的文档加上特殊terms，生成最后的terms 文档
        addSpecialTermsToProcessDoc(specialTerms);

    }

    public void addSpecialTermsToProcessDoc(ArrayList<String> doc)
    {
        for(String term: doc)
        {
            queryList.add(term);
        }
    }


}
