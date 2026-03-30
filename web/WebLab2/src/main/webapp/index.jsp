<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.stud.backend.model.Point" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Начальная страница</title>
    <link href="css/style.css" rel="stylesheet">
</head>
<body>
<table class="maintable">
    <!-- шапка -->
    <tr>
        <td colspan="2">
            <div class="card">
                <table class="header">
                    <tr>
                        <td><b>ФИО:</b></td>
                        <td>Соснина Анастасия Владимировна</td>
                    </tr>
                    <tr>
                        <td><b>Группа:</b></td>
                        <td>P3219</td>
                    </tr>
                    <tr>
                        <td><b>Вариант:</b></td>
                        <td>9943</td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>

    
    <tr>
        <td width="50%">
            <div class="card">
                <form id="data-form" action="${pageContext.request.contextPath}/control" method="POST">
                    <table>
                        <tr>
                            <td><label for="x">Выберите x</label></td>
                            <td>
                                <label><input class="x-checkbox" type="checkbox" name="X" value="-5">-5</label>
                                <label><input class="x-checkbox" type="checkbox" name="X" value="-4">-4</label>
                                <label><input class="x-checkbox" type="checkbox" name="X" value="-3">-3</label>
                                <label><input class="x-checkbox" type="checkbox" name="X" value="-2">-2</label>
                                <label><input class="x-checkbox" type="checkbox" name="X" value="-1">-1</label>
                                <label><input class="x-checkbox" type="checkbox" name="X" value="0">0</label>
                                <label><input class="x-checkbox" type="checkbox" name="X" value="1">1</label>
                                <label><input class="x-checkbox" type="checkbox" name="X" value="2">2</label>
                                <label><input class="x-checkbox" type="checkbox" name="X" value="3">3</label>
                            </td>
                        </tr>
                        <tr>
                            <td><label for="y">Выберите y</label></td>
                            <td>
                                <input type="text" name="Y" id="y" placeholder="от -5 до 5">  <!--required pattern="-?[0-9]+(\\.[0-9]+)?"-->
                            </td>
                        </tr>
                        <tr>
                            <td><label for="r">Выберите r</label></td>
                            <td>
                                <label><input class="r-checkbox" type="checkbox" name="R" value="1">1</label>
                                <label><input class="r-checkbox" type="checkbox" name="R" value="2">2</label>
                                <label><input class="r-checkbox" type="checkbox" name="R" value="3">3</label>
                                <label><input class="r-checkbox" type="checkbox" name="R" value="4">4</label>
                                <label><input class="r-checkbox" type="checkbox" name="R" value="5">5</label>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                                <button type="submit">ПРОВЕРИТЬ</button>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <c:if test="${not empty error}">
                                    <p class="error">${error}</p>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </form>
                <div id="result"></div>
            </div>
        </td>

        <td width="50%" align="center">
            <div class="card"> Координатная плоскость
                <svg id="plane-canvas" viewBox="0 0 512 384" xmlns="http://www.w3.org/2000/svg">
                    <!-- Области -->
                    <g id="plane-areas" fill="rgba(0,150,255,0.3)" stroke="#70d6ff" stroke-width="1">
                        <!-- треугольник (0,-R)(-R,0)(0,0) -->
                        <polygon points="256,192 192,192 256,256" />

                        <!-- окружность -->
                        <path d="M256,192 L192,192 A64,64 0 0,1 256,128 Z" />

                        <!-- прямоугольник (R,0)(0,-R/2) -->
                        <rect x="256" y="192" width="64" height="32" />
                    </g>

                    <!-- Оси -->
                    <g stroke="#fff" stroke-width="1">
                        <line x1="0" y1="192" x2="512" y2="192" />
                        <line x1="256" y1="0" x2="256" y2="384" />
                        <text x="500" y="182" fill="#fff">x</text>
                        <text x="264" y="12" fill="#fff">y</text>
                    </g>

                    <!-- Деления -->
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
<script src="js/script.js" defer></script>
<script src="js/click-handler.js" defer></script>
</body>
</html>


