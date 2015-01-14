
package com.antrou.dubbo.remoting.netty4;

import java.io.IOException;
import java.util.List;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.io.Bytes;
import com.alibaba.dubbo.common.io.UnsafeByteArrayInputStream;
import com.alibaba.dubbo.common.io.UnsafeByteArrayOutputStream;
import com.alibaba.dubbo.remoting.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * NettyCodecAdapter.
 * 
 * @author qian.lei
 */
final class NettyCodecAdapter {

    private final ChannelHandler encoder = new InternalEncoder();
    
    private final ChannelHandler decoder = new InternalDecoder();

    private final Codec          codec;
    
    private final URL            url;
    
    private final int            bufferSize;
    
    private final com.alibaba.dubbo.remoting.ChannelHandler handler;

    public NettyCodecAdapter(Codec codec, URL url, com.alibaba.dubbo.remoting.ChannelHandler handler) {
        this.codec = codec;
        this.url = url;
        this.handler = handler;
        int b = url.getPositiveParameter(Constants.BUFFER_KEY, Constants.DEFAULT_BUFFER_SIZE);
        this.bufferSize = b >= Constants.MIN_BUFFER_SIZE && b <= Constants.MAX_BUFFER_SIZE ? b : Constants.DEFAULT_BUFFER_SIZE;
    }

    public ChannelHandler getEncoder() {
        return encoder;
    }

    public ChannelHandler getDecoder() {
        return decoder;
    }

    @ChannelHandler.Sharable
    private class InternalEncoder extends MessageToByteEncoder<Object> {
        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            UnsafeByteArrayOutputStream os = new UnsafeByteArrayOutputStream(1024);
            com.antrou.dubbo.remoting.netty4.NettyChannel channel = com.antrou.dubbo.remoting.netty4.NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
            try {
                codec.encode(channel, os, msg);
            } finally {
                com.antrou.dubbo.remoting.netty4.NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }
            out.writeBytes(os.toByteBuffer());
        }
    }

    private class InternalDecoder extends ByteToMessageDecoder {

        private int    mOffset = 0, mLimit = 0;
        private byte[] mBuffer = null;

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
            int readable = input.readableBytes();
            if (readable <= 0) {
                return;
            }
            int off, limit;
            byte[] buf = mBuffer;
            if (buf == null) {
                buf = new byte[bufferSize];
                off = limit = 0;
            } else {
                off = mOffset;
                limit = mLimit;
            }
            com.antrou.dubbo.remoting.netty4.NettyChannel channel = com.antrou.dubbo.remoting.netty4.NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
            boolean remaining = true;
            Object msg;
            UnsafeByteArrayInputStream bis;
            try {
                do {
                    // read data into buffer.
                    int read = Math.min(readable, buf.length - limit);
                    input.readBytes(buf, limit, read);
                    limit += read;
                    readable -= read;
                    bis = new UnsafeByteArrayInputStream(buf, off, limit - off);
                    // decode object.
                    do {
                        try {
                            msg = codec.decode(channel, bis);
                        } catch (IOException e) {
                            remaining = false;
                            throw e;
                        }
                        if (msg == Codec.NEED_MORE_INPUT) {
                            if (off == 0) {
                                if (readable > 0) {
                                    buf = Bytes.copyOf(buf, buf.length << 1);
                                }
                            } else {
                                int len = limit - off;
                                System.arraycopy(buf, off, buf, 0, len); // adjust buffer.
                                off = 0;
                                limit = len;
                            }
                            break;
                        } else {
                            int pos = bis.position();
                            if (off == pos) {
                                remaining = false;
                                throw new IOException("Decode without read data.");
                            }
                            if (msg != null) {
                                out.add(msg);
                            }
                            off = pos;
                        }
                    } while (bis.available() > 0);
                } while (readable > 0);
            } finally {
                if (remaining) {
                    int len = limit - off;
                    if (len < buf.length / 2) {
                        System.arraycopy(buf, off, buf, 0, len);
                        off = 0;
                        limit = len;
                    }
                    mBuffer = buf;
                    mOffset = off;
                    mLimit = limit;
                } else {
                    mBuffer = null;
                    mOffset = mLimit = 0;
                }
                com.antrou.dubbo.remoting.netty4.NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }
        }
    }
}