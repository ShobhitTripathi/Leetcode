# Snake Game Low-Level Design (LLD)

This document describes the low-level design for a **Snake Game** in Java, where the snake grows every 5 steps.

---

## **Key Classes**

1. **GameBoard**: Represents the game board and its dimensions.
2. **Snake**: Represents the snake's current state (position, direction, and length).
3. **GameEngine**: Manages the game logic, including movement, collision detection, and game state updates.
4. **Food**: Represents food that can randomly appear on the board.
5. **Direction**: Enum for snake movement (UP, DOWN, LEFT, RIGHT).

---

## **Class Diagram**

```plaintext
+----------------+
|    GameBoard   |
+----------------+
| width          |
| height         |
| obstacles      |
+----------------+
| isPositionFree |
| generateFood   |
+----------------+

+----------------+
|     Snake      |
+----------------+
| body (Deque)   |
| direction      |
| stepsCount     |
+----------------+
| move           |
| grow           |
| changeDirection|
+----------------+

+----------------+
|   GameEngine   |
+----------------+
| gameBoard      |
| snake          |
| food           |
| isGameOver     |
+----------------+
| startGame      |
| moveSnake      |
| checkCollision |
+----------------+

+----------------+
|     Food       |
+----------------+
| position       |
+----------------+
| respawn        |
+----------------+

+----------------+
|   Direction    |
+----------------+
| UP, DOWN, LEFT, RIGHT |
+----------------+
```

---

## **Code Implementation**

### **Direction Enum**

```java
enum Direction {
    UP, DOWN, LEFT, RIGHT;
}
```

### **GameBoard Class**

```java
class GameBoard {
    private final int width;
    private final int height;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean isPositionFree(int x, int y, Deque<int[]> snakeBody) {
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        for (int[] cell : snakeBody) {
            if (cell[0] == x && cell[1] == y) return false;
        }
        return true;
    }

    public int[] generateFood(Deque<int[]> snakeBody) {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(width);
            y = random.nextInt(height);
        } while (!isPositionFree(x, y, snakeBody));
        return new int[]{x, y};
    }
}
```

### **Snake Class**

```java
class Snake {
    private Deque<int[]> body;  // Each int[] represents [x, y].
    private Direction direction;
    private int stepsCount;

    public Snake(int startX, int startY) {
        this.body = new LinkedList<>();
        this.body.addFirst(new int[]{startX, startY});
        this.direction = Direction.RIGHT;
        this.stepsCount = 0;
    }

    public Deque<int[]> getBody() {
        return body;
    }

    public void changeDirection(Direction newDirection) {
        if ((direction == Direction.UP && newDirection != Direction.DOWN) ||
            (direction == Direction.DOWN && newDirection != Direction.UP) ||
            (direction == Direction.LEFT && newDirection != Direction.RIGHT) ||
            (direction == Direction.RIGHT && newDirection != Direction.LEFT)) {
            this.direction = newDirection;
        }
    }

    public void move(boolean shouldGrow) {
        int[] head = body.peekFirst();
        int newX = head[0];
        int newY = head[1];

        switch (direction) {
            case UP -> newY--;
            case DOWN -> newY++;
            case LEFT -> newX--;
            case RIGHT -> newX++;
        }

        body.addFirst(new int[]{newX, newY});
        if (!shouldGrow) {
            body.removeLast(); // Remove the tail if not growing.
        }
        stepsCount++;
    }

    public int getStepsCount() {
        return stepsCount;
    }
}
```

### **Food Class**

```java
class Food {
    private int[] position;

    public Food(int x, int y) {
        this.position = new int[]{x, y};
    }

    public int[] getPosition() {
        return position;
    }

    public void respawn(int[] newPosition) {
        this.position = newPosition;
    }
}
```

### **GameEngine Class**

```java
class GameEngine {
    private final GameBoard gameBoard;
    private final Snake snake;
    private Food food;
    private boolean isGameOver;

    public GameEngine(GameBoard gameBoard, Snake snake) {
        this.gameBoard = gameBoard;
        this.snake = snake;
        this.food = new Food(-1, -1); // Initial invalid position.
        this.isGameOver = false;
    }

    public void startGame() {
        food.respawn(gameBoard.generateFood(snake.getBody()));
    }

    public void moveSnake() {
        int[] head = snake.getBody().peekFirst();
        int[] foodPosition = food.getPosition();

        boolean shouldGrow = head[0] == foodPosition[0] && head[1] == foodPosition[1];
        snake.move(shouldGrow);

        if (shouldGrow) {
            food.respawn(gameBoard.generateFood(snake.getBody()));
        } else if (snake.getStepsCount() % 5 == 0) {
            snake.move(true); // Grow the snake every 5 steps.
        }

        if (!gameBoard.isPositionFree(head[0], head[1], snake.getBody())) {
            isGameOver = true; // Snake hit a wall or itself.
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void changeDirection(Direction direction) {
        snake.changeDirection(direction);
    }
}
```

---

## **Game Flow**

1. **Initialization**:
   - Create a `GameBoard` with specified dimensions.
   - Initialize the `Snake` with a starting position.
   - Use the `GameEngine` to manage the game state.

2. **Game Loop**:
   - Continuously check user input to change the snake's direction.
   - Call `moveSnake()` to update the snake's position.
   - Check if the game is over after each move.

3. **Growth Logic**:
   - The snake grows when it eats food.
   - The snake also grows automatically every 5 steps.

---

## **Example Usage**

```java
public class Main {
    public static void main(String[] args) {
        GameBoard board = new GameBoard(10, 10);
        Snake snake = new Snake(5, 5);
        GameEngine engine = new GameEngine(board, snake);

        engine.startGame();

        Scanner scanner = new Scanner(System.in);
        while (!engine.isGameOver()) {
            System.out.println("Enter direction (W: UP, S: DOWN, A: LEFT, D: RIGHT): ");
            String input = scanner.nextLine().toUpperCase();
            switch (input) {
                case "W" -> engine.changeDirection(Direction.UP);
                case "S" -> engine.changeDirection(Direction.DOWN);
                case "A" -> engine.changeDirection(Direction.LEFT);
                case "D" -> engine.changeDirection(Direction.RIGHT);
            }
            engine.moveSnake();
        }
        System.out.println("Game Over!");
    }
}
```

---

This design ensures modularity and allows for easy extensions, such as adding obstacles or multiple levels.
