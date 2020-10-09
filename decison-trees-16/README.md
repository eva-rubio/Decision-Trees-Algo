# Decision Trees

## Contents

```
  java/
    bin/ -- For binaries during compilation.
    src/ -- Stores all Java source code.
        DecisionTree.java -- The Java implementation of a decision tree.

  python/ 
    decision-tree.py -- The Python implementation of a decision tree.

  planning.md -- A brief outline of the functionality of the decision tree
                 implementation.
```

## Java

First, compile. From the `java` directory, run:

    cd Desktop/ENDI-2020/ARTIF-INTEL/machine-Learning-for-AI/decison-trees/java

    javac -d bin src/*.java

To run, do:

    java -cp bin DecisionTree

That will print a usage statement.

Here's an example of training on the SMS Training set and predicting labels for
the SMS Development set (this assumes they are in `../data` relative to this
README):

    # Train
    java -cp bin DecisionTree -train ../data-dt/sms/train.csv sms-model.dat

    # Predict
    java -cp bin DecisionTree -predict ../data-dt/sms/dev.csv sms-model.dat

## Python

Requires Python 3 to run; you may need to change `python` to your Python 3 
executable (e.g., `python3`, `python3.8`, etc.) in the commands below. 

To run, from the `python` directory, do:

    python decision-tree.py

That will print a usage statement.

Here's an example of training on the SMS training set and predicting labels for
the SMS development set (this assumes they are in `../data` relative to this
README):

    # Train
    python decision-tree.py -train ../../data/sms/train.csv sms-model.dat

    # Predict
    python decision-tree.py -predict ../../data/sms/dev.csv sms-model.dat
    