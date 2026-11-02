<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Create an account"/></jsp:include>

<div class="su-auth">
    <div class="su-auth-side d-none d-md-flex">
        <img src="${ctx}/images/logo.png" alt="Sprinkle up by Aish logo">
        <p>Ordered with us before? Use the same email and your past orders will show up in your account.</p>
    </div>
    <div class="su-auth-main">
        <h1 class="su-page-title">Create your account</h1>
        <p class="text-muted">It takes less than a minute.</p>

        <form:form modelAttribute="registerForm" servletRelativeAction="/register" method="post" cssClass="mt-3">
            <c:if test="${not empty next}"><input type="hidden" name="next" value="${fn:escapeXml(next)}"></c:if>
            <div class="mb-3">
                <label class="form-label" for="fullName">Full name</label>
                <form:input path="fullName" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="name"/>
                <form:errors path="fullName" cssClass="invalid-feedback"/>
            </div>
            <div class="row g-3 mb-3">
                <div class="col-sm-6">
                    <label class="form-label" for="email">Email</label>
                    <form:input path="email" type="email" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="email"/>
                    <form:errors path="email" cssClass="invalid-feedback"/>
                </div>
                <div class="col-sm-6">
                    <label class="form-label" for="phone">Phone</label>
                    <form:input path="phone" type="tel" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="tel" placeholder="0771234567"/>
                    <form:errors path="phone" cssClass="invalid-feedback"/>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label" for="address">Address <span class="small text-muted">(optional)</span></label>
                <form:input path="address" cssClass="form-control" autocomplete="street-address"/>
            </div>
            <div class="row g-3 mb-4">
                <div class="col-sm-6">
                    <label class="form-label" for="password">Password</label>
                    <form:password path="password" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="new-password"/>
                    <form:errors path="password" cssClass="invalid-feedback"/>
                </div>
                <div class="col-sm-6">
                    <label class="form-label" for="confirmPassword">Confirm password</label>
                    <form:password path="confirmPassword" cssClass="form-control" cssErrorClass="form-control is-invalid" autocomplete="new-password"/>
                    <form:errors path="confirmPassword" cssClass="invalid-feedback"/>
                </div>
            </div>
            <button class="btn btn-su btn-lg w-100" type="submit">Create account</button>
        </form:form>

        <p class="text-center mt-4 mb-0">Already have an account? <a href="${ctx}/login">Sign in</a></p>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
