<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="editing" value="${not empty customerId}"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="${editing ? 'Edit customer' : 'New customer'}"/></jsp:include>

<a class="small" href="${ctx}/admin/customers"><i class="bi bi-arrow-left"></i> Customers</a>
<h1 class="su-page-title mt-1">${editing ? 'Edit customer' : 'New customer'}</h1>

<c:set var="action" value="${editing ? '/admin/customers/' += customerId : '/admin/customers'}"/>
<form:form modelAttribute="customer" servletRelativeAction="${action}" method="post" cssClass="su-panel su-form">
    <div class="row g-3">
        <div class="col-md-6">
            <label class="form-label" for="fullName">Full name</label>
            <form:input path="fullName" cssClass="form-control" cssErrorClass="form-control is-invalid" maxlength="100"/>
            <form:errors path="fullName" cssClass="invalid-feedback"/>
        </div>
        <div class="col-md-6">
            <label class="form-label" for="email">Email</label>
            <form:input path="email" type="email" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
            <form:errors path="email" cssClass="invalid-feedback"/>
        </div>
        <div class="col-md-6">
            <label class="form-label" for="phone">Phone</label>
            <form:input path="phone" type="tel" cssClass="form-control" cssErrorClass="form-control is-invalid" placeholder="0771234567"/>
            <form:errors path="phone" cssClass="invalid-feedback"/>
        </div>
        <div class="col-md-6">
            <label class="form-label" for="address">Address</label>
            <form:input path="address" cssClass="form-control" cssErrorClass="form-control is-invalid" maxlength="255"/>
            <form:errors path="address" cssClass="invalid-feedback"/>
        </div>
    </div>
    <div class="d-flex gap-2 mt-4">
        <button class="btn btn-su" type="submit"><i class="bi bi-check-lg"></i> ${editing ? 'Save changes' : 'Add customer'}</button>
        <a class="btn btn-outline-secondary" href="${ctx}/admin/customers">Cancel</a>
    </div>
</form:form>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
