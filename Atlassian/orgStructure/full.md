# **Find the Closest Common Group for Target Employees**

This is a **graph/tree data structure + concurrency + system design** problem.

The question has four progressive parts. The important thing is to evolve the solution as the underlying organizational model changes, rather than trying to solve all four parts with one algorithm.

Let's approach it as you would in a Senior/Staff Software Engineer interview.

## **1. Understand the problem**

Initially, assume the organization is a tree:

- Every group can have multiple subgroups.
- Every employee belongs to exactly one group.
- A group can have one parent (except the root).
- We need to find the lowest/closest common ancestor (LCA) of all target employees' groups.

Example:

[10.2: Organizational Structures and Design - Business LibreTexts](https://images.openai.com/static-rsc-4/VkGpSyKHG7oKpa6j7lq4s-tgwwmPKijG__lrpYrqBxTCtb8Ed1PXl16Rf3ZNnsa3otSBtYQLl0fvtg6uIN5DYQw9g384iHa2L9tW8Zf1emSu4iPMoVTAZ0P4YeFCX4D3a0HlaAn6OdsxxvNT2xHyKp3PB1Hd-esyhWrHOOdfD4I?purpose=inline)

[biz.libretexts.org](https://biz.libretexts.org/Bookshelves/Management/Principles_of_Management_%28OpenStax%29/10%3A_Organizational_Structure_and_Change/10.02%3A_Organizational_Structures_and_Design?utm_source=chatgpt.com)

Consider this hierarchy:

```
                 Company
                /       \
          Engineering   Sales
           /      \       |
      Platform   Product Marketing
       /    \
    Alice  Bob
```

For target employees Alice and Bob:

- Alice belongs to Platform.
- Bob belongs to Platform.
- Closest common group = Platform.

For Alice and an employee in Product:

\- Closest common group = Engineering.

For Alice and an employee in Marketing:

\- Closest common group = Company.

## **2. Part A: Basic tree implementation**

### **Approach: Lowest Common Ancestor (LCA)**

We can solve this using DFS and parent pointers.

**Data model (Java):**

```

class Group {
    String id;
    List<Group> children = new ArrayList<>();
    List<Employee> employees = new ArrayList<>();
    Group parent;

    Group(String id) {
        this.id = id;
    }
}

class Employee {
    String id;
    Group group;

    Employee(String id, Group group) {
        this.id = id;
        this.group = group;
    }
}

```

### **Algorithm**

1. Find the group of each target employee.
2. Take the first employee's group as the initial candidate.
3. Find the LCA between the candidate and each subsequent employee's group.
4. Update the candidate with the LCA.
5. Return the final candidate.

Why does this work?

LCA is associative for nodes in a tree:

\\[ LCA(LCA(A,B),C)=LCA(A,B,C) \\]

### **Java solution**

```

public Group getCommonGroupForEmployees(
        List<Employee> employees) {

    if (employees == null || employees.isEmpty()) {
        return null;
    }

    Group result = employees.get(0).group;

    for (int i = 1; i < employees.size(); i++) {
        result = lca(result, employees.get(i).group);

        if (result == null) {
            return null;
        }
    }

    return result;
}

private Group lca(Group a, Group b) {

    Set<Group> ancestors = new HashSet<>();

    while (a != null) {
        ancestors.add(a);
        a = a.parent;
    }

    while (b != null) {
        if (ancestors.contains(b)) {
            return b;
        }
        b = b.parent;
    }

    return null;
}

```

### **Complexity analysis**

Let:

- N = number of groups.
- K = number of target employees.
- H = height of the hierarchy.

| Operation                                 | Complexity |
| ----------------------------------------- | ---------- |
| Find employee's group (with employee map) | O(1)       |
| One LCA operation                         | O(H)       |
| K employees                               | O(K × H)   |
| Auxiliary space                           | O(H)       |

This is a reasonable baseline implementation.

## **3. Part B: Groups can be shared across multiple organizations**

Major change: Tree → Directed Acyclic Graph (DAG)

Now the assumptions change:

1. A group can have multiple parent groups.
2. An employee can belong to multiple groups.
3. A group can be shared across different organizational structures.
4. We still need to return exactly ONE closest common group.

A tree is no longer sufficient because a node can have multiple parents.

### **3.1 Change the data model**

Instead of maintaining a single `parent`, maintain multiple parents.

```

class Group {
    String id;

    Set<Group> parents = new HashSet<>();
    Set<Group> children = new HashSet<>();

    Set<Employee> employees = new HashSet<>();
}

class Employee {
    String id;

    Set<Group> groups = new HashSet<>();
}

```

The organization is now represented as a directed graph:

- Edge: Parent Group → Child Group.
- An employee can be associated with multiple groups.
- A group can have multiple incoming edges.

We should enforce that the graph remains acyclic. Otherwise, ancestor traversal could enter an infinite loop.

### **3.2 How do we find ONE closest common group?**

In a DAG, there can be multiple common ancestors.

For example:

```
             Company
             /     \
            A       B
             \     /
               C
              / \
             D   E
```

Suppose:

- Employee 1 belongs to D.
- Employee 2 belongs to E.

Both A and B are common ancestors of D and E, and neither is closer than the other.

There is no inherently unique LCA in this DAG.

**We need to define what closest means.**

My proposed definition:

Choose the common ancestor that minimizes the maximum shortest-path distance from any target employee's group.

Formally:

\\[ Score(G) = \max\_{i=1}^{K} Distance(E_i,G) \\]

Select:

\\[ G^\* = \arg\min\_{G \in CommonAncestors} Score(G) \\]

If multiple groups have the same score, break ties using:

1. Minimum sum of distances.
2. Lexicographically smallest group ID.

This guarantees ONE deterministic result.

### **3.3 Algorithm**

For every target employee:

1. Start BFS from all groups the employee belongs to.
2. Traverse parent edges.
3. Record the minimum distance to every reachable ancestor.
4. Intersect the ancestor sets of all employees.
5. Calculate the score of every common ancestor.
6. Return the group with the minimum score.

### **Java implementation**

```

public Group getCommonGroupForEmployees(
        List<Employee> employees) {

    if (employees == null || employees.isEmpty()) {
        return null;
    }

    List<Map<Group, Integer>> distances = new ArrayList<>();

    for (Employee employee : employees) {
        Map<Group, Integer> map = getAncestorDistances(employee.groups);

        if (map.isEmpty()) {
            return null;
        }

        distances.add(map);
    }

    Group bestGroup = null;
    int bestMaxDistance = Integer.MAX_VALUE;
    int bestTotalDistance = Integer.MAX_VALUE;

    for (Group candidate : distances.get(0).keySet()) {

        int maxDistance = 0;
        int totalDistance = 0;
        boolean common = true;

        for (Map<Group, Integer> distanceMap : distances) {

            Integer distance = distanceMap.get(candidate);

            if (distance == null) {
                common = false;
                break;
            }

            maxDistance = Math.max(maxDistance, distance);
            totalDistance += distance;
        }

        if (!common) {
            continue;
        }

        if (maxDistance < bestMaxDistance ||
            (maxDistance == bestMaxDistance &&
             totalDistance < bestTotalDistance) ||
            (maxDistance == bestMaxDistance &&
             totalDistance == bestTotalDistance &&
             (bestGroup == null ||
              candidate.id.compareTo(bestGroup.id) < 0))) {

            bestGroup = candidate;
            bestMaxDistance = maxDistance;
            bestTotalDistance = totalDistance;
        }
    }

    return bestGroup;
}

private Map<Group, Integer> getAncestorDistances(
        Set<Group> startingGroups) {

    Map<Group, Integer> distances = new HashMap<>();
    Queue<Group> queue = new LinkedList<>();

    for (Group group : startingGroups) {
        if (distances.putIfAbsent(group, 0) == null) {
            queue.offer(group);
        }
    }

    while (!queue.isEmpty()) {

        Group current = queue.poll();
        int nextDistance = distances.get(current) + 1;

        for (Group parent : current.parents) {

            if (!distances.containsKey(parent)) {
                distances.put(parent, nextDistance);
                queue.offer(parent);
            }
        }
    }

    return distances;
}

```

**Complexity**

Let V be the number of groups, E the number of hierarchy relationships, and K the number of employees.

- BFS for one employee: O(V + E).
- BFS for K employees: O(K × (V + E)).
- Finding the common ancestors: O(K × V).
- Space: O(K × V).

This is a correctness-first solution. For a large, frequently queried organization, we can optimize it using cached ancestor information and graph indexing.

## **4. Part C: Concurrent hierarchy updates and reads**

Major change: Concurrency and consistency

Now the organization supports four structural update operations, and these operations can execute in separate threads while `getCommonGroupForEmployees()` is running.

The requirement is:

**Every read must see a consistent, fully committed hierarchy, including all updates completed before that read starts.**

There is an important distinction here: a read cannot reflect an update that happens halfway through its execution without restarting or changing its snapshot. The appropriate guarantee is linearizability.

### **4.1 What can go wrong with a normal implementation?**

Imagine this sequence:

```
Thread 1 (Reader)          Thread 2 (Writer)

Start reading
Read Group A
                           Move Group C under B
Read Group B
Read Group C
Finish reading
```

The reader could observe:

- Group A from the old hierarchy.
- Group B from the new hierarchy.
- Group C from a partially updated hierarchy.

This produces an inconsistent result.

### **4.2 Possible approaches**

| Approach            | Reads              | Writes            | Limitation                            |
| ------------------- | ------------------ | ----------------- | ------------------------------------- |
| synchronized        | Blocked            | Blocked           | Poor concurrency                      |
| ReadWriteLock       | Concurrent readers | Exclusive writers | Readers can block during writes       |
| StampedLock         | Optimistic reads   | Exclusive writes  | Requires retry and careful validation |
| Immutable snapshots | Lock-free          | Copy and publish  | Higher write cost                     |

### **4.3 My recommendation: Immutable snapshots + AtomicReference**

For an organizational hierarchy, reads are likely to be significantly more frequent than structural updates.

I would use:

**Copy-on-write + immutable graph snapshots + AtomicReference.**

Architecture:

**AtomicReference**

# **Current Snapshot V10**

Single published version of the hierarchy

**Reader threads**

Get snapshot V10 and traverse without locks.

**Writer threads**

Create a new graph, validate and publish V11.

**Atomic snapshot replacement**

V10 → V11

### **4.4 How it works**

**Reader flow**

1. Fetch the current snapshot using `AtomicReference.get()`.
2. Execute the complete common-group algorithm against that snapshot.
3. Never mutate the snapshot.
4. Return the result.

**Writer flow**

1. Read the current snapshot.
2. Create a new version of the hierarchy.
3. Apply the requested structural update.
4. Validate graph invariants:
   - No cycles.
   - Valid parent-child relationships.
   - Valid employee-group memberships.
5. Atomically publish the new version.
6. The new version becomes visible to subsequent readers.

### **4.5 Java implementation sketch**

```

class HierarchySnapshot {
    final long version;

    final Map<String, Group> groups;
    final Map<String, Employee> employees;

    HierarchySnapshot(
            long version,
            Map<String, Group> groups,
            Map<String, Employee> employees) {

        this.version = version;
        this.groups = Map.copyOf(groups);
        this.employees = Map.copyOf(employees);
    }
}

class OrganizationService {

    private final AtomicReference<HierarchySnapshot>
            currentSnapshot;

    public OrganizationService(HierarchySnapshot initial) {
        this.currentSnapshot =
                new AtomicReference<>(initial);
    }

    public Group getCommonGroupForEmployees(
            List<String> employeeIds) {

        // Read one consistent version.
        HierarchySnapshot snapshot = currentSnapshot.get();

        return findCommonGroup(snapshot, employeeIds);
    }

    public void updateHierarchy(Update update) {

        while (true) {

            HierarchySnapshot oldSnapshot =
                    currentSnapshot.get();

            // Build a completely separate immutable snapshot.
            HierarchySnapshot newSnapshot =
                    applyUpdate(oldSnapshot, update);

            validate(newSnapshot);

            // Publish only if no other writer changed the version.
            if (currentSnapshot.compareAndSet(
                    oldSnapshot, newSnapshot)) {
                return;
            }

            // Another writer succeeded.
            // Reapply this update on the latest snapshot.
        }
    }

    private Group findCommonGroup(
            HierarchySnapshot snapshot,
            List<String> employeeIds) {
        // Part B algorithm using snapshot only.
        return null;
    }

    private HierarchySnapshot applyUpdate(
            HierarchySnapshot snapshot, Update update) {
        // Copy, apply update and return new snapshot.
        return null;
    }

    private void validate(HierarchySnapshot snapshot) {
        // Validate DAG and relationship invariants.
    }
}

```

*This is an architectural sketch; the graph objects and their nested collections must also be immutable. `Map.copyOf()` alone does not make nested objects immutable.*

### **4.6 Why this design?**

- **Read performance:** Readers do not acquire locks.
- **Consistency:** A reader operates on one complete hierarchy version.
- **Atomicity:** A writer publishes the entire update at once.
- **Concurrency:** Multiple readers can execute simultaneously.
- **Versioning:** Every snapshot has a version number for debugging and observability.

One important detail: if multiple writers update the hierarchy concurrently, the CAS loop ensures that a losing writer reapplies its update to the latest snapshot rather than overwriting another writer's changes.

If updates are expensive or numerous, we could serialize writers using a single writer lock or use persistent data structures to reduce copying.

### **4.7 What does "latest state" mean exactly?**

This is an important interview discussion.

| Scenario                             | Expected behavior                                   |
| ------------------------------------ | --------------------------------------------------- |
| Update completed before read starts  | Read must see the update                            |
| Update overlaps with read            | Read sees either the old or new complete snapshot   |
| Two updates overlap                  | Both must be preserved, with a defined commit order |
| Read starts after update publication | Read must see the new version                       |

If the interviewer insists that a read must reflect every update occurring even while that read is executing, explain that this is not a practical atomic-read guarantee. You would need to restart the read or define a stronger coordination protocol.

## **5. Part D: Organization has only one level of groups**

Simplification: No hierarchy traversal required

Now the problem changes significantly:

- There are no parent-child relationships.
- There are no subgroups.
- Each group has a set of employees.
- We need to find a group common to all target employees.

Example:

```
Organization
    |
    |--- Engineering
    |       Alice, Bob, Charlie
    |
    |--- Marketing
    |       Bob, David
    |
    |--- Finance
            Alice, David
```

Suppose the target employees are Alice and Bob.

- Alice belongs to Engineering and Finance.
- Bob belongs to Engineering and Marketing.
- Common group = Engineering.

### **5.1 Data model**

We can use an inverted index:

`Employee ID → Set of Group IDs`

```
Map<String, Set<String>> employeeGroups;
```

### **5.2 Algorithm: Set intersection**

1. Fetch the groups of the first employee.
2. Fetch the groups of every subsequent employee.
3. Perform set intersection.
4. Return one group from the intersection.

If multiple groups are common, choose one deterministically (for example, lexicographically smallest ID), because there is no hierarchy-based concept of closeness anymore.

### **Java implementation**

```

public String getCommonGroupForEmployees(
        List<String> employeeIds,
        Map<String, Set<String>> employeeGroups) {

    if (employeeIds == null || employeeIds.isEmpty()) {
        return null;
    }

    Set<String> commonGroups =
            new HashSet<>(
                employeeGroups.getOrDefault(
                    employeeIds.get(0),
                    Collections.emptySet()
                )
            );

    for (int i = 1; i < employeeIds.size(); i++) {

        Set<String> groups =
                employeeGroups.getOrDefault(
                    employeeIds.get(i),
                    Collections.emptySet()
                );

        commonGroups.retainAll(groups);

        if (commonGroups.isEmpty()) {
            return null;
        }
    }

    return commonGroups.stream()
            .min(String::compareTo)
            .orElse(null);
}

```

### **Complexity**

Let:

- K = number of target employees.
- G = average number of groups per employee.

| Metric            | Complexity       |
| ----------------- | ---------------- |
| Fetch memberships | O(1) average     |
| Set intersection  | O(K × G) average |
| Auxiliary space   | O(G)             |

If every employee belongs to exactly one group, the solution becomes even simpler: check whether all employees have the same group ID.

## **6. Final interview cheat sheet**

This is the most important part to remember. The interviewer is testing how you evolve your solution as requirements change.

| Part | Requirement                                    | Data structure      | Algorithm / Design                 |
| ---- | ---------------------------------------------- | ------------------- | ---------------------------------- |
| A    | One parent per group, one group per employee   | Tree                | LCA                                |
| B    | Multiple parents, employees in multiple groups | DAG                 | BFS + common ancestor intersection |
| C    | Concurrent reads and structural updates        | Immutable snapshots | AtomicReference + CAS              |
| D    | Single-level groups                            | HashMap + HashSet   | Set intersection                   |

## **7. How I would explain the evolution in an interview**

1. **Start with a tree**

   Each employee belongs to one group. Use parent pointers and LCA to find the lowest common ancestor.
2. **Move to a DAG**

   Shared groups introduce multiple parents. Replace single-parent pointers with adjacency lists and use BFS to calculate ancestor distances. Define a deterministic scoring strategy to select one common group.
3. **Introduce concurrency**

   Separate readers from writers using immutable hierarchy snapshots. Readers get lock-free consistent reads, while writers atomically publish new versions using CAS.
4. **Simplify for flat organizations**

   Remove hierarchy traversal entirely. Maintain employee-to-group membership indexes and calculate set intersections.

## **8. Important follow-up questions to prepare**

These are likely areas where an interviewer will probe your design:

1. **Part A:** Can we optimize LCA using binary lifting if we have millions of queries?
2. **Part B:** What if there are cycles in the graph? How do we detect and prevent them?
3. **Part B:** What if multiple common ancestors have exactly the same distance?
4. **Part C:** How do we prevent lost updates when two writers update simultaneously?
5. **Part C:** What happens if building a new snapshot takes several seconds?
6. **Part C:** How do we handle millions of groups without copying the entire graph for every update?
7. **Part D:** What if one employee belongs to thousands of groups?

**My recommendation for interview preparation:** Focus particularly on Parts B and C. Part A tests your understanding of LCA, but Parts B and C test your ability to reason about graph algorithms, ambiguous requirements, consistency guarantees, and concurrent system design.
