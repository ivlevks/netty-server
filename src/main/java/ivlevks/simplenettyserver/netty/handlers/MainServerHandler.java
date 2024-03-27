package ivlevks.simplenettyserver.netty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;
import ivlevks.simplenettyserver.service.MainService;
import ivlevks.simplenettyserver.utils.DataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.concurrent.*;

@Component
@Slf4j
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class MainServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final ThreadPoolExecutor executor;
    private final MainService service;
    private final DataParser parser;

    /**
     * Doing handshake with client
     * if exists free threads
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        boolean handshakeDone = false;

        while (!handshakeDone) {
            if (executor.getActiveCount() < executor.getMaximumPoolSize()) {
                ctx.writeAndFlush(Unpooled.copiedBuffer("Server is ready to receive the data.\n", CharsetUtil.UTF_8));
                ctx.fireChannelActive();
                handshakeDone = true;
            }
        }
    }

    /**
     * Reading msg, first time remove ReadTimeOutHandler,
     * get time from data, create task processing input message,
     * cancelling processing after timeout
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (ctx.pipeline().first().getClass().equals(ReadTimeoutHandler.class)) {
            ctx.pipeline().remove(ReadTimeoutHandler.class);
        }

        int timeout = parser.getTimeout(msg);

        Future<Object> future = executor.submit(() -> {
            service.processingMessage(msg);
            return null;
        });

        try {
            future.get(timeout, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            future.cancel(true);
            log.info("Processing done");
        }
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
