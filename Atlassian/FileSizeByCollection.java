/*
Given a list of [FileName, FileSize, [Collection]] - Collection is optional, i.e., a collection can have 1 or more files. Same file can be a part of more than 1 collection. How would you design a system

* To calculate total size of files processed.
* To calculate Top K collections based on size. Example:
file1.txt(size: 100)
file2.txt(size: 200) in collection "collection1"
file3.txt(size: 200) in collection "collection1"
file4.txt(size: 300) in collection "collection2"
file5.txt(size: 100)

Output:

Total size of files processed: 900
Top 2 collections:
- collection1 : 400
- collection2 : 300

Follow Up:

1. Concurrent calls handling - Synchronization
2. Tradeoff between speed and storage of file information
3. Why List and not Set etc.

*/



import java.util.*;

class File {
    private String name;
    private String collection;
    private long size;

    public File(String name, String collection, long size) {
        this.name = name;
        this.collection = collection;
        this.size = size;
    }

    public String getCollection() {
        return collection;
    }

    public long getSize() {
        return size;
    }
}

class CollectionSizeAnalyzer {
    public List<Map.Entry<String, Long>> getTopCollectionsBySize(List<File> files, int topN) {
        // Step 1: Aggregate file sizes by collection
        Map<String, Long> collectionSizeMap = new HashMap<>();
        for (File file : files) {
            if (file.getCollection() != null) {
                collectionSizeMap.put(
                    file.getCollection(),
                    collectionSizeMap.getOrDefault(file.getCollection(), 0L) + file.getSize()
                );
            }
        }

        // Step 2: Use a max-heap (priority queue with custom comparator)
        PriorityQueue<Map.Entry<String, Long>> maxHeap = new PriorityQueue<>(
            (e1, e2) -> Long.compare(e2.getValue(), e1.getValue()) // Descending order of size
        );

        // Add all collections to the max-heap
        maxHeap.addAll(collectionSizeMap.entrySet());

        // Step 3: Extract the top N collections from the max-heap
        List<Map.Entry<String, Long>> result = new ArrayList<>();
        for (int i = 0; i < topN && !maxHeap.isEmpty(); i++) {
            result.add(maxHeap.poll());
        }

        return result; // Already sorted in descending order
    }
}

public class Main {
    public static void main(String[] args) {
        // Sample input
        List<File> files = Arrays.asList(
            new File("file1.txt", "collection1", 500),
            new File("file2.txt", "collection2", 1000),
            new File("file3.txt", "collection1", 700),
            new File("file4.txt", "collection3", 300),
            new File("file5.txt", "collection2", 1200),
            new File("file6.txt", null, 400),
            new File("file7.txt", "collection3", 200),
            new File("file8.txt", "collection4", 1500)
        );

        CollectionSizeAnalyzer analyzer = new CollectionSizeAnalyzer();
        List<Map.Entry<String, Long>> topCollections = analyzer.getTopCollectionsBySize(files, 5);

        // Output the results
        System.out.println("Top Collections by Total File Size:");
        for (Map.Entry<String, Long> entry : topCollections) {
            System.out.println("Collection: " + entry.getKey() + ", Total Size: " + entry.getValue());
        }
    }
}


/*
Summary of Follow-Ups
Concurrent Calls:

Use thread-safe data structures like ConcurrentHashMap and ensure atomic operations via synchronization.
Consider parallel streams for aggregation.
Speed vs. Storage:

Speed: Use in-memory caching like Redis or Guava Cache.
Storage: Use on-demand computation via queries or aggregation.
List vs. Set:

List is preferred for ordered results.
Set isn't ideal here as ordering and duplicates are irrelevant for the top N requirement.
*/

import java.util.*;
import java.util.concurrent.*;

class File {
    private String name;
    private String collection;
    private long size;

    public File(String name, String collection, long size) {
        this.name = name;
        this.collection = collection;
        this.size = size;
    }

    public String getCollection() {
        return collection;
    }

    public long getSize() {
        return size;
    }
}

class CollectionSizeAnalyzer {

    public List<Map.Entry<String, Long>> getTopCollectionsBySize(List<File> files, int topN) {
        // Step 1: Thread-safe map for aggregating collection sizes
        Map<String, Long> collectionSizeMap = new ConcurrentHashMap<>();

        // Aggregate file sizes by collection using parallelStream
        files.parallelStream().forEach(file -> {
            if (file.getCollection() != null) {
                collectionSizeMap.merge(file.getCollection(), file.getSize(), Long::sum);
            }
        });

        // Step 2: Thread-safe max-heap for Top-N extraction
        PriorityBlockingQueue<Map.Entry<String, Long>> maxHeap = new PriorityBlockingQueue<>(
            collectionSizeMap.size(),
            (e1, e2) -> Long.compare(e2.getValue(), e1.getValue()) // Descending order of size
        );

        // Add all entries from the map to the heap
        maxHeap.addAll(collectionSizeMap.entrySet());

        // Step 3: Extract the top N collections
        List<Map.Entry<String, Long>> result = new ArrayList<>();
        for (int i = 0; i < topN && !maxHeap.isEmpty(); i++) {
            result.add(maxHeap.poll());
        }

        return result;
    }

    public long getTotalFileSize(List<File> files) {
        // Calculate total size of all files using parallelStream
        return files.parallelStream().mapToLong(File::getSize).sum();
    }
}

public class Main {
    public static void main(String[] args) {
        // Sample input
        List<File> files = Arrays.asList(
            new File("file1.txt", null, 100),
            new File("file2.txt", "collection1", 200),
            new File("file3.txt", "collection1", 200),
            new File("file4.txt", "collection2", 300),
            new File("file5.txt", null, 100)
        );

        CollectionSizeAnalyzer analyzer = new CollectionSizeAnalyzer();

        // Calculate total file size
        long totalSize = analyzer.getTotalFileSize(files);
        System.out.println("Total size of files processed: " + totalSize);

        // Calculate top collections by size
        List<Map.Entry<String, Long>> topCollections = analyzer.getTopCollectionsBySize(files, 2);
        System.out.println("Top Collections by Total File Size:");
        for (Map.Entry<String, Long> entry : topCollections) {
            System.out.println("Collection: " + entry.getKey() + ", Total Size: " + entry.getValue());
        }
    }
}




