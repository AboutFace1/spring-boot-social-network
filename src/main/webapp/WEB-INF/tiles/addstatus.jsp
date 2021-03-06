<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

    <div class="row">

        <div class="col-md-8 col-md-offset-2">

            <div class="panel panel-default">

                <div class="panel-heading">
                    <div class="panel-title">Add a status update</div>
                </div>

                    <form:form modelAttribute="statusUpdate"> <!-- NOT ERROR -->
                        <!-- ERROR while validating -->
                        <div class="errors">
                            <form:errors path="text"/>
                        </div>
                        <div class="form-group">
                            <form:textarea path="text" name="text" rows="10" cols="50"></form:textarea>
                        </div>

                        <input type="submit" name="submit" value="Add status" />
                    </form:form>

            </div>

            <div class="panel panel-default">

                <div class="panel-heading">
                    <div class="panel-title">Status update added on <fmt:formatDate pattern="EEEE d MMMM y 'at' H:mm:s" value="${latest.added}" /></div>

                </div>

                <div class="panel-body">
                    ${latestStatusUpdate.text} <!-- or latest -->
                </div>

            </div>

        </div>

    </div>

<script src="https://cdn.tiny.cloud/1/no-api-key/tinymce/5/tinymce.min.js" referrerpolicy="origin"></script>
<script type="text/javascript">
    tinymce.init({
        selector: 'textarea',
        plugins: "link"
    });
</script>