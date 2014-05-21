package quickdt.predictiveModels.featureEngineering;

import quickdt.data.AbstractInstance;

/**
 * Created by ian on 5/21/14.
 */
public interface AttributesEnricherBuildStrategy {
    public AttributesEnricher build(Iterable<? extends AbstractInstance> trainingData);
}
