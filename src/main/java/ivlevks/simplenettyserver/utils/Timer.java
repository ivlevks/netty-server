package ivlevks.simplenettyserver.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Timer {


    public void startTimer(int ms) {
        long start = System.currentTimeMillis();
        long end = start + ms; // 60 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end) {
            // run
        }
    }
}
