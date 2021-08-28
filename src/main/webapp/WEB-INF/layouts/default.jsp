<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title><tiles:insertAttribute name="title" /></title>

    <c:set var="contextRoot" value="${pageContext.request.contextPath}" />

    <!-- Bootstrap -->
    <link href="${contextRoot}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextRoot}/css/main.css" rel="stylesheet">

    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
</head>
<body>


<!-- Static navbar -->
<nav class="navbar navbar-default navbar-static-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">Spring Boot tutorial</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">

            <sec:authorize access="!isAuthenticated()">
            <li><a href="${contextRoot}/login">Login</a></li>
            </sec:authorize>

            <sec:authorize access="isAuthenticated()">
            <li><a href="javascript:$('#logoutForm').submit();">Logout</a></li>
            <ul class="nav navbar-nav">
                <li class="active"><a href="${contextRoot}/">Home</a></li>
                <li><a href="${contextRoot}/about">about</a></li>

            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="${contextRoot}/addstatus">Add status</a></li>

            </ul>
            </sec:authorize>
        </div><!--/.nav-collapse -->
    </div>
</nav>

<c:url var="lougoutLink" value="/logout" />
<form id="logoutForm" method="post" action="${lougoutLink}" >
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

<div class="container">
    <tiles:insertAttribute name="content" />
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-Piv4xVNRyMGpqkS2by6br4gNJ7DXjqk09RmUpJ8jgGtD7zP9yug3goQfGII0yAns" crossorigin="anonymous"></script>


</body>
</html>