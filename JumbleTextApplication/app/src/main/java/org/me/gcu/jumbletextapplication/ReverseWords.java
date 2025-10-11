package org.me.gcu.jumbletextapplication;

public class ReverseWords {

    public String reverseWords(String in) {
        String[] out = in.trim().split("\\s+");
        String res = "";
        for (int i = out.length - 1; i >= 0; i--)
        {
            res += out[i] + " ";
        }
        return res;
//        for (int i = 0, j = out.length-1; i < out.length/2; i++, j--)
//        {
//            temp = out[i];
//            out[i] = out[j];
//            out[j] = temp;
//        }
//        return String.join(" ", out);
    }
}
