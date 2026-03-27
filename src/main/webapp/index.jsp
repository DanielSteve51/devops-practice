<html>
<body>
<h2>Hello. This build is done using Jenkins -> Sonar QualityGate Check -> ECR -> ECS v2</h2>

<form action="<%= request.getContextPath() %>/calculate" method="post">
    <input type="text" name="a" placeholder="Enter value1">
    <input type="text" name="b" placeholder="Enter value2">
    <br><br>
    <input type="submit" name="op" value="Add">
    <input type="submit" name="op" value="Subtract">
    <input type="submit" name="op" value="Multiply">
    <input type="submit" name="op" value="Divide">
    <input type="submit" name="op" value="Modulus">
</form>

</body>
</html>