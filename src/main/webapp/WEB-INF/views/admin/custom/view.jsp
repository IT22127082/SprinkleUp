<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Custom request #${order.id}"/></jsp:include>

<a class="small" href="${ctx}/admin/custom-orders"><i class="bi bi-arrow-left"></i> Custom cakes</a>
<div class="d-flex flex-wrap align-items-center gap-3 mt-1 mb-4">
    <h1 class="su-page-title mb-0">Custom request #${order.id}</h1>
    <span class="badge fs-6 text-bg-${order.statusBadge}">${order.statusLabel}</span>
</div>

<div class="row g-4">
    <div class="col-lg-7">
        <jsp:include page="/WEB-INF/views/custom/details.jsp"/>
        <div class="su-panel mt-4">
            <h2>Customer</h2>
            <p class="mb-1 fw-bold">${fn:escapeXml(order.customer.fullName)}</p>
            <p class="mb-1"><i class="bi bi-envelope me-1"></i>${fn:escapeXml(order.customer.email)}</p>
            <p class="mb-0"><i class="bi bi-telephone me-1"></i>${fn:escapeXml(order.customer.phone)}</p>
            <c:if test="${not empty order.budget}"><p class="mt-2 mb-0"><i class="bi bi-wallet2 me-1"></i>Budget: Rs. <fmt:formatNumber value="${order.budget}" pattern="#,##0"/></p></c:if>
        </div>
    </div>

    <div class="col-lg-5">
        <c:if test="${order.status == 'REQUESTED' or order.status == 'QUOTED'}">
            <form class="su-panel mb-4" method="post" action="${ctx}/admin/custom-orders/${order.id}/quote">
                <h2>${empty order.quotedPrice ? 'Send a quote' : 'Revise the quote'}</h2>
                <div class="mb-3">
                    <label class="form-label" for="price">Price (Rs.)</label>
                    <input class="form-control" type="number" id="price" name="price" min="1" step="0.01" required value="${order.quotedPrice}">
                </div>
                <div class="mb-3">
                    <label class="form-label" for="note">Note to the customer</label>
                    <textarea class="form-control" id="note" name="note" rows="3" maxlength="500"
                              placeholder="e.g. Includes fresh flowers. 50% deposit on pickup day.">${fn:escapeXml(order.bakerNote)}</textarea>
                </div>
                <button class="btn btn-su w-100" type="submit"><i class="bi bi-send"></i> Send quote</button>
            </form>
        </c:if>
        <c:if test="${not empty order.quotedPrice and not (order.status == 'REQUESTED' or order.status == 'QUOTED')}">
            <div class="su-panel su-quote mb-4">
                <h2>Agreed price</h2>
                <p class="su-quote-price mb-0">Rs. <fmt:formatNumber value="${order.quotedPrice}" pattern="#,##0.00"/></p>
            </div>
        </c:if>

        <form class="su-panel mb-4" method="post" action="${ctx}/admin/custom-orders/${order.id}/status">
            <h2>Status</h2>
            <c:if test="${order.status.closed}"><p class="small text-muted">This request is closed and can no longer change.</p></c:if>
            <div class="d-flex gap-2">
                <select class="form-select" name="status" aria-label="Status" ${order.status.closed ? 'disabled' : ''}>
                    <c:forEach var="s" items="${statuses}">
                        <option value="${s}" ${order.status == s ? 'selected' : ''}>${s.label}</option>
                    </c:forEach>
                </select>
                <button class="btn btn-outline-su text-nowrap" type="submit" ${order.status.closed ? 'disabled' : ''}>Update</button>
            </div>
        </form>

        <form method="post" action="${ctx}/admin/custom-orders/${order.id}/delete" data-confirm="Delete custom request #${order.id}?">
            <button class="btn btn-outline-danger w-100" type="submit"><i class="bi bi-trash"></i> Delete request</button>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
