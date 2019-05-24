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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec());// http 编解码
        pipeline.addLast("httpAggregator", new HttpObjectAggregator(512 * 1024)); // http 消息聚合器 512*1024为接收的最大contentlength
        pipeline.addLast(new HttpRequestHandler());// 请求处理器

        //HttpRequestHandler 指定的handler， handler是用来承载我们具体逻辑实现代码的地方
        /**
         * 1. channel 代表了一个socket.
         * 2. ChannelPipeline 就是一个“羊肉串”，这个“羊肉串”里边的每一块羊肉就是一个 handler.
         *    handler分为两种，inbound handler,outbound handler 。顾名思义，分别处理 流入，流出。
         * 3. HttpServerCodec 是 http消息的编解码器。
         * 4. HttpObjectAggregator 是Http消息聚合器，Aggregator这个单次就是“聚合，聚集”的意思。http消息在传输的过程中可能是一片片的消息片端，所以当服务器接收到的是一片片的时候，就需要HttpObjectAggregator来把它们聚合起来。
         * 5. 接收到请求之后，你要做什么，准备怎么做，就在HttpRequestHandler中实现。
         */

    }
}