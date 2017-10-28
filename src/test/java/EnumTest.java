
import org.junit.Test;

enum Color{RED,YELLOW,BLUE}
enum Color2{
    RED(10),YELLOW(3),BLUE(5);
    private Color2(int s) {
        System.out.println(this.name()+"亮了"+s+"秒钟！");
    }
}
public class EnumTest {
    //有构造方法的枚举
    @Test
    public void test()
    {
        Color2 c=Color2.RED;
        System.out.println(c.name()+" "+c.ordinal());
    }
    //知道枚举的对象，求名称和下标
    @Test
    public void test1()
    {
        Color c=Color.RED;
        System.out.println(c.name()+" "+c.ordinal());
    }
    //知道枚举的名称，求对象和下标
    @Test
    public void test2()
    {
        String name="YELLOW";
        Color c=Color.valueOf(name);
        System.out.println(c+" "+c.ordinal());
    }
    //知道枚举的下标，求名称和对象
    @Test
    public void test3()
    {
        int index=2;
        Color c=Color.values()[index];
        System.out.println(c.name()+" "+c);
    }
}