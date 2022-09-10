/*
Given a string of lowercase English, there may be occurrences of consecutive repeated letters. 
Remove the minimum number of letters so that there will be no occurence of 3 or more repeated consecutive letters. 
Ex.: For input 'xxxtxxx', output: 'xxtxx'. For input 'aaabbbccc', output 'aabbcc'. 
For input 'aaaaaaaaa', output 'aa'. 
Input length can be as long as 200,000 characters.
*/


public class Main
{
    private static String correctString(String str) {
        StringBuilder sb = new StringBuilder();
        char[] s = str.toCharArray();
        
        sb.append(s[0]);
        sb.append(s[1]);
        
        for (int i = 2;i < s.length;i++) {
            if (s[i] != s[i -1] || s[i] != s[i -2 ]) {
                sb.append(s[i]);
            }
        }
        return sb.toString();
       
    }
	public static void main(String[] args) {
		System.out.println("Hello World");
		String s = "xxxtxx";
		System.out.println("Result : "+ correctString(s));
	}
}
