<%@page contentType="text/html; charset=UTF-8" %>

<%@taglib prefix="template-engine" uri="http://kwonnam.pe.kr/jsp/template-inheritance"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<template-engine:extends name="/modules/base/baseHTMLFrame.jsp">
   <template-engine:put block="styles" type="REPLACE">
        <link href="<c:url value='/resources/vendor/css/ng-tags-input.css'/>" rel="stylesheet" tpye="text/css">
        <link href="<c:url value='/resources/vendor/css/angular-ui-switch.css'/>" rel="stylesheet" tpye="text/css">
        <link href="<c:url value='/resources/libs/css/modalForm.css'/>" rel="stylesheet" tpye="text/css">
        <link href="<c:url value='/resources/modules/css/schedulesList.css'/>" rel="stylesheet" tpye="text/css">
    </template-engine:put>
    <template-engine:put block="content" type="REPLACE">
        <schedules-list></schedules-list>
    </template-engine:put>
    <template-engine:put block="scripts" type="REPLACE">
        <script src="<c:url value='/resources/vendor/js/angular-ui-switch.js'/>"></script>
        <script src="<c:url value='/resources/vendor/js/ng-tags-input.js'/>"></script>
        <script src="<c:url value='/resources/libs/js/customInputs.min.js'/>"></script>
        <script src="<c:url value='/resources/libs/js/arrayUtils.min.js'/>"></script>
        <script src="<c:url value='/resources/libs/js/modalForm.min.js'/>"></script>
        <script src="<c:url value='/resources/libs/js/rest.min.js'/>"></script>
        <script src="<c:url value='/resources/libs/js/messages.min.js'/>"></script>
        <script src="<c:url value='/resources/modules/js/schedulesList.min.js'/>"></script>        
        <script type="text/javascript">
            (function(document){
                angular.module("schedulesListModule").constant("URL_RESOURCES", "<c:url value='/resources/'/>");
                
                angular.bootstrap(document, ["schedulesListModule"]);
            })(document);
        </script> 
    </template-engine:put>
</template-engine:extends>