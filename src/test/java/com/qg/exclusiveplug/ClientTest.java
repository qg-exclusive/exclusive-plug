package com.qg.exclusiveplug;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.Socket;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientTest {

    @Test
    public void test() throws IOException {
        // 1、创建客户端的 Socket 服务
        Socket socket = new Socket("127.0.0.1", 8090);

        InputStream inputStream = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\test.txt"));

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        // 2、获取 Socket 流中输入流
        OutputStream out = socket.getOutputStream();

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(out));

        while(null != (line = bufferedReader.readLine())){
            log.info(line);
            // 3、使用输出流将指定的数据写出去
            bufferedWriter.write(line);
        }

        // 4、关闭 Socket 服务
        socket.close();
    }
}
