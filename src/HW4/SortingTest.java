package HW4;

import java.io.*;
import java.util.*;

public class SortingTest {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
            int[] value;	// 입력 받을 숫자들의 배열
            String nums = br.readLine();	// 첫 줄을 입력 받음
            if (nums.charAt(0) == 'r') {
                // 난수일 경우
                isRandom = true;	// 난수임을 표시

                String[] nums_arg = nums.split(" ");

                int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
                int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
                int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

                Random rand = new Random();	// 난수 인스턴스를 생성한다.

                value = new int[numsize];	// 배열을 생성한다.
                for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
                    value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
            } else {
                // 난수가 아닐 경우
                int numsize = Integer.parseInt(nums);

                value = new int[numsize];	// 배열을 생성한다.
                for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
                    value[i] = Integer.parseInt(br.readLine());
            }

            // 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
            while (true) {
                int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

                String command = br.readLine();

                long t = System.currentTimeMillis();
                switch (command.charAt(0)) {
                    case 'B':	// Bubble Sort
                        newvalue = DoBubbleSort(newvalue);
                        break;
                    case 'I':	// Insertion Sort
                        newvalue = DoInsertionSort(newvalue);
                        break;
                    case 'H':	// Heap Sort
                        newvalue = DoHeapSort(newvalue);
                        break;
                    case 'M':	// Merge Sort
                        newvalue = DoMergeSort(newvalue);
                        break;
                    case 'Q':	// Quick Sort
                        newvalue = DoQuickSort(newvalue);
                        break;
                    case 'R':	// Radix Sort
                        newvalue = DoRadixSort(newvalue);
                        break;
                    case 'X':
                        return;	// 프로그램을 종료한다.
                    default:
                        throw new IOException("잘못된 정렬 방법을 입력했습니다.");
                }
                if (isRandom) {
                    // 난수일 경우 수행시간을 출력한다.
                    System.out.println((System.currentTimeMillis() - t) + " ms");
                } else {
                    // 난수가 아닐 경우 정렬된 결과값을 출력한다.
                    for (int i = 0; i < newvalue.length; i++) {
                        System.out.println(newvalue[i]);
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoBubbleSort(int[] value) {
        // value는 정렬안된 숫자들의 배열이며 value.length 는 배열의 크기가 된다.
        // 결과로 정렬된 배열은 리턴해 주어야 하며, 두가지 방법이 있으므로 잘 생각해서 사용할것.
        // 주어진 value 배열에서 안의 값만을 바꾸고 value를 다시 리턴하거나
        // 같은 크기의 새로운 배열을 만들어 그 배열을 리턴할 수도 있다.
        int length = value.length;
        int tmp;
        for (int i = length - 1; i > 0; --i) {
            for (int j = 0; j < i; ++j) {
                if (value[j] > value[j+1]) {
                    tmp = value[j];
                    value[j] = value[j+1];
                    value[j+1] = tmp;
                }
            }
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoInsertionSort(int[] value) {
        int length = value.length;
        int tmp, j;
        for (int i = 1; i < length; ++i) {
            tmp = value[i];
            j = i - 1;
            while (j >= 0 && tmp < value[j]) {
                value[j+1] = value[j];
                j--;
            }
            value[j+1] = tmp;
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoHeapSort(int[] value) {
        Heap heap = new Heap(value, value.length);
        heap.heapSort();
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoMergeSort(int[] value) {
        mergeSortHelper(value, 0, value.length - 1);
        return (value);
    }

    private static void mergeSortHelper(int[] value, int p, int r) {
        if (p < r) {
            int mid = (p + r) / 2;
            mergeSortHelper(value, p, mid);
            mergeSortHelper(value, mid + 1, r);
            merge(value, p, mid, r);

        }
    }

    private static void merge(int[] value, int p, int mid, int r) {
        int i = p, j = mid + 1, t = 0;
        int[] result = new int[r + 1 - p];

        while (i <= mid && j <= r) {
            if (value[i] < value[j]) {
                result[t++] = value[i++];
            } else {
                result[t++] = value[j++];
            }
        }
        while (i <= mid) {
            result[t++] = value[i++];
        }
        while (j <= r) {
            result[t++] = value[j++];
        }
        System.arraycopy(result, 0, value, p, r + 1 - p);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoQuickSort(int[] value) {
        quickSortHelper(value, 0, value.length - 1);
        return (value);
    }

    private static void quickSortHelper(int[] value, int p, int r) {
        if (p < r) {
            int q = partition(value, p, r);
            quickSortHelper(value, p, q - 1);
            quickSortHelper(value, q + 1, r);
        }
    }

    private static int partition(int[] value, int p, int r) {
        int target = value[r];
        int i = p - 1, tmp;
        for (int j = p; j < r; ++j) {
            if (value[j] < target) {
                tmp = value[++i];
                value[i] = value[j];
                value[j] = tmp;
            }
        }
        tmp = value[i+1];
        value[i+1] = value[r];
        value[r] = tmp;

        return i + 1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoRadixSort(int[] value) {
        int maxValue = Math.abs(value[0]);
        for (int i = 1; i < value.length; ++i) {
            if (Math.abs(value[i]) > maxValue) maxValue = Math.abs(value[i]);
        }

        int maxDigit = 1;
        while (maxDigit * 10 <= maxValue) {
            maxDigit *= 10;
        }


        for (int digit = 1; digit <= maxDigit; digit *= 10) {
            value = radixSortHelper(value, digit);
        }

        value = checkSign(value);
        return (value);
    }

    private static int[] radixSortHelper(int[] value, int digit) {
        int[] result = new int[value.length];
        int digitValue, index = 0;
        List<Queue<Integer>> bucket = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            bucket.add(new LinkedList<>());
        }

        for (int val : value) {
            int absVal = Math.abs(val);
            digitValue = (absVal / digit) % 10;
            bucket.get(digitValue).add(val);
        }

        for (int i = 0; i < 10; ++i) {
            Queue<Integer> queue = bucket.get(i);
            while(!queue.isEmpty()) {
                result[index++] = queue.poll();
            }
        }

        return result;
    }

    private static int[] checkSign(int[] value)  {
        int[] result = new int[value.length];
        int index = 0;
        Queue<Integer> positiveBucket = new LinkedList<>();
        Stack<Integer> negativeBucket = new Stack<>();  // -로 정렬하면 역으로 정렬되므로, stack 이용 

        for (int val : value) {
            if (val < 0) {
                negativeBucket.push(val);  // negative
            } else {
                positiveBucket.add(val);  // positive
            }
        }

        while(!negativeBucket.isEmpty()) {
            result[index++] = negativeBucket.pop();
        }
        while(!positiveBucket.isEmpty()) {
            result[index++] = positiveBucket.poll();
        }
        return result;
    }
}

class Heap {
    public int[] arr;
    private int numItems;

    public Heap(int[] value, int numEles) {
        arr = value;
        numItems = numEles;
    }

    public void buildHeap() {
        if (numItems > 1) {
            for (int i = (numItems - 2) / 2; i >= 0; --i) {
                this.percolateDown(i);
            }
        }
    }

    public int deleteMax() {
        if (!this.isEmpty()) {
            int x = arr[0];
            arr[0] = arr[numItems - 1];
            numItems--;
            this.percolateDown(0);
            return x;
        } else {
            System.out.println("invalid heap operation");
            return -1;
        }
    }

    public void percolateDown(int i) {
        int child = 2 * i + 1;
        int rightChild = 2 * i + 2;
        if (child < numItems) {
            if (rightChild < numItems && arr[child] < arr[rightChild]) {
                child = rightChild;
            }
            if (arr[child] > arr[i]) {
                int tmp = arr[i];
                arr[i] = arr[child];
                arr[child] = tmp;
                this.percolateDown(child);
            }
        }
    }

    public void heapSort() {
        this.buildHeap();
        for (int i = numItems - 1; i > 0; --i) {
            arr[i] = this.deleteMax();
        }
    }

    public boolean isEmpty() {
        return numItems == 0;
    }
}
