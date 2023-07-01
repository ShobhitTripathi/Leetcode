import java.util.ArrayList;
import java.util.List;

public class Permutations {
    public static List<String> generatePermutations(String input) {
        List<String> permutations = new ArrayList<>();
        generate("", input, permutations);
        return permutations;
    }

    private static void generate(String prefix, String remaining, List<String> permutations) {
        int length = remaining.length();
        if (length == 0) {
            permutations.add(prefix);
        } else {
            for (int i = 0; i < length; i++) {
                String newPrefix = prefix + remaining.charAt(i);
                String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
                generate(newPrefix, newRemaining, permutations);
            }
        }
    }

    public static void main(String[] args) {
        String input = "abc";
        List<String> permutations = generatePermutations(input);
        System.out.println(permutations);
    }
}
