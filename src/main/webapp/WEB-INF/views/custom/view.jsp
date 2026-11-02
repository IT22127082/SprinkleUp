<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Custom cake #${order.id}"/></jsp:include>

<a class="small" href="${ctx}/account"><i class="bi bi-arrow-left"></i> My orders</a>
<div class="d-flex flex-wrap align-items-center gap-3 mt-1 mb-4">
    <h1 class="su-page-title mb-0">Custom cake #${order.id}</h1>
    <span class="badge fs-6 text-bg-${order.statusBadge}">${order.statusLabel}</span>
</div>

<div class="row g-4">
    <div class="col-lg-7">
        <jsp:include page="/WEB-INF/views/custom/details.jsp"/>
    </div>

    <div class="col-lg-5">
        <div class="su-panel su-quote mb-4">
            <h2>Quote</h2>
            <c:choose>
                <c:when test="${empty order.quotedPrice}">
                    <p class="mb-0 text-muted">We're looking at your idea and will send a price soon.
                        <c:if test="${not empty order.budget}">Your budget: Rs. <fmt:formatNumber value="${order.budget}" pattern="#,##0"/>.</c:if></p>
                </c:when>
                <c:otherwise>
                    <p class="su-quote-price mb-2">Rs. <fmt:formatNumber value="${order.quotedPrice}" pattern="#,##0.00"/></p>
                    <c:if test="${not empty order.bakerNote}">
                        <blockquote class="su-note mb-0"><i class="bi bi-chat-heart me-1"></i>${fn:escapeXml(order.bakerNote)}</blockquote>
                    </c:if>
                </c:otherwise>
            </c:choose>
            <c:if test="${order.status == 'QUOTED'}">
                <form method="post" action="${ctx}/custom-orders/${order.id}/accept" class="mt-3">
                    <button class="btn btn-su w-100" type="submit"><i class="bi bi-check2-heart"></i> Accept quote</button>
                </form>
            </c:if>
        </div>

        <c:if test="${order.editable or order.cancellable}">
            <div class="su-panel">
                <h2>Change of plans?</h2>
                <c:if test="${order.editable}">
                    <a class="btn btn-outline-su w-100 mb-2" href="${ctx}/custom-orders/${order.id}/edit"><i class="bi bi-pencil"></i> Edit request</a>
                </c:if>
                <form method="post" action="${ctx}/custom-orders/${order.id}/cancel" data-confirm="Cancel this custom cake request?">
                    <button class="btn btn-outline-danger w-100" type="submit"><i class="bi bi-x-circle"></i> Cancel request</button>
                </form>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
