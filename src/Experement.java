import controllers.StatisticController;
import entity.Statistic;
import entity.Utils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by andrii on 28.02.17.
 */
public class Experement {


    public static void main(String[] args) throws FileNotFoundException {
//        System.out.println("String");
//        System.out.println("SubString(1,2): " + "String".substring(1,3));
//        System.out.println("CharAt(1): " + "String".charAt(1));
//        int a = 7;
//        int      b = 4;
//        System.out.println(-a % b);
//
//        Integer in = new Integer("1");
        Scanner scannerInt = new Scanner(System.in);
        Scanner scannerString = new Scanner(System.in);
        int choise;
        System.out.println("***Lab 3 helper program***");

//        String text = Utils.loadTextFromFile("/media/andrii/Новый том/Навчання/Курс 3/Семестр 2/Lab1_4/src/resources/Z70");
        String text = "VMG 'CC DZ'D XTMCMO1T'C UXK'EUI 'EU Z1IDMG1T 'EU TYCDYG'C KXKMG1'C GXIXGF'D1MEW \n" +
                "LZ1CX IMCF1EO T1D-SBY1CU1EO D'IAIQ MGO'E1I'D1ME 'EU MJD1K1I'D1ME MV DZX DG'VV1T \n" +
                " DG'EIJMGD VCMLI 'EU FXZ1TCXIQ BY1CU1EO 'EU KMUXGE1I'D1ME MV GM'UI 'EU DG'EIJMGD\n" +
                " 1EVG'IDGYTDYGX 'GX DM BX TMEI1UXGXUP IJXT1V1XU 'JJGM'TZ TMKJCXN1D- GXHY1GXI UXF\n" +
                "XCMJKXED 'EU 1KJC1T'D1ME MV DZX EXLW KMGX TMKJCXN KMUXCI MV JDI 'EU 1DRI TMKJMEX\n" +
                "EDI B'IXU ME DZX YI'OX MV TMKJYDXGI 'EU KMUXGE 1EVMGK'D1ME DXTZEMCMO1XIP DZGXX C\n" +
                "XFXCI MV JDI TMKJYDXGS'1UXU UXI1OE TMYCU BX JM1EDXU MYDW LZ1TZ 'GX U1VVXGXED1'DX\n" +
                "U B- C1ID 'EU TZ'G'TDXG MV IMCF1EO D'IAIP 1E MYG MJ1E1ME I-IDXK 'EU J'GD1TYC'GC-";
        Statistic statistic = Statistic.calculateStatistic(text);
        statistic.sortByValue();
//        text = text.replace(',', ' ');

        while (true) {
            System.out.println("Enter action: ");
            System.out.println("1. Print text.");
            System.out.println("2. Change symbol in text.");
            System.out.println("3. Show statistic for unchanged text.");
            System.out.println("4. Print words in text.");
            System.out.println("0. Exit");

            switch (choise = scannerInt.nextInt()) {
                case 0: {
                    System.exit(0);
                    break;
                }
                case 1: {
                    System.out.println(text);
                    break;
                }
                case 2: {
                    System.out.println("Enter to change:");
                    String oldSymb = scannerString.nextLine();
                    System.out.println("New symbol: ");
                    String newSymb = scannerString.nextLine();
                    text = text.replace(oldSymb.charAt(0),newSymb.charAt(0));
                    break;
                }
                case 3: {
                    System.out.println("1. One symbol.");
                    System.out.println("2. Two symbols.");
                    System.out.println("3. Three symbols.");

                    choise = scannerInt.nextInt();
                    if (choise == 1) {
                        System.out.println(Utils.mapToString(statistic.getOneSymbolFrequency()));
                    }
                    if (choise == 2) {
                        System.out.println(Utils.mapToString(statistic.getTwoSymbolsFrequency()));
                    }
                    if (choise == 3) {
                        System.out.println(Utils.mapToString(statistic.getThreeSymbolFrequency()));
                    }
                    break;
                }
                case 4:{
                    String[] words = text.split(" ");
                    Map<String, Integer> freq = new HashMap<>();
                    for (String word : words){
                        if (freq.containsKey(word)){
                            int oldFreq = freq.get(word);
                            int newFreq = ++oldFreq;
                            freq.replace(word, newFreq);
                        }else {
                            freq.put(word, 1);
                        }
                    }

                    freq = Utils.sortByValueAsc(freq);
                    freq.entrySet().forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
                }
            }
        }

    }
}
