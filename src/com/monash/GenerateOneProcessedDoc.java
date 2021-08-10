package com.monash;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GenerateOneProcessedDoc {

    // 读取一个original doc
    // 对原始文件转换为以空格为分界线的划分
    // 调取stopWord.java
    // 调取tokenization.java
    // 调取stemmer.java
    // 生成处理好的文件列表


    private ArrayList<String> processedDoc;
    private ArrayList<String> specialTerms;
    private FileIO fileIO;

    public GenerateOneProcessedDoc()
    {
        processedDoc = new ArrayList<>();
        specialTerms = new ArrayList<>();
        fileIO = new FileIO();
    }

    public void generateOneProcessedDoc(String nameID)
    {
        StopWord stopWord = new StopWord();
        Tokenization tokenization = new Tokenization();
        Stemming stemming = new Stemming();

        readOneOriginalDoc(nameID);
        processedDoc = stopWord.removeStopWordsFromDocument(processedDoc);

        // 生成不包含特殊term的文档，以便进行下一步的Stemming步骤
        processedDoc = tokenization.normalTokenization(processedDoc);
        // 得到不需要经过Stemming处理的special terms
        specialTerms = tokenization.getSpecialTerms();

        // 对普通文档进行Stemming步骤
        processedDoc = stemming.stemming(processedDoc);

        // 将stemming处理后的文档加上特殊terms，生成最后的terms 文档
        addSpecialTermsToProcessDoc(specialTerms);

        // 生成txt文档
        generateOneProcessedDocToTxt(nameID);
    }

    public void readOneOriginalDoc(String nameID)
    {

        try
        {
            File file = new File("./src/DataBaseOriginalDocs/" + nameID + ".txt");

            ArrayList<String> DocVer1 = fileIO.fileReader(file);
            ArrayList<String> DocVer2 = fileIO.transferRawTextToSingleWordsBySpace(DocVer1);
            processedDoc = DocVer2;

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addSpecialTermsToProcessDoc(ArrayList<String> doc)
    {
        for(String term: doc)
        {
            processedDoc.add(term);
        }
    }

    // 生成临时的处理好的文本
    public void generateOneProcessedDocToTxt(String nameID)
    {
        try
        {
            File file = new File("./src/TempProcessedDocs", nameID + ".txt");
            fileIO.fileWriter(file, processedDoc);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


}
