<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.stud.backend.service.RequestStats" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Статистика запросов</title>
    <link href="css/style.css" rel="stylesheet">
</head>
<body>
<table class="maintable" align="center" style="margin: 0 auto; text-align: center;">
    <tr>
        <td colspan="2" style="text-align: center;">
            <div class="card" style="text-align: center;">
                <h2 class="result-title" style="margin-bottom: 10px;">Статистика запросов</h2>
                <%
                    RequestStats stats = (RequestStats) application.getAttribute("requestStats");
                    if (stats == null) {
                %>
                    <h3>Пока нет статистики. Сделайте хотя бы один запрос.</h3>
                <% } else { %>
                    <table id="result-table" border="1" cellpadding="6" cellspacing="0" 
                           style="margin: 0 auto; text-align: center; font-weight: 500;">
                        <tr>
                            <th>Общее число запросов</th>
                            <th>Валидные</th>
                            <th>Невалидные</th>
                            <th>Неполные</th>
                        </tr>
                        <tr>
                            <td><%= stats.getTotal() %></td>
                            <td><%= stats.getValid() %></td>
                            <td><%= stats.getUnValid() %></td>
                            <td><%= stats.getUnFull() %></td>
                        </tr>
                    </table>
                <% } %>
            </div>
        </td>
    </tr>

    <tr>
        <td colspan="2" style="text-align: center;">
            <div class="card back" style="text-align: center;">
                <p class="back-text" style="margin-bottom: 10px;">
                    Чтобы вернуться назад на начальную страницу, нажмите кнопку =>
                </p>
                <button type="button" id="goback" class="back-button">BACK</button>
            </div>
        </td>
    </tr>
</table>

<script src="js/script.js"></script>
</body>
</html>
