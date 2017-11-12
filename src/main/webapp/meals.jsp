<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <h2>Meals</h2>
    <hr/>
    <h3>Filter</h3>
    <table width="35%">
        <form name ="filterForm" method="get" action="meals">
            <tr>
                <td width="8%" height="40px">
                    From date:
                </td>
                <td width="10%">
                    <input type="date" name="fromDate" value="${fromDate}">
                </td>
                <td width="7%">
                    From time:
                </td>
                <td width="10%">
                    <input type="time" name="fromTime" value="${fromTime}">
                </td>
            </tr>
            <tr>
                <td width="8%" height="50px">
                    To date:
                </td>
                <td width="10%">
                    <input type="date" name="toDate" value="${toDate}">
                </td>
                <td width="7%">
                    To time:
                </td>
                <td width="10%">
                    <input type="time" name="toTime" value="${toTime}">
                </td>
            </tr>
            <tr>
                <td colspan="4" height="50px" align="center">
                    <button type="submit" style="font-size: 15px" onclick="clearForm()">Clear</button>
                    <button type="submit" style="font-size: 15px">Apply</button>
                </td>
            </tr>
            <script>
                function clearForm() {
                    filterForm.fromDate.value = null;
                    filterForm.toDate.value = null;
                    filterForm.fromTime.value = null;
                    filterForm.toTime.value = null;
                }
            </script>
        </form>
    </table>
    <hr/>

    <table>
        <tr>
            <td height="15"></td>
    <a href="meals?action=create">Add Meal</a>
            </td>
        </tr>
    </table>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>