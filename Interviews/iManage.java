public class iManage {
    public static String computeString(String S) {
        if (S.length() == 0 || S == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String finalString = "";
        int count = 0;
        for(int i = 0; i < S.length();i++) {
            if (Character.isDigit(S.charAt(i))) {
                sb.append(S.charAt(i));
                count++;
            }
            if (count == 3) {
                sb.append('-');
                count = 0;
            }
        }
        if (isLastTokenSingle(sb.toString())) {
            int secondLastIndex = sb.length() - 2;
            int thirdLastIndex = sb.length() - 3;
            char temp = sb.charAt(thirdLastIndex);
            sb.insert(secondLastIndex, temp);
            sb.insert(thirdLastIndex, '-');

            finalString = sb.substring(0, sb.length() - 2);
        } else {
            finalString = sb.toString();
        }
        return finalString;
    }


    public static boolean isLastTokenSingle (String S) {
        String[] numbers = S.split("-");
        int len = numbers.length;
        if (numbers[len - 1].length() < 2) {
            return true;
        }
        else
            return false;
    }

    public static void main(String[] args) {
        String s = " 3 3 4--------";
        System.out.println(computeString(s));
    }
}