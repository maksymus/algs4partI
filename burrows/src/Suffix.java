public class Suffix implements Comparable<Suffix> {
    private String string;
    public int idx;

    public Suffix(String string, int idx) {
        this.string = string;
        this.idx = idx;
    }

    public char charAt(int i) {
        return string.charAt((i + idx) % string.length());
    }

    public boolean less(Suffix other, int d) {
        for (int i = d; i < string.length(); i++) {
            int c1 = this.charAt(i);
            int c2 = other.charAt(i);
            if (c1 < c2) {
                return true;
            } else if (c2 < c1) {
                return false;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Suffix suffix) {
        for (int i = 0; i < string.length(); i++) {
            char thisChar = this.charAt(i);
            char otherChar = suffix.charAt(i);

            int compare = Character.compare(thisChar, otherChar);
            if (compare != 0)
                return compare;
        }

        return 0;
    }
}