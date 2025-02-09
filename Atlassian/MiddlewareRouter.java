/*
 Middleware Router
    * Interface: addRoute(path, result) and callRoute(path).
    * Follow-up: Handle wildcard paths using regex or pattern matching.
    * Reference: Middleware Router Design.
*/



// String bases Router
import java.util.*;

public class StringRouter {
    private final TrieNode root;

    public StringRouter() {
        this.root = new TrieNode();
    }

    // Add a route with a specific path and result
    public void addRoute(String path, String result) {
        String[] parts = path.split("/");
        TrieNode current = root;

        for (String part : parts) {
            current.children.putIfAbsent(part, new TrieNode());
            current = current.children.get(part);
        }

        current.result = result;
        current.isEndpoint = true;
    }

    // Call a route and return the result (handles wildcard paths)
    public String callRoute(String path) {
        String[] parts = path.split("/");
        return findRoute(parts, 0, root);
    }

    // Recursive helper for route matching
    private String findRoute(String[] parts, int index, TrieNode node) {
        if (node == null) return null;

        // If we've reached the end of the path
        if (index == parts.length) {
            return node.isEndpoint ? node.result : null;
        }

        String part = parts[index];

        // Exact match
        if (node.children.containsKey(part)) {
            String result = findRoute(parts, index + 1, node.children.get(part));
            if (result != null) return result;
        }

        // Wildcard (*) match (single segment)
        if (node.children.containsKey("*")) {
            String result = findRoute(parts, index + 1, node.children.get("*"));
            if (result != null) return result;
        }

        // Double wildcard (**) match (all remaining segments)
        if (node.children.containsKey("**")) {
            return findRoute(parts, parts.length, node.children.get("**")); // Skip to the end
        }

        // No match
        return null;
    }

    // TrieNode structure for the router
    private static class TrieNode {
        Map<String, TrieNode> children = new HashMap<>();
        String result;
        boolean isEndpoint;

        TrieNode() {
            this.isEndpoint = false;
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        StringRouter router = new StringRouter();

        // Add routes
        router.addRoute("/home", "HomePage");
        router.addRoute("/about", "AboutPage");
        router.addRoute("/user/*", "UserDetails");
        router.addRoute("/admin/**", "AdminPanel");

        // Test exact matches
        System.out.println(router.callRoute("/home")); // Output: HomePage
        System.out.println(router.callRoute("/about")); // Output: AboutPage

        // Test wildcard matches
        System.out.println(router.callRoute("/user/123")); // Output: UserDetails
        System.out.println(router.callRoute("/admin/settings/general")); // Output: AdminPanel

        // Test unmatched routes
        System.out.println(router.callRoute("/unknown")); // Output: null
    }
}


// Generic Router
import java.util.*;

public class MiddlewareRouter<T> {
    private final TrieNode<T> root;

    public MiddlewareRouter() {
        this.root = new TrieNode<>();
    }

    // Add a route and associate it with a result
    public void addRoute(String path, T result) {
        String[] parts = path.split("/");
        TrieNode<T> current = root;

        for (String part : parts) {
            current.children.putIfAbsent(part, new TrieNode<>());
            current = current.children.get(part);
        }

        current.result = result;
        current.isEndpoint = true;
    }

    // Call a route and return the result (handles wildcard paths)
    public T callRoute(String path) {
        String[] parts = path.split("/");
        return findRoute(parts, 0, root);
    }

    // Recursive helper to find a route
    private T findRoute(String[] parts, int index, TrieNode<T> node) {
        if (node == null) return null;

        // If we're at the last segment of the path
        if (index == parts.length) {
            return node.isEndpoint ? node.result : null;
        }

        String part = parts[index];

        // Match exact path
        if (node.children.containsKey(part)) {
            T result = findRoute(parts, index + 1, node.children.get(part));
            if (result != null) return result;
        }

        // Match wildcard (*): Single segment match
        if (node.children.containsKey("*")) {
            T result = findRoute(parts, index + 1, node.children.get("*"));
            if (result != null) return result;
        }

        // Match wildcard (**): Matches all remaining segments
        if (node.children.containsKey("**")) {
            return findRoute(parts, parts.length, node.children.get("**")); // Skip to end
        }

        // No match found
        return null;
    }

     public void printTrie() {
        printTrieHelper(root, "");
    }

    // Recursive helper method to print the Trie structure
    private void printTrieHelper(TrieNode<T> node, String prefix) {
        if (node == null) return;

        if (node.isEndpoint) {
            System.out.println(prefix + " -> " + node.result);
        }

        for (Map.Entry<String, TrieNode<T>> entry : node.children.entrySet()) {
            printTrieHelper(entry.getValue(), prefix + "/" + entry.getKey());
        }
    }

    // TrieNode structure
    private static class TrieNode<T> {
        Map<String, TrieNode<T>> children = new HashMap<>();
        T result;
        boolean isEndpoint;

        TrieNode() {
            this.isEndpoint = false;
        }
    }

    // Test the implementation
    public static void main(String[] args) {
        MiddlewareRouter<String> router = new MiddlewareRouter<>();
        
        // Add routes
        router.addRoute("/user/profile", "UserProfile");
        router.addRoute("/user/settings", "UserSettings");
        router.addRoute("/order/*", "OrderDetails");
        router.addRoute("/product/**", "ProductDetails");

        // Test exact paths
        System.out.println(router.callRoute("/user/profile")); // Output: UserProfile
        System.out.println(router.callRoute("/user/settings")); // Output: UserSettings

        // Test wildcard paths
        System.out.println(router.callRoute("/order/12345")); // Output: OrderDetails
        System.out.println(router.callRoute("/product/electronics/phones")); // Output: ProductDetails

        // Test unmatched paths
        System.out.println(router.callRoute("/unknown/path")); // Output: null

       router.printTrie();
    }
}
