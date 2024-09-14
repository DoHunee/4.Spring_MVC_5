package hello.login.web.interceptor;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID, uuid);
        
        // Handler가 HandlerMethod 타입일 때만 로깅
        if (handler instanceof HandlerMethod hm) {
            // Controller 메서드에 대한 정보 로깅
            log.info("Controller method: {}#{}", hm.getBeanType().getName(), hm.getMethod().getName());
        }
        
        log.info("REQUEST [UUID: {}][URI: {}][Handler: {}]", uuid, requestURI, handler);
        return true; // false일 경우 진행 중단
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @SuppressWarnings("null") ModelAndView modelAndView) throws Exception {
        log.info("POST-HANDLE [ModelAndView: {}]", modelAndView);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @SuppressWarnings("null") Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);
        log.info("RESPONSE [UUID: {}][URI: {}]", logId, requestURI);
        
        if (ex != null) {
            log.error("AFTER COMPLETION ERROR!!", ex);
        }
    }

}