package javaconcurrencye2.synchronizedcollections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Zhu Zhaohua (Isaac)
 */
public class InteratorAndCMESingleThread {
    public static void main(String... args) {
        List<String> strings = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> copy = new ArrayList<>(strings);

        //        for( int i = 0; i < strings.size(); i++){  // this will not encounter CME because it's not accessing iterator
        //        for(String s : copy) {    // this will not encounter CME because you're modifying different list
        for (String s : strings) { // this block will result in ConcurrentModificationException
            //            String s = strings.get(i);
            if (s.equalsIgnoreCase("a")) {
                strings.remove(s);
            }
        }
    }
}
