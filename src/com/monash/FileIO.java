package com.monash;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileIO {

    public static ArrayList<String> fileReader(File fileName) throws IOException
    {
        // 将txt文档进行读取
        ArrayList<String> text = new ArrayList<>();
        String file = null;
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        while ((file = br.readLine()) != null)
        {
            text.add(file);
        }

        fr.close();

        return text;
    }

    public static ArrayList<String> transferRawTextToSingleWordsBySpace(ArrayList<String> rawText)
    {

          ArrayList<String> newTransferDoc = new ArrayList<>();

          for (String term: rawText)
          {
              // 按照空格，将单词一个个划分出来
              String[] data = term.split(" ");


              for (String newT: data)
              {
                  // 删掉可能出现的空值
                  if(!newT.equals(""))
                  {
                      // 将划分出来的词存入
                      newTransferDoc.add(newT);
                  }
              }

          }

          return newTransferDoc;

    }

    public static ArrayList<String> transferRawTextToSingleWordsByLine(ArrayList<String> rawText)
    {

        ArrayList<String> newTransferDoc = new ArrayList<>();

        for (String term: rawText)
        {
            // 按照line，将单词一个个划分出来
            String[] data = term.split("\n");


            for (String newT: data)
            {
                // 删掉可能出现的空值
                if(!newT.equals(""))
                {
                    // 将划分出来的词存入
                    newTransferDoc.add(newT);
                }
            }

        }

        return newTransferDoc;

    }

    public static void fileWriter(File fileName, ArrayList<String> doc) throws IOException
    {
        FileWriter fileWriter = new FileWriter(fileName);

        for(String term: doc)
        {
            fileWriter.write(term + "\n");
        }

        fileWriter.flush();
        fileWriter.close();
    }

    public static void fileWriterForOneValue(File fileName, String value) throws IOException
    {
        FileWriter fileWriter = new FileWriter(fileName, true);

        fileWriter.write(value + "\n");

        fileWriter.flush();
        fileWriter.close();
    }

    public static String acceptString(String words)
    {
        Scanner sr = new Scanner(System.in);
        System.out.println(words);
        String input = sr.nextLine();
        return input;
    }


    // 对一个String 进行空格分割，组成一个ArrayList

    public static ArrayList<String> wordsReader(String words)
    {

        ArrayList<String> newTransferDoc = new ArrayList<>();

            // 按照空格，将单词一个个划分出来
            String[] data = words.split(" ");

            for (String newT: data)
            {
                // 删掉可能出现的空值
                if(!newT.equals(""))
                {
                    // 将划分出来的词存入
                    newTransferDoc.add(newT);
                }
            }

        return newTransferDoc;
    }


}
