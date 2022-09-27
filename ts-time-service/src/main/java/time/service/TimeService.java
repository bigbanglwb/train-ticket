package time.service;

import edu.fudan.common.util.Response;
import org.springframework.http.HttpHeaders;

/**
 * @author Chenjie Xu
 * @date 2017/5/9.
 */
public interface TimeService {

    Response queryLoggingTime(HttpHeaders headers);

    Response clearTime(HttpHeaders headers);
}
