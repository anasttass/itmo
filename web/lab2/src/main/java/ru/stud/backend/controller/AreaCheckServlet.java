package ru.stud.backend.controller;

import ru.stud.backend.model.ContextParams;
import ru.stud.backend.model.HitChecker;
import ru.stud.backend.model.Point;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.stud.backend.service.HistoryCollector;
import ru.stud.backend.service.HistoryService;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/area-check")
public class AreaCheckServlet extends HttpServlet {
    private HistoryService historyService;
    private HitChecker hitChecker;

    @Override
    public void init(){
        historyService = new HistoryCollector(getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request,response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            double x = Double.parseDouble(request.getParameter(ContextParams.X.getKey()));
            double r = Double.parseDouble(request.getParameter(ContextParams.R.getKey()));
            BigDecimal y = new BigDecimal(request.getParameter(ContextParams.Y.getKey()));

            boolean isHit=hitChecker.isHit(x,y,r);
            Point point = new Point(x, y, r,isHit);

            historyService.addPoint(point);

            request.setAttribute(ContextParams.POINT.getKey(), point);
            request.setAttribute(ContextParams.RESULTS.getKey(), historyService.getAllPoints());
            request.getRequestDispatcher("/result.jsp").forward(request, response);
        } catch (Exception e){

                request.setAttribute(ContextParams.ERROR.getKey(), "Ошибка при обработке данных: "+ e.getMessage());
                request.getRequestDispatcher("/index.jsp").forward(request,response);
        }
    }

}
