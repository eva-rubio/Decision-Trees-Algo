# Class Activity 16: Decision tree implementation

In this class activity, you will work on implementing the code to build a decision tree.  You may work alone or with your group.


Start by downloading this code base. 
Take a look at README.md and planning.md to get a feel for the layout.

Pick the language you'd like to use (Java or Python) and complete the build() and predict() functions (Java: in java/src/DecisionTree.java; Python: in python/decision-tree.py). When implementing this, keep the following in mind:

## build()
    the pseudo code for build() from the video is in planning.md (called BuildTree)
    for finding the best threshold, call findBestThreshold()
        for now, have this pick an arbitrary threshold in the range of the given feature (e.g., the value of that feature in the first observation)
    for eval() in the pseudo code, call evaluate() in the implementation
        for now, have this function return a randomly generated number 


## predict()
    there is no pseudo code for this, but you can follow the process outlined in the videos from homework

    
Feel free to add additional helper functions and classes.

Work on completing this for 65 minutes. Submit the files you modified after that time.