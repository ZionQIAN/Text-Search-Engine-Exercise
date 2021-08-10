package com.monash;

import sun.jvm.hotspot.debugger.win32.coff.TestDebugInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateIndex {

    // 读取所有处理好的文件
    // 调取方法，生成idf
    // 生成Index 文件

    // 将全部的文档中的term装进一个ArrayList中
    // 将term对应的数据装进一个hashmap中
    // 写Index的时候，先写ArrayList里面的term
    // 再写HashMap中的对应的值

    // hashMap先装tf，再计算idf，再装

    private FileIO fileIO;

    private ArrayList<String> allTerms;

    private ArrayList<ArrayList<String>> allDocs;

    private HashMap<String, ArrayList<String>> allIndexValue;

    private HashMap<String, Double> allIdfs;

    public CreateIndex()
    {
        fileIO = new FileIO();
        allTerms = new ArrayList<>();
        allDocs = new ArrayList<>();
        allIndexValue = new HashMap<>();
        allIdfs = new HashMap<>();
        indexFileIsExistedOrNot();
    }


    public void createIndex()
    {
        dealWithAllProcessedDocs();
        writeTermAndDocNumberAndFrequencyInTheDoc();
    }

    public void dealWithAllProcessedDocs()
    {
        for(int i = 1; i <= 1400; i++)
        {
            dealWithOneProcessedDoc(String.valueOf(i));
        }
    }

    // 先对一个处理好的文件进行操作
    // 计算该文档每个term的频率,生成相应的格式数据
    // 例子：cat，d1，2
    // 代表 cat这个词，在1.txt文档中，出现两次

    public void dealWithOneProcessedDoc(String docName)
    {
        ArrayList<String> tempDoc = readOneProcessedDocs(docName);

        tempDoc = fileIO.transferRawTextToSingleWordsByLine(tempDoc);

        // 储存在一个文档中出现的不重复的term
        ArrayList<String> isAppearedTerm = new ArrayList<>();

        // 将处理成ArrayList的doc存在进List中
        allDocs.add(tempDoc);

        for(String term: tempDoc)
        {

            // 加一个判断，如果是同一个文档的同一个term，不能出现两次
            if(!isExistedInTermList(term, isAppearedTerm))
            {

                // 加一个判断，如果是同一个文档的同一个term，不能出现两次
                isAppearedTerm.add(term);


                // 将第一次出现的单词存进整个application的term List中
                if (!isExistedInTermList(term, allTerms)) {
                    allTerms.add(term);
                }

                ArrayList<String> tempList = new ArrayList<>();

                int count = 0;

                count = calculateTheNumOfOneTermInOneDoc(term, tempDoc);

                //如果已经存在term key
                for (String keyForMap : allIndexValue.keySet()) {
                    if (keyForMap.equalsIgnoreCase(term)) {
                        //  调取map中term对应的value list
                        tempList = allIndexValue.get(keyForMap);
                    }
                }


                tempList.add(",d" + docName + "," + count);

                allIndexValue.put(term, tempList);
            }
        }

    }

    public double calculateIdf(String term)
    {
        double idfValue = 0;
        Idf idf = new Idf();
        idfValue = idf.idf(term, allDocs);

        return idfValue;

    }


    public ArrayList<String> readOneProcessedDocs(String docName)
    {

        ArrayList<String> tempDoc = new ArrayList<>();

        File file = new File("./src/TempProcessedDocs/" + docName + ".txt");

        try
        {
            tempDoc = fileIO.fileReader(file);
        }
        catch (IOException e) {
        e.printStackTrace();
        }

        return tempDoc;

    }

    public int calculateTheNumOfOneTermInOneDoc(String term, ArrayList<String> doc)
    {
        int count = 0;
        for(String t: doc)
        {
            if(term.equals(t))
            {
                count++;
            }
        }
        return count;
    }

    public boolean isExistedInTermList(String term, ArrayList<String> list)
    {
        for(String existTerm: list)
        {
            if(existTerm.equalsIgnoreCase(term))
            {
                return true;
            }
        }
        return false;
    }

    public void addTermAndIdfToHashMap(String term, double idf)
    {
        allIdfs.put(term, idf);
    }

    public void writeTermAndDocNumberAndFrequencyInTheDoc()
    {
        //ArrayList<String> tempListForWrite = new ArrayList<>();

        File file = new File("./src/Index/Index.txt");

        for(String term: allTerms)
        {
            StringBuffer stringBufferForWrite = new StringBuffer();

            ArrayList<String> hashMapValue = allIndexValue.get(term);

            // 加入term名字
            stringBufferForWrite.append(term);

            // 加入term在每个doc中的频率
            for(String frequencyAndNo: hashMapValue)
            {
                stringBufferForWrite.append(frequencyAndNo);
            }

            // 加入term的IDF值
            stringBufferForWrite.append(",");
            stringBufferForWrite.append(String.valueOf(calculateIdf(term)));

            addTermAndIdfToHashMap(term, calculateIdf(term));
            //tempListForWrite.add(stringBufferForWrite.toString());

            try
            {
                fileIO.fileWriterForOneValue(file, stringBufferForWrite.toString());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, Double> getAllIdfs()
    {
        return allIdfs;
    }

    public ArrayList<String> getAllTerms()
    {
        return allTerms;
    }

    public ArrayList<ArrayList<String>> getAllDocs()
    {
        return allDocs;
    }

    public void indexFileIsExistedOrNot()
    {
        File file = new File("./src/Index/Index.txt");
        if(file.exists())
        {
            file.delete();
        }
    }

}
