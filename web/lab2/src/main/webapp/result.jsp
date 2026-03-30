<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.stud.backend.model.Point" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Страница результата</title>
    <link href="css/style.css" rel="stylesheet">
</head>
<body>
    <table class="maintable">
    <tr>
        <td colspan="2">
            <div class="card">
                <table class="header">
                    <tr><td><b>ФИО:</b></td><td>Соснина Анастасия Владимировна</td></tr>
                    <tr><td><b>Группа:</b></td><td>P3219</td></tr>
                    <tr><td><b>Вариант:</b></td><td>9943</td></tr>
                </table>
            </div>
        </td>
    </tr>

    <tr>
        <!-- левая часть: таблица результата -->
        <td width="50%" valign="top">
            <div class="card">
                <h2 class="result-title">Результат</h2>
                <% Point point = (Point) request.getAttribute("point");
                   if (point == null) { %>
                    <h3>Нет результата</h3>
                <% } else { %>
                    <table id="result-table" border="1" cellpadding="4" cellspacing="0">
                        <tr>
                            <th>X</th><th>Y</th><th>R</th><th>Попадание</th><th>Время</th>
                        </tr>
                        <tr>
                            <td><%= point.getX() %></td>
                            <td><%= point.getY() %></td>
                            <td><%= point.getR() %></td>
                            <td><%= point.getHit() ? "Попадает" : "Мимо" %></td>
                            <td><%= point.getTime() %></td>
                        </tr>
                    </table>
                <% } %>
            </div>
        </td>

        <!-- правая: координатная плоскость -->
        <td width="50%" align="center" valign="top">
            <div class="card">
                Координатная плоскость
                <svg id="plane-canvas" viewBox="0 0 512 384" xmlns="http://www.w3.org/2000/svg">
                    <g id="plane-areas" fill="rgba(0,150,255,0.3)" stroke="#70d6ff" stroke-width="1">
                        <polygon points="256,192 192,192 256,256" />
                        <path d="M256,192 L192,192 A64,64 0 0,1 256,128 Z" />
                        <rect x="256" y="192" width="64" height="32" />
                    </g>

                    <g stroke="#fff" stroke-width="1">
                        <line x1="0" y1="192" x2="512" y2="192" />
                        <line x1="256" y1="0" x2="256" y2="384" />
                        <text x="500" y="182" fill="#fff">x</text>
                        <text x="264" y="12" fill="#fff">y</text>
                    </g>

                    <g fill="#fff" font-size="12" text-anchor="middle">
                        <text x="192" y="182">-R</text>
                        <text x="224" y="182">-R/2</text>
                        <text x="288" y="182">R/2</text>
                        <text x="320" y="182">R</text>
                        <text x="264" y="128">R</text>
                        <text x="264" y="160">R/2</text>
                        <text x="264" y="224">-R/2</text>
                        <text x="264" y="256">-R</text>
                    </g>

                    <!-- <circle id="hit-circle" r="5" cx="-100" cy="-100" fill="#ffcc70"/> -->
                </svg>
            </div>
        </td>
    </tr>
</table>


    <div class="card">
        <h2 class="memory-title">История</h2>
        <div class="history">
            <% List <Point> history = (List <Point>) request.getAttribute("results");
            if (history==null){
            %>
            <h3>Записи в истории отсутствуют</h3>
            <% } else { %>
            <div id="memory">
            <table border="1" cellpadding="4" cellspacing="0">
                <tr>
                    <td>X</td>
                    <td>Y</td> 
                    <td>R</td> 
                    <td>Попадание</td> 
                    <td>Время</td>                 
                </tr>
                <script> const allPoints = []; </script>
                <% for (Point p : history){%>
                <tr>
                    <td><%= p.getX() %></td>
                    <td><%= p.getY() %></td> 
                    <td><%= p.getR() %></td> 
                    <td><%= p.getHit() ? "Попадает" : "Мимо" %></td> 
                    <td><%= p.getTime() %></td>  
                </tr>
                <script>
                    allPoints.push({
                    x: <%= p.getX() %>,
                    y: "<%= p.getY() %>",
                    r: <%= p.getR() %>,
                    hit: <%= p.getHit() %>
                    });
                </script>
                <% } %>
            </table>
            </div>
            <% } %>
        </div>
    </div>

    <div class="card back">
        <p class="back-text">Чтобы вернуться назад и попробовать снова, нажмите кнопку BACK. Чтобы открыть статистику, нажмите кнопку STATS</p>
        <button type="button" id="goback" class="back-button">BACK</button>
        <button type="button" id="goadmin" class="back-button" onclick="window.location.href='admin.jsp'">STATS</button>
    </div>
<script>//отрисовка всех точек
        document.addEventListener('DOMContentLoaded',()=>{
        allPoints.forEach(p => drawPoint(p.x, p.y, p.r, p.hit));
        });
</script>
<script src="js/script.js"></script>
<script src="js/click-handler.js"></script>
</body>
</html>