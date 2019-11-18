import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: heweiwei@hztianque.com
 * @Date: 2019/10/29
 * @Time: 17:20
 * Description:
 */
public class Test {
    public static void main(String[] args) {
//        String property = System.getProperty("line.separator");
//        System.out.println("property = " + property.toString());
//        System.out.println("-----------" + property.length());


        String message="hello world"+System.getProperty("line.separator") +"ok";
        System.out.println(message);

        Map<String,Object> map=new HashMap<>();
        map.put("hww",24);
        map.put("llh",25);
        map.put("lb",26);
        map.put("see",27);
        map.put("lzq",28);
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        System.out.println("entries = " + entries);

    }
}
