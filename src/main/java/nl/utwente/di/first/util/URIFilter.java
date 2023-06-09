package nl.utwente.di.first.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class URIFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response; 
        String path = req.getRequestURI().substring(req.getContextPath().length());
    
        if (path.startsWith("/api")) {
            request.getRequestDispatcher(path).forward(request, response);
        } else if (path.startsWith("/assets") || path.endsWith("/")) {
            chain.doFilter(request, response);
        } else if (path.endsWith(".html")) {
            ((HttpServletResponse) response).sendRedirect(path.substring(0, path.length()-5));
        } else {
            request.getRequestDispatcher(path + ".html").forward(request, response);
        }
    }
}
