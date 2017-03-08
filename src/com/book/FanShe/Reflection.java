package com.book.FanShe;

import java.lang.reflect.Array;   
import java.lang.reflect.Constructor;   
import java.lang.reflect.Field;   
import java.lang.reflect.Method;   
  
  
/**  
 * Java Reflection Cookbook  
 * 1.getName()
 * 	一个Class对象描述了一个特定类的特定属性，而这个方法就是返回String形式的该类的简要描述。由于历史原因，对数组的Class对象调用该方法会产生奇怪的结果。
 * 2.newInstance()
 * 	该方法可以根据某个Class对象产生其对应类的实例。需要强调的是，它调用的是此类的默认构造方法。
 * 	例如：MyObject x = new MyObject();
 * 		MyObject y = x.getClass().newInstance();
 * 3.getClassLoader()
 * 	返回该Class对象对应的类的类加载器。
 * 4.getComponentType()
 * 	该方法针对数组对象的Class对象，可以得到该数组的组成元素所对应对象的Class对象。例
 * 	如：int[] ints = new int[]{1,2,3};
 * 		Class class1 = ints.getClass();
 * 		Class class2 = class1.getComponentType();
 * 	而这里得到的class2对象所对应的就应该是int这个基本类型的Class对象。
 * 5.getSuperClass()
 * 	返回某子类所对应的直接父类所对应的Class对象。
 * 6.isArray()
 * 	判定此Class对象所对应的是否是一个数组对象。
 */  
  
public class Reflection {   
    /**  
     * 得到某个对象的公共属性  
     *  
     * @param owner, fieldName  
     * @return 该属性对象  
     * @throws Exception  
     *  
     */  
    public Object getProperty(Object owner, String fieldName) throws Exception {   
        Class ownerClass = owner.getClass();   
  
        Field field = ownerClass.getField(fieldName);   
  
        Object property = field.get(owner);   
  
        return property;   
    }   
  
    /**  
     * 得到某类的静态公共属性  
     *  
     * @param className   类名  
     * @param fieldName   属性名  
     * @return 该属性对象  
     * @throws Exception  
     */  
    public Object getStaticProperty(String className, String fieldName)   
            throws Exception {   
        Class ownerClass = Class.forName(className);   
  
        Field field = ownerClass.getField(fieldName);   
  
        Object property = field.get(ownerClass);   
  
        return property;   
    }   
  
  
    /**  
     * 执行某对象方法  
     *  
     * @param owner  
     *            对象  
     * @param methodName  
     *            方法名  
     * @param args  
     *            参数  
     * @return 方法返回值  
     * @throws Exception  
     */  
    public Object invokeMethod(Object owner, String methodName, Object[] args)   
            throws Exception {   
  
        Class ownerClass = owner.getClass();   
  
        Class[] argsClass = new Class[args.length];   
  
        for (int i = 0, j = args.length; i < j; i++) {   
            argsClass[i] = args[i].getClass();   
        }   
  
        Method method = ownerClass.getMethod(methodName, argsClass);   
  
        return method.invoke(owner, args);   
    }   
  
  
      /**  
     * 执行某类的静态方法  
     *  
     * @param className  
     *            类名  
     * @param methodName  
     *            方法名  
     * @param args  
     *            参数数组  
     * @return 执行方法返回的结果  
     * @throws Exception  
     */  
    public Object invokeStaticMethod(String className, String methodName,   
            Object[] args) throws Exception {   
        Class ownerClass = Class.forName(className);   
  
        Class[] argsClass = new Class[args.length];   
  
        for (int i = 0, j = args.length; i < j; i++) {   
            argsClass[i] = args[i].getClass();   
        }   
  
        Method method = ownerClass.getMethod(methodName, argsClass);   
  
        return method.invoke(null, args);   
    }   
  
  
  
    /**  
     * 新建实例  
     *  
     * @param className  
     *            类名  
     * @param args  
     *            构造函数的参数  
     * @return 新建的实例  
     * @throws Exception  
     */  
    public Object newInstance(String className, Object[] args) throws Exception {   
        Class newoneClass = Class.forName(className);   
  
        Class[] argsClass = new Class[args.length];   
  
        for (int i = 0, j = args.length; i < j; i++) {   
            argsClass[i] = args[i].getClass();   
        }   
  
        Constructor cons = newoneClass.getConstructor(argsClass);   
  
        return cons.newInstance(args);   
  
    }   
  
  
       
    /**  
     * 是不是某个类的实例  
     * @param obj 实例  
     * @param cls 类  
     * @return 如果 obj 是此类的实例，则返回 true  
     */  
    public boolean isInstance(Object obj, Class cls) {   
        return cls.isInstance(obj);   
    }   
       
    /**  
     * 得到数组中的某个元素  
     * @param array 数组  
     * @param index 索引  
     * @return 返回指定数组对象中索引组件的值  
     */  
    public Object getByArray(Object array, int index) {   
        return Array.get(array,index);   
    }   
}  