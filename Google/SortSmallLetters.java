import java.util.*;

public class Main
{
    private static String sortSmallLetters(String s) {
        if (s.length() == 0) return "";
        
        int[] count = new int[26];
        
        //value for count array
        for(char ch : s.toCharArray()) {
	        int ascii = ch - 'a';
	        if(ascii>=0 && ascii<=25){
	            count[ascii]++;
	        }
	    }
	    
	    //get first index of character present in count
	    int index = 0;
	    for(int i=0; i<26;i++){
	        if(count[i] != 0) {
	            index = i;
	            break;
	        }
	    }
	    StringBuilder sb = new StringBuilder(s);
	    
	    for(int i=0; i < s.length(); i++){
	        char curr = s.charAt(i);
	        int asciiCurr = curr - 'a';
	        
	        if(asciiCurr >=0 && asciiCurr <= 25) {
	            sb.setCharAt(i,(char)(index+97));
	            count[index]--;
	            
	            if(count[index] == 0){
	                while(index < 26 && count[index] == 0){
	                    index++;
	                }
	            }
	        }
	    }
	    return sb.toString();
    }
    
	public static void main(String[] args) {
		String test = "Test@123 Google";
		String result = sortSmallLetters(test);
		
		System.out.println(result);
	}
}
