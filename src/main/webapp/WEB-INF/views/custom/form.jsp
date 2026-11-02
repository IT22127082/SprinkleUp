<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="editing" value="${not empty orderId}"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="${editing ? 'Edit custom cake request' : 'Custom cake request'}"/></jsp:include>

<c:choose>
    <c:when test="${editing}"><a class="small" href="${ctx}/custom-orders/${orderId}"><i class="bi bi-arrow-left"></i> Back to request #${orderId}</a></c:when>
    <c:otherwise><a class="small" href="${ctx}/account"><i class="bi bi-arrow-left"></i> My account</a></c:otherwise>
</c:choose>

<div class="row g-4 mt-0">
    <div class="col-lg-8">
        <h1 class="su-page-title">${editing ? 'Edit your request' : 'Design your dream cake'}</h1>
        <p class="text-muted">Tell us what you have in mind. Aish will look at your idea and send you a price. Nothing is charged until you accept the quote.</p>

        <c:set var="action" value="${editing ? '/custom-orders/' += orderId : '/custom-orders'}"/>
        <form:form modelAttribute="customOrderForm" servletRelativeAction="${action}" method="post" cssClass="su-panel">
            <h2 class="h4">The cake</h2>
            <div class="row g-3 mb-4">
                <div class="col-sm-6">
                    <label class="form-label" for="occasion">Occasion</label>
                    <form:select path="occasion" cssClass="form-select" cssErrorClass="form-select is-invalid">
                        <form:option value="">Choose…</form:option>
                        <form:options items="${occasions}"/>
                    </form:select>
                    <form:errors path="occasion" cssClass="invalid-feedback"/>
                </div>
                <div class="col-sm-6">
                    <label class="form-label" for="flavour">Flavour</label>
                    <form:select path="flavour" cssClass="form-select" cssErrorClass="form-select is-invalid">
                        <form:option value="">Choose…</form:option>
                        <form:options items="${flavours}"/>
                    </form:select>
                    <form:errors path="flavour" cssClass="invalid-feedback"/>
                </div>
                <div class="col-6 col-sm-4">
                    <label class="form-label" for="weightKg">Weight (kg)</label>
                    <form:input path="weightKg" type="number" step="0.5" min="0.5" max="20" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                    <form:errors path="weightKg" cssClass="invalid-feedback"/>
                </div>
                <div class="col-6 col-sm-4">
                    <label class="form-label" for="tiers">Tiers</label>
                    <form:select path="tiers" cssClass="form-select" cssErrorClass="form-select is-invalid">
                        <c:forEach var="t" begin="1" end="5"><form:option value="${t}">${t}</form:option></c:forEach>
                    </form:select>
                    <form:errors path="tiers" cssClass="invalid-feedback"/>
                </div>
                <div class="col-sm-4 d-flex align-items-end">
                    <div class="form-check mb-2">
                        <form:checkbox path="eggless" cssClass="form-check-input" id="eggless"/>
                        <label class="form-check-label" for="eggless">Eggless</label>
                    </div>
                </div>
            </div>

            <h2 class="h4">The look</h2>
            <div class="mb-3">
                <label class="form-label" for="design">Design and decoration</label>
                <form:textarea path="design" rows="4" maxlength="1000" cssClass="form-control" cssErrorClass="form-control is-invalid"
                               placeholder="e.g. Pastel pink buttercream, gold drip, fresh flowers on top, unicorn theme…"/>
                <form:errors path="design" cssClass="invalid-feedback"/>
            </div>
            <div class="mb-4">
                <label class="form-label" for="messageOnCake">Message on the cake <span class="small text-muted">(optional)</span></label>
                <form:input path="messageOnCake" maxlength="100" cssClass="form-control" cssErrorClass="form-control is-invalid" placeholder="Happy 5th Birthday Amaya!"/>
                <form:errors path="messageOnCake" cssClass="invalid-feedback"/>
            </div>

            <h2 class="h4">When and budget</h2>
            <div class="row g-3 mb-4">
                <div class="col-sm-6">
                    <label class="form-label" for="neededBy">Needed by</label>
                    <form:input path="neededBy" type="date" min="${earliestDate}" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                    <form:errors path="neededBy" cssClass="invalid-feedback"/>
                    <div class="form-text">We need at least ${minDaysNotice} days' notice.</div>
                </div>
                <div class="col-sm-6">
                    <label class="form-label" for="budget">Your budget (Rs.) <span class="small text-muted">(optional)</span></label>
                    <form:input path="budget" type="number" min="0" step="100" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                    <form:errors path="budget" cssClass="invalid-feedback"/>
                </div>
            </div>

            <div class="d-flex gap-2">
                <button class="btn btn-su btn-lg" type="submit"><i class="bi bi-send-heart"></i> ${editing ? 'Save changes' : 'Send request'}</button>
                <a class="btn btn-outline-secondary btn-lg" href="${ctx}${editing ? '/custom-orders/' += orderId : '/account'}">Cancel</a>
            </div>
        </form:form>
    </div>
    <div class="col-lg-4">
        <div class="su-panel su-sticky su-steps">
            <h2 class="h4">How it works</h2>
            <ol class="mb-0">
                <li><strong>Send your idea.</strong> You can still edit it until we reply.</li>
                <li><strong>Get a quote.</strong> We'll send a price and any notes.</li>
                <li><strong>Accept it.</strong> Then we start baking.</li>
                <li><strong>Pick it up</strong> on your chosen day.</li>
            </ol>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
