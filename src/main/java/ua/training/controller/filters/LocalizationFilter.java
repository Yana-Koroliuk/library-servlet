package ua.training.controller.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;

/**
 *
 *  Class of localization filter
 *
 */
public class LocalizationFilter implements Filter {
    public static Locale locale = new Locale("en");

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String lang = request.getParameter("language");
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (lang != null) {
            if (lang.equals("en")) {
                locale = new Locale("en");
                servletRequest.getSession().setAttribute("language", "en");
            } else {
                locale = new Locale("uk");
                servletRequest.getSession().setAttribute("language", "uk");
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
