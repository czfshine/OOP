package cn.czfshine.oop;

import java.util.Calendar;
import java.util.Scanner;

/**
 * @author:czfshine
 * @date:2017/9/30 22:50
 */

public class Printer {
    private int year; 
    private  Calendar calendar = Calendar.getInstance();
    public int GetYear() {
        int year = 2017;
        System.out.println("输入年份就行：");
        try (Scanner reader = new Scanner(System.in)) {
            year = reader.nextInt();
        } catch (Exception e) {
            System.out.println("This world is mad! trying use year=2017");
        }
        return year;
    }

    private void Printtitle(int month) {
        String[] MonName = new String[]{
                "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

        System.out.printf("\n%d \t %s\n" +
                "----------------------------\n"+
                " Sun Mon Tue Wed Thu Fri Sat\n", year, MonName[month]);
    }

    private void PrintDays(){
        System.out.printf("%4d", calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void PrintMonth(){
        Printtitle(calendar.get(Calendar.MONTH));
        int j;
        for (j = 0; j < calendar.get(Calendar.DAY_OF_WEEK) - 1; j++) {
            System.out.printf("    ", calendar.get(Calendar.DAY_OF_MONTH));
        }
    }
    Printer() {

        year=GetYear();
        calendar.set(year, 0, 1, 0, 0, 0);


        int i = 0;
        for (i = 0; i < 367; i++) {
            if (year != calendar.get(Calendar.YEAR)) {
                return;
            }
            if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                PrintMonth();
            }
            PrintDays();
            if (calendar.get(Calendar.DAY_OF_WEEK) == 7) {
                System.out.println();
            }
            calendar.add(Calendar.DATE, 1);
        }

    }
}
