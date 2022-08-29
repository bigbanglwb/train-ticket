package fdse.microservice.filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import org.springframework.core.annotation.Order;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

import org.springframework.stereotype.Component;

@Slf4j
@Order(1)
@Component
@WebFilter(urlPatterns = {"/**"},filterName = "logRequestFilter")
public class logRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("TokenFilter init {}",filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("get request time:{}",System.nanoTime());
        chain.doFilter(request,response);//到下一个链

    }

    @Override
    public void destroy() {
        log.info("TokenFilter destroy");
    }
}