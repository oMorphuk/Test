import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;


public class Calc {

    private static HashMap<String, Double> vars = new HashMap<>();

    static boolean isOperator(char ch) {
        return ch == '+' || ch == '/' || ch == '*' || ch == '-';
    }

    static int getPriority(char operator) {

        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        } else {
            return -1;
        }
    }

    static void calcBinary(LinkedList<Double>stack, char operator) {
        double operand2 = stack.removeLast();
        double operand1 = stack.removeLast();

        if (operator == '+') {
            stack.add(operand1 + operand2);
        } else if (operator == '-') {
            stack.add(operand1 - operand2);
        } else if (operator == '*') {
            stack.add(operand1 * operand2);
        } else if (operator == '/') {
            stack.add(operand1 / operand2);
        }
    }

    public static double eval(String str) {
        LinkedList<Double>numbers = new LinkedList<Double>();
        LinkedList<Character>stack = new LinkedList<Character>();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch == ' ') {
                continue;
            }

            //  левая скобка
            if (ch == '(') {
                stack.add('(');
            // правая скобка
            } else if (ch == ')') {
                while (stack.getLast() != '(') {
                    calcBinary(numbers, stack.removeLast());
                }

                stack.removeLast();
            // оператор
            } else if (isOperator(ch)) {
                while (!stack.isEmpty() && getPriority(stack.getLast()) >= getPriority(ch)) {
                    calcBinary(numbers, stack.removeLast());
                }

                stack.add(ch);

            // часть числа или переменной
            } else {
                String st = "", var = "";

                while (i < str.length()) {
                    if (Character.isDigit(str.charAt(i)) || str.charAt(i) == '.') {
                        st += str.charAt(i++);
                    } else if (Character.isAlphabetic(str.charAt(i))) {
                        var += str.charAt(i++);
                    } else if (str.charAt(i) == ' ') {
                        i++;
                    } else {
                        break;
                    }
                }

                --i;

                // встречались символы алфавита
                if (var != "") {
                    if (vars.containsKey(var)) {
                        numbers.add(vars.get(var));
                    } else {
                        numbers.add(0.0);
                    }
                } else if (st != "") {
                    numbers.add(Double.parseDouble(st));
                }
            }
        }

        while (!stack.isEmpty()) {
            calcBinary(numbers, stack.removeLast());
        }

        return numbers.get(0);
    }

    public static void main(String[] args) throws Exception {
        Scanner cs = new Scanner(System.in);

        while (cs.hasNext()) {
            double result;
            String[] parts;
            String var = "";

            String str = cs.nextLine();

            if (str.contains("=")) {
                parts = str.split("=");
                var = parts[0].trim();
                str = parts[1].trim();
            }

            result = eval(str);

            if (var != "") {
                Calc.vars.put(var, result);
            }

            System.out.println(result);
        }
    }
}