package aima.core.probability.bayes.impl;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.ConditionalProbabilityDistribution;
import aima.core.probability.bayes.ConditionalProbabilityTable;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.Node;

import java.util.List;

/**
 * Default implementation of the FiniteNode interface that uses a fully
 * specified Conditional Probability Table to represent the Node's conditional
 * distribution.
 *
 * @author Ciaran O'Reilly
 */
public class FullCPTNode extends AbstractNode implements FiniteNode {
    private ConditionalProbabilityTable cpt = null;

    public FullCPTNode(RandomVariable var, double[] distribution) {
        this(var, distribution, (Node[]) null);
    }

    public FullCPTNode(RandomVariable var, double[] values, Node... parents) {
        super(var, parents);

        RandomVariable[] conditionedOn = new RandomVariable[getParents().size()];
        int i = 0;
        for (Node p : getParents()) {
            conditionedOn[i++] = p.getRandomVariable();
        }

        cpt = new CPT(var, values, conditionedOn);
    }

    //
    // START-Node
    @Override
    public ConditionalProbabilityDistribution getCPD() {
        return getCPT();
    }

    // END-Node
    //

    //
    // START-FiniteNode

    @Override
    public ConditionalProbabilityTable getCPT() {
        return cpt;
    }

    public void selectDomain(List<Node> interestedParents, Object... domain) {
        CategoricalDistribution conditioningCase = cpt.getConditioningCase(domain);
        double[] values = conditioningCase.getValues();
        RandomVariable[] vars = this.getParents().stream().filter(p -> !interestedParents.contains(p))
                .map(Node::getRandomVariable)
                .toArray(RandomVariable[]::new);
        this.cpt = new CPT(this.getRandomVariable(), values, vars);
    }

    // END-FiniteNode
    //
}
