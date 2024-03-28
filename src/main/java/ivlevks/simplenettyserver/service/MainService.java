package ivlevks.simplenettyserver.service;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import ivlevks.simplenettyserver.entity.Data;
import ivlevks.simplenettyserver.repository.DataRepository;
import ivlevks.simplenettyserver.utils.DataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MainService {

    private final DataRepository dataRepository;
    private final DataParser parser;

    /**
     * Save data from database
     * log about running task every second
     * @param msg
     * @throws InterruptedException
     */
    public void processingMessage(Object msg) throws InterruptedException {

        int timeout = parser.getTimeout(msg);
        String message = parser.getMessage(msg);

        dataRepository.save(new Data(timeout, message));

        for (int i = 0; i < 1000; i++) {
            Thread.sleep(1000);
            log.info("task running");
        }
    }
}
