<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="My details"/></jsp:include>

<a class="small" href="${ctx}/account"><i class="bi bi-arrow-left"></i> My account</a>
<h1 class="su-page-title mt-1">My details</h1>

<div class="row g-4">
    <div class="col-lg-7">
        <form:form modelAttribute="profileForm" servletRelativeAction="/account/edit" method="post" cssClass="su-panel">
            <h2>Contact details</h2>
            <div class="mb-3">
                <label class="form-label" for="emailShown">Email</label>
                <input class="form-control" id="emailShown" value="${fn:escapeXml(currentUser.email)}" disabled>
                <div class="form-text">Your email is how you sign in, so it can't be changed here.</div>
            </div>
            <div class="mb-3">
                <label class="form-label" for="fullName">Full name</label>
                <form:input path="fullName" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                <form:errors path="fullName" cssClass="invalid-feedback"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="phone">Phone</label>
                <form:input path="phone" type="tel" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                <form:errors path="phone" cssClass="invalid-feedback"/>
            </div>
            <div class="mb-4">
                <label class="form-label" for="address">Address</label>
                <form:input path="address" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                <form:errors path="address" cssClass="invalid-feedback"/>
            </div>
            <button class="btn btn-su" type="submit"><i class="bi bi-check-lg"></i> Save details</button>
        </form:form>
    </div>
    <div class="col-lg-5">
        <form class="su-panel" method="post" action="${ctx}/account/password">
            <h2>Change password</h2>
            <div class="mb-3">
                <label class="form-label" for="currentPassword">Current password</label>
                <input class="form-control" type="password" id="currentPassword" name="currentPassword" required autocomplete="current-password">
            </div>
            <div class="mb-3">
                <label class="form-label" for="newPassword">New password</label>
                <input class="form-control" type="password" id="newPassword" name="newPassword" required minlength="6" maxlength="72" autocomplete="new-password">
            </div>
            <div class="mb-4">
                <label class="form-label" for="confirmPassword">Confirm new password</label>
                <input class="form-control" type="password" id="confirmPassword" name="confirmPassword" required minlength="6" maxlength="72" autocomplete="new-password">
            </div>
            <button class="btn btn-outline-su" type="submit">Change password</button>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
