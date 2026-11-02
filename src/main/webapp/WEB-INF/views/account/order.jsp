<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Order #${order.id}"/></jsp:include>

<a class="small" href="${ctx}/account"><i class="bi bi-arrow-left"></i> My orders</a>
<div class="d-flex flex-wrap align-items-center gap-3 mt-1 mb-4">
    <h1 class="su-page-title mb-0">Order #${order.id}</h1>
    <span class="badge fs-6 text-bg-${order.statusBadge}">${order.statusLabel}</span>
</div>

<div class="su-panel" style="max-width: 760px">
    <div class="table-responsive">
        <table class="table su-table mb-0">
            <thead><tr><th>Item</th><th class="text-end">Price</th><th class="text-end">Qty</th><th class="text-end">Amount</th></tr></thead>
            <tbody>
            <c:forEach var="i" items="${order.items}">
                <tr>
                    <td>${fn:escapeXml(i.productName)}</td>
                    <td class="text-end"><fmt:formatNumber value="${i.unitPrice}" pattern="#,##0.00"/></td>
                    <td class="text-end">${i.quantity}</td>
                    <td class="text-end"><fmt:formatNumber value="${i.lineTotal}" pattern="#,##0.00"/></td>
                </tr>
            </c:forEach>
            </tbody>
            <tfoot>
            <tr><th colspan="3">Total</th><th class="text-end">Rs. <fmt:formatNumber value="${order.total}" pattern="#,##0.00"/></th></tr>
            </tfoot>
        </table>
    </div>
    <p class="small text-muted mt-3 mb-1">Placed ${order.createdAt.format(dateTimeFmt)}</p>
    <c:if test="${not empty order.pickupDate}"><p class="mb-1"><i class="bi bi-calendar-event me-1"></i>Pickup on <strong>${order.pickupDate.format(dateFmt)}</strong></p></c:if>
    <c:if test="${not empty order.notes}"><p class="mb-0"><i class="bi bi-chat-heart me-1"></i>${fn:escapeXml(order.notes)}</p></c:if>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
