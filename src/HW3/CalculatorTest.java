package HW3;

import java.io.*;
import java.util.Stack;

class InvalidExpressionException extends RuntimeException {

}

class InvalidOperationException extends RuntimeException {

}

public class CalculatorTest {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            try {
                String input = br.readLine();
                if (input.compareTo("q") == 0) {
                    break;
                }
                command(input);
            } catch (Exception e) {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
    }

    private static void command(String input) {
        try {
            String postfix = infix2postfix(input.trim());
            long result = calculate(postfix);
            System.out.println(postfix);
            System.out.println(result);
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("ERROR");
        }
    }

    private static String infix2postfix(String input) throws InvalidExpressionException{
        /*
        algorithm
        연산자인 경우:
            stacktop보다 현재 연산자가 priority가 높음: 그냥 list에 삽입
            낮음: stacktop의 연산자를 pop해서 list에 삽입, 이 연산자를 stack에 push
         숫자인 경우:
            list에 숫자 삽입
         괄호인 경우:
            ( : 스택에 삽입
            ) : (이 뽑힐때 까지 list에 보관한 후 재귀로 처리
         */

        StringBuilder postfix = new StringBuilder();
        Stack<Character> opStack = new Stack<>();
        Stack<Character> parenthesisStack = new Stack<>();
        StringBuilder insideOfParenthesis = new StringBuilder();

        char letter, poppedOp;
        boolean digitPreviously = false, unitExpressionEnded = false, parenthesisOpened = false;
        int priority, topPriority;

        for(int i = 0; i < input.length(); ++i) {
            letter = input.charAt(i);
            if (parenthesisOpened) {
                // 괄호 내용을 재귀로 처리하기 위해 따로 보관
                insideOfParenthesis.append(letter);
            }
            // 공백은 pass
            if (letter != ' ' && letter !='\t') {
                if (isOperator(letter)) {
                    // operator
                    if (parenthesisOpened) continue; // 괄호 기록중에는 작업 x

                    if (!digitPreviously) {
                        if (letter == '-') {
                            // unary minus operator
                            letter = '~';
                        } else {
                            // invalid expression
                            throw new InvalidExpressionException();
                        }
                    }

                    // operator의 priority 계산. 값이 작을수록 높은 priority
                    priority = getOperatorPriority(letter);
                    while (!opStack.isEmpty()) {
                        // 현재 연산자의 priority가 topPriority보다 낮음
                        topPriority = getOperatorPriority(opStack.peek());
                        if (topPriorityIsHigher(priority, topPriority, letter != '^' && letter != '~')) {
                            poppedOp = opStack.pop();
                            postfix.append(' ');
                            postfix.append(poppedOp);
                        } else {
                            break;
                        }
                    }

                    // push operator
                    opStack.push(letter);

                    digitPreviously = false;
                    unitExpressionEnded = true;
                } else if (isDigit(letter)){
                    // number
                    if (parenthesisOpened) continue; // 괄호 기록중에는 작업 x

                    if (unitExpressionEnded && digitPreviously) {
                        // 연속된 두 정수
                        throw new InvalidExpressionException();
                    }

                    if (!digitPreviously) {
                        postfix.append(' ');
                    }
                    postfix.append(letter);

                    digitPreviously = true;
                    unitExpressionEnded = false;
                } else if (isParenthesis(letter)) {
                    // parenthesis
                    if (letter == '(') {
                        parenthesisStack.push(letter);
                        parenthesisOpened = true;
                    } else {
                        if (!parenthesisStack.isEmpty()) {
                            parenthesisStack.pop();
                            if (parenthesisStack.isEmpty()) {
                                // 가장 처음에 들어간 괄호가 pop된 경우

                                // 괄호 안 내용 처리
                                insideOfParenthesis.deleteCharAt(insideOfParenthesis.length() - 1); // 마지막에 추가로 들어간 괄호 제거
                                String result = infix2postfix(insideOfParenthesis.toString());
                                postfix.append(' ');
                                postfix.append(result);

                                digitPreviously = true; // 괄호 안의 expression은 계산 후 하나의 숫자로 대체될 수 있음.
                                unitExpressionEnded = true; // 괄호 한 단위가 닫히면 하나의 expression이 끝났다고 볼 수 있음.
                                insideOfParenthesis.setLength(0);
                                parenthesisOpened = false;
                            }
                        } else {
                            // ')' 괄호가 더 많은 경우
                            throw new InvalidExpressionException();
                        }
                    }
                } else {
                    // invalid character
                    throw new InvalidExpressionException();
                }
            } else {
                unitExpressionEnded = true;
            }
        }

        while(!opStack.isEmpty()) {
            postfix.append(' ');
            postfix.append(opStack.pop());
        }

        if (!parenthesisStack.isEmpty()) {
            // '(' 괄호가 더 많은 경우
            throw new InvalidExpressionException();
        }
        return postfix.toString().trim();
    }

    private static int getOperatorPriority(char letter) {
        String priorityString = "^~*/%+-";
        int index = priorityString.indexOf(Character.toString(letter));
        int priority = -1;
        switch (index) {
            case 0: priority = 0; break;
            case 1: priority = 1; break;
            case 2:
            case 3:
            case 4: priority = 2; break;
            case 5:
            case 6: priority = 3; break;
        }

        return priority;
    }

    private static boolean isOperator(char letter) {
        String opDelimiter = "[-^%/*+~]";
        return Character.toString(letter).matches(opDelimiter);
    }

    private static boolean isDigit(char letter) {
        String digitDelimiter = "[0-9]";
        return Character.toString(letter).matches(digitDelimiter);
    }

    private static boolean isParenthesis(char letter) {
        String parenthesisDelimiter = "[()]";
        return Character.toString(letter).matches(parenthesisDelimiter);
    }

    private static boolean topPriorityIsHigher(int priority, int topPriority, boolean leftAssociative) {
        // true: top priority가 priority보다 높음

        if (leftAssociative) {
            // leftAssociative: priority 같으면 먼저 들어온 것 먼저
            return priority >= topPriority;
        } else {
            // rightAssociative: priority 같으면 나중에 들어온 것 먼저
            return priority > topPriority;
        }
    }

    private static long calculate(String postfix) throws InvalidOperationException{
        // 수업시간에 활용한 postfix 계산 함수 응용
        // postfix: valid 가정
        char letter;
        boolean digitPreviously = false;
        Stack<Long> numStack = new Stack<>();
        long operand1, operand2, result;


        for (int i = 0; i < postfix.length(); ++i) {
            letter = postfix.charAt(i);
            if (letter != ' ') {
                if (isDigit(letter)) {
                    if (digitPreviously) {
                        // 숫자 자릿수 처리
                        numStack.push(10 * numStack.pop() + (letter - '0'));
                    } else {
                        numStack.push((long) (letter - '0'));
                    }
                    digitPreviously = true;
                } else if (isOperator(letter)) {
                    if (letter == '~') {
                        // unary
                        operand1 = numStack.pop();
                        result = operate(operand1, letter);
                    } else {
                        // binary
                        operand1 = numStack.pop();
                        operand2 = numStack.pop();
                        if (isValidOperation(operand1, operand2, letter)) {
                            result = operate(operand1, operand2, letter);
                        } else throw new InvalidOperationException();
                    }
                    numStack.push(result);
                    digitPreviously = false;
                }
            } else {
                digitPreviously = false;
            }
        }

        return numStack.pop();
    }

    private static boolean isValidOperation(long operand1, long operand2, char operator) {
        if (!isOperator(operator)){
            return false;
        }

        if (operator == '/' || operator == '%') {
            return operand1 != 0;
        }
        if (operator == '^' && operand1 < 0) {
            return operand2 != 0;
        }
        return true;
    }

    private static long operate(long operand1, long operand2, char operator) {
        // binary operation
        long result = 0;
        switch (operator) {
            case '^': result = (long) Math.pow(operand2, operand1); break;
            case '*': result = operand2 * operand1; break;
            case '/': result = operand2 / operand1; break;
            case '%': result = operand2 % operand1; break;
            case '+': result = operand2 + operand1; break;
            case '-': result = operand2 - operand1; break;
        }

        return result;
    }
    private static long operate(long operand, char operator) {
        // unary operation
        return -operand;
    }
}
