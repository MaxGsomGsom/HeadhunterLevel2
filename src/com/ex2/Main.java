package com.ex2;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.math.BigInteger;

public class Main {

    private static Scanner input = new Scanner(System.in);
    private static PrintStream output = System.out;

    public static void main(String[] args) {
        String inputStr = ""; //входная последовательность
        //ввод последовательности с проверкой ввода
        Pattern p = Pattern.compile("^\\d+$");
        while (!p.matcher(inputStr).matches() || inputStr.length() > 50) {
            output.println("Введите последовательность:");
            inputStr = input.nextLine();
        }

        //поиск минимального подходящего числа num в input
        NumSub numSub = FindInclude(inputStr);

        //подсчет кол-ва символов с начала последовательности S
        BigInteger countDigits = CalcIncludePos(numSub.num);
        //вычитаем несколько символов, если последовательность начинается не с полного числа
        countDigits = countDigits.subtract(BigInteger.valueOf(numSub.sub));


        output.println("===============================");
        output.println("Первое вхождение в S находится на позиции:");
        output.println(countDigits);
        input.nextLine();
    }

    private static NumSub FindInclude(String inputStr) {

        NumSub numSub = new NumSub();
        numSub.num = BigInteger.TEN.pow(51);
        numSub.sub = 0;

        //для каждого символа из input
        for (int start = 0; start < inputStr.length(); start++) {
            //для каждой длины числа num, большей, чем позиция с начала последовательности input, но меньшей длины последовательности
            for (int len = start + 1; len <= (inputStr.length() - start); len++) {
                //копируем число в буфер
                BigInteger newNum = new BigInteger(inputStr.substring(start, start+len));
                BigInteger newNumAdd = newNum;

                //=== проверка начальной части последовательности ===
                //если число num стоит не в начале последовательности input
                if (start > 0) {
                    newNumAdd = newNumAdd.subtract(BigInteger.ONE);

                    String newNumStr = newNumAdd.toString();
                    //если начальная часть числа в буфере не совпадает с конечной частью числа num, то выход
                    if (!newNumStr.substring(newNumStr.length() - start, newNumStr.length()).equals(inputStr.substring(0, start)))
                        continue;

                    newNumAdd = newNumAdd.add(BigInteger.ONE);
                }

                //=== проверка середины последовательности ===
                //для всех следующих за num чисел той же длины в последовательности input
                boolean isGood = true;
                int cur = (start + len);
                for (; cur <= (inputStr.length() - len); cur += len) {
                    newNumAdd = newNumAdd.add(BigInteger.ONE);
                    //если числа не равны
                    if (!newNumAdd.toString().equals(inputStr.substring(cur, cur + len))) {
                        isGood = false;
                        break;
                    }
                }
                if (!isGood) continue;

                //=== проверка конечной части последовательности ===
                int endLen = inputStr.length() - cur;

                //если в конце input остались символы
                if (endLen < len && endLen > 0) {
                    newNumAdd = newNumAdd.add(BigInteger.ONE);
                    //если эти символы не совпадают с начальной частью числа в буфере, то выход
                    if (!inputStr.substring(cur).equals(newNumAdd.toString().substring(0, endLen)))
                        continue;
                }

                //если число в буфере подходит и оно меньше найденного ранее num
                if (numSub.num.compareTo(newNum) > 0) {
                    numSub.num = newNum;
                    numSub.sub = start;
                }

            }
        }

        return numSub;

    }

    private static BigInteger CalcIncludePos(BigInteger num) {
        BigInteger countDigits = BigInteger.ZERO; //всего символов с начала последовательности S
        BigInteger cur = BigInteger.ONE; //текущий разряд
        BigInteger digits = BigInteger.ZERO; //кол-во цифр в разряде
        BigInteger next; //следующий разряд

        while (num.compareTo(BigInteger.ZERO) > 0) {
            //добавляем разряд
            digits = digits.add(BigInteger.ONE);
            next = cur.multiply(BigInteger.TEN);
            //вычитаем кол-во чисел этого разряда из исходного числа
            num = num.add(cur).subtract(next);

            //добавляем кол-во цифр всех чисел этого разряда к позиции от начала последовательности S
            if (num.compareTo(BigInteger.ZERO) >= 0)
                countDigits = countDigits.add(next.subtract(cur).multiply(digits));
            else
                countDigits = countDigits.add(next.subtract(cur).add(num).multiply(digits));

            cur = next;
        }

        return countDigits.subtract(digits).add(BigInteger.ONE); //начальная позиция - первая цифра числа num
    }



    private static class NumSub{
        private BigInteger num = BigInteger.ZERO; //минимальное число, входящее в input и удовлетворяющее условие задачи
        private int sub = 0; //сколько нужно вычесть из countDigits, если число num стоит не в начале последовательности input

        private NumSub() {}
    }
}