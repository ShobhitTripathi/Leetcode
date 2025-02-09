# Scheduler: Low-Level Design (LLD)

This document describes the Low-Level Design (LLD) for a Scheduler system in Java. The system schedules HTTP requests to execute at specific times, with features like handling timezones, GUI/backend synchronization, and system crash recovery.

---

## **Features**
- Schedule HTTP requests to run at a specific time.
- Handle timezones using `java.time` API.
- Persist tasks in a database for crash recovery.
- GUI and backend synchronization through REST APIs.
- Retry failed tasks with exponential backoff.
- Support for canceling and retrieving scheduled tasks.

---

## **Components**

### 1. **Scheduler Interface**
Defines the API for scheduling, canceling, and retrieving tasks.

### 2. **Task Management**
Each task includes the following details:
- URL, HTTP method, headers, and payload.
- Execution time and timezone.

### 3. **Persistence**
A database is used to store scheduled tasks for recovery during a system crash.

### 4. **Worker Service**
Executes tasks due for execution.

### 5. **Timezones**
Handles timezones using `java.time.ZoneId`.

### 6. **Crash Recovery**
Reloads scheduled tasks from the database at system startup.

### 7. **Backend/GUI Sync**
Provides REST APIs to interact with the scheduler and maintain consistency between GUI and backend.

---

## **Class Diagram**
```
SchedulerService
├── scheduleTask(Task task): UUID
├── cancelTask(UUID taskId): boolean
├── getTask(UUID taskId): Task
└── getAllTasks(): List<Task>

TaskExecutorService
├── start()
├── stop()
├── executeTask(Task task): void
└── retryFailedTask(Task task): void

Task (Entity)
├── UUID id
├── String url
├── HttpMethod method
├── Map<String, String> headers
├── String payload
├── LocalDateTime scheduledTime
├── ZoneId timezone
├── TaskStatus status
└── int retryCount

TaskRepository
├── save(Task task): void
├── delete(UUID taskId): void
├── findById(UUID taskId): Task
└── findAllDueTasks(LocalDateTime now): List<Task>
```

---

## **Code Implementation**

### **1. Task Entity**
```java
public class Task {
    private UUID id;
    private String url;
    private HttpMethod method;
    private Map<String, String> headers;
    private String payload;
    private LocalDateTime scheduledTime;
    private ZoneId timezone;
    private TaskStatus status;
    private int retryCount;

    // Constructors, Getters, Setters
}
```

### **2. TaskStatus Enum**
```java
public enum TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}
```

### **3. SchedulerService Interface**
```java
public interface SchedulerService {
    UUID scheduleTask(Task task);
    boolean cancelTask(UUID taskId);
    Task getTask(UUID taskId);
    List<Task> getAllTasks();
}
```

### **4. SchedulerService Implementation**
```java
public class SchedulerServiceImpl implements SchedulerService {
    private final TaskRepository taskRepository;
    private final TaskExecutorService taskExecutor;

    public SchedulerServiceImpl(TaskRepository taskRepository, TaskExecutorService taskExecutor) {
        this.taskRepository = taskRepository;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public UUID scheduleTask(Task task) {
        task.setId(UUID.randomUUID());
        task.setStatus(TaskStatus.PENDING);
        taskRepository.save(task);
        return task.getId();
    }

    @Override
    public boolean cancelTask(UUID taskId) {
        Task task = taskRepository.findById(taskId);
        if (task != null && task.getStatus() == TaskStatus.PENDING) {
            taskRepository.delete(taskId);
            return true;
        }
        return false;
    }

    @Override
    public Task getTask(UUID taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
```

### **5. TaskExecutorService**
```java
public class TaskExecutorService {
    private final TaskRepository taskRepository;

    public TaskExecutorService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void start() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            List<Task> dueTasks = taskRepository.findAllDueTasks(LocalDateTime.now());
            for (Task task : dueTasks) {
                executeTask(task);
            }
        }, 0, 1, TimeUnit.SECONDS); // Poll every second
    }

    public void executeTask(Task task) {
        try {
            task.setStatus(TaskStatus.IN_PROGRESS);
            // Execute HTTP request using HttpClient
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(task.getUrl()))
                .method(task.getMethod().toString(), HttpRequest.BodyPublishers.ofString(task.getPayload()))
                .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
            task.setStatus(TaskStatus.COMPLETED);
        } catch (Exception e) {
            task.setStatus(TaskStatus.FAILED);
            task.setRetryCount(task.getRetryCount() + 1);
        } finally {
            taskRepository.save(task);
        }
    }
}
```

### **6. TaskRepository**
```java
public interface TaskRepository {
    void save(Task task);
    void delete(UUID taskId);
    Task findById(UUID taskId);
    List<Task> findAll();
    List<Task> findAllDueTasks(LocalDateTime now);
}
```

---

## **Crash Recovery**
At application startup, reload pending tasks from the database:
```java
public void recoverTasks() {
    List<Task> pendingTasks = taskRepository.findAll();
    for (Task task : pendingTasks) {
        if (task.getStatus() == TaskStatus.PENDING) {
            taskExecutor.executeTask(task);
        }
    }
}
```

---

## **Sync with GUI**
Expose REST APIs for GUI/backend synchronization:
- `POST /tasks` - Schedule a task.
- `GET /tasks` - Retrieve all tasks.
- `DELETE /tasks/{taskId}` - Cancel a task.
- `GET /tasks/{taskId}` - Fetch task details.

---

## **Considerations**

### **Timezones**
- Convert all `LocalDateTime` to UTC before saving to the database.
- Convert back to the user's timezone when retrieving.

### **Retries**
- Add exponential backoff logic for failed tasks.

### **Scalability**
- Use a message queue (like Kafka) to offload task execution to worker threads.

---

## **Future Improvements**
1. Support for distributed task execution using microservices.
2. Advanced retry policies and notifications for failed tasks.
3. Improved logging and monitoring with tools like ELK or Prometheus.
