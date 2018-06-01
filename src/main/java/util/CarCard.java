package util;

import java.util.Random;

/**
 * 随机自动生成浙江省车牌号
 */
public class CarCard {
    //浙A 杭州，浙B 宁波，浙C 温州，浙D 绍兴，浙E 湖州，浙F 嘉兴，浙G 金华，浙H 衢州，浙J 台州，浙K 丽水，浙L 舟山

    //生成随机数字和字母,
    public static String getCarNum() {
        String a[]={"浙A","浙B","浙C","浙D","浙E","浙F","浙G","浙H","浙J","浙K","浙L"};
        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < 5; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        int num2 = random.nextInt(10);
        val  = a[num2] + val;
        return val;
    }

}
