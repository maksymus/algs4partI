import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int SEQUENCE_LENGTH = 256; // first 256 chars

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] alphabet = new char[SEQUENCE_LENGTH];

        for (int i = 0; i < SEQUENCE_LENGTH; i++)
            alphabet[i] = (char) i;

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int pos = indexOf(alphabet, c);
            BinaryStdOut.write(pos, 8);

            if (pos > 0) {
                System.arraycopy(alphabet, 0, alphabet, 1, pos);
                alphabet[0] = c;
            }
        }

        BinaryStdOut.close();
    }

    private static int indexOf(char[] alphabet, char found) {
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == found) {
                return i;
            }
        }
        return -1;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] alphabet = new char[SEQUENCE_LENGTH];

        for (char i = 0; i < SEQUENCE_LENGTH; i++)
            alphabet[i] = i;

        while (!BinaryStdIn.isEmpty()) {
            int pos = BinaryStdIn.readChar();
            char c = alphabet[pos];
            BinaryStdOut.write(c, 8);

            if (pos > 0) {
                System.arraycopy(alphabet, 0, alphabet, 1, pos);
                alphabet[0] = c;
            }
        }

        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length != 1) return;

        String command = args[0];
        if ("-".equals(command)) {
            encode();
        } else if ("+".equals(command)) {
            decode();
        }
    }
}