package com.example.mydatabase;

import java.util.Random;

public class Encryption {
    public String encrypt(String str) {
        int  i, n;
        String str1 = "";
        str = str.toLowerCase();
        n = str.length();
        Random rand=new Random();
        int k=1+rand.nextInt(9);
        char ch3;
        char ch1[] = str.toCharArray();
        System.out.println();

        //System.out.println("Encrypted text is");
        for (i = 0; i < n; i++) {
            if (Character.isLetter(ch1[i])) {
                ch3 = (char) (((int) ch1[i] + k - 97) % 26 + 97);
                //System.out.println(ch1[i]+" = "+ch3);
                str1 = str1 + ch3;
            } else if (ch1[i] == ' ') {
                str1 = str1 + ch1[i];
            }
        }
        str1=str1+""+k;
        //System.out.println(str1);
        return str1;
    }
}