package HW5;

import java.io.*;
import java.util.Iterator;

public class Matching {
    // hash의 key는 Key, slot의 value는 AVLTree<Key, Pair>
    // AVL의 key는 Key, Node의 item은 LinkedList<Pair>

    private static HashTable<StringKey, Pair> hashTable = new HashTable<>();
    final static int LENGTH = 6;

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;

                command(input);
            }
            catch (IOException e) {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
    }

    private static void command(String input) {
        char startChar = input.charAt(0);

        switch (startChar) {
            case '<': // data input
                buildDataStructure(input.substring(2));
                break;
            case '@': // index num
                printSlotOf(Integer.parseInt(input.substring(2)));
                break;
            case '?': // search pattern
                searchPattern(input.substring(2));
                break;
            default:
                System.out.println("wrong input");
        }
    }

    private static void buildDataStructure(String fileName) {
        File file = new File(fileName);
        try {
            BufferedReader fileBr = new BufferedReader(new FileReader(file));
            String line;
            int i = 0;

            hashTable = new HashTable<>();

            while ((line = fileBr.readLine()) != null) {
                for (int j = 0; j < line.length() - LENGTH + 1; ++j) {
                    String subStr = line.substring(j, j + LENGTH);
                    StringKey key = new StringKey(subStr);
                    Pair pair = new Pair(i+1, j+1);

                    // function call 순서
                    // 1. hastTable.insert(Key, Pair) (to AVL Tree)
                    // 2. AVLTree.insert(Key, Pair) (to AVL Node)
                    // 3. AVLNode.insertPair(Pair) (to item의 LinkedList)
                    // 4. LinkedList.append(Pair)
                    hashTable.insert(key, pair);
                }
                i++;
            }

        } catch (IOException e) {
            // file not found
            e.printStackTrace();
        }

    }

    private static void printSlotOf(int slotNum) {
        // hash slot에 해당하는 avl tree를 preorder 탐색
        if (hashTable.getSlotOf(slotNum) == null) {
            System.out.println("EMPTY");
        } else {
            String resultString = hashTable.getSlotOf(slotNum).preOrderAsString();
            System.out.println(resultString.substring(0, resultString.length() - 1));
        }
    }

    private static void searchPattern(String pattern) {
        // 1~6 index를 search, 7~12를 search ...
        // 각 substring의 search 결과의 i가 같고, j가 6씩 차이나면서 연달아 있으면 가장 첫머리 출력

        if (pattern.length() < LENGTH) {
            System.out.println("Illegal length");
            return;
        }
        StringBuilder sb = new StringBuilder();

        String firstPiece = pattern.substring(0, LENGTH);
        LinkedList<Pair> searchResultFirst = hashTable.search(new StringKey(firstPiece));
        if (searchResultFirst == null) {
            //System.out.println("no first");
            System.out.println(new Pair(0, 0).toString());
            return;
        }

        // 첫 substring의 pair들의 다음 substring들을 각각 탐색
        for (Iterator<Pair> it = searchResultFirst.iterator(); it.hasNext(); ) {
            Pair firstPair = it.next();
            int firstI = firstPair.i;
            int firstJ = firstPair.j;
            boolean isMatched = true;

            for (int startIndex = 0; startIndex <= pattern.length() - LENGTH; ) {
                String nextPiece = pattern.substring(startIndex, startIndex + LENGTH);
                LinkedList<Pair> searchResultNext = hashTable.search(new StringKey(nextPiece));

                // search 결과 x
                if (searchResultNext == null) {
                    isMatched = false;
                    break;
                }

                // search 결과는 있지만, substring에 matching되는게 x
                if (!searchResultNext.contains(new Pair(firstI, firstJ + startIndex))) {
                    isMatched = false;
                    break;
                }

                // index control
                if (startIndex == pattern.length() - LENGTH) break;
                if (startIndex + 2 * LENGTH > pattern.length()) {
                    // 마지막에 완전하지 않은 piece 처리
                    startIndex = pattern.length() - LENGTH;
                } else {
                    startIndex += LENGTH;
                }
            }

            if (isMatched) {
                sb.append(firstPair.toString()).append(" ");
            }
        }
        if (!sb.toString().equals("")) {
            System.out.println(sb.substring(0, sb.length() - 1));
        } else {
            // 탐색 결과 없음
            System.out.println(new Pair(0, 0).toString());
        }
    }
}
