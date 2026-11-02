<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Sign in"/></jsp:include>

<div class="su-auth">
    <div class="su-auth-side d-none d-md-flex">
        <img src="${ctx}/images/logo.png" alt="Sprinkle up by Aish logo">
        <p>Track your orders, request custom cakes and check out faster.</p>
    </div>
    <div class="su-auth-main">
        <h1 class="su-page-title">Welcome back</h1>
        <p class="text-muted">Sign in to your SprinkleUp account.</p>

        <form method="post" action="${ctx}/login" class="mt-3">
            <c:if test="${not empty next}"><input type="hidden" name="next" value="${fn:escapeXml(next)}"></c:if>
            <div class="mb-3">
                <label class="form-label" for="email">Email</label>
                <input class="form-control form-control-lg" type="email" id="email" name="email" required autocomplete="email" autofocus
                       value="${fn:escapeXml(email)}">
            </div>
            <div class="mb-4">
                <label class="form-label" for="password">Password</label>
                <input class="form-control form-control-lg" type="password" id="password" name="password" required autocomplete="current-password">
            </div>
            <button class="btn btn-su btn-lg w-100" type="submit">Sign in</button>
        </form>

        <c:url var="registerUrl" value="/register"><c:if test="${not empty next}"><c:param name="next" value="${next}"/></c:if></c:url>
        <p class="text-center mt-4 mb-0">New here? <a href="${fn:escapeXml(registerUrl)}">Create an account</a></p>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
