<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <link href="<c:url value="/res/style.css"/>"  rel="stylesheet" type="text/css"/>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table>
    <tr>
    <b>
        <th>Date</th>
        <th>Time</th>
        <th>Description</th>
        <th>Calories</th>
    </b>
    </tr>

<jsp:useBean id="meals" scope="request" type="java.util.List"/>
<c:forEach var="meal" items="${meals}">
    <tr>
    <c:if test="${meal.excess}">
            <td><p1> ${meal.getDateTime().toLocalDate()} </p1></td>
            <td><p1> ${meal.getDateTime().toLocalTime()} </p1></td>
            <td><p1> ${meal.getDescription()} </p1></td>
            <td><p1> ${meal.getCalories()} </p1></td>
    </c:if>
        <c:if test="${!meal.excess}">
            <td><p2> ${meal.getDateTime().toLocalDate()} </p2></td>
            <td><p2> ${meal.getDateTime().toLocalTime()} </p2></td>
            <td><p2> ${meal.getDescription()} </p2></td>
            <td><p2> ${meal.getCalories()} </p2></td>
        </c:if>
    </tr>
</c:forEach>
</table>
</body>
</html>
