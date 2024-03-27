package ivlevks.simplenettyserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MainService {

    public void start(Object msg) {
        System.out.println(msg);
    }

}
