package ivlevks.simplenettyserver.netty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.CharsetUtil;
import ivlevks.simplenettyserver.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class MainServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final ThreadPoolExecutor executor;
    private final MainService service;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        boolean handshakeDone = false;

        while (!handshakeDone) {
            if (executor.getQueue().isEmpty()) {
                ctx.writeAndFlush(Unpooled.copiedBuffer("Server is ready to receive the data.\n", CharsetUtil.UTF_8));
                ctx.fireChannelActive();
                handshakeDone = true;
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            log.info("No data received. Connection will close");
            ctx.close();
        } else {
            super.exceptionCaught(ctx, cause);
        }
    }
}
