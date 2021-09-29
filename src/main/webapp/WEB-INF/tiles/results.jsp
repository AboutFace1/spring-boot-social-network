<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jwp"%>

<div class="row">
    <div class="col-md-12 resutls-noresult">
        <c:if test="${empty page.content}">
            No results.
        </c:if>
    </div>
</div>

<c:url var="searchUrl" value="/search?s=${s}" />
<c:url var="img" value="/img" />

<div class="row">
    <div class="col-md-12">
    <jwp:pagination url="${searchUrl}" page="${page}" size="3" />
    </div>
</div>

<c:forEach var="result" items="${page.content}">

    <c:url var="profilePhoto" value="/profilephoto/${result.userId}"/>
    <c:url var="profileLink" value="/profile/${result.userId}"/>
    <c:url var="charviewLink" value="/chatview/${result.userId}"/>

    <div class="row person-row">
        <div class="col-md-12">

            <div class="results-photo">
                <a href="${profileLink}"><img id="profilePhotoImage" src="${profilePhoto}"/></a>
            </div>

            <div class="results-details">

                <div class="results-name">
                    <a href="${profileLink}"><c:out value="${result.firstname}" /> <c:out value="${result.surname}" /></a>
                </div>

                <div class="results-interests">
                    <c:forEach var="interest" items="${result.interests}" varStatus="status">

                        <c:url var="interestLink" value="/search?s=${interest}" />
                        <a href="${interestLink}"> <c:out value="${interest}" /></a>

                        <c:if test="${!status.last}"> | </c:if>
                    </c:forEach>
                </div>
                <div>
                    <div class="results-contact">
                        <a href="${charviewLink}"><img src="${img}/message.jpg"
                            alt="contact this user"></a>
                    </div>
                </div>
            </div>


            </p>

        </div>
    </div>

</c:forEach>


