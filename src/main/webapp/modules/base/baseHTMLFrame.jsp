<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="template-engine" uri="http://kwonnam.pe.kr/jsp/template-inheritance" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html ng-cloak>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title></title>        
        <link href="<c:url value='/resources/vendor/css/bootstrap.css'/>" rel="stylesheet" tpye="text/css">
        <link href="<c:url value='/resources/modules/css/base.css'/>" rel="stylesheet" tpye="text/css">
        <template-engine:block name="styles"></template-engine:block>
    </head>
    <body>
        <div class="base-content">
            <template-engine:block name="content"></template-engine:block>
        </div>
    </body>
    <script src="<c:url value='/resources/vendor/js/jquery.js'/>"></script> 
    <script src="<c:url value='/resources/vendor/js/angular.js'/>"></script>
    <template-engine:block name="scripts"></template-engine:block>
</html>
