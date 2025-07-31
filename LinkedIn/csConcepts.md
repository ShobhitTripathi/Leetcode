### 📘 Operating System Concepts

#### 1. Paging

* Paging is a memory management technique where the **logical address space** of a process is divided into **fixed-size pages**, and the **physical memory** is divided into **fixed-size frames**.
* The **Page Table** maintains a mapping between logical pages and physical frames.
* **Advantages**:

  * Eliminates **external fragmentation**.
  * Efficient use of memory.
* **Disadvantages**:

  * Can cause **internal fragmentation**.
  * Needs additional memory for the page table.

#### 2. Thrashing

* Occurs when the system spends more time swapping pages in and out of memory than executing actual processes.
* Caused due to **over-commitment of memory**, i.e., more processes are loaded than the memory can handle.
* **Symptoms**: High page-fault rate, low CPU utilization.
* **Solutions**:

  * Reduce the degree of multiprogramming.
  * Use the **Working Set Model**.

#### 3. Segmentation

* Memory is divided into **variable-sized segments**, each corresponding to logical divisions like code, stack, data, etc.
* Each segment has a **base address** and a **limit**.
* Avoids internal fragmentation but can suffer from **external fragmentation**.

#### 4. Page Fault

* A **page fault** occurs when a process tries to access a page that is not currently in physical memory.
* The OS handles it by:

  1. Suspending the process.
  2. Loading the required page from the disk into RAM.
  3. Updating the page table.
  4. Resuming the process.

---

### ⚙️ Threads vs Processes

| Feature        | Process                         | Thread                           |
| -------------- | ------------------------------- | -------------------------------- |
| Definition     | Independent execution unit      | Lightweight subprocess           |
| Memory         | Has its own memory              | Shares memory with other threads |
| Overhead       | High                            | Low                              |
| Communication  | IPC (slow, complex)             | Shared memory (fast)             |
| Failure        | One crash doesn't affect others | May affect entire process        |
| Context Switch | Expensive                       | Cheaper                          |

---

### 🔐 Mutex vs Semaphore

| Feature   | Mutex                           | Semaphore                      |
| --------- | ------------------------------- | ------------------------------ |
| Value     | Binary (0 or 1)                 | Integer                        |
| Ownership | Thread-specific                 | No ownership                   |
| Purpose   | Mutual exclusion                | Signaling, resource counting   |
| Use-case  | Single shared resource          | Multiple instances of resource |
| Blocking  | Can be blocking or non-blocking | Typically blocking             |

---

### ☕ Core Java Concepts

#### 1. final, finally, finalize

| Keyword      | Description                                                                                                   |
| ------------ | ------------------------------------------------------------------------------------------------------------- |
| `final`      | Variable: cannot be changed after assignment.<br>Method: cannot be overridden.<br>Class: cannot be inherited. |
| `finally`    | Block that executes regardless of exception being thrown or caught.                                           |
| `finalize()` | Called by the Garbage Collector before object is removed from memory. *(Deprecated)*                          |

#### 2. Garbage Collection (GC)

* Automatic memory management mechanism.
* Frees up memory occupied by objects no longer in use.
* **Phases**:

  * Mark: Marks all reachable objects.
  * Sweep: Removes unmarked objects.
* **Generations**:

  * **Young Generation**: Minor GC.
  * **Old Generation**: Major GC.
  * **Metaspace** (Java 8+): Stores class metadata.
* **GC Algorithms**: Serial GC, Parallel GC, CMS, G1.

---

### 🧱 OOP Principles in Java

1. **Encapsulation**

   * Wrapping data (variables) and code (methods) into a single unit.
   * Achieved using classes, private access modifiers, and getter/setters.

2. **Abstraction**

   * Hides complexity and shows only essential features.
   * Achieved using interfaces and abstract classes.

3. **Inheritance**

   * Mechanism to inherit properties and methods from parent class.
   * Promotes code reuse.

4. **Polymorphism**

   * Ability to perform one action in different ways.

     * **Compile-time**: Method Overloading.
     * **Runtime**: Method Overriding.

---

### 📚 Java Collections: ArrayList vs LinkedList

| Feature       | ArrayList                   | LinkedList                  |
| ------------- | --------------------------- | --------------------------- |
| Internal DS   | Dynamic array               | Doubly Linked List          |
| Access Time   | Fast (O(1) for index)       | Slow (O(n))                 |
| Insert/Delete | Slow at middle/start (O(n)) | Fast at middle/start (O(1)) |
| Memory Usage  | Less (compact)              | More (due to node pointers) |
| Thread-safe   | No                          | No                          |

---

### 🌐 Networking: TCP vs UDP

| Feature       | TCP                             | UDP                        |
| ------------- | ------------------------------- | -------------------------- |
| Type          | Connection-oriented             | Connectionless             |
| Reliability   | Reliable, ordered delivery      | Unreliable, no ordering    |
| Speed         | Slower (handshake overhead)     | Faster                     |
| Use-cases     | HTTP, FTP, Email, File Transfer | DNS, Video Streaming, VoIP |
| Error Control | Yes                             | No                         |

---

### 🗃️ HashMap Implementation in Java

#### Structure:

* HashMap uses an **array of buckets** internally.
* Each bucket holds a **LinkedList** or **Balanced Tree** (if size > threshold).

#### Working:

1. Key's `hashCode()` is computed.
2. Index is calculated using `hash % array length`.
3. If index is empty → insert node.
4. If collision occurs → store as a linked list or tree (post Java 8).

#### Key Points:

* Allows one `null` key, multiple `null` values.
* Not thread-safe. Use `ConcurrentHashMap` for concurrent access.
* Rehashing occurs when size exceeds `capacity * load factor` (default load factor = 0.75).
* Java 8+: Converts to **TreeNode** if bucket size > 8.

---

Let me know if you'd like this in `.pdf` or `.md` format!
