package com.monash;

import java.util.ArrayList;

public class Tokenization {

    //1. 连字符必须变成单个字符，最终token不能包括连字符；
    //2. Email，Url，IP必须保留为单个token；（单独存储）
    //3. 单引号和反引号内的词单独保存为token；（单独存储）
    //4. 将两个或者多个空格隔开的开头字母大写的单词列为单个token（意味着token list中包含空格）；（单独存储）
    //5. 首字母缩略词应该单独存为token，同时保留与词相关的”.”。（单独存储）
    //6. 对于其他文本，使用符号{.,:;”’()?!}将其拆分为token ；

    // TODO: 可以合并多个循环过程，减少循环次数，减少程序处理时间
    private ArrayList<String> newDoc;
    private ArrayList<String> specialTerms;
    private ArrayList<String> tempTerms;

    public Tokenization()
    {
        newDoc = new ArrayList<>();
        specialTerms = new ArrayList<>();
        tempTerms = new ArrayList<>();
    }

    public ArrayList<String> normalTokenization(ArrayList<String> doc)
    {
        newDoc = doc;

        // tokenization process
        deleteHyphen();
        keepEmail();
        keepUrl();
        keepIP();
        dealWithSingleQuoteAndBackQuote();
        dealWithMultiCapitalWords();
        dealWithAcronymWord();

        // 删掉不需要进行特殊符号判定的特殊terms
        deleteTempTermsFromNewDoc();

        transferUpcaseToLowcase();
        deleteAllChars();
        deleteAllNullElement();

        // 把特殊的词汇重新塞进原doc中
        // addSpecialTermToNewDoc();

        return newDoc;
    }

    public void deleteHyphen()
    {
        for(int i = 0; i < newDoc.size(); i++)
        {

                if(newDoc.get(i).contains("-")) {
                    String newTerm = newDoc.get(i).replace("-", "");
                    newDoc.set(i, newTerm);
                }
        }

    }

    public void keepEmail()
    {


        for(String term: newDoc)
        {

            // email regular expression
            if(term.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"))
            {
                // 从原doc中删去这个词，添加进特殊term list
                specialTerms.add(term);
                tempTerms.add(term);
            }
        }
    }

    public void keepUrl()
    {
        for(String term: newDoc)
        {
            // url regular expression
            if(term.matches("(((http|ftp|https)://)|(www\\.))[a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6}(:[0-9]{1,4})?(/[a-zA-Z0-9\\&%_\\./-~-]*)?"))
            {
                // 从原doc中删去这个词，添加进特殊term list
                specialTerms.add(term);
                tempTerms.add(term);
            }
        }
    }

    public void keepIP()
    {
        for(String term: newDoc)
        {
            // IP regular expression
            if(term.matches("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}"))
            {
                // 从原doc中删去这个词，添加进特殊term list
                specialTerms.add(term);
                tempTerms.add(term);
            }
        }

    }

    public void dealWithSingleQuoteAndBackQuote()
    {
        for(int i = 0; i < newDoc.size(); i++)
        {
            if(newDoc.get(i).startsWith("'") && (countTheNumOfCharExistInOneTerm(newDoc.get(i), "'") == 1))
            {

                // 找到单引号开头的单词
                // 确认不止单引号内不止一个单词
                // 下一步是找到以单引号结尾的单词

                findTheWordEndWithChar(i, "'");

            }
            else if(newDoc.get(i).startsWith("`") && (countTheNumOfCharExistInOneTerm(newDoc.get(i), "`") == 1))
            {
                findTheWordEndWithChar(i, "`");
            }
        }

    }

    public void findTheWordEndWithChar(int i, String chars)
    {

        StringBuffer sb = new StringBuffer();

        ArrayList<Integer> specialCharWords = new ArrayList<>();

        int a = i + 1;

        specialCharWords.add(i);
        specialCharWords.add(a);

        sb.append(deleteSpecialChar(newDoc.get(i), chars));
        sb.append(" ");
        sb.append(deleteSpecialChar(newDoc.get(a), chars));

        boolean flag = true;

        while(flag == true)
        {
            if (newDoc.get(a).contains(chars)) {
                // 找到以特殊符号结尾的单词
                // 由于空格划分的原因，特殊符号结尾的词被space划分后，可能出现以其他符号结尾的情况
                // 所以不使用endswith，而使用contains
                flag = false;
            } else {
                // 没找到，继续找下一个
                a++;
                specialCharWords.add(a);
                sb.append(" ");
                sb.append(deleteSpecialChar(newDoc.get(a), chars));
            }
        }

        // 找到全部特殊符号内的单词组合后
        // 将组合存储
        // 在原doc中删除对应的单词
        // 在之后重新添加

        // 删除原doc中组合单词的单独存在

        for(int w = 0; w < specialCharWords.size(); w++)
        {
            tempTerms.add(newDoc.get(specialCharWords.get(w)));
        }

        // 将组合单词存储，以便最后在token list上添加

        specialTerms.add(sb.toString());

    }

    public String deleteSpecialChar(String term, String chars)
    {

        // 判断 如果是特殊符号的后半部分，进行下面的操作
        if(!(String.valueOf(term.charAt(0)).equals(chars)))
        {
            term = deleteRedundantCharAfterSpecialChar(term, chars);
        }

        String newTerm = "";
        newTerm = term.replace(chars, "");
        return newTerm;
    }

    public void dealWithMultiCapitalWords()
    {

        deleteAllNullElement();

        for(int i = 0; i < newDoc.size(); i++)
        {
            if(theFirstLetterIsUpcaseOrNot(i))
            {
                // 是首字母大写
                if(firstCapitalWordOrNot(i))
                {
                    // 不是首字母大写单词
                }
                else
                    {
                        // 是首字母大写单词
                        multiCapitalWords(i);
                    }
            }
        }

    }

    public void multiCapitalWords(int i) {

        // 已知index i 对应的term是首字母大写单词
        // 判断与之相连的单词是否是首字母大写的单词
        // 如果接下来的单词是首字母大写的单词，那么将该单词和上一个单词放在一起
        // 当出现两个及以上首字母大写单词时
        // 将单词组合构成新的token
        // 并且在初始的doc文档中删去组合中的单词
        // 在最后的token list中，再添加组合token



        ArrayList<Integer> capitalWords = new ArrayList<>();

        StringBuffer sb = new StringBuffer();

        sb.append(newDoc.get(i));

        capitalWords.add(i);

        int a = i + 1;
        boolean flag = true;

        // 循环进行判断，直到出现一个非首字母大写的单词为止；


        while (flag == true)

        {
            // 目前的位置信息i能否取到值

            if(a >= newDoc.size())
            {
                flag = false;
            }

            // 判断下一个词是否是首字母大写

            if (theFirstLetterIsUpcaseOrNot(a)) {

                // 是首字母大写

                if (firstCapitalWordOrNot(a)) {

                    // 不是单首字母大写单词
                    // 结束循环

                    flag = false;
                } else {

                    // 将首字母大写的单词放在一起并添加空格

                    sb.append(" ");
                    sb.append(newDoc.get(a));
                    capitalWords.add(a);
                    a++;
                }
            }
            else
                {
                    flag = false;
                }
        }

        // 进行判断，是否有两个及以上的首字母大写单词的组合

        if(capitalWords.size() >= 2)
        {
            // 是的
            // 在原本的doc中删掉分开的组合中的单词


            for(Integer tempI: capitalWords)
            {
                tempTerms.add(newDoc.get(tempI));
            }

            // 将组合单词存储，以便最后在token list上添加

            specialTerms.add(sb.toString());
        }

    }

    public boolean firstCapitalWordOrNot(int i)
    {
        // 已知首字母大写
        // 判断该单词接下来的字母是否大写
        // 该判定前提是提供的单词书写满足规范
        // 不存在大写，小写，以及特殊符号出现在同一个单词的情况

        int count = 0;
        String word = newDoc.get(i);
        for(int j = 0; j < word.length(); j++)
        {
            char letter = word.charAt(j);
            if (letter >= 'A' && letter <= 'Z')
            {
                count++;
            }
        }

        if(count == word.length())
        {
            return true;
        }else
        {
            return false;
        }

    }

    public boolean theFirstLetterIsUpcaseOrNot(int i)
    {

        char letter = newDoc.get(i).charAt(0);

        if(!Character.isLetter(letter))
        {
            return false;
        }

        if (letter >= 'A' && letter <= 'Z')
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void dealWithAcronymWord()
    {
        // 判断首字母是否大写
        // 该判定前提是提供的单词书写满足规范
        // 不存在大写，小写，以及特殊符号出现在同一个单词的情况

        for(int i = 0; i < newDoc.size(); i++)
        {
            if(theFirstLetterIsUpcaseOrNot(i))
            {

                // 首字母大写

                if(firstCapitalWordOrNot(i))
                {
                    // 是全大写单词
                    // 将特殊单词存储

                    specialTerms.add(newDoc.get(i));

                    // 从原doc中删除该全大写单词，在之后再添加

                    tempTerms.add(newDoc.get(i));
                }
            }
        }

    }

    public void deleteAllChars()
    {
        // 列出全部的特殊符号
        String[] chars = new String[] { ",", ".", "/", "!", "@", "#", "$", "%", "^", "&", "*", "'", "(", ")", "{" , "}", ":", ";", "\""};



        for(int i = 0; i < newDoc.size(); i++)
        {

            for(String c: chars)
            {
                if (newDoc.get(i).contains(c)) {
                    String newTerm = newDoc.get(i).replace(c, "");
                    newDoc.set(i, newTerm);
                }
            }
        }
    }

    //public void addSpecialTermToNewDoc()
    //{
    //    for(String term: specialTerms)
    //    {
    //        newDoc.add(term);
    //    }
    //}

    // 判断特殊符号内是否只有一个单词，无空格
    // 因为空格分割的方式，无法使用endswith 方法判断
    public int countTheNumOfCharExistInOneTerm(String term, String chars)
    {
        int count = 0;
        ArrayList<Character> ch = new ArrayList<>();

        for(int i = 0; i < term.length(); i++)
        {
            ch.add(term.charAt(i));
        }

        for(Character c: ch)
        {
            if(String.valueOf(c).equals(chars))
            {
                count++;
            }
        }

        return count;
    }

    public String deleteRedundantCharAfterSpecialChar(String term, String chars)
    {
        int i = term.indexOf(chars);

        StringBuffer tempTerm = new StringBuffer();
        ArrayList<Character> ch = new ArrayList<>();

        // 将特殊符号前的词全部提取出来，塞进list里


        for(int a = 0; a < i; a++)
        {
            ch.add(term.charAt(a));
        }

        for(Character c: ch)
        {
            tempTerm.append(String.valueOf(c));
        }

        String temp = tempTerm.toString();

        return temp;

    }

    public void transferUpcaseToLowcase()
    {
        ArrayList<String> lowcaseList = new ArrayList<>();

        for(String term: newDoc)
        {
            lowcaseList.add(term.toLowerCase());
        }

        newDoc = lowcaseList;
    }

    public void deleteTempTermsFromNewDoc()
    {
        newDoc.removeAll(tempTerms);
    }


    public void deleteAllNullElement()
    {
        ArrayList<String> noNullList = new ArrayList<>();

        for(String term: newDoc)
        {
            if(!term.equals(""))
            {
                noNullList.add(term);
            }
        }

        newDoc = noNullList;
    }

    public ArrayList<String> getSpecialTerms()
    {
        return specialTerms;
    }




}
