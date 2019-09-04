package javaconcurrencye2.synchronizedcollections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InteratorAndCMESingleThread {
    public static void main(String... args) {
        List<String> strings = new ArrayList<>(Arrays.asList("a", "b", "c"));

        // this block will result in ConcurrentModificationException
        for(String s : strings){
            if(s.equalsIgnoreCase("a")){
                strings.remove(s);
            }
        }
    }
}
