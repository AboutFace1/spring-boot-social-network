<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">
    <div class="col-md-12 resutls-noresult">
        <c:if test="${empty results}">
            No results.
        </c:if>
    </div>
</div>


<c:forEach var="result" items="${results}">

    <c:url var="profilePhoto" value="/profilephoto/${result.userId}"/>

    <div class="row">
        <div class="col-md-12">

            <div class="results-photo">
                <img id="profilePhotoImage" src="${profilePhoto}"/>
            </div>

            <div class="results-details">

                <div class="results-name">
                    <c:out value="${result.firstname}" /> <c:out value="${result.surname}" />
                </div>
                <c:forEach var="interest" items="${result.interests}">
                    ${interest}
                </c:forEach>
            </div>


            </p>

        </div>
    </div>

</c:forEach>


