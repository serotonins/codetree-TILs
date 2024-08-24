import java.io.*;
import java.util.*;

public class Main {
    /*
    뿌리기: 가장 작은 위치 모두에 +1
    말기: 뭐가 얹어진 곳까지 똑 떼서 오른쪽으로 엎어(그 높이가 남은 열 길이보다 길면 중단)
    누르기: 인접한 밀가루끼리 Math.abs(a-b)/5를 큰 쪽에서 빼고 작은 쪽에 더하기
        열이 작고 행이 큰(낮은 층)부터 왼쪽에
    접기: 반 접어서 오른쪽으로 엎기 * 2 + 누르기
    */
    static int n, k, layer, inf = Integer.MAX_VALUE;
    static boolean answer, next;
    static ArrayDeque<Integer>[] dough;
    static int[][] doughOdd, doughEven, dr = {{0,1,0,-1}, {1,0,-1,0}};

    public static void spread() {
        // System.out.println("1. 뿌리기");
        int maxi = 0;
        int mini = inf;
        ArrayDeque<Integer> que = new ArrayDeque<>();
        int idx = 0;
        for (int i : doughEven[0]) {
            maxi = Math.max(i, maxi);
            if (mini > i) {
                mini = Math.min(i, mini);
                que.clear();
                que.add(idx);
            } else if (mini == i) {
                que.add(idx);
            }
            idx++;
        }

        if (maxi - mini <= k) {
            answer = true;
            return;
        } else if (maxi - mini - 1 <= k) {
            answer = true;
            next = true;
            return;
        }

        for (int i : que) {
            doughEven[0][i]++;
        }

        // System.out.println(Arrays.toString(doughEven[0]));
    }

    public static void roll() {
        // System.out.println("2. 말기");
        int cnt = 0;
        while (true) {
            if (cnt % 2 == 0) {
                int layer = 0;
                for (int i = 1; i < n; i++) {
                    if (doughEven[i][0] == 0) {
                        layer = i;
                        break;
                    }
                }
                int width = 0;
                for (int i = 0; i < n; i++) {
                    if (doughEven[1][i] == 0) {
                        width = i;
                        break;
                    }
                }

                int base = 0;
                for (int i = 0; i < n; i++) {
                    if (doughEven[0][i] == 0) {
                        base = i;
                        break;
                    } else if (i == n-1) {
                        base = n;
                    }
                }

                if (base - width < layer) {
                    break;
                }

                doughOdd = new int[n][n];

                if (layer == 1) {
                    doughOdd[1][0] = doughEven[0][0];
                    for (int i = 1; i < n; i++) {
                        doughOdd[0][i-1] = doughEven[0][i];
                    }
                    cnt++;
                    continue;
                }

                for (int w = width-1; w >= 0; w--) {
                    for (int l = 0; l < layer; l++) {
                        doughOdd[width-w][l] = doughEven[l][w];
                    }
                }
                for (int i = width; i < n; i++) {
                    doughOdd[0][i-width] = doughEven[0][i];
                    if (doughEven[0][i] == 0) {
                        break;
                    }
                }

            } else {
                int layer = 0;
                for (int i = 1; i < n; i++) {
                    if (doughOdd[i][0] == 0) {
                        layer = i;
                        break;
                    }
                }
                int width = 0;
                for (int i = 0; i < n; i++) {
                    if (doughOdd[1][i] == 0) {
                        width = i;
                        break;
                    }
                }

                int base = 0;
                for (int i = 0; i < n; i++) {
                    if (doughOdd[0][i] == 0) {
                        base = i;
                        break;
                    } else if (i == n-1) {
                        base = n;
                    }
                }

                if (base - width < layer) {
                    break;
                }

                doughEven = new int[n][n];

                for (int w = width-1; w >= 0; w--) {
                    for (int l = 0; l < layer; l++) {
                        doughEven[width-w][l] = doughOdd[l][w];
                    }
                }
                for (int i = width; i < n; i++) {
                    doughEven[0][i-width] = doughOdd[0][i];
                    if (doughOdd[0][i] == 0) {
                        break;
                    }
                }

            }
            cnt++;
        }

        if (cnt % 2 != 0) {
            doughEven = new int[n][n];
            for (int i = 0; i < n; i++) {
                if (doughOdd[i][0] == 0) {
                    break;
                }
                for (int j = 0; j < n; j++) {
                    if (doughOdd[i][j] == 0) {
                        break;
                    }
                    doughEven[i][j] = doughOdd[i][j];
                }
            }
        }

        // for (int i = n-1; i >= 0; i--) {
        //     System.out.println(Arrays.toString(doughEven[i]));
        // }
    }
    
    public static boolean isOut(int y, int x) {
        return y < 0 || y >= n || x < 0 || x >= n;
    }

    public static void press() {
        // System.out.println("3. 누르기");
        doughOdd = new int[n][n];

        for (int i = 0; i < n; i++) {
            if (doughEven[i][0] == 0) break;
            for (int j = 0; j < n; j++) {
                if (doughEven[i][j] == 0) {
                    break;
                }
                for (int d = 0; d < 4; d++) {
                    int y = i + dr[0][d];
                    int x = j + dr[1][d];
                    if (isOut(y,x) || doughEven[y][x] == 0 || doughEven[i][j] - 5 < doughEven[y][x]) {
                        continue;
                    }
                    int cha = (doughEven[i][j] - doughEven[y][x]) / 5;
                    doughOdd[i][j] -= cha;
                    doughOdd[y][x] += cha;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (doughEven[i][0] == 0) break;
            for (int j = 0; j < n; j++) {
                if (doughEven[i][j] == 0) {
                    break;
                }
                doughOdd[i][j] += doughEven[i][j];
            }
        }

        doughEven = new int[n][n];
        int idx = 0;
        for (int j = 0; j < n; j++) {
            if (doughOdd[0][j] == 0) break;
            for (int i = 0; i < n; i++) {
                if (doughOdd[i][j] == 0) {
                    break;
                }
                doughEven[0][idx++] = doughOdd[i][j];
            }
        }

        // System.out.println(Arrays.toString(doughEven[0]));
    }

    public static void fold() {
        // System.out.println("4. 접기");
        doughOdd = new int[n][n];
        for (int i = 0; i < n/2; i++) {
            doughOdd[1][n/2-i-1] = doughEven[0][i];
            doughOdd[0][i] = doughEven[0][n/2+i];
        }
        
        // for (int i = n-1; i >= 0; i--) {
        //     System.out.println(Arrays.toString(doughOdd[i]));
        // }

        doughEven = new int[n][n];
        for (int i = 0; i < n/4; i++) {
            doughEven[2][n/4-i-1] = doughOdd[1][i];
            doughEven[1][i] = doughOdd[1][n/4+i];
            doughEven[3][n/4-i-1] = doughOdd[0][i];
            doughEven[0][i] = doughOdd[0][n/4+i];
        }

        // System.out.println("2차");
        // for (int i = n-1; i >= 0; i--) {
        //     System.out.println(Arrays.toString(doughEven[i]));
        // }

        press();
    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        
        dough = new ArrayDeque[n];
        doughOdd = new int[n][n];
        doughEven = new int[n][n];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            doughEven[0][i] = Integer.parseInt(st.nextToken());
        }

        layer = 1;

        int answerCount = 0;
        while (!answer) {
            spread();
            if (answer) {
                break;
            }
            roll();
            press();
            fold();
            answerCount++;
        }

        System.out.println(answerCount + (next ? 1 : 0));

    }
}