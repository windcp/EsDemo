package es;

import entity.Pointdata;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EsComplexQuery {
    public static void main(String[] args) throws Exception{
        Class clazz = Pointdata.class;

        Constructor[] constructors = Pointdata.class.getConstructors();


        Pointdata obj = (Pointdata) constructors[0].newInstance();

            Field field = clazz.getDeclaredField("username");

            field.setAccessible(true);
            field.set(obj,"fuckyou");

           Method[] methods = clazz.getDeclaredMethods();
        Method method = clazz.getMethod("getUsername");

        System.out.println(method.getName()+":"+method.invoke(obj));
        System.out.println(methods[0].invoke(obj));
        System.out.println(obj.getUsername());

    }
}
