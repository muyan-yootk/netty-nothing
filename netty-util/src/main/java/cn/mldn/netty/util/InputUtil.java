package cn.mldn.netty.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputUtil {
    private InputUtil () {}
    public static String getString(String prompt) {
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in)) ;
        String content = null ; // 保存返回的输入数据
        boolean flag = true ;
        while (flag) {
            System.out.print(prompt); // 打印提示信息
            try {
                content = buf.readLine() ;	// 读取输入数据
                if (content == null || "".equals(content)) {	// 数据为空
                    System.out.println("输入的内容不允许为空！");
                } else {
                    flag = false ; // 结束循环
                }
            } catch (IOException e) {
                System.out.println("输入数据错误！！");
            }
        }
        return content ;
    }
}
