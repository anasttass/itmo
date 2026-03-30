package ru.stud.backend.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.stud.backend.model.ContextParams;
import ru.stud.backend.service.RequestStats;

import java.io.IOException;

public class FullParamFilter implements Filter
{
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

            stats.incrementTotal(); //+ к общему числу запросов, только тут
            try {
                if (x == null || y == null || r == null || x.isEmpty() || y.isEmpty() || r.isEmpty()) {
                    req.setAttribute(ContextParams.ERROR.getKey(), "Все поля должны быть заполнены");

                    stats.incrementUnFull();//+ к неполным

                    req.getRequestDispatcher("/index.jsp").forward(req, resp);
                    return;
                }
            } catch (Exception e) {
                req.setAttribute(ContextParams.ERROR.getKey(), "Ошибка при обработке данных: " + e.getMessage());
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
                return;
            }
        }
        filterChain.doFilter(req,resp);
    }
}
