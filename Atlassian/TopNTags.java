/*
I am given files, file size and the tag e.g. <f1, 100>, <f2, 200> <f3, 10>

<tag1, <f1, f2>> , <tag2, f3>

As output result top N tags where to files size is highest.

*/

import java.util.*;

public class TopNTagsWithHeap {

    public static void main(String[] args) {
        // Input: Files and their sizes
        Map<String, Integer> fileSizes = new HashMap<>();
        fileSizes.put("f1", 100);
        fileSizes.put("f2", 200);
        fileSizes.put("f3", 10);

        // Input: Tags and their associated files
        Map<String, List<String>> tagsToFiles = new HashMap<>();
        tagsToFiles.put("tag1", Arrays.asList("f1", "f2"));
        tagsToFiles.put("tag2", Arrays.asList("f3"));

        // Number of top tags to find
        int topN = 1;

        // Find the top N tags
        List<Map.Entry<String, Integer>> result = getTopNTagsWithHeap(fileSizes, tagsToFiles, topN);

        // Output the results
        System.out.println("Top " + topN + " tags by file size:");
        for (Map.Entry<String, Integer> entry : result) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public static List<Map.Entry<String, Integer>> getTopNTagsWithHeap(Map<String, Integer> fileSizes, Map<String, List<String>> tagsToFiles, int N) {
        // Calculate total file size for each tag
        Map<String, Integer> tagSizes = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : tagsToFiles.entrySet()) {
            String tag = entry.getKey();
            List<String> files = entry.getValue();

            int totalSize = 0;
            for (String file : files) {
                totalSize += fileSizes.getOrDefault(file, 0);
            }

            tagSizes.put(tag, totalSize);
        }

        // Min-heap to store the top N tags
        PriorityQueue<Map.Entry<String, Integer>> minHeap = new PriorityQueue<>(Map.Entry.comparingByValue());

        for (Map.Entry<String, Integer> entry : tagSizes.entrySet()) {
            minHeap.offer(entry);

            // If heap size exceeds N, remove the smallest element
            if (minHeap.size() > N) {
                minHeap.poll();
            }
        }

        // Extract elements from the heap and store them in a list
        List<Map.Entry<String, Integer>> topTags = new ArrayList<>(minHeap);
        // Sort the list in descending order (optional, for presentation)
        topTags.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        return topTags;
    }
}

/*
Time Complexity:
Calculating Tag Sizes: 
O(T⋅F), where 
T is the number of tags, and 
F is the average number of files per tag.

Heap Operations:
Inserting into the heap: 
O(logN) per tag.

Total heap operations: 
O(T⋅logN).

Overall Time Complexity: 
O(T⋅F+T⋅logN).

*/

