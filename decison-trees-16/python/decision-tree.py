# Supports training a decision tree using an adapted version of the C4.5
# algorithm as well as running new observations through the decision tree to
# predict a label.
#
# @author Hank Feild
# @author YOU

import sys
import math

class Observation:
    '''
    Supports observations that are both labeled and unlabeled (e.g., for unseen
    observations).
    '''
    def __init__(self, features, label=None):
        '''
        Sets the data members.
        @param features A list of feature values.
        @param label OPTIONAL a class label for this observation.
        '''
        self.features = features
        self.label = label

class Node:
    def __init__(self, labelDistribution, n, featureIndex=None, 
        threshold=None, lessThanEqualChild=None, greaterThanChild=None):
        '''
        Sets the Node's data members.

        @param labelDistribution The distribution over labels represented in
                                 this subtree.
        @param n The total number of labels represented in this subtree.
        @param featureIndex The index of the feature to split on (only for
                            internal nodes).
        @param threshold The threshold of the feature to use to determine which
                         child to direct new observations to (only for internal
                         nodes).
        @param lessThanChild The child to send observations to with a feature
                             value less than or equal to the threshold (only for
                             internal nodes).
        @param greatherThanChild The child to send observations to with a 
                                 feature value greater than the threshold (only 
                                 for internal nodes).
        '''
        self.labelDistribution = labelDistribution
        self.n = n
        self.featureIndex = featureIndex
        self.threshold = threshold
        self.lessThanEqualChild = lessThanEqualChild 
        self.greaterThanChild = greaterThanChild

    def isLeafNode(self):
        '''
        @return True if this is a leaf node.
        '''
        return self.lessThanEqualChild == None


class DecisionTree:
    def __init__(self):
        self.root = None

    def train(self, observations):
        '''
        Trains a decision tree on the set of observations and stores this in
        `self.root`.
        @param observations A list of Observations with labels.
        '''
        self.root = self.build(observations)
        self.prune()

    def build(self, observations):
        '''
        Builds a decision tree on the set of observations.
        @param observations A list of Observations with labels.
        @return A Node representing a subtree or leaf.
        '''

        pass

    def prune(self):
        '''
        Prunes the tree rooted at `self.root`.
        '''
        pass

    def findBestThreshold(self, observations, featureIndex):
        '''
        Finds the threshold for the given feature that does the best job 
        splits observations into two sets according to the `evaluate` function. 
        
        @param observations A list of Observation instances, with labels.
        @param featureIndex The index of the feature in each Obseration to
                            consider a threshold for.
        @return The best threshold for the specified feature.
        '''
        pass

    def evaluate(self, split1, split2):
        '''
        Evaluates the effectiveness of split1 and split2 for partioning 
        observations by label.

        @param split1 A list of Observations.
        @param split2 A list of Observations.
        @return A score of how good the splits are. Higher is better.
        '''
        pass

    def saveModel(self, filename):
        '''
        Saves a model of the decision tree (`self.root`) to the given file.
        This file can be used with the `loadModel()` function.

        @param filename The name of the file to save the model to.
        '''
        pass

    def loadModel(self, filename):
        '''
        Loads a decision tree model into this instance.

        @param filename The name of the file to load the model from.
        '''
        pass

    def predict(self, obseration):
        '''
        Runs the observation through the decision tree and produces a class
        label prediction.

        @param observation The Observation to classify.
        @return The predicted class label of `observation`.
        '''
        pass

def parseDataFile(inputFile, hasLabel=True):
    '''
    Extracts features and, optionally labels, from a data file.

    @param inputFile A file with a header and one observation per line.
                     Each line should have n columns.  Columns must be comma 
                     separated.
    @param hasLabel If hasLabel is true, then the last column is treated as a
                    label and all the other columns as features. Otherwise, all
                    columns are considered features.
    @return A 2-tuple: (a list of Observation instances, the extracted header).
    '''
    observations = []

    fd = open(inputFile)
    header = fd.readline().strip().split(',')
    for line in fd:
        cols = line.strip().split(',')
        if hasLabel:
            features = [float(x) for x in cols[:-1]]
            label = cols[-1]
            observations.append(Observation(features, label))
        else:
            observations.append(Observation([float(x) for x in cols]))

    fd.close()

    return (observations, header)


## Main.
if __name__ == "__main__":
    USAGE = (
        'There are two modes to this: training and prediction.\n\n'
        'Training:\n'
        'Usage: decision-tree.py -train <training file> <model output file>\n'
        'where...\n'
        '   <training file> is a comma separated table of features and a label\n'
        '                   in the final column; header included\n'
        '   <model output file> is the name of the file to write the trained model to\n'
        '\n'
        'Prediction:\n'
        'Usage: decision-tree.py -predict <testing file> <model file>\n'
        'where...\n'
        '   <testing file> is a comma separated table of features in the same\n'
        '                  order used during training. Optionally, a label can\n'
        '                  be in the final column; a header should be present\n'
        '   <model file> should contain the decision tree model to use for prediction\n'
        '\n'
        'The output of the of prediction is the <testing file> data with a new\n'
        'column: predicted_label'
    )

    if len(sys.argv) < 4:
        sys.stderr.write('Too few arguments.\n\n')
        sys.stderr.write(USAGE);
        sys.exit()

    ## Training.
    if sys.argv[1] == '-train':
        trainFile = sys.argv[2]
        modelFile = sys.argv[3]

        (trainObservations, trainHeader) = parseDataFile(trainFile)
        
        tree = DecisionTree()
        tree.train(trainObservations)
        tree.saveModel(modelFile)


    ## Prediction.
    elif sys.argv[1] == '-predict':

        testFile = sys.argv[2]
        modelFile = sys.argv[3]

        (testObservations, testHeader) = parseDataFile(testFile)
        tree = DecisionTree()
        tree.loadModel(modelFile)

        print(','.join(testHeader) + ',predicted_label')
        for obs in testObservations:
            predictedLabel = tree.predict(obs)
            print(f'''{','.join([str(x) for x in obs.features])},{obs.label},{predictedLabel}''')

    ## Invalid argument.
    else:
        sys.stderr.write(f'Unrecognized first argument: {sys.argv[1]}\n')
        sys.stderr.write(USAGE);
        sys.exit()




        


