import java.math.BigDecimal;
import java.util.*;

class Pairs {
    double low;
    double probability;
    char letter;

    Pairs(double low, double probability, char letter) {
        this.low = low;
        this.probability = probability;
        this.letter = letter;
    }

    public double getHigh() {
        return new BigDecimal(Double.toString(probability)).add(new BigDecimal(Double.toString(low))).doubleValue();
    }

    public double getLow() {
        return low;
    }
}

public class ArithmeticCoding {
    static double low = 0.0;
    static double high = 1;
    static int counter = 0;
    static ArrayList<Pairs> pairs = new ArrayList<>();
    static LinkedHashMap<Character, Double> tableProbability = new LinkedHashMap<>();

    private static void encoding(double high_bukva, double low_bukva) {
        double range = calcSub(high, low);
        high = calcSum(low, calcMult(range, high_bukva));
        low = calcSum(low, calcMult(range, low_bukva));
    }

    private static double calcSum(double a1, double a2) {
        return new BigDecimal(Double.toString(a1)).add(new BigDecimal(Double.toString(a2))).doubleValue();
    }

    private static double calcSub(double a1, double a2) {
        return new BigDecimal(Double.toString(a1)).subtract(new BigDecimal(Double.toString(a2))).doubleValue();
    }

    private static double calcMult(double a1, double a2) {
        return new BigDecimal(Double.toString(a1)).multiply(new BigDecimal(Double.toString(a2))).doubleValue();
    }

    private static double[] calculatedProbability(int charSetLen, Double[] probability) {
        double[] comProb = new double[charSetLen + 1];
        comProb[0] = 0;

        for (int i = 1; i <= charSetLen; i++) {
            comProb[i] = probability[i - 1];
        }

        for (int i = 1; i <= charSetLen; i++) {
            comProb[i] += comProb[i - 1];
        }

        return comProb;
    }

    private static LinkedHashMap<Character, Double> sortMap(LinkedHashMap<Character, Double> tableProbability) {
        LinkedHashMap<Character, Double> sorted = new LinkedHashMap<>();
        tableProbability.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));

        return sorted;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your string for encoding: ");
        String input = scanner.nextLine();
        char[] letter = input.toCharArray();

        System.out.println("Enter probability for each letter: ");
        Double[] probability = new Double[letter.length];
        for (int i = 0; i < letter.length; i++) {
            System.out.println(letter[i]);
            probability[i] = scanner.nextDouble();
        }
        scanner.close();

        double n = encode(letter, letter.length, probability, input);
        double[] comProb = calculatedProbability(probability.length, probability);

        System.out.println("Encoded: " + n);
        System.out.println("Decoded: " + decode(letter, letter.length, comProb, n, input.length()));

        for (int i = 0; i < letter.length; i++) {
            tableProbability.put(letter[i], probability[i]);
        }

        LinkedHashMap<Character, Double> sorted = sortMap(tableProbability);

        for (Map.Entry<Character, Double> set : sorted.entrySet()) {
            pairs.add(new Pairs(low, set.getValue(), set.getKey()));
            low = pairs.get(counter).getHigh();
            counter++;
        }

        high = 1;
        low = 0;

        List<Character> sortedKeys = new ArrayList<>(sorted.keySet());

        for (char c : letter) {
            for (int j = 0; j < letter.length; j++) {
                if (c == sortedKeys.get(j).toString().charAt(0)) {
                    encoding(pairs.get(j).getHigh(), pairs.get(j).getLow());
                    break;
                }
            }
        }

        System.out.println("Code word interval: " + low + " " + high);
    }

    public static double encode(char[] charSet, int charSetLen, Double[] probability, String str) {
        TreeMap<Character, Integer> index = new TreeMap<>();
        double lNew, l = 0, uNew, u = 1;

        for (int i = 0; i < charSetLen; i++) {
            index.put(charSet[i], i + 1);
        }

        double[] comProb = calculatedProbability(charSetLen, probability);

        for (int i = 0; i < str.length(); i++) {
            lNew = l + (u - l) * comProb[index.get(str.charAt(i)) - 1];
            uNew = l + (u - l) * comProb[index.get(str.charAt(i))];

            l = lNew;
            u = uNew;
        }
        return ((l + u) / 2);
    }

    public static String decode(char[] charSet, int charSetLen, double[] comProb, double tag, int compLen) {
        TreeMap<Integer, Character> index = new TreeMap<>();

        for (int i = 0; i < charSetLen; i++) {
            index.put(i + 1, charSet[i]);
        }

        double lNew, l = 0, uNew, u = 1;

        StringBuilder decoding = new StringBuilder();

        for (int i = 0; i < compLen; i++) {
            for (int j = 1; j <= charSetLen; ++j) {
                lNew = l + (u - l) * comProb[j - 1];
                uNew = l + (u - l) * comProb[j];
                if (tag >= lNew && tag <= uNew) {
                    decoding.append(index.get(j));

                    l = lNew;
                    u = uNew;

                    break;
                }
            }
        }

        return decoding.toString();
    }
}