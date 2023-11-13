import java.util.Scanner;
import java.math.BigInteger;

public class RSA {

    // Static array of alphabetic characters for Problem 1
    static String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z" };

    /**
     * GCD function given two long values, input order does not matter
     * 
     * @param a
     * @param b
     * @return - the GCD of the two numbers
     */
    public static long GCD(long a, long b) {
        long x = Math.max(a, b);
        long y = Math.min(a, b);
        while (y != 0) {
            long z = x % y;
            x = y;
            y = z;
        }
        return x;
    }

    /**
     * Method to compute private exponent d, given public exp e, and primes p and q
     * Is correct, but can also use
     * https://www.wolframalpha.com/widgets/view.jsp?id=d5bb63088eb2fb2e762f1c260d2b886d
     * to verify.
     * Will struggle with large numbers as not formatted for those
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
            int tmp = newd;
            newd = d - (quotient * newd);
            d = tmp;
            tmp = newr;
            newr = r - (quotient * newr);
            r = tmp;
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
     * Has issues with long/BigIntegers
     * 
     * @param m
     * @param n
     * @param e
     * @return ciphertext
     */
    private static String rsa_Encrypt(String m, long n, long e) {
        // Ciphertext
        String c = "";
        // Plaintext x (number), Ciphertext x (number)
        long px, cx;
        Scanner scnr = new Scanner(m);

        // while message has more bytes to encrypt
        while (scnr.hasNextLong()) {
            px = scnr.nextLong();
            // Encrypt
            long tmp = (long) Math.pow(px, e);
            // System.out.println("Value of tmp: " + tmp);
            cx = tmp % n;
            // System.out.println("Value of cx: " + px);
            // cx = (long) (Math.pow(px, e) % n);
            // Add to ciphertext, with or without space if is last byte
            if (scnr.hasNextLong()) {
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
     * Has issues with long/BigIntegers
     * 
     * @param c
     * @param n
     * @param d
     * @return plaintext
     */
    private static String rsa_Decrypt(String c, long n, long d) {
        // Plaintext
        String m = "";
        // Plaintext x (number), Ciphertext x (number)
        long px, cx;
        Scanner scnr = new Scanner(c);

        // while message has more bytes to decrypt
        while (scnr.hasNextLong()) {
            cx = scnr.nextLong();
            // Decrypt
            long tmp = (long) Math.pow(cx, d);
            // System.out.println("Value of tmp: " + tmp);
            px = tmp % n;
            // System.out.println("Value of px: " + px);
            // px = (long) (Math.pow(cx, d) % n);
            // Add to plaintext, with or without space if is last byte
            if (scnr.hasNextLong()) {
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
            ix = (ix - 1) % alphabet.length;
            // System.out.println("IX Value: " + ix);
            output += alphabet[ix];
        }

        scnr.close();
        return output;
    }

    /**
     * Function to verify e is relatively prime to phi. Calls GCD functions, and
     * returns true if GCD is 1, false otherwise.
     * 
     * @param e
     * @param phi
     * @return - true if GCD is 1, false otherwise
     */
    private static boolean verify_e(int e, int phi) {
        long x = GCD(e, phi);
        return x == 1;
    }

    public static void main(String[] args) throws Exception {
        // Problem 1 Variables
        // This all works, just uncomment it if want to run it/check. But 
        // int n = 33;
        // // Guessing these would have to be 3 and 11
        // int p = 3;
        // int q = 11;
        // int e = 3;
        // int d;
        // String cryptoGram = "14 17 3 28 27 24 16 4 14 9 13 24 1 19 23 1 28 26 5 27 24
        // 16 4 14 26 31 23 3 14 17 14 17 26 24 28 1 4 24 3 19 3 14 3 22 26";

        // System.out.println();
        // d = rsa_compute_d(e, p, q);
        // System.out.println("Value computed for d: " + d);

        // String plaintext = rsa_Decrypt(cryptoGram, n, d);
        // System.out.println("String decrypted " + plaintext);

        // String message = translate(plaintext);
        // System.out.println("Message after translation:\n");
        // System.out.println(message);
        // System.out.println();
        // ****************************************************

        // Problem 2 Variables *****************************************
        // *******************************************
        // OLD STUFF THAT DOES NOT WORK
        // int p = 9497;
        // int q = 7187;
        // int e = 3;
        // int n = p * q;
        // int phi = (p - 1) * (q - 1);
        // int d;
        // String ptext = "12345678";
        // String ctext;
        // String c2text = "12345679";
        // String p2text;

        // System.out.println();
        // System.out.println("Verification of e: " + verify_e(e, phi));
        // System.out.println();

        // d = rsa_compute_d(e, p, q);
        // System.out.println("Value of d: " + d + "\n");

        // ctext = rsa_Encrypt(ptext, n, e);
        // System.out.println("Ciphertext of P: \t" + ctext + "\n");

        // System.out.println(rsa_Decrypt(ctext, n, d));

        // HAD ISSUES WITH LONGS AND NOT CORRECT MOD/EXP, SO FOUND THIS LIBRARY
        BigInteger p2 = new BigInteger("9497");
        BigInteger q2 = new BigInteger("7187");
        BigInteger e2 = new BigInteger("3");
        BigInteger n2 = new BigInteger("68254939");
        BigInteger phi2 = new BigInteger("68238256");
        BigInteger pText2 = new BigInteger("12345678");
        BigInteger c2Text2 = new BigInteger("12345679");
        // BigInteger d = new BigInteger("45492171");

        // Check relatively prime
        BigInteger gcd = e2.gcd(phi2);
        if (gcd.compareTo(new BigInteger("1")) == 0) {
            System.out.println("Yep");
        }
        // Calc d using modInverse. I have a function above that is correct too.
        // Otherwise can use
        // https://www.wolframalpha.com/widgets/view.jsp?id=d5bb63088eb2fb2e762f1c260d2b886d
        BigInteger d2 = e2.modInverse(phi2);

        // Encrypt P using modPow
        BigInteger cText2 = pText2.modPow(e2, n2);
        System.out.println(cText2);

        // Decrypt C back to original P
        BigInteger ptText2 = cText2.modPow(d2, n2);
        System.out.println(ptText2);

        // Decrypt C'
        BigInteger p2Text2 = c2Text2.modPow(d2, n2);
        System.out.println(p2Text2);

        // Encrypt P' back to original C'
        BigInteger ctText2 = p2Text2.modPow(e2, n2);
        System.out.println(ctText2);
    }
}
