import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;


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

    @Test
    public void fun2() throws Exception {
        String endTime = "2019-01-15";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Date date2 = null;
        try {
            date = format.parse(endTime);
            date2 = format2.parse(endTime + " 23:59:59");
            System.out.println();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
