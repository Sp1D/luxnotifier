<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Luxnotifier login page</title>
    <script type="text/javascript" src="<c:url value='/static/jquery-3.3.1.js'/>"></script>
    <script>
        var contextPath = '<%=application.getContextPath()%>';
        function deleteSubscription(id) {
            $.ajax({
                url: contextPath + '/subscription/' + id,
                type: 'DELETE',
                beforeSend: function(request) {
                  request.setRequestHeader('${_csrf.headerName}', '${_csrf.token}');
                },
                success: function(result) {
                    window.location.reload();
                }
            });
        }
    </script>
</head>
<body>
<h1>Subscribe for service</h1>
<sf:form action="subscription" modelAttribute="subscription">
    <sf:label path="serviceId">Select service:</sf:label><sf:select path="serviceId" items="${services}"/><br/>
    <sf:label path="languageId">Select language:</sf:label><sf:select path="languageId" items="${languages}"/><br/>
    <sf:label path="searchUntilDate">Search visits until:</sf:label><input type="date" id="searchUntilDate" name="searchUntilDate"/><br/>
    <sf:label path="bookingEnabled">Book a visit automatically?</sf:label><sf:checkbox path="bookingEnabled"/><br/>
    <input type="submit"/>
</sf:form>
<br>
<h1>Current subscriptions</h1>
<ul>
    <c:forEach items="${subscriptions}" var="subscription">
        <li><c:out value="${subscription.serviceName} (${subscription.languageName} language) until ${subscription.searchUntilDate} ${subscription.bookingEnabled ? 'Auto booking' : 'No booking'}"/>&nbsp;
            <a href="#" onclick="deleteSubscription(${subscription.serviceId})">Cancel</a></li>
    </c:forEach>
</ul>
</body>
</html>