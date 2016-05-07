package pl.com.tegess.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Szymek on 13/12/15.
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter extends OncePerRequestFilter {
    private static final Log LOG = LogFactory.getLog(CORSFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");

        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
            LOG.trace("Sending Header....");
            // CORS "pre-flight" request
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.addHeader("Access-Control-Allow-Headers", "Authorization, accept");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With, X-AUTH-TOKEN");
            response.addHeader("Access-Control-Max-Age", "1500");
        }
        if(!"OPTIONS".equals(request.getMethod())){
            filterChain.doFilter(request, response);
        }


    }

}
