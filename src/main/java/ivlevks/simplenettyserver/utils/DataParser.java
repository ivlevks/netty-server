package ivlevks.simplenettyserver.utils;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Parse received data.
 * For simplifying timeout goes first in msg,
 * then goes " " and then msg.
 * For example - "5 hello"
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DataParser {

    /**
     * Get processing timeout from receiving data
     * @param msg
     * @return
     */
    public int getTimeout(Object msg) {

        ByteBuf in = (ByteBuf) msg;
        String data = in.toString(CharsetUtil.UTF_8);

        return parseTimeout(data);
    }

    private int parseTimeout(String data) {

        String[] array = data.split(" ");
        return Integer.parseInt(array[0]);

    }
}
