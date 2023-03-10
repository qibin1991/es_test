package com.Test;

/**
 * @ClassName test
 * @Description TODO
 * @Author QiBin
 * @Date 2021/11/1614:59
 * @Version 1.0
 **/
public class test {


    public static void main(String[] args) {

        String a ="abcdme";
        String b ="mbcdmefff";

        char[] charsA = a.toCharArray();

        char[] charsB = b.toCharArray();

        String commonString ="";

        for (int i = 0; i < charsA.length; i++) {
            for (int j = 0; j < charsB.length; j++) {
                if (charsA[i] == charsB[j]){
                    int i1 = i;
                    int j1 = j;
                    for (;i1<charsA.length && j1<charsB.length && charsA[i1] == charsB[j1]; i1++,j1++){
                        if (i1!=i1-1){
                            String substring = a.substring(i, i1+1);

                            if (commonString.length()<substring.length()){
                                commonString = substring;
                            }
                        }
                    }
                }
            }
        }
        System.out.println(commonString);
    }

}
