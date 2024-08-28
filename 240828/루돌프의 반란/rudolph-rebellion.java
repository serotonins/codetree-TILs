import java.util.*;
import java.io.*;

public class Main {
    static int n, m, p, c, d;
    static int[] santa, score;
    static int[][] dr = {{1,1,1,0,0,-1,-1,-1},{1,0,-1,1,-1,1,0,-1}};
    static int[][] pan;

    static int turn = 1, deadSanta;
    public static class WV implements Comparable<WV> {
        int y, x;
        public WV(int y, int x) {
            this.y = y;
            this.x = x;
        }
        public int compareTo(WV o) {
            if (o.y == this.y) return o.x - this.x;
            return o.y - this.y;
        }
    }
    static WV rudol;

    public static class FS implements Comparable<FS> {
        int y, x, l;
        public FS(int y, int x, int l) {
            this.y = y;
            this.x = x;
            this.l = l;
        }
        public int compareTo(FS o) {
            if (o.l == this.l) {
                if (o.y == this.y) return o.x - this.x;
                return o.y - this.y;
            }
            return this.l - o.l;
        }
        public String toString() {
            return "[" + y + ", " + x + ", " + l + "거리]";
        }
    }

    public static boolean isOut(int y, int x) {
        return y < 0 || y >= n || x < 0 || x >= n;
    }

    public static int calLen(WV s) {
        return (int) (Math.pow(s.y-rudol.y, 2) + Math.pow(s.x-rudol.x, 2));
    }

    public static void findSanta() {
        // System.out.println("findSanta now rudolph : " + rudol.y + " " + rudol.x);
        PriorityQueue<FS> que = new PriorityQueue<>();
        TreeSet<FS> set = new TreeSet<>();
        que.add(new FS(rudol.y, rudol.x, 0));
        boolean[][] visit = new boolean[n][n];
        visit[rudol.y][rudol.x] = true;
        int len = Integer.MAX_VALUE;

        while (!que.isEmpty()) {
            FS now = que.poll();
            if (len < now.l) {
                break;
            }
            if (pan[now.y][now.x] != 0) {
                set.add(now);
                len = Math.min(len, now.l);
                continue;
            }

            for (int i = 0; i < 8; i++) {
                int y = now.y + dr[0][i];
                int x = now.x + dr[1][i];
                if (isOut(y,x) || visit[y][x]) {
                    continue;
                }
                visit[y][x] = true;
                que.add(new FS(y,x,(int) (Math.pow(y-rudol.y, 2) + Math.pow(x-rudol.x, 2))));
            }
        }

        FS target = set.first();
        int ty = target.y > rudol.y ? 1 : (target.y < rudol.y ? -1 : 0);
        int tx = target.x > rudol.x ? 1 : (target.x < rudol.x ? -1 : 0);
        rudol.y += ty;
        rudol.x += tx;

        // System.out.println("rudolph : " + rudol.y + ", " + rudol.x);

        if (pan[rudol.y][rudol.x] != 0) {
            crush(pan[rudol.y][rudol.x], c, ty, tx);
        }

        
        
    }

    public static void communicate(int sn, int y, int x, int dy, int dx) {
        if (isOut(y+dy, x+dx)) {
            santa[sn] = 1001;
            deadSanta++;
            return;
        }

        if (pan[y+dy][x+dx] != 0) {
            communicate(pan[y+dy][x+dx], y+dy, x+dx, dy, dx);
        }
        pan[y+dy][x+dx] = sn;
    }

    public static void crush(int sn, int cd, int dy, int dx) {
        // System.out.println("crush rudolph "+ rudol.y + " " + rudol.x + " santa " + sn + " " + dy + " " + dx);
        santa[sn] = turn+1;
        int y = rudol.y;
        int x = rudol.x;
        score[sn] += cd;
        pan[y][x] = 0;
        int gy = y + cd * dy;
        int gx = x + cd * dx;
        if (isOut(gy, gx)) {
            santa[sn] = 1001;
            deadSanta++;
            return;
        } else {
            if (pan[gy][gx] != 0) {
                communicate(pan[gy][gx], gy, gx, dy, dx);
            }
            pan[gy][gx] = sn;
        }
        return;
    }

    public static void moveSanta() {
        // System.out.println("moveSanta");
        int[][] sr = {{-1,0,1,0, 0}, {0,1,0,-1, 0}};
        TreeSet<FS> set = new TreeSet<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (pan[i][j] != 0 && santa[pan[i][j]] < turn) {
                    set.add(new FS(i,j,pan[i][j]));
                } 
            }
        }

        for (FS fs : set) {
            int i = fs.y;
            int j = fs.x;
            int sn = fs.l;
            
            int dy = Integer.compare(rudol.y, i);
            int dx = Integer.compare(rudol.x, j);
            int len = calLen(new WV(i, j));
            int temps = 4;

            for (int s = 0; s < 4; s++) {
                if (sr[0][s] != 0 && sr[0][s] == dy && !isOut(i+dy, j)) {
                    if (pan[i+dy][j] == 0) {
                        int l = calLen(new WV(i+dy, j));
                        if (len > l) {
                            len = l;
                            temps = s;
                        }
                    }
                } else if (sr[1][s] != 0 && sr[1][s] == dx && !isOut(i, j+dx)) {
                    if (pan[i][j+dx] == 0) {
                        int l = calLen(new WV(i, j+dx));
                        if (len > l) {
                            len = l;
                            temps = s;
                        }
                        break;
                    }
                }
            }

            dy = sr[0][temps];
            dx = sr[1][temps];
            pan[i][j] = 0;
            pan[i+sr[0][temps]][j+sr[1][temps]] = sn;

            if (rudol.y == i+dy && rudol.x == j+dx) {
                crush(sn, d, dy*(-1), -1 * dx);
            }
        }
    }

    public static void panPrint() {
        for (int i = 0; i < n; i++) {
            System.out.println(Arrays.toString(pan[i]));
        }
    }

    public static void getScore() {
        for (int i = 1; i <= p; i++) {
            if (santa[i] != 1001) score[i]++;
        }
    }
    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        p = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        d = Integer.parseInt(st.nextToken());
        
        pan = new int[n][n];
        santa = new int[p+1];
        score = new int[p+1];

        st = new StringTokenizer(br.readLine());
        rudol = new WV(Integer.parseInt(st.nextToken())-1, Integer.parseInt(st.nextToken())-1);
        for (int i = 0; i < p; i++) {
            st = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(st.nextToken());
            pan[Integer.parseInt(st.nextToken())-1][Integer.parseInt(st.nextToken())-1] = num;
        }

        for (; turn <= m && deadSanta < p; turn++) {
            findSanta();
            moveSanta();
            // panPrint();
            getScore();
            // System.out.println("santa : " + Arrays.toString(santa));
            // System.out.println("score : " + Arrays.toString(score));
            
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= p; i++) {
            sb.append(score[i]);
            sb.append(' ');
        }
        System.out.println(sb.toString());
    }
}