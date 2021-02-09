import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;
import java.util.Vector;


public class LZW {
    public static Vector<Integer> encoding(String input, TreeMap<String, Integer> table) {
        System.out.println("Encoding\n");

        int code = table.size();

        String encodedString = "";
        StringBuilder nextInputCharacter = new StringBuilder();

        encodedString += input.charAt(0);

        Vector<Integer> outputCode = new Vector<>();
        System.out.println("String\tOutput_Code\tAddition\n");

        for (int i = 0; i < input.length(); i++) {
            if (i != input.length() - 1) {
                nextInputCharacter.append(input.charAt(i + 1));
            }
            if (table.containsKey(encodedString + nextInputCharacter)) {
                encodedString = encodedString + nextInputCharacter;
            } else {
                System.out.println(encodedString + "\t" + table.get(encodedString) + "\t\t" + (encodedString + nextInputCharacter) + "\t" + code);
                outputCode.add(table.get(encodedString));
                table.put(encodedString + nextInputCharacter, code++);
                encodedString = nextInputCharacter.toString();
            }
            nextInputCharacter = new StringBuilder();
        }
        System.out.println(encodedString + "\t" + table.get(encodedString));
        outputCode.add(table.get(encodedString));
        return outputCode;
    }

    public static void decoding(Vector<Integer> vector, TreeMap<Integer, String> table) {
        System.out.println("\nDecoding\n");

        int old = vector.get(0);
        int nextInputCode;
        String stringCodedFromVector = table.get(old);
        String characterFromString = "";

        characterFromString += stringCodedFromVector.charAt(0);
        System.out.print(stringCodedFromVector);

        int count = table.size();

        for (int i = 0; i < vector.size() - 1; i++) {
            nextInputCode = vector.get(i + 1);
            if (!table.containsKey(nextInputCode)) {
                stringCodedFromVector = table.get(old);
                stringCodedFromVector += characterFromString;
            } else {
                stringCodedFromVector = table.get(nextInputCode);
            }
            System.out.print(stringCodedFromVector);
            characterFromString = "";
            characterFromString += stringCodedFromVector.charAt(0);
            table.put(count++, table.get(old) + characterFromString);
            old = nextInputCode;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Press 1 to encode your string\nPress 2 to decode a code into a string");
        String input = bufferedReader.readLine();

        if (Integer.parseInt(input) == 1) {
            System.out.println("Press 1 to use 0 - 255 ASCII dictionary table\nPress 2 to use your defined dictionary table");
            input = bufferedReader.readLine();

            TreeMap<String, Integer> table = new TreeMap<>();

            if (Integer.parseInt(input) == 1) {
                for (int i = 0; i <= 255; i++) {
                    String character = "";
                    character += (char) i;
                    table.put(character, i);
                }
            } else {
                System.out.println("Enter the values of the table using String followed by empty space then the Integer. In the format \"<String> <Integer>\".\nExample:\nL 1\nZ 2\nW 3\nType \"end\" to finish");
                while (!(input = bufferedReader.readLine()).equals("end")) {
                    String[] entries = input.split(" ");
                    table.put(entries[0], Integer.parseInt(entries[1]));
                }
            }
            System.out.println("Enter your desired string:");
            input = bufferedReader.readLine();
            Vector<Integer> encoded = encoding(input, table);

            System.out.print("\nYour encoded sequence is: ");

            for (Integer integer : encoded) {
                System.out.print(integer + " ");
            }
        } else {
            System.out.println("Press 1 to use 0 - 255 ASCII dictionary table\nPress 2 to use your defined dictionary table");
            input = bufferedReader.readLine();

            TreeMap<Integer, String> table = new TreeMap<>();

            if (Integer.parseInt(input) == 1) {
                for (int i = 0; i <= 255; i++) {
                    String character = "";
                    character += (char) i;
                    table.put(i, character);
                }
            } else {
                System.out.println("Enter the values of the table using String followed by empty space then the Integer. In the format \"<String> <Integer>\".\nExample:\nL 1\nZ 2\nW 3\nType \"end\" to finish");
                while (!(input = bufferedReader.readLine()).equals("end")) {
                    String[] entries = input.split(" ");
                    table.put(Integer.parseInt(entries[1]), entries[0]);
                }
            }

            System.out.println("Enter your desired sequence:");

            input = bufferedReader.readLine();

            Vector<Integer> vector = new Vector<>();
            String[] stringArray = input.split(" ");

            for (String s : stringArray) {
                vector.add(Integer.parseInt(s));
            }

            decoding(vector, table);
        }
    }
}
