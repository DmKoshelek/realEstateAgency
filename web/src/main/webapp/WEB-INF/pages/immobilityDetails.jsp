<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/templates" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="span" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<template:page>
    <form:form method="post" enctype="multipart/form-data" modelAttribute="immobilityDto">
        <sec:csrfInput/>
        <template:form_elem label="Name*" path="name"/>
        <c:if test="${empty create}">
            <div class="form-group">
                <label for="ownerLogin">Owner</label>
                <span class="form-control" id="ownerLogin"><c:out value="${immobilityDto.ownerLogin}"/></span>
            </div>
        </c:if>
        <template:form_elem label="Description*" path="description" type="textarea"/>
        <template:form_elem label="Price*" path="price"/>
        <template:form_elem label="City*" path="cityName"/>
        <template:form_elem label="Number of rooms*" path="numberOfRooms"/>
        <template:form_elem label="Square*" path="square"/>
        <template:form_elem label="Address*" path="address"/>

        <div class="form-group">
            <label for="typeImmobility">Type</label>
            <select id="typeImmobility" name="typeImmobility">
                <option ${immobilityDto.typeImmobility eq "HOUSE" ? 'selected' : ''} value="HOUSE">House</option>
                <option ${immobilityDto.typeImmobility eq "APARTMENT" ? 'selected' : ''} value="APARTMENT">Apartment
                </option>
            </select>
        </div>
        <div class="form-group">
            <label for="inputFile">Images</label>
            <input type="file" name="uploadedFiles" accept="image/*" id="inputFile" onchange="addFileInput()">
        </div>
        <div id="gallery">
            <c:forEach var="photo" items="${immobilityDto.photos}" varStatus="i">
                <div>
                    <a href="#" class="deleteImage">Delete</a>
                    <img class="previewImage" src="<c:url value="/images/${photo.path}"/>"/>
                    <form:hidden path="photos[${i.index}].id"/>
                </div>
            </c:forEach>
            <div class="previewImageTemplate">
                <a href="#" class="deleteImage">Delete</a>
                <img class="previewImage"/>
            </div>
        </div>
        <div class="form-group">
            <button type="submit" name="save" class="btn btn-success">
                Save
            </button>
            <c:if test="${empty create}">
                <button type="submit" name="remove" class="btn btn-danger">
                    Remove
                </button>
            </c:if>
        </div>
    </form:form>
</template:page>