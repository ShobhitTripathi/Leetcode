package Amazon;

public class NumberOfIslands {

    private static int countNumberOfIslands(char[][] grid) {
        if (grid == null)
            return 0;
        int count = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    clearRestOfLand(grid, i, j);
                }
            }
        }
        return count;
    }

    private static void clearRestOfLand(char[][] grid, int i, int j) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[i].length || grid[i][j] == '0')
            return;

        grid[i][j] = '0';
        clearRestOfLand(grid, i + 1, j);
        printArray(grid);
        clearRestOfLand(grid, i - 1, j);
        printArray(grid);
        clearRestOfLand(grid, i, j + 1);
        printArray(grid);
        clearRestOfLand(grid, i, j - 1);
        printArray(grid);
        return;
    }



    public static void main(String[] args) {
        char[][] arr = {{'1','1','1','1','0'},
                        {'1','1','0','1','0'},
                        {'1','1','0','0','0'},
                        {'0','0','0','0','0'}};
        System.out.println("Input array: ");
        printArray(arr);
        int ans = countNumberOfIslands(arr);
        System.out.println("Number of islands : " + ans);
    }

    private static void printArray(char[][] arr) {
        for (int i = 0;i < arr.length;i++) {
            for (int j = 0;j < arr[i].length;j++) {
                System.out.print(" " + arr[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}


