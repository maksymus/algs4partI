import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {

    private static final class Synset {
        public Synset(int id, List<String> nouns) {
            this.id = id;
            this.nouns = nouns;
        }

        private int id;
        private List<String> nouns = new ArrayList<String>();

        public int getId() {
            return id;
        }

        public List<String> getNouns() {
            return new ArrayList<String>(nouns);
        }
    }

    private SAP sap;

    private SortedSet<String> nounSet = new TreeSet<String>();
    private Map<String, List<Integer>> nounIndex = new HashMap<String, List<Integer>>();
    private Map<Integer, String> synsetIndex = new HashMap<Integer, String>();

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new NullPointerException("Input is null");

        int vertexCount = populateSynsets(synsets);
        Digraph wordNet = populateWordNet(hypernyms, vertexCount);
        sap = new SAP(wordNet);
    }

    private Digraph populateWordNet(String hypernyms, int vertexCount) {
        Digraph wordNet = new Digraph(vertexCount);

        In hypernymsIn = new In(hypernyms);
        if (!hypernymsIn.exists())
            throw new IllegalArgumentException("File " + hypernyms + " not exist");

        for (String line = hypernymsIn.readLine(); line != null; line = hypernymsIn.readLine()) {
            String[] split = line.split(",");
            String id = split[0];
            for (int i = 1; i < split.length; i++) {
                wordNet.addEdge(Integer.parseInt(id), Integer.parseInt(split[i]));
            }
        }

        if (wordNet.V() > 2) {
            DirectedCycle finder = new DirectedCycle(wordNet);
            if (finder.hasCycle())
                throw new IllegalArgumentException("Has cycles");

            int roots = 0;
            for (int i = 0; i < wordNet.V(); i++) {
                if (!wordNet.adj(i).iterator().hasNext()) {
                    roots++;
                }
                if (roots > 1)
                    throw new IllegalArgumentException("More than one root");
            }
        }

        return wordNet;
    }

    private int populateSynsets(String synsets) {
        In synsetsIn = new In(synsets);
        if (!synsetsIn.exists())
            throw new IllegalArgumentException("File " + synsets + " not exist");

        int vertexCount = 0;
        for (String line = synsetsIn.readLine(); line != null; line = synsetsIn.readLine()) {
            String[] split = line.split(",");
            int id = Integer.parseInt(split[0]);
            String[] nouns = split[1].split(" ");

            vertexCount++;
            nounSet.addAll(Arrays.asList(nouns));
            synsetIndex.put(id, split[1]);

            for (String noun: nouns) {
                List<Integer> idx = nounIndex.get(noun);
                if (idx == null) {
                    idx = new ArrayList<Integer>();
                    nounIndex.put(noun, idx);
                }

                idx.add(id);
            }
        }

        return vertexCount;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return new ArrayList<String>(nounSet);
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new NullPointerException();

        return nounSet.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new NullPointerException();

        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        List<Integer> idxA = nounIndex.get(nounA);
        List<Integer> idxB = nounIndex.get(nounB);

        return sap.length(idxA, idxB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new NullPointerException();

        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        List<Integer> idxA = nounIndex.get(nounA);
        List<Integer> idxB = nounIndex.get(nounB);

        return synsetIndex.get(sap.ancestor(idxA, idxB));
    }

    // do unit testing of this class
    public static void main(String[] args) {
//        WordNet wordNet = new WordNet(args[0], args[1]);
//        Iterable<String> nouns = wordNet.nouns();
//        System.out.println(nouns);

        Digraph wordNet1 = new Digraph(82192);

        In hypernymsIn = new In("/home/maksym/Projects/home/algs4partI/wordnet/resources/hypernyms8WrongBFS.txt");
        if (!hypernymsIn.exists())
            throw new IllegalArgumentException("File not exist");

        for (String line = hypernymsIn.readLine(); line != null; line = hypernymsIn.readLine()) {
            String[] split = line.split(",");
            String id = split[0];
            for (int i = 1; i < split.length; i++) {
                wordNet1.addEdge(Integer.parseInt(id), Integer.parseInt(split[i]));
            }
        }

        if (wordNet1.V() > 2) {
            DirectedCycle finder = new DirectedCycle(wordNet1);
            if (finder.hasCycle())
                throw new IllegalArgumentException("Has cycles");

            int roots = 0;
            for (int i = 0; i < wordNet1.V(); i++) {
                if (!wordNet1.adj(i).iterator().hasNext()) {
                    roots++;
                }
                if (roots > 1)
                    throw new IllegalArgumentException("More than one root");
            }
        }
    }
}