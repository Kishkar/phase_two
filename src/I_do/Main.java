package I_do;

import java.math.BigDecimal;
import java.util.*;

/**
 * Задача номер 2 калькулятор
 * разделители между цифрами операторами и скобками являются пробел
 * без порбела не посчитает
 */

public class Main {

    //Метод возвращает true, если проверяемый символ - "("
    private static boolean IsFrontBracket(String c)
    {
        String  string = "(";
        if (string.equals(c))
            return true;
        return false;
    }

    //Метод возвращает true, если проверяемый символ - ")"
    private static boolean IsBackBracket(String c)
    {
        String  string = ")";
        if (string.equals(c))
            return true;
        return false;
    }

    //Метод возвращает true, если проверяемый символ - оператор
    private static boolean IsOperator(String с)
    {
        String string = "+-/*^";
        if ((string.indexOf(с) != -1))
            return true;
        return false;
    }

    //Метод проверяет на корректное число
    private static boolean IsNumber(String string){
        try {
            new BigDecimal(string);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }

    //Метод возвращает приоритет
    private static byte GetPriority(String token){
        if (token.equals("(")) {
            return 0;
        } else if (token.equals(")")) {
            return 1;
        } else if (token.equals("+") || token.equals("-") ) {
            return 2;
        }  else if (token.equals("*") || token.equals("/")) {
            return 3;
        } else if (token.equals("^")) {
            return 4;
        }
        return 5;
    }

    //Метод проверяет на корректный ввод
    private static boolean Check(List<String> list){
        int size = list.size();
        int brackets = 0;
        boolean flagOperator = false , flagNumber = false;
        for(int i = 0 ; i < size ; i++){
            size = list.size();
            String string = list.get(i);
            //Если начинается с ")" или с операторов кроме -
            if(i == 0 && !string.equals("-")){
                if(IsOperator(string)||IsBackBracket(string)){
                    System.out.println("Неверное начало выражения  " + string );
                    return false;
                }
            }
            if( i == 0 && string.equals("-")){
                list.add(0,"0");
                string = list.get(i);
            }
            //Если заканчиваетя на "(" или на операторы
            if(i == (size - 1)){
                if(IsOperator(string)||IsFrontBracket(string)){
                    System.out.println("Неверный конец выражения " + string);
                    return false;
                }
            }
            if(IsNumber(string)){
                if(i!=0 && flagNumber ){
                    System.out.println("Неверное записанное выражение " + list.get(i-1) + string);
                    return false;
                }
                flagNumber = true;
                flagOperator = false;
                if(i!=0 && IsBackBracket(list.get(i-1))){
                    System.out.println("Неверное записанное выражение " + list.get(i-1) + string);
                    return false;
                }
            }
            else if(IsOperator(string)){
                if(flagOperator){
                    System.out.println("Неверное записанное выражение " + list.get(i-1) + string);
                    return false;
                }
                flagNumber = false;
                flagOperator = true;
                if(!string.equals("+") && !string.equals("-")){
                    if(IsFrontBracket(list.get(i-1))) {
                        System.out.println("Неверное записанное выражение " + list.get(i - 1) + string);
                        return false;
                    }
                }
                else if(IsFrontBracket(list.get(i-1))){
                    list.add(i,"0");
                    string = list.get(i);
                }
            }
            else if(IsFrontBracket(string)){
                brackets++;
                if( i != 0 && IsNumber(list.get(i-1))){
                    System.out.println("Неверное записанное выражение " + list.get(i-1) + string);
                    return false;
                }
            }
            else if(IsBackBracket(string)){
                brackets--;
                if(IsOperator(list.get(i-1))){
                    System.out.println("Неверное записанное выражение " + list.get(i-1) + string);
                    return false;
                }
            }
            else {
                System.out.println("Неизвестный оператор " + string);
                return false;
            }
        }
        if(brackets != 0){
            System.out.println("Нечетное число скобок");
            return false;
        }
        return true;
    }

    // Метод считает данное выражение
    public static BigDecimal Calculate(String input)
    {
        String output = GetExpression(input); //Преобразовываем выражение в постфиксную запись
        if(output.equals("mistake")){
            return null;
        }

        BigDecimal result = Counting(output); //Решаем полученное выражение
        return result; //Возвращаем результат
    }

    //Метод преобразует выражение в префиксную форму
    private static String GetExpression(String input)
    {
        List<String> list = new ArrayList<>(Arrays.asList(input.replace(",",".").split(" +")));
        //Лист для хранения выражения
        if(!Check(list)){
            return "mistake";
        }
        StringBuilder output = new StringBuilder(); //Строка для хранения выражения
        Stack<String> operStack = new Stack<>(); //Стек для хранения операторов


        for(String string : list){
            if(IsNumber(string)) {
                output.append(string);
                output.append(" ");
            }
            if(IsFrontBracket(string)){
                operStack.push(string);
            }
            else if(IsBackBracket(string)) {
                String str = operStack.pop();
                while (!IsFrontBracket(str)) {
                    output.append(str);
                    output.append(" ");
                    str = operStack.pop();
                }
            }
            if(IsOperator(string)){
                if(!operStack.empty()) {
                    while ( !operStack.empty() && GetPriority(string) <=  GetPriority(operStack.peek())) {
                        output.append(operStack.pop());
                        output.append(" ");
                    }
                }
                operStack.push(string);
            }
        }

        //Когда прошли по всем символам, выкидываем из стека все оставшиеся там операторы в строку
        while (!operStack.empty()) {
            output.append(operStack.pop());
            output.append(" ");
        }

        return new String(output); //Возвращаем выражение в постфиксной записи
    }

    // Метод считает из префиксной формы выражение
    private static BigDecimal Counting(String input)
    {
        Stack<BigDecimal> temp = new Stack<BigDecimal>(); //стек для решения
        BigDecimal result = null;
        for (String string : input.split(" ")){
            if(IsNumber(string)){
                temp.push(new BigDecimal(string));
            }
            else if(IsOperator(string)){
                BigDecimal one = temp.pop();
                BigDecimal two = temp.pop();

                switch (string){
                    case "+":
                        result = two.add(one);
                        break;
                    case "-":
                        result = two.subtract(one);
                        break;
                    case "/":
                        if(one.compareTo(new BigDecimal("0.00")) == 0 ){
                            System.out.println("Деление на 0 ");
                            return null;
                        }
                        result = two.divide(one);
                        break;
                    case "*":
                        result = two.multiply(one);
                        break;
                    case "^":
                        if(one.scale() != 0 ){
                            System.out.println("Возведение в нецелую степень");
                            return null;
                        }
                        int flag = one.signum();
                        BigDecimal bigDecimal = new BigDecimal(two.toString());
                        long i = 1;
                        long help = Math.abs(one.longValue());
                        if(help != 0 ){
                            if(flag < 0){
                                while(i <= (help+1) ){
                                    result = two.divide(bigDecimal);
                                    i++;
                                    two = result;
                                }
                            }
                            else {
                                while(i < help){
                                    result = two.multiply(bigDecimal);
                                    i++;
                                    two = result;
                                }
                            }
                        }
                        else{
                            result = two.subtract(two);
                        }
                        break;
                }
                temp.push(result);
            }

        }
        return temp.pop();
    }

    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            System.out.println("Максимальная степень 9223372036854775807 и минимальная степень -9223372036854775808");
            System.out.println("Введите выражение в одну строку :");
            while(scanner.hasNext()){
                BigDecimal result = Calculate(scanner.nextLine());
                if(result != null){
                    System.out.println(result);
                }
                else
                    System.out.println("Введите выражение в одну строку :");

            }
        }
    }
}
