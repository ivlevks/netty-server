package ivlevks.simplenettyserver.netty.handlers;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;
import ivlevks.simplenettyserver.configuration.NettyProperties;
import ivlevks.simplenettyserver.service.MainService;
import ivlevks.simplenettyserver.utils.DataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.concurrent.*;


/**
 * Main Handler
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class MainServerHandler extends ChannelInboundHandlerAdapter {

    private final ThreadPoolExecutor executor;
    private final MainService service;
    private final DataParser parser;
    private final NettyProperties nettyProperties;

    /**
     * Activate channel
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        sendWelcomeMessage(ctx);

    }


    /**
     * Send welcome msg if free thread exist
     * added Timeout waiting for receiving data
     * @param ctx
     */
    private void sendWelcomeMessage(ChannelHandlerContext ctx) {

        boolean hasFreeThread = false;

        while (!hasFreeThread) {

            if (executor.getActiveCount() < executor.getMaximumPoolSize()) {
                ctx.writeAndFlush(Unpooled.copiedBuffer("Server is ready to receive the data.\n", CharsetUtil.UTF_8));

                ctx.pipeline().addFirst(new ReadTimeoutHandler(nettyProperties.getTimeoutReading()));

                hasFreeThread = true;
            }
        }
    }

    /**
     * Read msg, start processing
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        removeTimeoutAwaitingData(ctx);

        int timeoutProcessingMessage = parser.getTimeout(msg);

        processingMessage(ctx, msg, timeoutProcessingMessage);

    }

    /**
     * Remove timeout
     * @param ctx
     */
    private void removeTimeoutAwaitingData(ChannelHandlerContext ctx) {

        if (ctx.pipeline().first().getClass().equals(ReadTimeoutHandler.class)) {
            ctx.pipeline().remove(ReadTimeoutHandler.class);
        }

    }

    /**
     * Processing message during timeout
     * return control on welcome message
     * @param ctx
     * @param msg
     * @param timeoutProcessingMessage
     */
    private void processingMessage(ChannelHandlerContext ctx, Object msg, int timeoutProcessingMessage) {

        Future<Object> future = executor.submit(() -> {
            service.processingMessage(msg);
            return null;
        });

        try {

            future.get(timeoutProcessingMessage, TimeUnit.SECONDS);

        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            future.cancel(true);

            log.info("Processing done");

            sendWelcomeMessage(ctx);
        }
    }

    /**
     * Exception, timeout handling
     * @param ctx
     * @param cause
     * @throws Exception
     */
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
