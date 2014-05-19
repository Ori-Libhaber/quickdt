package quickdt.experiments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickdt.crossValidation.*;
import quickdt.data.AbstractInstance;
import quickdt.data.Instance;
import quickdt.predictiveModels.decisionTree.TreeBuilder;
import quickdt.predictiveModels.randomForest.RandomForest;
import quickdt.predictiveModels.randomForest.RandomForestBuilder;

import java.util.List;

/**
 * Created by alexanderhawk on 1/16/14.
 */
public class OutOfTimeCrossValidatorRunner {
    private static final Logger logger =  LoggerFactory.getLogger(StationaryCrossValidator.class);


    public static void main(String[] args) {
        int numTraniningExamples = 40100;
        String bidRequestAttributes[] = {"seller_id", "user_id", "users_favorite_beer_id", "favorite_soccer_team_id", "user_iq"};
        TrainingDataGenerator2 trainingDataGenerator = new TrainingDataGenerator2(numTraniningExamples, .005, bidRequestAttributes);
        List<AbstractInstance> trainingData = trainingDataGenerator.createTrainingData();
        int millisInMinute = 60000;
        int instanceNumber = 0;
        for (AbstractInstance instance : trainingData) {
            instance.getAttributes().put("currentTimeMillis", millisInMinute * instanceNumber);
            instanceNumber++;
        }
        logger.info("trainingDataSize " + trainingData.size());
        RandomForestBuilder randomForestBuilder = getRandomForestBuilder(5, 5);
        CrossValidator crossValidator = new OutOfTimeCrossValidator(new AucCrossValLoss(), 0.25, 30, new TestDateTimeExtractor()); //number of validation time slices

        double totalLoss = crossValidator.getCrossValidatedLoss(randomForestBuilder, trainingData);
    }

    private static RandomForest getRandomForest(List<AbstractInstance> trainingData, int maxDepth, int numTrees) {
        TreeBuilder treeBuilder = new TreeBuilder().maxDepth(maxDepth).ignoreAttributeAtNodeProbability(.7);
        RandomForestBuilder randomForestBuilder = new RandomForestBuilder(treeBuilder).numTrees(numTrees);
        return randomForestBuilder.buildPredictiveModel(trainingData);
    }
    private static RandomForestBuilder getRandomForestBuilder(int maxDepth, int numTrees) {
        TreeBuilder treeBuilder = new TreeBuilder().maxDepth(maxDepth).ignoreAttributeAtNodeProbability(.7);
        RandomForestBuilder randomForestBuilder = new RandomForestBuilder(treeBuilder).numTrees(numTrees);
        return randomForestBuilder;
    }

}
