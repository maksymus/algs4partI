public class Quick3SuffixesInts {
    private static final int CUTOFF =  15;   // cutoff to insertion sort

    // sort the array indices[] of suffix indices for string
    public static void sort(String a, int[] inds) {
        sort(a, inds, 0, inds.length-1, 0);
    }

    // return the dth character of suffix starting at s[sufIdx],
    // -1 if d = length of s
    private static int charAt(String s, int sufIdx, int d) {
        assert d >= 0 && d <= s.length();
        int idx = sufIdx + d;
        if (idx >= s.length()) {
            idx = idx - s.length();
        }
        if (d == s.length()) return -1;
        return s.charAt(idx);
    }


    // 3-way string quicksort inds[lo..hi] starting at dth character
    private static void sort(String a, int[] inds, int lo, int hi, int d) {
        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, inds, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = charAt(a, inds[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(a, inds[i], d);
            if      (t < v) exch(inds, lt++, i++);
            else if (t > v) exch(inds, i, gt--);
            else              i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(a, inds, lo, lt-1, d);
        if (v >= 0) sort(a, inds, lt, gt, d+1);
        sort(a, inds, gt+1, hi, d);
    }



    // sort from a[lo] to a[hi], starting at the dth character
    private static void insertion(String a, int[] inds, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a, inds[j], inds[j-1], d); j--)
                exch(inds, j, j-1);
    }

    // exchange a[i] and a[j]
    private static void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // compare two suffixes, starting at character d
    private static boolean less(String a, int sufInd1, int sufInd2, int d) {
        for (int i = d; i < a.length(); i++) {
            int c1 = charAt(a, sufInd1, i);
            int c2 = charAt(a, sufInd2, i);
            if (c1 < c2) {
                return true;
            } else if (c2 < c1) {
                return false;
            }
        }
        return false;
    }
}