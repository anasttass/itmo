package ru.stud.backend.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.stud.backend.model.ContextParams;
import ru.stud.backend.service.RequestStats;

import java.io.IOException;
import java.math.BigDecimal;

public class ValidationFilter implements Filter {

    private RequestStats stats;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext context = filterConfig.getServletContext();
        stats = (RequestStats) context.getAttribute(ContextParams.REQUESTSTATS.getKey());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String x = req.getParameter(ContextParams.X.getKey());
            String y = req.getParameter(ContextParams.Y.getKey());
            String r = req.getParameter(ContextParams.R.getKey());

            try {
                if (Double.parseDouble(x) < -5 || Double.parseDouble(x) > 3 || Double.parseDouble(r) < 1 || Double.parseDouble(r) > 5
                        || new BigDecimal(y).abs().compareTo(BigDecimal.valueOf(5)) > 0) {
                    req.setAttribute(ContextParams.ERROR.getKey(), "Параметры выходят за рамки диапазона");

                    stats.incrementUnValid();//+ к невалидным

                    req.getRequestDispatcher("/index.jsp").forward(req, resp);
                    return;
                } else {
                    stats.incrementValid();//+ к валидным
                }
            } catch (NumberFormatException e) {
                req.setAttribute(ContextParams.ERROR.getKey(), "Некорректный формат числа.");
                stats.incrementUnValid();
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
                return;
            }
        }
        filterChain.doFilter(req,resp);
    }
}
