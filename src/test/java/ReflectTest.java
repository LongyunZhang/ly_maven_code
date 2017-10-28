import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

//http://www.cnblogs.com/fengmingyue/p/5973260.html

class Student {
    private String name;
    private String sid;
    public Student() {}
    public Student(String name, String sid) {
        this.name = name;
        this.sid = sid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSid() {
        return sid;
    }
    public void setSid(String sid) {
        this.sid = sid;
    }
    public static void  s1() {
        System.out.println(111);
    }
    public static int add(int a,int b) {
        return a+b;
    }
}

public class ReflectTest {
    //使用反射操作类里面的无参数的构造方法
    @Test
    public void test1() throws Exception {
        /**
         * 共有三种获取Class类的方法，下面这种是最常用的，除此以外还可以用Class c= Student.class
         * 和Class clazz2 = new Person().getClass();
         */
        Class c = Class.forName("Student");
        Student s = (Student) c.newInstance();//要对一个类进行实例化，可以new，不使用new，就用这种方法
        s.setName("张三");
        System.out.println(s.getName());
    }

    //使用反射操作类里面的有参数的构造方法
    @Test
    public void test2() throws Exception {
        Class c= Class.forName("Student");
        Constructor constructor = c.getConstructor(String.class,String.class);
        Student s=(Student) constructor.newInstance("李四","123456");
        System.out.println(s.getName());
    }

    //使用反射操作属性
    @Test
    public void test3() throws Exception {
        Class c = Class.forName("Student");
        Student s = (Student) c.newInstance();
        Field f = c.getDeclaredField("name");
        f.setAccessible(true);//操作私有属性或者私有方法要设置权限
        f.set(s, "王五");
        System.out.println(f.get(s));//和s.getName()作用一样
    }

    //使用反射操作方法
    @Test
    public void test4() throws Exception {
        Class c = Class.forName("Student");
        Student s = (Student) c.newInstance();
        Method m = c.getDeclaredMethod("setName", String.class);
        m.invoke(s, "赵六");
        System.out.println(s.getName());
    }

    //使用反射操作无参数静态方法
    @Test
    public void test5() throws Exception {
        Class c = Class.forName("Student");
        Student s = (Student) c.newInstance();
        Method m = c.getDeclaredMethod("s1");
        m.invoke(null);//静态方法传null，普通方法对象
    }
    //使用反射操作有参数静态方法
    @Test
    public void test6() throws Exception {
        Class c = Class.forName("Student");
        Method m = c.getDeclaredMethod("add",int.class,int.class);
        System.out.println(m.invoke(null,12,34));
    }
}


