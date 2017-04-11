package entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrii on 28.02.17.
 */
public class Statistic {

    private String inputText;
    private String outputText;
    private Map<String, Integer> oneSymbolFrequency;
    private Map<String, Integer> twoSymbolsFrequency;
    private Map<String, Integer> threeSymbolFrequency;
    private String alphabet;

    public static Statistic calculateStatistic(String text){
        Statistic statistic = new Statistic();

        statistic.setOneSymbolFrequency(findFrequency(text,1));
        statistic.setTwoSymbolsFrequency(findFrequency(text, 2));
        statistic.setThreeSymbolFrequency(findFrequency(text, 3));

        StringBuilder alphabet = new StringBuilder();

        statistic.getOneSymbolFrequency().keySet().forEach(alphabet::append);
        statistic.setAlphabet(alphabet.toString());

        return statistic;
    }

    public static Map<String, Integer> findFrequency(String text, int numberOfSymbols){
        char[] letters = text.toCharArray();
        Map<String, Integer> symbolsFrequency = new HashMap<>();
        String symbols;
        int length = letters.length - (numberOfSymbols - 1);

        for (int i = 0; i < length; i++){
            symbols = text.substring(i, i+numberOfSymbols);
            if (symbolsFrequency.containsKey(symbols)){
                int oldValue = symbolsFrequency.get(symbols).intValue();
                int newValue = ++oldValue;
                symbolsFrequency.replace(symbols, newValue);
            }else {
                symbolsFrequency.put(symbols, 1);
            }
        }
        symbolsFrequency = Utils.deleteStringWithNewLine(symbolsFrequency);
        symbolsFrequency = Utils.deleteStringWithWhitespace(symbolsFrequency);

        return symbolsFrequency;
    }

    public void sortByKey(){
        this.setOneSymbolFrequency(Utils.sortByKeyDsc(this.getOneSymbolFrequency()));
        this.setTwoSymbolsFrequency(Utils.sortByKeyDsc(this.getTwoSymbolsFrequency()));
        this.setThreeSymbolFrequency(Utils.sortByKeyDsc(this.getThreeSymbolFrequency()));
    }

    public void sortByValue(){
        this.setOneSymbolFrequency(Utils.sortByValueDsc(this.getOneSymbolFrequency()));
        this.setTwoSymbolsFrequency(Utils.sortByValueDsc(this.getTwoSymbolsFrequency()));
        this.setThreeSymbolFrequency(Utils.sortByValueDsc(this.getThreeSymbolFrequency()));
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public Map<String, Integer> getOneSymbolFrequency() {
        return oneSymbolFrequency;
    }

    public void setOneSymbolFrequency(Map<String, Integer> oneSymbolFrequency) {
        this.oneSymbolFrequency = oneSymbolFrequency;
    }

    public Map<String, Integer> getTwoSymbolsFrequency() {
        return twoSymbolsFrequency;
    }

    public void setTwoSymbolsFrequency(Map<String, Integer> twoSymbolsFrequency) {
        this.twoSymbolsFrequency = twoSymbolsFrequency;
    }

    public Map<String, Integer> getThreeSymbolFrequency() {
        return threeSymbolFrequency;
    }

    public void setThreeSymbolFrequency(Map<String, Integer> threeSymbolFrequency) {
        this.threeSymbolFrequency = threeSymbolFrequency;
    }
}
