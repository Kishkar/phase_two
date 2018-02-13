package task1;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;


/**
 * Задача 1 поиск строк в которых слова соответствуют данным словам или регулярным выражениям
 * за слова считаются только латинские буквы
 */
public class Main
{
    // точка входа в программу
    public static void main(String[] args)
    {
        //проверяем кол-во аргументов
        if (args.length == 0)
        {
            System.err.print("Invalid parametrs for program. ");
            System.err.println("Program must have one parametr.");
            return;
        }
        // Проверям регулярные выражения валиные true невалидные false
        Map<String,Boolean> map = new LinkedHashMap<>();
        for(String iterStr: args){
            map.put(iterStr , isRegex(iterStr));
        }
        try(Scanner in = new Scanner(System.in)){

            //начинаем разбор по строчкам
            while (in.hasNext())
            {
                String str = in.nextLine();
                if(str.equals("\n")){break;}
                String [] test = str.trim().split("[^a-zA-Z]");
                for(String string: test){
                    boolean flag = false;
                    for (Map.Entry<String, Boolean> iterStr: map.entrySet()){
                        if(iterStr.getValue()){
                            if (Pattern.compile(iterStr.getKey()).matcher(string).matches())
                            {
                                System.out.println(str);
                                flag = true;
                                break;
                            }
                        }
                        else{
                            if(string.equals(iterStr.getKey())){
                                System.out.println(str);
                                flag = true;
                                break;
                            }
                        }
                    }
                    if(flag){
                        flag = false;
                        break;
                    }
                }
            }
        }
    }
    // Проверка на валидное регулярное выражение
    public static boolean isRegex(final String str) {
        try {
            Pattern.compile(str);
            return true;
        } catch (java.util.regex.PatternSyntaxException e) {
            return false;
        }
    }
}
