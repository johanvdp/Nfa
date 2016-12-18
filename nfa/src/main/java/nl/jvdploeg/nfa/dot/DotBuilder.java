package nl.jvdploeg.nfa.dot;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;

import nl.jvdploeg.nfa.Nfa;
import nl.jvdploeg.nfa.NfaFactory;
import nl.jvdploeg.nfa.NfaService;
import nl.jvdploeg.nfa.State;
import nl.jvdploeg.nfa.StateNetwork;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DotBuilder implements AutoCloseable {

    private Writer out;
    private final State entry;
    private final NfaFactory<Nfa<State>> factory;
    private StateNetwork<State> network;

    /**
     * Start building dot representation of the {@link State} network, including {@link Nfa} nodes when provided.
     *
     * @param out
     *            The output.
     * @param entry
     *            The entry of the network.
     * @param factory
     *            The factory that created the network or <code>null</code>.
     *
     */
    public DotBuilder(final Writer out, final State entry, final NfaFactory factory) {
        this.out = out;
        this.entry = entry;
        this.factory = factory;
    }

    @Override
    public void close() {
        if (out != null) {
            try {
                out.close();
            } catch (final IOException e) {
                out = null;
            }
        }
    }

    /**
     * Create dot representation of the {@link State} network, including {@link Nfa} nodes when provided.<br>
     *
     * @throws IOException
     *             On error.
     *
     * @see http://www.graphviz.org
     * @see http://graphviz.org/Documentation/dotguide.pdf
     */
    public void write() throws IOException {

        network = NfaService.getInstance().createStateNetwork();
        network.scan(entry);

        writeDotHeader();

        // show all network nodes
        writeStateNodes();
        writeEntryStateTransition();
        writeStateTransitions();

        // show all nfa nodes if provided
        if (factory != null) {
            writeNfaNodes();
            writeNfaToStateLinks();
        }

        writeDotFooter();
    }

    /**
     * The {@link Nfa} label should be HTML encoded.
     */
    private String getNfaLabel(final Nfa nfa) {
        final String label = nfa.getLabel();
        return StringEscapeUtils.escapeHtml4(label);
    }

    /**
     * The {@link Nfa} name is a sequence number.
     */
    private String getNfaName(final Nfa nfa) {
        return String.format("n%d", factory.getNfas().indexOf(nfa));
    }

    /**
     * The {@link State} name is a sequence number.
     */
    private String getStateName(final State<?> entry) {
        return String.format("s%d", network.getStates().indexOf(entry));
    }

    private void writeDotFooter() throws IOException {
        // omit newline at end (match simple stream reader)
        out.write("}");
    }

    private void writeDotHeader() throws IOException {
        // show simple command-line usage of graphviz package
        out.write("// File in graphviz .dot format\n");
        out.write("// Example, create png file using: dot <filename> -Tpng -o out.png\n");
        // draw digraph diagram named 'nfa'
        out.write("digraph nfa {\n");
        // order nodes from left to right
        out.write("rankdir=LR;\n");
    }

    /**
     * Write transition to the entry state.
     */
    private void writeEntryStateTransition() throws IOException {
        // start with an invisible node
        out.write("_start_ [style=invis];\n");
        // point to the entry state
        out.write("_start_ -> " + getStateName(entry) + "\n");
    }

    /**
     * Write node definition for all {@link Nfa}s.
     */
    private void writeNfaNodes() throws IOException {
        out.write("{\n");
        // arrange all Nfa nodes on the same rank
        out.write("rank = same;\n");
        for (final Nfa nfa : factory.getNfas()) {
            out.write(getNfaName(nfa) + " [label=\"" + getNfaLabel(nfa) + "\"][shape = box;];\n");
        }
        out.write("};\n");
    }

    /**
     * Write all {@link Nfa} to entry and exit state links.
     */
    private void writeNfaToStateLinks() throws IOException {
        // show nfa node to state links
        for (final Nfa<State> nfa : factory.getNfas()) {
            out.write(getNfaName(nfa) + " -> " + getStateName(nfa.getEntry()) + " [label=\"entry\"][style=dotted];\n");
            out.write(getNfaName(nfa) + " -> " + getStateName(nfa.getExit()) + " [label=\"exit\"][style=dotted];\n");
        }
    }

    /**
     * Write empty transitions for one state.
     */
    private void writeStateAnyTokenTransitions(final State state) throws IOException {
        final List<State> anyTokenTransitions = state.getAnyTokenTransitions();
        for (final State nextState : anyTokenTransitions) {
            out.write(getStateName(state) + " -> " + getStateName(nextState) + " [label=\"(any)\"];\n");
        }
    }

    /**
     * Write empty transitions for one state.
     */
    private void writeStateEmptyTransitions(final State state) throws IOException {
        final List<State> emptyTransitions = state.getEmptyTransitions();
        for (final State nextState : emptyTransitions) {
            out.write(getStateName(state) + " -> " + getStateName(nextState) + ";\n");
        }
    }

    /**
     * Write node definition for one state.
     */
    private void writeStateNode(final State state) throws IOException {
        // decorate node with double line if it is an end state
        final String endDecorator = state.isEnd() ? " [peripheries=2]" : "";
        out.write(getStateName(state) + endDecorator + ";\n");
    }

    /**
     * Write node definition for all states.
     */
    private void writeStateNodes() throws IOException {
        for (final State state : network.getStates()) {
            writeStateNode(state);
        }
    }

    /**
     * Write token transitions for one state.
     */
    private void writeStateTokenTransitions(final State state) throws IOException {
        final Map<String, List<State>> tokenTransitions = state.getTokenTransitions();
        for (final Entry<String, List<State>> transitions : tokenTransitions.entrySet()) {
            final String token = transitions.getKey();
            final List<State> nextStates = transitions.getValue();
            for (final State nextState : nextStates) {
                out.write(getStateName(state) + " -> " + getStateName(nextState) + " [label=\"" + token + "\"];\n");
            }
        }
    }

    /**
     * Write transitions for all states.
     */
    private void writeStateTransitions() throws IOException {
        for (final State state : network.getStates()) {
            writeStateTokenTransitions(state);
            writeStateEmptyTransitions(state);
            writeStateAnyTokenTransitions(state);
        }
    }
}
