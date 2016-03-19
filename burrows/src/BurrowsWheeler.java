import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int SEQUENCE_LENGTH = 256;

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(s);

        int first = 0;

        for (int i = 0; i < s.length(); i++) {
            if (suffixArray.index(i) == 0) {
                first = i;
                break;
            }
        }

        BinaryStdOut.write(first);

        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(s.charAt((suffixArray.index(i) + s.length() - 1) % s.length()));
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();

        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        char[] t = s.toCharArray();

        int []baseIndex = new int[SEQUENCE_LENGTH];

        sort(t);

        int[] next = new int[input.length];

        for (int i = 0; i < input.length; i++)
            next[i] = getNextForChar(t[i], input, baseIndex);

        for (int i = 0, ptr = first; i < next.length; i++, ptr = next[ptr])
            BinaryStdOut.write(t[ptr], 8);

        BinaryStdOut.close();
    }

    private static int getNextForChar(char c, char[] input, int[] baseIndex) {
        for (int i = baseIndex[c]; i < input.length; i++) {
            if (input[i] == c) {
                baseIndex[c] = i + 1;
                return i;
            }
        }

        assert false;
        return 0;
    }

    private static void sort(char[] chars) {
        int N = chars.length;
        char aux[] = new char[chars.length];

        int[] count = new int[SEQUENCE_LENGTH + 1];

        for (int i = 0; i < N; i++)
            count[chars[i]+1]++;

        for (int r = 0; r < SEQUENCE_LENGTH; r++)
            count[r+1] += count[r];

        for (int i = 0; i < N; i++)
            aux[count[chars[i]]++] = chars[i];

        for (int i = 0; i < N; i++)
            chars[i] = aux[i];
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
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
