import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;


//http://www.cnblogs.com/fengmingyue/p/5987814.html
//生成简易验证码的小工具类
public class Demo2 {
    @Test
    public void fun1() throws Exception {
        VerifyCode verifyCode = new VerifyCode();
        BufferedImage bi = verifyCode.getImage();
        System.out.println(verifyCode.getText());
        VerifyCode.output(bi, new FileOutputStream(""));
    }
}
