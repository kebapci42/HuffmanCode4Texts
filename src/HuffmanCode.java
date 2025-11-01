import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HuffmanCode {
    private String file;
    private ArrayList<String> tokens;
    private HashMap<Character, Integer> frequencyMap;

    public HuffmanCode(String file) {
        this.file = file;
    }

    public HashMap<Character, Integer> getFrequencyMap() {
        return frequencyMap;
    }

    public void splitTokens() {
        tokens = new ArrayList<>();

        try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = bf.readLine()) != null) {
                // Match words or punctuation
                for (String token : line.split("(?<=\\p{Punct})|(?=\\p{Punct})|\\s+")) {
                    if (!token.isEmpty()) { // Avoid adding empty strings
                        tokens.add(token);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void computeHuffmanCodes() {
        frequencyMap = new HashMap<>();
        tokens.forEach(token -> {
            for (char letter : token.toCharArray()) {
                frequencyMap.put(letter, frequencyMap.getOrDefault(letter, 0) + 1);
            }
        });
    }

    public void decryptFile(String encryptedFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(encryptedFile))) {
            StringBuilder decrypted = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                for (String frequency : line.split("\\s+")) {
                    Character token = findToken(Integer.parseInt(frequency));
                    if (token != null) {
                        decrypted.append(token);
                    }
                }
            }
            writeFile(false, decrypted.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Character findToken(Integer value) {
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void encryptFile() {
        StringBuilder encrypted = new StringBuilder();
        tokens.forEach(token -> {
            for (char letter : token.toCharArray()) {
                encrypted.append(frequencyMap.get(letter)).append(" ");
            }
        });
        encrypted.setLength(encrypted.length() - 1);
        writeFile(true, encrypted.toString());
    }

    private void writeFile(boolean isEncrypting, String content) {
        String newFile = ((isEncrypting) ? "Encrypted-" : "Decrypted-") + file;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(newFile))) {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
