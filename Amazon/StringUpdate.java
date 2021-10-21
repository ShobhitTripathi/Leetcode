package Amazon;

public class StringUpdate {
    public static void main(String[] args) {
        String s = "My name is [name] and my age is [age].";

        String str = fillDetails(s, "Shobhit", "26");
        System.out.println(str);
    }


    private static String fillDetails(String s, String name, String s1) {
        StringBuilder sb = new StringBuilder();
        String[] words = s.split(" ");
        int index = 0;
        for (String word : words) {
            if ((word.charAt(0) == '[')) {
                if (index == 0) {
                    sb.append(name + " ");
                    index++;
                } else
                    sb.append(s1 + " ");
            } else {
                sb.append(word + " ");
            }
        }
        return sb.toString();
    }
}
