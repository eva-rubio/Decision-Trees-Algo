import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Supports training a decision tree using an adapted version of the C4.5
 * algorithm as well as running new observations through the decision tree to
 * predict a label.
 * 
 * @author Hank Feild
 * @author Eva Rubio
 */
public class DecisionTree {

    /**
     * First Inner class.
     *         Holds a list of Features and a label.
     *     
     * Supports observations that are both labeled and unlabeled (e.g., for unseen observations).
     * 
     */
    public class Observation {
        public ArrayList<Double> features;
        public String label;

        /**
         * Sets the features.
         
         * the constructor which sets the data members to those 2.
         * 
         * @param features A list of feature values.
         * @param label A class label for this observation. Is null for an
         *              observation without a label.
         */
        public Observation(ArrayList<Double> features, String label) {
            this.features = features;
            this.label = label;
        }

        /**
         * @return A CSV string for of this Observation. All features are
         *         listed in order followed by the label if not null.
         */
        public String toString() {
            StringBuffer output = new StringBuffer();

            for (int i = 0; i < features.size(); i++) {
                output.append(features.get(i));
                if (i < features.size() - 1)
                    output.append(",");
            }
            if (label != null)
                output.append(",").append(label);

            return output.toString();
        }
    }

    public class FeatureInfo {
        public double evalMeasure;
        public int featureIndex;
        public double threshold;

        public FeatureInfo(double evalMeasure, int featureIndex, double threshold) {
            this.evalMeasure = evalMeasure;
            this.featureIndex = featureIndex;
            this.threshold = threshold;
        }
    }

    /**
     * Third inner class. It is a wrapper.
     * 
     * Once we get our classification I want to print out 2 things:
     * 
     *      - the original Test file that we are given, and
     * 
     *      - the extra column of information. 
     *              So i need to get the header, which is NOT actually and observation. 
     *                      I get this from the parse data file class.
     * 
     * Represents a data file: a header with names of columns and a list of
     * observations.
     */
    public class Dataset {
        //the headers 
        public ArrayList<String> columnNames;
        // each of these observations have features and also have the label.
        public ArrayList<Observation> observations;

        /**
         * Constructor. Sets the data members.
         * @param columnNames A list of column names.
         * @param observations A list of observations.
         */
        public Dataset(ArrayList<String> columnNames, 
                       ArrayList<Observation> observations) {
            this.columnNames = columnNames;
            this.observations = observations;
        }

        /**
         * To get the column names as a CSV. (no need to deal with label here)
         * 
         * @return A CSV version of the header.
         */
        public String columnNamesAsCSV() {
            StringBuffer output = new StringBuffer();

            for(int i = 0; i < columnNames.size(); i++){
                output.append(columnNames.get(i));
                if(i < columnNames.size()-1)
                    output.append(",");
            }
                
            return output.toString();
        }
    }



    /**
     * Generate a list of observations from the data file.
     * 
     * @param filename The name of the file to parse. Should have a header and be in
     *                 comma separated value (CSV) format.
     * @param hasLabel If true, the last column will be used as the label for each
     *                 observation and all other columns will be features. If false,
     *                 *all* columns will be used as features.
     * @return The observations and header (column names).
     * @throws IOException
     */
    public Dataset parseDataFile(String filename, boolean hasLabel) 
        throws IOException {

        ArrayList<String> columnNames = new ArrayList<String>();
        ArrayList<Observation> observations = new ArrayList<Observation>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        for(String col : reader.readLine().split(",")){
            columnNames.add(col);
        }

        while(reader.ready()){
            String[] columns = reader.readLine().split(",");
            ArrayList<Double> features = new ArrayList<Double>();
            String label = null;
            if(hasLabel){
                for(int i = 0; i < columns.length-1; i++){
                    features.add(Double.parseDouble(columns[i]));
                }
                label = columns[columns.length-1];
            } else {
                for(int i = 0; i < columns.length; i++){
                    features.add(Double.parseDouble(columns[i]));
                }
            }
            
            observations.add(new Observation(features, label));
        }
        reader.close();

        return new Dataset(columnNames, observations);
    }

    /**
     * Represents a node in the decision tree.
     */
    public class Node {
        
        HashMap<String,Integer> labelDistribution;  // a map of labels and their counts in this and descendent nodes
        int n; // the number of training observations represented in this and descendent nodes
        
                    //------Only for Internal Nodes:--------
        int featureIndex;   // the feature this node splits on
        double threshold;   // the threshold on the feature for splitting
        Node lessThanEqualChild;    // the Node to traverse for observations whose feature is ≤ threshold
        Node greaterThanChild;  // the Node to traverse for observations whose feature is > threshold

        /**
         * Sets the Node's data members for a leaf node.
         * 
         * @param labelDistribution The distribution over labels represented in
         *                          this subtree.
         * @param n The total number of labels represented in this subtree.
         */
        public Node(HashMap<String,Integer> labelDistribution, int n){
            this(labelDistribution, n, 0, 0, null, null);
        }

        /**
         * Sets the Node's data members for an internal node.
         * 
         * @param labelDistribution The distribution over labels represented in this
         *                          subtree.
         * @param n                 The total number of labels represented in this
         *                          subtree.
         * @param featureIndex      The index of the feature to split on (only for internal nodes).
         *                          
         * 
         * @param threshold         The threshold of the feature to use to determine
         *                          which child to direct new observations to (only for internal nodes).
         *                          
         * @param lessThanChild     The child to send observations to with a feature
         *                          value less than or equal to the threshold (only for
         *                          internal nodes).
         * @param greatherThanChild The child to send observations to with a feature
         *                          value greater than the threshold (only for internal
         *                          nodes).
         */
        public Node(HashMap<String,Integer> labelDistribution, int n, 
                    int featureIndex, double threshold, Node lessThanEqualChild, 
                    Node greaterThanChild){
            
            this.labelDistribution = labelDistribution;
            this.n = n;
            this.featureIndex = featureIndex;
            this.threshold = threshold;
            this.lessThanEqualChild = lessThanEqualChild;
            this.greaterThanChild = greaterThanChild;
        }

        /**
         * @return True if this is a leaf node.
         */
        public boolean isLeafNode(){
            return lessThanEqualChild == null;
        }
    }

    Node root;

    /**
     * Initializes the root of the tree to null.
     */
    public DecisionTree() {
        root = null;
    }

    /**
     * Trains a decision tree on the set of observations and stores this in
     * `self.root`.
     * 
     * @param observations A list of Observations with labels.
     */
    public void train(ArrayList<Observation> observations) {
        root = build(observations);
        prune();
    }

    public Boolean hasOneClass(ArrayList<String> arrayListLabels) {
        for (String lab : arrayListLabels) {
            if (!lab.equals(arrayListLabels.get(0))) {
                return false;
            }
        }

        return true;
    }
    
    public class SplitData {
        public ArrayList<Observation> leftSplit;
        public ArrayList<Observation> rightSplit;

        public SplitData(ArrayList<Observation> leftSplit, ArrayList<Observation> rightSplit) {
            this.leftSplit = leftSplit;
            this.rightSplit = rightSplit;
        }
    }

    public SplitData makeSplit(ArrayList<Observation> observations, double threshold, int featIndx) {
        ArrayList<Observation> leftSplit = new ArrayList<Observation>();
        ArrayList<Observation> rightSplit = new ArrayList<Observation>();

        for (Observation ob : observations) {
            if (ob.features.get(featIndx) <= threshold) {
                leftSplit.add(ob);

            } else {
                rightSplit.add(ob);
            }
        }

        return new SplitData(leftSplit, rightSplit);
    }

    public HashMap<String, Integer> getDistribution(ArrayList<String> arrayListLabels) {
        HashMap<String, Integer> labelDistrib = new HashMap<String, Integer>();
        int count =0;

        for (String cla : arrayListLabels) {
            if (!labelDistrib.containsKey(cla)) {
                count++;
                labelDistrib.put(cla, count);
            } else {
                count++;
                labelDistrib.replace(cla, count);
            }
        }
        return labelDistrib;

    }

    /**
     * Builds a decision tree on the set of observations.
     * 
     * This is the algo from the video. 
     * 
     * Needs:
     *      - bestFeature
     *      - threshold
     *      - evalMeasure
     * 
     * 
     * @param observations A list of Observations with labels.
     * @return A Node representing a subtree or leaf.
     */
    public Node build(ArrayList<Observation> observations) {
        int numberOfFeatures = observations.get(0).features.size();
        ArrayList<Observation> leftSplit = new ArrayList<Observation>();
        ArrayList<Observation> rightSplit = new ArrayList<Observation>();
        SplitData splitted = new SplitData(leftSplit, rightSplit);
        
        ArrayList<String> arrayListLabels = new ArrayList<String>();
        HashMap<String, Integer> labelDistrib = new HashMap<String, Integer>();
        FeatureInfo bestFeatInfo = new FeatureInfo(0, 0, 0);
        double myBestThreshold = 0;
        double myEvalMeasure = 0;
         

        for (Observation obs : observations) {
            arrayListLabels.add(obs.label);
        }
        //BASE CASE 1 - We are at a Leaf Node. Only has 1 class. 
        if (hasOneClass(arrayListLabels)) {
            labelDistrib.put(arrayListLabels.get(0), arrayListLabels.size());
            return new Node(labelDistrib, arrayListLabels.size());
        }

        //we go through every feature
        for (int i = 0; i < numberOfFeatures; i++) {
            myBestThreshold = findBestThreshold(observations, i);

            splitted = makeSplit(observations, myBestThreshold, i);

            myEvalMeasure = evaluate(splitted.leftSplit, splitted.rightSplit);

            if (myEvalMeasure > bestFeatInfo.evalMeasure) {
                bestFeatInfo.evalMeasure = myEvalMeasure;
                bestFeatInfo.featureIndex = i;
                bestFeatInfo.threshold = myBestThreshold;

            }
        }
        //----------------BASE CASE 2---------------
        // if unable to split data any further
        if (bestFeatInfo.evalMeasure == 0) {
            /**
             * labelDistrib.put(arrayListLabels.get(0), arrayListLabels.size()); return new
             * Node(labelDistrib, arrayListLabels.size());
             * 
             * make map of... all labels with all their counts. 
             * 
             */
            labelDistrib = getDistribution(arrayListLabels);
            return new Node(labelDistrib, arrayListLabels.size());

        }
        return new Node(labelDistrib, arrayListLabels.size(), bestFeatInfo.featureIndex, bestFeatInfo.threshold,
                build(splitted.leftSplit), build(splitted.rightSplit));

        
        

        /**
         * At each Internal-Node we ask:
         *      1. What is the bestFeature to split the current list on, 
         *      2. At what threshhold should we split it?
         * 
         * bestFeature = FeatureInfo(0, null, null)
         *  We need to figure out, what is the best feature.
         *  based on this feature, we will then procede to split our current list of observations.
         * 
         * we need to find the feature that we want to split on. 
         * 
         * After selecting bestFeature, we need to figure out what the threshold should be for THIS feature (bestFeature). 
         * 
         * features my dataset has:
         * - sepal length
         * - sepal width
         * - petal length
         * - petal width
         */


        
        
    }

    /**
     * Prunes the tree rooted at `self.root`.
     */
    public void prune(){

    }

    /**
     * Finds the threshold for the given feature that does the best job splits
     * observations into two sets according to the `evaluate` function.
     * 
     * returns the threshold of feature that splits the data best according to
     * evaluate()
     * 
     * After getting our bestFeature, we need to find out, 
     *      what would be the best threshhold. 
     *      The threshhold, will then be used in the evaluation() funct.
     *          - It is the number we will use to split our current list of observations by.
     * 
     * we are going to fo through every single feature that our dataset has, and:
     *      we are going to find a threshold, that does a good job for the feature that we previously selected.
     * 
     * 
     * 
     * for now, have this pick an arbitrary threshold in the range of the given
     * feature (e.g., the value of that feature in the first observation)
     * 
     * 
     * @param observations A list of Observation instances, with labels. (list of
     *                     observations)
     * @param featureIndex The index of the feature in each Obseration to consider a
     *                     threshold for.
     * @return The best threshold for the specified feature.
     */
    public double findBestThreshold(ArrayList<Observation> observations, 
                             int featureIndex)
    {
        
        return observations.get(0).features.get(featureIndex);
    }

    /**
     * Evaluates the effectiveness of split1 and split2 for partioning observations
     * by label.
     * 
     * the eval function is going to determine if: -- this split is helpful or not.
     *      i.e. If we split our current set of observations this way, 
     *              is it leading us toward narrowing down into a single class??
     * 
     * If this particular feature, and this particular threshold gives us 
     *      the biggest evalValue we have seen so far:
     *      We want to store this feature and this threshold in: 
     *          bestFeature = new FeatureInfo(bigeestEvalValue, bestFeature, bestThreshold)
     * 
     * bestFeature will be used at the end. it will tell us:
     *      - what the evalValue is. 
     *              (we need this for comparing features and picking the best one)
     *      - what feature we are going to split on, 
     *      - what the threshold for that feature ^ should be. 
     *      
     * 
     * ---------------------- BASE CASE 2 -----------------------
     *                  If our evalMeasure = 0
     * If the bestFeature we could find, with the bestThreshold, evaluates to 0:
     * 
     *  It means -> We cannot come up with a good split. 
     *      (We are unable to split the data any furhter).
     * 
     * At this point, our data contains multiple classes. So:
     *      We create a Histogram/ map / dictionary. 
     *          Which contains a list of the classes with the amount of observations that each class has. 
     *      We then use the MajorityClass.
     *  We then return a new LeafNode(the map/Histogram).
     * 
     * (if we do not reach this Base Case, it means we have a good Internal-Node)
     * -----------------------------------------------------------
     * 
     * two lists of observations, returns a number (bigger is better)
     * 
     * for now, have this function return a randomly generated number
     * 
     * @param split1 A list of Observations. Corresponds to the observations that are <= threshold.
     * @param split2 A list of Observations. The observations that are > threshold. 
     * @return A score of how good the splits are. Higher is better.
     */
    public double evaluate(ArrayList<Observation> split1, 
                           ArrayList<Observation> split2){
        return Math.random();
    }

    /**
     * Saves a model of the decision tree (`self.root`) to the given file.
     * This file can be used with the `loadModel()` function.
     * 
     * @param filename The name of the file to save the model to.
     */
    public void saveModel(String filename){

    }

    /**
     * Loads a decision tree model into this instance.
     * 
     * @param filename The name of the file to load the model from.
     */
    public void loadModel(String filename){

    }

    /**
     * Runs the observation through the decision tree and produces a class
     * label prediction.
     *  (produces a prediction for the given observation)
     * 
     * @param observation The Observation to classify.
     * @return The predicted class label of `observation`.
     */
    public String predict(Observation observation){
        return "";
    }

    /**
     * handles training and running
     */
    public static void main(String[] args) throws IOException {
        final String USAGE = 
            "There are two modes to this: training and prediction.\n\n"+
            "Training:\n"+
            "Usage: DecisionTree -train <training file> <model output file>\n"+
            "where...\n"+
            "   <training file> is a comma separated table of features and a label\n"+
            "                   in the final column; header included\n"+
            "   <model output file> is the name of the file to write the trained model to\n"+
            "\n"+
            "Prediction:\n"+
            "Usage: decision-tree.py -predict <testing file> <model file>\n"+
            "where...\n"+
            "   <testing file> is a comma separated table of features in the same\n"+
            "                  order used during training. Optionally, a label can\n"+
            "                  be in the final column; a header should be present\n"+
            "   <model file> should contain the decision tree model to use for prediction\n"+
            "\n"+
            "The output of the of prediction is the <testing file> data with a new\n"+
            "column: predicted_label";
        

        DecisionTree tree = new DecisionTree();
        String trainingFilename, testingFilename, modelFilename;
        Dataset trainData, testData;

        // Check that enough arguments were specified.
        if(args.length < 3){
            System.err.println("Too few arguments.\n");
            System.err.println(USAGE);
            System.exit(1);
        }

        // Training mode.
        if(args[0].equals("-train")){
            trainingFilename = args[1];
            modelFilename = args[2];

            trainData = tree.parseDataFile(trainingFilename, true);
            tree.train(trainData.observations);
            tree.saveModel(modelFilename);

        // Prediction mode.
        } else if(args[0].equals("-predict")){
            testingFilename = args[1];
            modelFilename = args[2];

            testData = tree.parseDataFile(testingFilename, true);
            tree.loadModel(modelFilename);

            // Print out each testing observation and its predicted label.
            System.out.println(testData.columnNamesAsCSV() +
                ",predicted_label");
            for(Observation obs : testData.observations)
                System.out.println(obs +","+ tree.predict(obs));

        // Unknown mode.
        } else {
            System.err.println("Unrecognized mode: "+ args[0] +"\n");
            System.err.println(USAGE);
            System.exit(1);
        }







    }
}