package ivlevks.simplenettyserver.netty.handlers;

import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class TimeoutHandler extends ReadTimeoutHandler {

    //TODO - its work only once
   public TimeoutHandler(int timeoutReading) {
        super(timeoutReading);
    }
}
