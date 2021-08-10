package com.monash;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class StopWord {

    // 调取stop words， 形成arrayList
    // 读取的文档
    // 移除文档中的stop words

    private ArrayList<String> stopWords;

    public StopWord()
    {
        stopWords = new ArrayList<>();
    }

    public void readStopWords()
    {
        // 读取stop words
        // 将所有的stop word 转换为小写

        try {
            FileIO fileIO = new FileIO();
            File file = new File("./src/StopWords/SampleStopWords.txt");
            ArrayList<String> rawStopWords = fileIO.fileReader(file);
            ArrayList<String> rawStopWordsSplitBySpace = fileIO.transferRawTextToSingleWordsBySpace(rawStopWords);

            for(String stop: rawStopWordsSplitBySpace)
            {
                stopWords.add(stop.toLowerCase());
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> removeStopWordsFromDocument(ArrayList<String> doc)
    {
        ArrayList<String> newDoc = doc;
        ArrayList<String> needRemoveTerms = new ArrayList<>();

        readStopWords();

        for(String stop: stopWords)
        {
            for(String term: newDoc)
            {
                if(term.toLowerCase().equals(stop))
                {
                    needRemoveTerms.add(term);
                }
            }
        }

        newDoc.removeAll(needRemoveTerms);


        return newDoc;

    }
}
