package ivlevks.simplenettyserver.utils;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataParser {

    public int getTimeout(Object msg) {
        ByteBuf in = (ByteBuf) msg;
        String data = in.toString(CharsetUtil.UTF_8);

        return parseTimeoutProcessingData(data);
    }

    private int parseTimeoutProcessingData(String data) {
        String[] array = data.split(" ");
        return Integer.parseInt(array[0]);
    }
}
