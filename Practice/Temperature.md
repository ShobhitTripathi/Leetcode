Problem Summary
Given a huge file (millions of lines) where each line is:
CITY_CODE, TEMPERATURE

Example:
```
DL, 30
BOM, 32
DL, 31
CCU, 29
HYD, 25
DL, 32
DL, 20
```

We must compute for each city:

City: MIN / MAX / AVERAGE


Example output:
```
DL: 20 / 32 / 28.25
BOM: 32 / 32 / 32
CCU: 29 / 29 / 29
HYD: 25 / 25 / 25
```

Brief Approach
```
1. Use streaming line-by-line processing
Avoid reading entire file into memory.
Use BufferedReader, Files.lines(), or Java Streams.

2. Maintain aggregated stats per city
For each city:
min temperature
max temperature
sum of temperatures
count of temperature entries

This can be stored in a simple POJO or Map structure:
Map<String, Stats>
Where Stats = { min, max, sum, count }
Memory is tiny because max 100 cities.
```

Map solution without file reading
```
import java.util.*;

class Solution {

    static class Stats {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;
        long count = 0;

        void add(double temp) {
            min = Math.min(min, temp);
            max = Math.max(max, temp);
            sum += temp;
            count++;
        }

        double avg() {
            return sum / count;
        }
    }

    public static void main(String[] args) {
        // Example input lines (can be millions)
        List<String> lines = Arrays.asList(
            "DL, 30",
            "BOM, 32",
            "DL, 31",
            "CCU, 29",
            "HYD, 25",
            "DL, 32",
            "DL, 20"
        );

        // Core MAP solution
        Map<String, Stats> map = new HashMap<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            String city = parts[0].trim();
            double temp = Double.parseDouble(parts[1].trim());

            map.computeIfAbsent(city, k -> new Stats()).add(temp);
        }

        // Print final results
        map.forEach((city, stats) -> {
            System.out.println(city + ": " +
                stats.min + "/" +
                stats.max + "/" +
                stats.avg());
        });
    }
}

```

MAP solution with file reading
```java
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SimpleCityTemperatureStats {

    static class Stats {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;
        long count = 0;

        void add(double temp) {
            min = Math.min(min, temp);
            max = Math.max(max, temp);
            sum += temp;
            count++;
        }

        double avg() {
            return sum / count;
        }
    }

    public static void main(String[] args) throws Exception {

        Path path = Paths.get("input.txt");

        // MAP ONLY
        Map<String, Stats> map = new HashMap<>();

        // STREAM file line by line
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) continue;

                String[] parts = line.split(",");
                String city = parts[0].trim();
                double temp = Double.parseDouble(parts[1].trim());

                map.computeIfAbsent(city, c -> new Stats()).add(temp);
            }
        }

        // Print results
        map.forEach((city, stats) -> {
            System.out.println(city + ": " 
                + stats.min + "/" 
                + stats.max + "/" 
                + stats.avg());
        });
    }
}

```

ConcurrentHashMap soltuion without file reading
```java
import java.util.*;
import java.util.concurrent.*;

public class ConcurrentCityStats {

    // Thread-safe Stats container
    static class Stats {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;
        long count = 0;

        // synchronized ensures atomic updates within each Stats object
        synchronized void add(double temp) {
            min = Math.min(min, temp);
            max = Math.max(max, temp);
            sum += temp;
            count++;
        }

        double avg() {
            return sum / count;
        }
    }

    public static void main(String[] args) throws Exception {

        // Example input (can be millions of entries)
        List<String> lines = Arrays.asList(
            "DL, 30",
            "BOM, 32",
            "DL, 31",
            "CCU, 29",
            "HYD, 25",
            "DL, 32",
            "DL, 20"
        );

        // GLOBAL concurrent map (thread-safe)
        ConcurrentHashMap<String, Stats> map = new ConcurrentHashMap<>();

        // Fixed thread pool (4 threads for a 4-core machine)
        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (String line : lines) {
            executor.submit(() -> {
                String[] parts = line.split(",");
                String city = parts[0].trim();
                double temp = Double.parseDouble(parts[1].trim());

                // Insert or get existing Stats atomically
                map.computeIfAbsent(city, k -> new Stats()).add(temp);
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // Final result print
        map.forEach((city, stats) -> {
            System.out.println(city + ": " + 
                stats.min + "/" + 
                stats.max + "/" + 
                stats.avg());
        });
    }
}


```

Concurrent Solution with file reading
```java
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class CityTemperatureStats {

    static class Stats {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;
        long count = 0;

        synchronized void add(double temp) {
            min = Math.min(min, temp);
            max = Math.max(max, temp);
            sum += temp;
            count++;
        }

        double avg() {
            return sum / count;
        }
    }

    public static void main(String[] args) throws Exception {
        Path file = Paths.get("input.txt");

        // Thread-safe global map for merging stats
        ConcurrentHashMap<String, Stats> map = new ConcurrentHashMap<>();

        // Parallel processing of large file
        Files.lines(file)
            .parallel()
            .forEach(line -> {
                if (line.trim().isEmpty()) return;

                String[] parts = line.split(",");
                String city = parts[0].trim();
                double temp = Double.parseDouble(parts[1].trim());

                // Insert or update atomically
                map.computeIfAbsent(city, k -> new Stats()).add(temp);
            });

        // Output results
        map.forEach((city, stats) -> {
            System.out.println(city + ": " + 
                stats.min + "/" + 
                stats.max + "/" + 
                stats.avg());
        });
    }
}
```
