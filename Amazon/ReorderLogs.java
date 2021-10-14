package Amazon;

import java.util.Arrays;
import java.util.Comparator;

public class ReorderLogs {

    public static String[] reorderLogs (String[] logs) {

        Comparator<String> myComparator = new Comparator<String>() {
            @Override
            public int compare(String log1, String log2) {
                int s1SpaceIndex = log1.indexOf(' ');
                int s2SpaceIndex = log2.indexOf(' ');
                char s1FirstCharacter = log1.charAt(s1SpaceIndex + 1);
                char s2FirstCharacter = log1.charAt(s2SpaceIndex + 1);

                if (s1FirstCharacter <= '9') {
                    if (s2FirstCharacter <= '9') {
                        return 0;
                    } else {
                        return 1;
                    }
                }
                if (s2FirstCharacter <= '9') return -1;


                int preCompute = log1.substring(s1SpaceIndex + 1).compareTo(log2.substring(s2SpaceIndex + 1));
                if (preCompute == 0) {
                    return log1.substring(0, s1SpaceIndex).compareTo(log2.substring(0, s2SpaceIndex));
                }
                return preCompute;
            }
        };

        Arrays.sort(logs, myComparator);
        return logs;
    }

    public static String[] reorderLogFiles(String[] logs) {

        Comparator<String> myComp = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                int s1SpaceIndex = s1.indexOf(' ');
                int s2SpaceIndex = s2.indexOf(' ');
                char s1FirstCharacter = s1.charAt(s1SpaceIndex + 1);
                char s2FirstCharacter = s2.charAt(s2SpaceIndex + 1);

                if (s1FirstCharacter <= '9') {
                    if (s2FirstCharacter <= '9') return 0;
                    else return 1;
                }
                if (s2FirstCharacter <= '9') return -1;

                int preCompute = s1.substring(s1SpaceIndex + 1).compareTo(s2.substring(s2SpaceIndex + 1));
                if (preCompute == 0) return s1.substring(0, s1SpaceIndex).compareTo(s2.substring(0, s2SpaceIndex));
                return preCompute;
            }
        };

        Arrays.sort(logs, myComp);
        return logs;
    }

    public static void main(String[] args) {
        String[] logs = {"dig1 8 1 5 1","let1 art can","dig2 3 6","let2 own kit dig","let3 art zero"};
        printArray(logs);
//        String[] newLogs = reorderLogs(logs);
        String[] newLogs = reorderLogFiles(logs);
        System.out.println("Reordered logs: ");
        printArray(newLogs);
    }

    private static void printArray(String[] logs) {
        for (String word : logs) {
            System.out.print(" " + word);
        }
        System.out.println();
    }
}
