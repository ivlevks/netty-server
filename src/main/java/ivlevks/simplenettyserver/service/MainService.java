package ivlevks.simplenettyserver.service;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MainService {

    public void processingMessage(Object msg) {

        ByteBuf in = (ByteBuf) msg;
        String data = in.toString(CharsetUtil.UTF_8);


    }

}
