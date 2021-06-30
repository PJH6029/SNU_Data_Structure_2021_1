package HW1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;


public class BigInteger {
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";

    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("");

    private int sign;
    private int length;
    private int[] num;

    public BigInteger(int[] num) {
        this.sign = 1;
        this.length = num.length;
        this.num = new int[this.length];
        System.arraycopy(num, 0, this.num, 0, length);
    }

    public BigInteger(String s) {
        char c = s.charAt(0);
        if (c == '+' || c == '-') {
            this.sign = (c == '+') ? 1 : -1;

            int start = -1;
            for (int i = 1; i < s.length(); ++i) {
                if (s.charAt(i) != ' ') {
                    start = i;
                    break;
                }
            }

            this.length = s.length() - start;
            this.num = String2intArray(s.substring(start));
        } else {
            this.sign = 1;
            this.length = s.length();
            this.num = String2intArray(s);
        }
    }

    public BigInteger(int sign, int[] num) {
        // length should equal to num.length
        int start = -1;

        for (int i = 0; i < num.length; ++i) {
            if (num[i] != 0) {
                start = i;
                break;
            }
        }

        this.sign = sign;

        if (start == -1) {
            this.num = new int[] {0};
            this.length = 1;
        } else {
            this.length = num.length - start;
            this.num = new int[this.length];
            System.arraycopy(num, start, this.num, 0, this.length);
        }
    }


    public BigInteger add(BigInteger big) {
        boolean thisPlus = (this.sign == 1);
        boolean bigPlus = (big.sign == 1);
        BigInteger tmp;
        if (thisPlus && bigPlus) {
            tmp = this.positiveAdd(big);
        } else if (!thisPlus && !bigPlus) {
            tmp = this.positiveAdd(big);
            tmp.inverseSign();
        } else if (thisPlus && !bigPlus) {
            tmp = this.positiveSubtract(big);
            int signTmp = this.positiveCompareTo(big);
            if (signTmp == -1) {
                tmp.inverseSign();
            }
        } else { // !thisPlus && bigPlus
            tmp = this.positiveSubtract(big);
            int signTmp = this.positiveCompareTo(big);
            if (signTmp == 1) {
                tmp.inverseSign();
            }
        }
        return tmp;
    }

    public BigInteger subtract(BigInteger big) {
        boolean thisPlus = (this.sign == 1);
        boolean bigPlus = (big.sign == 1);
        BigInteger tmp;
        if (thisPlus && bigPlus) {
            tmp = this.positiveSubtract(big);
            int signTmp = this.positiveCompareTo(big);
            if (signTmp == -1) {
                tmp.inverseSign();
            }
        } else if (!thisPlus && !bigPlus) {
            tmp = this.positiveSubtract(big);
            int signTmp = this.positiveCompareTo(big);
            if (signTmp == 1) {
                tmp.inverseSign();
            }
        } else if (thisPlus && !bigPlus) {
            tmp = this.positiveAdd(big);
        } else { // !thisPlus && bigPlus
            tmp = this.positiveAdd(big);
            tmp.inverseSign();
        }
        return tmp;
    }

    public BigInteger multiply(BigInteger big) {
        boolean thisPlus = (this.sign == 1);
        boolean bigPlus = (big.sign == 1);

        BigInteger tmp = this.positiveMultiply(big);

        if (thisPlus != bigPlus) {
            tmp.inverseSign();
        }

        return tmp;
    }

    private BigInteger positiveAdd(BigInteger big) {
        if (big.length == 1 && big.num[0] == 0) {
            return new BigInteger(1, this.num);
        }

        if (this.length < big.length) {
            return big.positiveAdd(this);
        }

        int[] tmp = new int[this.length+1];
        int carryIn = 0, carryOut = 0, insideTmp = 0;
        int lengthDiff = this.length - big.length;

        for (int i = tmp.length - 1; i >= 0; --i) {
            carryIn = carryOut;
            if (i != 0) {
                if (i > lengthDiff) {
                    insideTmp = this.num[i-1] + big.num[i-lengthDiff-1] + carryIn;
                } else {
                    insideTmp = this.num[i-1] + carryIn;
                }
            } else {
                insideTmp = carryIn;
            }

            if (insideTmp >= 10) {
                carryOut = insideTmp / 10;
            } else {
                carryOut = 0;
            }

            insideTmp %= 10;
            tmp[i] = insideTmp;
        }
        return new BigInteger(1, tmp);
    }

    private BigInteger positiveSubtract(BigInteger big) {
        int isBigger = this.positiveCompareTo(big);
        if (isBigger == 0) {
            return new BigInteger(1, new int[] {0});
        } else if (isBigger == -1) {
            return big.positiveSubtract(this);
        }

        if (big.length == 1 && big.num[0] == 0) {
            return new BigInteger(1, this.num);
        }

        int[] tmp = new int[this.length];
        int carryIn = 0, carryOut = 0, insideTmp = 0;
        int lengthDiff = this.length - big.length;

        for (int i = tmp.length - 1; i >= 0; --i) {
            carryIn = carryOut;

            if (i >= lengthDiff) {
                insideTmp = this.num[i] - big.num[i-lengthDiff] - carryIn;
            } else {
                insideTmp = this.num[i] - carryIn;
            }

            if (insideTmp < 0) {
                carryOut = 1;
                insideTmp += 10;
            } else {
                carryOut = 0;
            }

            tmp[i] = insideTmp;
        }
        return new BigInteger(1, tmp);
    }

    private BigInteger positiveMultiply(BigInteger big) {
        if (big.length == 1 && big.num[0] == 0) {
            return new BigInteger(1, new int[] {0});
        }

        if (this.length < big.length) {
            return big.positiveMultiply(this);
        }

        if (big.length == 1) {
            return this.positiveMultiplyByOneDigit(big.num[0]);
        } else {
            BigInteger tmp = new BigInteger(1, new int[] {0});
            BigInteger insideTmp;
            for (int i = 0; i < big.length; ++i) {
                insideTmp = this.positiveMultiplyByOneDigit(big.num[i]);
                insideTmp.shift10Digits(big.length - i - 1);
                tmp = tmp.add(insideTmp);
            }

            return tmp;
        }
    }

    private BigInteger positiveMultiplyByOneDigit(int n) {
        int[] tmp = new int[this.length+1];
        int carryIn = 0, carryOut = 0, insideTmp = 0;

        for (int i = tmp.length - 1; i >= 0; --i) {
            carryIn = carryOut;
            if (i != 0) {
                insideTmp = num[i-1] * n + carryIn;
            } else {
                insideTmp = carryIn;
            }

            if (insideTmp >= 10) {
                carryOut = insideTmp / 10;
            } else {
                carryOut = 0;
            }

            insideTmp %= 10;
            tmp[i] = insideTmp;
        }
        return new BigInteger(1, tmp);

    }

    private void shift10Digits(int bits) {
        if (bits == 0) {
            return;
        }
        int[] tmp = new int[this.length + bits];
        System.arraycopy(this.num, 0, tmp, 0, this.length);
        for (int i = this.length; i < tmp.length; ++i) {
            tmp[i] = 0;
        }
        this.num = tmp;
        this.length = tmp.length;
    }

    private int positiveCompareTo(BigInteger big) {
        if (this.length > big.length) {
            return 1;
        }
        if (this.length < big.length) {
            return -1;
        }

        for (int i = 0; i < this.length; ++i) {
            if (this.num[i] > big.num[i]) {
                return 1;
            } else if (this.num[i] < big.num[i]) {
                return -1;
            }
        }
        return 0;
    }

    private void inverseSign() {
        this.sign = (this.sign == 1) ? -1 : 1;
    }

    private int[] String2intArray(String s) {
        int[] num = new int[s.length()];

        for (int i = 0; i < s.length(); ++i) {
            num[i] = Character.getNumericValue(s.charAt(i));
        }

        return num;
    }

    @Override
    public String toString() {
        StringBuilder builder;
        if (this.sign == 1) {
            builder = new StringBuilder();
            for (int i = 0; i < this.length; ++i) {
                builder.append(this.num[i]);
            }
        } else {
            builder = new StringBuilder("-");
            for (int i = 0; i < this.length; ++i) {
                builder.append(this.num[i]);
            }
        }
        return builder.toString();
    }

    static BigInteger evaluate(String input) throws IllegalArgumentException {
        BigInteger num1;
        BigInteger num2;
        BigInteger num3;
        String operator = "";

        int opIndex = -1;
        input = input.trim();
        for (int i = 0; i < input.length(); ++i){
            char c = input.charAt(i);
            if (i != 0 && (c == '+' || c == '-' || c == '*')) {
                opIndex = i;
                operator = Character.toString(c);
                break;
            }
        }

        if (opIndex != -1 && !operator.equals("")) {
            String arg1 = input.substring(0, opIndex).trim();
            String arg2 = input.substring(opIndex + 1).trim();

            num1 = new BigInteger(arg1);
            num2 = new BigInteger(arg2);

            switch (operator) {
                case "+":
                    num3 = num1.add(num2);
                    break;
                case "-":
                    num3 = num1.subtract(num2);
                    break;
                case "*":
                    num3 = num1.multiply(num2);
                    break;
                default:
                    num3 = new BigInteger("-1"); // Wrong input
                    break;
            }

            return num3;

        } else {
            return new BigInteger("-1"); // Wrong input
        }

    }

    public static void main(String[] args) throws Exception {
        try (InputStreamReader isr = new InputStreamReader(System.in)) {
            try (BufferedReader reader = new BufferedReader(isr)) {
                boolean done = false;
                while (!done) {
                    String input = reader.readLine();

                    try {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e) {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }

    static boolean processInput(String input) throws IllegalArgumentException {
        boolean quit = isQuitCmd(input);

        if (quit) {
            return true;
        } else {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());

            return false;
        }
    }

    static boolean isQuitCmd(String input) {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
