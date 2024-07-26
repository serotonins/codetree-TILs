import java.io.*;
import java.util.*;

public class Main {

    public static class Gift {
        int num, front, back;
        public Gift(int num, int front, int back) {
            this.num = num;
            this.front = front;
            this.back = back;
        }
        public String toString() {
            return "Gift_" + num + " front: " + front + " back: " + back;
        }
    }

    public static class Belt {
        int num, head, tail, cnt;
        public Belt(int num, int head, int tail, int cnt) {
            this.num = num;
            this.head = head;
            this.tail = tail;
            this.cnt = cnt;
        }
        public String toString() {
            return "Belt_" + num + " head: " + head + " tail: " + tail + " cnt: " + cnt;
        }
    }

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static Belt[] belts;
    static Gift[] gifts;

    public static void factoryEstablish(int n, int m) {
        belts = new Belt[n+1];
        for (int i = 0; i <= n; i++) {
            belts[i] = new Belt(i, -1, -1, 0);
        }

        gifts = new Gift[m+1];
        for (int i = 1; i <= m; i++) {
            int beltNum = Integer.parseInt(st.nextToken());

            if (belts[beltNum].cnt == 0) {
                belts[beltNum].head = i;
                gifts[i] = new Gift(i, -1, -1);
            } else {
                int front = belts[beltNum].tail;
                gifts[i] = new Gift(i, front, -1);
                gifts[front].back = i;
            }
            belts[beltNum].tail = i;
            belts[beltNum].cnt++;
        }
    }

    public static void moveAll(int src, int dst) {
        if (belts[src].cnt == 0) {
            System.out.println(belts[dst].cnt);
            return;
        }

        int originSrcHead = belts[src].head;
        int originSrcTail = belts[src].tail;
        int originDstHead = belts[dst].head;

        belts[dst].head = originSrcHead;
        belts[dst].cnt += belts[src].cnt;
        gifts[originSrcTail].back = originDstHead;

        if (belts[dst].cnt != 0) {
            gifts[originDstHead].front = originSrcTail;
        }
        
        belts[src].head = -1;
        belts[src].tail = -1;
        belts[src].cnt = 0;

        System.out.println(belts[dst].cnt);
    }

    public static void changeHead(int src, int dst) {
        int originSrcHead = belts[src].head;
        int originDstHead = belts[dst].head;

        belts[src].head = originDstHead;
        belts[dst].head = originSrcHead;
        
        if (belts[src].cnt == 0 && belts[dst].cnt == 0) {}
        else if (belts[src].cnt == 0) {
            belts[src].tail = originDstHead;
            belts[src].cnt++;

            belts[dst].head = gifts[originDstHead].back;
            belts[dst].cnt--;
            
            if (gifts[originDstHead].back != -1) gifts[gifts[originDstHead].back].front = -1;
            gifts[originDstHead].back = -1;
            
        } else if (belts[dst].cnt == 0) {
            belts[dst].tail = originSrcHead;
            belts[dst].cnt++;

            belts[src].head = gifts[originSrcHead].back;
            belts[src].cnt--;
            
            if (gifts[originSrcHead].back != -1) gifts[gifts[originSrcHead].back].front = -1;
            gifts[originSrcHead].back = -1;

        } else {
            int originSrc2nd = gifts[originSrcHead].back;
            int originDst2nd = gifts[originDstHead].back;
            gifts[originSrcHead].back = originDst2nd;
            gifts[originDstHead].back = originSrc2nd;

            if (gifts[originSrcHead].back != -1) gifts[gifts[originSrcHead].back].front = originDstHead;
            if (gifts[originDstHead].back != -1) gifts[gifts[originDstHead].back].front = originSrcHead;
        }

        if (belts[src].cnt <= 1) belts[src].tail = belts[src].head;
        if (belts[dst].cnt <= 1) belts[dst].tail = belts[dst].head;

        System.out.println(belts[dst].cnt);
    }

    public static void divide(int src, int dst) {
        if (belts[src].cnt <= 1) {
            System.out.println(belts[dst].cnt);
            return;
        }

        int n = belts[src].cnt/2;

        int idx = belts[src].head;
        int originSrcHead = belts[src].head; // 3
        int originDstHead = belts[dst].head; // 2
        
        for (int i = 1; i < n; i++) {
            idx = gifts[idx].back;
        }

        int moveHead = originSrcHead; // 3
        int moveTail = idx; // 3
        if (moveTail == -1) moveTail++; ///////////////////////////////////////////////////
        int newSrcHead = gifts[moveTail].back; // 4
        

        belts[src].head = newSrcHead; // 4
        belts[dst].head = moveHead; // 3
        gifts[newSrcHead].front = -1;

        if (belts[dst].cnt == 0) {
            belts[dst].tail = moveTail;
            gifts[moveTail].back = -1;
        } else {
            gifts[originDstHead].front = moveTail;
            gifts[moveTail].back = originDstHead;
        }

        belts[dst].cnt += n;
        belts[src].cnt -= n;

        System.out.println(belts[dst].cnt);
    }

    public static void getGiftInfo(int num) {
        System.out.println(gifts[num].front + gifts[num].back * 2);
    }

    public static void getBeltInfo(int num) {
        System.out.println(belts[num].head + belts[num].tail * 2 + belts[num].cnt * 3);
    }

    public static void beltToString(int inst) {
        // System.out.println(inst);
        // int i = -1;
        // for (Belt b : belts) {
        //     i++;
        //     if (i==0) continue;
        //     int idx = b.head;
        //     if (idx == -1) continue;
        //     System.out.print(i + " " + b.head + " ");
        //     while (gifts[idx].back != -1) {
        //         idx = gifts[idx].back;
        //         System.out.print(idx + " ");
        //     }
        //     System.out.println();
        // }
    }

    public static void main(String[] args) throws IOException {
        
        st = new StringTokenizer(br.readLine());
        int q = Integer.parseInt(st.nextToken());

        for (int t = 0; t < q; t++) {
            st = new StringTokenizer(br.readLine());
            int inst = Integer.parseInt(st.nextToken());
            switch (inst) {
                case 100: {
                    factoryEstablish(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
                    beltToString(inst);
                    break;
                }
                case 200: {
                    moveAll(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
                    beltToString(inst);
                    break;
                }
                case 300: {
                    changeHead(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
                    // System.out.println(Arrays.toString(belts));
                    beltToString(inst);
                    break;
                }
                case 400: {
                    divide(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
                    beltToString(inst);
                    break;
                }
                case 500: {
                    getGiftInfo(Integer.parseInt(st.nextToken()));
                    break;
                }
                default: getBeltInfo(Integer.parseInt(st.nextToken()));
            }
        }
    }
}