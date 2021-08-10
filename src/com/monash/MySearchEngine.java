package com.monash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class  MySearchEngine {

    // private boolean hasGenerateAllProcessedDocs = false;

    private static HashMap<String, Double> allIdfs;
    private static ArrayList<String> allTerms;
    private static ArrayList<ArrayList<String>> allDocs;
    private static HashMap<Integer, Double> cosineResultOfQuery;

    public MySearchEngine()
    {
        allIdfs = new HashMap<>();
        allTerms = new ArrayList<>();
        allDocs = new ArrayList<>();
        cosineResultOfQuery = new HashMap<>();
    }

    public static void main(String[] args) {

        beforeSearch();

        while(true)
        {
            displayWords();
            int choose = getInt();

            if(choose == 1)
            {
                System.out.println("Please wait a moment...");
                search();
                rankAndDisplay();
            }
            else if(choose == 2)
            {
                System.exit(0);
            }
            else
                {
                    System.out.println("Please enter 1 or 2 only");
                }
        }

    }

    public static void beforeSearch()
    {

        System.out.println("Search Engine is working...Do not leave...");
        // 处理文本
        GenerateAllProcessedDocs generateAllProcessedDocs = new GenerateAllProcessedDocs();
        generateAllProcessedDocs.generateAllProcessDocs();

        // 生成index
        CreateIndex createIndex = new CreateIndex();
        createIndex.createIndex();

        // 将一些搜索需要的数据提取出来
        allIdfs = createIndex.getAllIdfs();
        allTerms = createIndex.getAllTerms();
        allDocs = createIndex.getAllDocs();
    }

    public static void search()
    {
        Search search = new Search(allDocs, allIdfs);
        cosineResultOfQuery = search.search(allDocs,allTerms,allIdfs);
    }

    public static void rankAndDisplay()
    {
        Rank rank = new Rank(cosineResultOfQuery);
        rank.rank(cosineResultOfQuery);
    }

    public static void displayWords()
    {
        System.out.println(" ");
        System.out.println("Please enter number want you want to do");
        System.out.println("1. Search;");
        System.out.println("2. Leave;");
    }

    public static int getInt()
    {
        Scanner sc  = new Scanner(System.in);
        Validation validation = new Validation();

        return validation.getInt(sc, "");
    }
}