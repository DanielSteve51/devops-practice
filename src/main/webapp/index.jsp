<html>
<body>
<h2>Hello. This build is done using Jenkins -> Sonar QualityGate Check -> Nexus -> Tomcat v7</h2>

<form action="<%= request.getContextPath() %>/divide" method="post">
    <input type="text" name="a" placeholder="Enter value1">
    <input type="text" name="b" placeholder="Enter value2">
    <input type="submit" value="dividedValue">
</form>

</body>
</html>
