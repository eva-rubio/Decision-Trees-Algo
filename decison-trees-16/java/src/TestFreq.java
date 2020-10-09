import java.util.*;

public class TestFreq {

    /* public static String majorityLabel(ArrayList<String> arrayListLabels) {
        int count = 0;
        int maxCount = 0;
        String theMajorityLabel = "";

        for (int i = 0; i < arrayListLabels.size(); i++) {
            count = 1;
            for (int j = i + 1; j < arrayListLabels.size(); j++) {
                if (arrayListLabels.get(i).equals(arrayListLabels.get(j))) {
                    count++;
                }
            }

            System.out.println("count: " + count);
            System.out.println("maxCount: " + maxCount);
            if (count > maxCount) {
                maxCount = count;
                theMajorityLabel = arrayListLabels.get(i);
            } else if (count == maxCount) {
                theMajorityLabel = "Son iguales";
            } else if (count == arrayListLabels.size()) {
                theMajorityLabel = "Solo hay 1 clase";
            }
        }
        
        System.out.println("----");
        System.out.println("arrayListLabels.size(): " + arrayListLabels.size());
        System.out.println("count: " + count);
        System.out.println("maxCount: " + maxCount);

        return theMajorityLabel;

    } */

    public static void main(String[] args) {
        //int[] elements = { 1, 3, 4, 1, 5, 2, 3, 6, 6, 6, 4, 1, 2, 6, 2, 3, 1, 2, 1, 5, 5, 1, 1, 5, 4 };
        String[] allLabels = { "geeks", "for", "geeks", "a", 
                         "portal", "to", "learn", "can", "be", 
                         "computer", "science", "zoom", "yup", 
                         "fire", "in", "be", "data", "geeks" };

        int count = 0;
        int maxCount = 0;
        String theMajorityLabel = "";
        ArrayList<String> distinctLabels = new ArrayList<String>();
        ArrayList<Integer> counts = new ArrayList<Integer>();
        ArrayList<String> classesOfSameSize = new ArrayList<String>();

        for (String label : allLabels) {
            int index = distinctLabels.indexOf(label);
            // if this label has already been added to distinctLabels:
            if (index != -1) {
                // increase its count - increase the number of times this label appears
                int newCount = counts.get(index) + 1;
                counts.set(index, newCount);
            } else {
                // If it is the first time we are seeing this label, add it to distinctLabels,
                // and set its count to be 1
                distinctLabels.add(label);
                counts.add(1);
            }
        }
        //Collections.sort(distinctLabels);
        for (int i = 0; i < counts.size(); i++) {
            for (int j = i + 1; j < counts.size(); j++) {
                if (counts.get(i).equals(counts.get(j))) {
                    //duplicate -- these 2 classes have the same amount of observations

                
                }
            }
            

            if (counts.get(i) > maxCount) {
                maxCount = counts.get(i);
                theMajorityLabel = distinctLabels.get(i);
            }
        }

        // fill in the appropriate results:
        System.out.println("The most frequent element " + distinctLabels.get(theMajorityLabel) + " occurs " + maxCount + " times");
    }

}