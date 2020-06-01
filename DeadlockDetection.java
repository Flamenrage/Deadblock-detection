package laba;

public class DeadlockDetection {
    int n = 3; 
    int m = 4;  
    int[] available;
    int[][] allocation = new int[n][m];
    int[][] request = new int[n][m]; 
    int[] safeSequence; 
    boolean isSafe = false;
    boolean[] deadlocked;

    public void initialization() {
        System.out.println("Number of processes: " + n);

        System.out.println("Number of resource types: " + m);
        allocation = new int[][] { {0, 0, 1, 0}, {2, 0, 0, 1}, {0, 1, 2, 0}};

        System.out.println("The allocation matrix: ");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(" " + allocation[i][j]);
            }
            System.out.println("");
        }
        request = new int[][] {{2, 0, 0, 1},{1, 0, 1, 0,},{2, 1, 0, 0}};
        System.out.println("The request matrix: ");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(" " + request[i][j]);
            }
            System.out.println("");
        }
        available = new int[] {4, 2, 3, 1};
        System.out.println("The number of instances of each resource: ");
        for (int i = 0; i < m; i++) {
            System.out.print(" " + available[i]);
        }
        System.out.println("");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                available[i] -= allocation[j][i];
            }
        }
        deadlocked = new boolean[n];
        for (int i = 0; i < n; i++){
            deadlocked[i]=false;
        }
    }

    public void detect(boolean activate) {
        safeSequence = new int[n];
        int counter = 0;
        boolean finish[] = new boolean[n];
        for (int i = 0; i < n; i++) {
            finish[i] = false;
        }
        int work[] = new int[m];
        for (int i = 0; i < m; i++) {
            work[i] = available[i];
        }
        while (counter < n) {
            boolean check = false;
            for (int i = 0; i < n; i++) {
                if (!finish[i]) {
                    int j;
                    for (j = 0; j < m; j++) {
                        if (request[i][j] > work[j]) {
                            break;
                        }
                    }
                    if (j == m) {
                        safeSequence[counter] = i;
                        counter++;
                        finish[i] = true;
                        check = true;
                        for (j = 0; j < m; j++) {
                            work[j] = work[j] + allocation[i][j];
                        }
                    }
                }
            }
            if (!check)
                break;
        }
        for (int i = 0; i < n; i++) {
            if (finish[i] == false) {
                deadlocked[i]=true;
                System.out.println("Process" + i + " is deadlocked");
            }
        } 
        if (counter < n) {
            System.out.println("System is not safe.");
            }
        else {
            isSafe=true;
            System.out.println("Safe sequence is:");
            for (int i = 0; i < n; i++) {
                if (!deadlocked[i]) {
                    System.out.print("P" + safeSequence[i]);
                    if (i != n - 1)
                        System.out.print(" -> ");
                }
            }
        }
        if (!isSafe  && (activate == true)){
            System.out.println("Aborting all deadlocked processes");
            for (int i = 0; i < n; i++) {
                if (finish[i] == false) {
                    for (int j = 0; j < m; j++) {
                        available[j] += allocation[i][j];
                    }
                }
            }
            detect(activate);
        }
    }
}