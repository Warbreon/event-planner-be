package com.cognizant.EventPlanner.security.jwt;
import com.cognizant.EventPlanner.util.ErrorResponseEntityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AccessDenied implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ErrorResponseEntityUtil.writeErrorResponse(response, HttpStatus.FORBIDDEN,
                "ACCESS DENIED", request.getRequestURI());
    }
}