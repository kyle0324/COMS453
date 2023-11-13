import java.util.Scanner;

public class RSA {

    static String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z" };

    /**
     * Method to compute private exponent d, given public exp e, and primes p and q
     * 
     * @param e
     * @param p
     * @param q
     * @return private exponent d
     */
    private static int rsa_compute_d(int e, int p, int q) {
        int n = (p - 1) * (q - 1);
        int d = 0;
        int newd = 1;
        int r = n;
        int newr = e;

        while (newr != 0) {
            int quotient = r / newr;
            d = newd;
            newd = d - (quotient * newd);
            r = newr;
            newr = r - (quotient * newr);
        }

        if (r > 1) {
            return -1;
        }
        if (d < 0) {
            d += n;
        }
        return d;
    }

    /**
     * RSA Encryption algorithm given a message, N, and e
     * 
     * @param m
     * @param n
     * @param e
     * @return ciphertext
     */
    private static String rsa_Encrypt(String m, int n, int e) {
        // Ciphertext
        String c = "";
        // Plaintext x (number), Ciphertext x (number)
        int px, cx;
        Scanner scnr = new Scanner(m);

        // while message has more bytes to encrypt
        while (scnr.hasNextInt()) {
            px = scnr.nextInt();
            // Encrypt
            cx = (int) (Math.pow(px, e) % n);
            // Add to ciphertext, with or without space if is last byte
            if (scnr.hasNextInt()) {
                c += cx + " ";
            } else {
                c += cx;
            }
        }
        scnr.close();
        return c;
    }

    /**
     * RSA Decryption algorithm given a ciphertext, N, and d
     * 
     * @param c
     * @param n
     * @param d
     * @return plaintext
     */
    private static String rsa_Decrypt(String c, int n, int d) {
        // Plaintext
        String m = "";
        // Plaintext x (number), Ciphertext x (number)
        int px, cx;
        Scanner scnr = new Scanner(c);

        // while message has more bytes to decrypt
        while (scnr.hasNextInt()) {
            cx = scnr.nextInt();
            // Decrypt
            px = (int) (Math.pow(cx, d) % n);
            // Add to plaintext, with or without space if is last byte
            if (scnr.hasNextInt()) {
                m += px + " ";
            } else {
                m += px;
            }
        }
        scnr.close();
        return m;
    }

    /**
     * Method for translating the encoding of A=1, B=2, back to letters
     * 
     * @param input
     * @return English letters of the message
     */
    private static String translate(String input) {
        String output = "";
        int ix;
        Scanner scnr = new Scanner(input);

        while (scnr.hasNextInt()) {
            ix = scnr.nextInt();
            output += alphabet[(ix % 26) - 1];
        }

        scnr.close();
        return output;
    }

    public static void main(String[] args) throws Exception {
        // Problem 1 Variables
        int n = 33;
        // Guessing these would have to be 3 and 11
        int p = 3;
        int q = 11;
        int e = 3;
        int d;
        String cryptoGram = "14 17 3 28 27 24 16 4 14 9 13 24 1 19 23 1 28 26 5 27 24 16 4 14 26 31 23 3 14 17 14 17 26 24 28 1 4 24 3 19 3 14 3 22 26";

        d = rsa_compute_d(e, p, q);
        System.out.println("Value computed for d: " + d);

        String plaintext = rsa_Decrypt(cryptoGram, n, d);
        System.out.println("String decrypted" + plaintext);

        String message = translate(plaintext);
        System.out.println("Message after translation:\n");
        System.out.println(message);
    }
}
