package nl.utwente.di.first.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class URIFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        //Just casting the request and response to the correct subclass of ServletRequest/ServletResponse
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response; 
        
        //Getting the URI to the resource/file (this needs to be done this way, so that the prefix '/earnit/' is not included
        String path = req.getRequestURI().substring(req.getContextPath().length());
        
        //Getting the extension of the file that is requested, will return empty string if it does not specify an extension.
        String extension = getExtensionByStringHandling(path).orElse("");
        
        
        if (path.startsWith("/api")) {
            //If the request is aimed towards the api, we should just let it pass and not alter the URL in any way.
            //After this, the request will be handled by the next servlet matching its URL-pattern
            request.getRequestDispatcher(path).forward(request, response);
            return;
        } else if (path.startsWith("/student")) {
            HttpSession session = req.getSession();
            if (session.getAttribute("role") == null || !session.getAttribute("role").equals("STUDENT")) {
                System.out.println(session.getAttribute("role"));
                request.getRequestDispatcher("/401.html").forward(request,response);
            }
            
            // TODO implement a similar rule system for company and admin pages. Either by case specific rules or a generalisation.
            
        }

        if (extension.equals("")) {
            // IMPORTANT! Right now, the filter will interpret any URI without a specified extension as a html file
            // This means that any reference to a file of a different extension type should always have this specified in the URI 
            String filePath = request.getServletContext().getRealPath(path + ".html");
            File f = new File(filePath);
            if (f.exists()) {
                //The file exists so just fetch it (using forward so that the URL in the browser stays the same).
                request.getRequestDispatcher(path + ".html").forward(request,response);
            } else {
                //The file does not exist and thus the URL is invalid. Hence, fetch the 404 page.
                request.getRequestDispatcher("/404.html").forward(request,response);
            }
        } else if (extension.equals("html")) {
            // If the URI *does* contain a specified file extension, we should redirect the client instead of forwarding the request.
            // This makes sure that the URL displayed in the browser stays clear of extensions, although in reality we are fetching the URL with the extension.
            res.sendRedirect(req.getRequestURI().substring(0, req.getRequestURI().length() - extension.length() - 1));
        } else {
            chain.doFilter(request, response);
        }
    }
    
    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
