package org.dsA2;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: ${NAME}
 * Package: org.dsA2
 * Description:
 *
 * @Author Shiqiang Ren
 * @Create ${DATE} ${TIME}
 * @Version 1.0
 */
public class Main {

    static List<String[]> hobbies = new ArrayList<>();
    public static void main(String[] args) {
        String[] hobby1 = {"reading", "writing"};
        String[] hobby2 = {"swimming", "hiking", "fishing"};
        hobbies.add(hobby1);
        hobbies.add(hobby2);

        System.out.println("Hello world!");
        for (String[] hobbyArray : hobbies) {
//            for (String hobby : hobbyArray) {
//                System.out.print(hobby + " ");
//            }
            System.out.println(hobbyArray[0]);
            //String jsonStr = JSON.toJSONString(hobbies);
            //List<String[]> hobbies = JSON.parseObject(jsonStr, new TypeReference<List<String[]>>(){});
        }
    }
}