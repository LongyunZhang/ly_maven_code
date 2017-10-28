import org.junit.*;

import java.util.ArrayList;
import java.util.Iterator;

public class JUnitTest {

    //http://www.cnblogs.com/fengmingyue/p/5967511.html

    @Before
    public void testBefore()
    {
        System.out.println("在任何方法前都会运行。。。。。。。。。。。。。");
    }

    @Test
    public void test1()
    {
        System.out.println("111111111111111111111111");
    }

    @Test
    public void test2()
    {
        ArrayList<Integer> list=new ArrayList<Integer>();
        list.add(2222);list.add(3333);list.add(4444);
        Iterator<Integer> iterator=list.iterator();//迭代器遍历
        while(iterator.hasNext())
        {
            System.out.print(iterator.next());
        }
        System.out.println();
    }

    @Test
    public void test3()
    {
        int num=9;
        Assert.assertEquals(num, 9);             //断言
    }

    @Ignore
    public void testIgnore()
    {
        System.out.println("运行时会忽视这个方法。。。。。。。。。");
    }

    @After
    public void testAfter()
    {
        System.out.println("在任何方法运行后都会运行。。。。。。。。。。。。。");
    }
}

