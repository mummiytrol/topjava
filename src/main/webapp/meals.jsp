<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="res/style.css"  type="text/css"/>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<% List<MealTo> meals = (List) request.getAttribute("meals");
%>
<table>
    <b>
        <th width="120">Date</th>
        <th width="50">Time</th>
        <th width="260">Description</th>
        <th width="100">Calories</th>
    </b>
    </tr>
<%
    for(MealTo meal : meals) {
%>
    <tr>
        <%
            if (meal.isExcess()) {
        %>
        <p1>
    <td> <%= meal.getDateTime().toLocalDate()%></td>
    <td> <%= meal.getDateTime().toLocalTime()%></td>
    <td><%= meal.getDescription()%></td>
    <td><%= meal.getCalories()%></td>
    <td><%= meal.isExcess()%></td>
    </p1>
      <%  } if (!meal.isExcess()) { %>

        <p2>
            <td> <%= meal.getDateTime().toLocalDate()%></td>
            <td> <%= meal.getDateTime().toLocalTime()%></td>
            <td><%= meal.getDescription()%></td>
            <td><%= meal.getCalories()%></td>
            <td><%= meal.isExcess()%></td>
        </p2>

        <% }
    %>
    </tr>
<%
    }
%>
</table>
</body>
</html>
