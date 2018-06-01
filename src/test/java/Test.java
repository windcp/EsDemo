import util.CarCard;
import util.IDCardNo;
import util.RandomValue;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {


        long start = System.currentTimeMillis();
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println(start);
        for (int i = 0; i<1; i++) {
            String carNum =  CarCard.getCarNum();
            /*long end1 = System.currentTimeMillis();
            System.out.println(end1-start);*/
            String name = RandomValue.getChineseName();
            /*long end2 = System.currentTimeMillis();
            System.out.println(end2-end1);*/
            String phone = RandomValue.getTel();
        /*    long end3 = System.currentTimeMillis();
            System.out.println(end3-end2);*/
            String idCard = IDCardNo.getRandomID();
          /*  long end4 = System.currentTimeMillis();
            System.out.println(end4-end3);*/
            map.put("plateNum", carNum);
            map.put("owner", name);
            map.put("phone", phone);
            map.put("IDCard", idCard);
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println(map);
    }

}
