public class CircularSuffixArray {
    private String string;
    private Suffix[] refs;
//    private int[] indices;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new NullPointerException();

        this.string = s;
        this.refs = new Suffix[s.length()];
        for (int i = 0; i < s.length(); i++)
            refs[i] = new Suffix(s, i);

        Quick3Suffixes.sort(string, refs);

//        indices = new int[s.length()];
//        // Construct original array
//        for (int i = 0; i < indices.length; i++) {
//            indices[i] = i;
//        }
        // Sort suffixes
//        Quick3SuffixesInts.sort(s, indices);
    }

    // length of s
    public int length() {
        return string.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > string.length())
            throw new IndexOutOfBoundsException();

        return refs[i].idx;
//        return indices[i];
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {



        System.out.println(new CircularSuffixArray("AAAAAAAAAAAAAAAAA").index(0));
//        System.out.println();

//        int[] ints = {11, 10, 7, 0, 3, 5, 8, 1, 4, 6, 9, 2};
//        for (int i = 0; i < 12; i++) {
//            int res = new CircularSuffixArray("ABRACADABRA!").index(i);
//            System.out.print(res + " ");
//            if (res != ints[i])
//                System.out.printf("Wrong number at pos %d [expected: %d observed: %d]\n", i, ints[i], res);
//        }
    }
}