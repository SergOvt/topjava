<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Users</title>
    <style>
        table {
            font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
            font-size: 14px;
            border-spacing: 0;
            text-align: center;
        }

        .list th {
            background: #BCEBDD;
            color: #ecffcc;
            text-shadow: 0 1px 1px #2D2020;
            padding: 10px 20px;
        }

        .list th, td {
            border-style: solid;
            border-width: 0 1px 1px 0;
            border-color: white;
        }

        .list th:first-child {
            border-top-left-radius: 10px;
        }

        .list th:last-child {
            border-top-right-radius: 10px;
            border-right: none;
        }

        .list td {
            padding: 10px 20px;
            background: #F8E391;
        }

        .list tr:last-child td:first-child {
            border-radius: 0 0 0 10px;
        }

        .list tr:last-child td:last-child {
            border-radius: 0 0 10px 0;
        }

        .list tr td:last-child {
            border-right: none;
        }

        .inputMeal input {
            font-size: 16px;
        }

        .inputMeal th {
            padding: 20px 0 10px 0;
        }

    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<section>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <table class="list" align="left" width="60%">
        <tr>
            <th width=5% height="30px">ID</th>
            <th width=10%>Еда</th>
            <th width=15%>Дата</th>
            <th width=10%>Время</th>
            <th width=10%>Ккал</th>
            <th width=5%>Изменить</th>
            <th width=5%>Удалить</th>
        </tr>
        <c:forEach var="meal" items="${meals}">
            <c:set var="color" value="color: forestgreen"/>
            <c:if test="${meal.isExceed()}">
                <c:set var="color" value="color: darkred"/>
            </c:if>
            <tr style="${color}">
                <td align="center">${meal.getId()}</td>
                <td align="center">${meal.getDescription()}</td>
                <td align="center">${meal.getDateTime().toString().substring(0, 10)}</td>
                <td align="center">${meal.getDateTime().toString().substring(11)}</td>
                <td align="center">${meal.getCalories()}</td>
                <form method="post">
                    <input type="hidden" name="action" value="readyUpdate">
                    <input type="hidden" name="id" value="${meal.getId()}">
                    <td>
                        <button type="submit" style="font-size: 16px; width: 100px">Update</button>
                    </td>
                </form>
                <form method="post">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="${meal.getId()}">
                    <td>
                        <button type="submit" style="font-size: 16px; width: 100px">Delete</button>
                    </td>
                </form>
            </tr>
        </c:forEach>
    </table>

    <table width="100%"/>

    <table class="inputMeal" width="60%">
        <tr>
            <th width=10% height="30px">Еда</th>
            <th width=30%>Дата/Время</th>
            <th width=10%>Ккал</th>
        </tr>
        <c:choose>
            <c:when test="${updatingMeal == null}">
                <form method="post">
                    <input type="hidden" name="action" value="add">
                    <tr>
                        <td><input type="text" name="description"></td>
                        <td><input type="datetime-local" name="dateTime"></td>
                        <td><input type="number" name="calories"></td>
                    </tr>
                    <tr>
                        <td colspan="3" align="center" style="padding: 15px 0 0 0">
                            <button type="submit" style="font-size: 16px; width: 100px">Add</button>
                        </td>
                    </tr>
                </form>
            </c:when>
            <c:otherwise>
                <form method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value=${updatingMeal.getId()}>
                    <tr>
                        <td><input type="text" name="description" value=${updatingMeal.getDescription()}></td>
                        <td><input type="datetime-local" name="dateTime" value=${updatingMeal.getDateTime()}></td>
                        <td><input type="number" name="calories" value=${updatingMeal.getCalories()}></td>
                    </tr>
                    <tr>
                        <td colspan="3" align="center" style="padding: 15px 0 0 0">
                            <button type="submit" style="font-size: 16px; width: 100px">Update</button>
                        </td>
                    </tr>
                </form>
            </c:otherwise>
        </c:choose>
    </table>
</section>
</body>
</html>


