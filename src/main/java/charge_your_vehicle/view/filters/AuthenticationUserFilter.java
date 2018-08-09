package charge_your_vehicle.view.filters;

import charge_your_vehicle.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
        filterName = "AuthenticationUserFilter",
        urlPatterns = {"/about2", "/find-the-closest-by-address2", "/find-the-closest-in-radius-by-address2", "/find-the-closest-in-radius2",
        "/find-the-closest2", "/search-by-country2", "/search-by-town2", "/statistics2"}
      )
public class AuthenticationUserFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.sendError(403);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}