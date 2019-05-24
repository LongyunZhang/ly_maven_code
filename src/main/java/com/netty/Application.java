/*******************************************************
 * Copyright (C) 2019 iQIYI.COM - All Rights Reserved
 *
 * This file is part of {cupid_3}.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * Date: 2019/5/24
 * Author(s): zhanglongyun<zhanglongyun@qiyi.com>
 *
 *******************************************************/
package com.netty;

public class Application {
    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer(8082);// 8082为启动端口
        server.start();
    }
}
