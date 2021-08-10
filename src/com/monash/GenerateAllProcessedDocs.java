package com.monash;

public class GenerateAllProcessedDocs {

    // 遍历全部的初始文档
    // 将全部的初始文档处理
    // 生成相应的处理好的文档

    static int NoOfOriginalDocs;
    private GenerateOneProcessedDoc generateOneProcessedDoc;

    public GenerateAllProcessedDocs()
    {

        // NoOfOriginalDocs = 1;
        NoOfOriginalDocs = 1400;
        generateOneProcessedDoc = new GenerateOneProcessedDoc();
    }

    public void generateAllProcessDocs()
    {

        for(int i = 1; i <= NoOfOriginalDocs; i++)
        {
            String nameID = String.valueOf(i);
            generateOneProcessedDoc.generateOneProcessedDoc(nameID);
        }
    }
}
