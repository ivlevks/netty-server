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

    public void processingMessage(Object msg) throws InterruptedException {

        for (int i = 0; i < 1000; i++) {
            Thread.sleep(1000);
            System.out.println("task running");
        }

    }
}
