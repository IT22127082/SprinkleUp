<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Order #${order.id}"/></jsp:include>

<a class="small" href="${ctx}/admin/orders"><i class="bi bi-arrow-left"></i> Orders</a>
<div class="d-flex flex-wrap align-items-center gap-3 mt-1 mb-4">
    <h1 class="su-page-title mb-0">Order #${order.id}</h1>
    <span class="badge fs-6 text-bg-${order.status.badge}">${order.status.label}</span>
</div>

<div class="row g-4">
    <div class="col-lg-7">
        <div class="su-panel mb-4">
            <h2 class="h5 mb-3">Items</h2>
            <div class="table-responsive">
                <table class="table su-table mb-0">
                    <thead><tr><th>Item</th><th class="text-end">Price</th><th class="text-end">Qty</th><th class="text-end">Amount</th></tr></thead>
                    <tbody>
                    <c:forEach var="i" items="${order.items}">
                        <tr>
                            <td>${fn:escapeXml(i.productName)}
                                <c:if test="${empty i.product}"><span class="badge text-bg-light">no longer on menu</span></c:if></td>
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
        </div>
        <div class="su-panel">
            <h2 class="h5 mb-3">Customer</h2>
            <p class="mb-1 fw-semibold">${fn:escapeXml(order.customer.fullName)}</p>
            <p class="mb-1"><i class="bi bi-envelope me-1"></i>${fn:escapeXml(order.customer.email)}</p>
            <p class="mb-1"><i class="bi bi-telephone me-1"></i>${fn:escapeXml(order.customer.phone)}</p>
            <c:if test="${not empty order.customer.address}"><p class="mb-0"><i class="bi bi-geo-alt me-1"></i>${fn:escapeXml(order.customer.address)}</p></c:if>
            <p class="small text-muted mt-3 mb-0">Placed ${order.createdAt.format(dateTimeFmt)}</p>
        </div>
    </div>

    <div class="col-lg-5">
        <form class="su-panel mb-4" method="post" action="${ctx}/admin/orders/${order.id}">
            <h2 class="h5 mb-3">Update order</h2>
            <c:if test="${order.status.closed}">
                <p class="small text-muted">This order is ${fn:toLowerCase(order.status.label)} and its status can no longer change.</p>
            </c:if>
            <div class="mb-3">
                <label class="form-label" for="status">Status</label>
                <select class="form-select" id="status" name="status" ${order.status.closed ? 'disabled' : ''}>
                    <c:forEach var="s" items="${statuses}">
                        <option value="${s}" ${order.status == s ? 'selected' : ''}>${s.label}</option>
                    </c:forEach>
                </select>
                <c:if test="${order.status.closed}"><input type="hidden" name="status" value="${order.status}"></c:if>
            </div>
            <div class="mb-3">
                <label class="form-label" for="pickupDate">Pickup date</label>
                <input class="form-control" type="date" id="pickupDate" name="pickupDate" value="${order.pickupDate}">
            </div>
            <div class="mb-3">
                <label class="form-label" for="notes">Notes</label>
                <textarea class="form-control" id="notes" name="notes" rows="3" maxlength="500">${fn:escapeXml(order.notes)}</textarea>
            </div>
            <button class="btn btn-su w-100" type="submit"><i class="bi bi-check-lg"></i> Save changes</button>
        </form>

        <form method="post" action="${ctx}/admin/orders/${order.id}/delete"
              data-confirm="Delete order #${order.id}? Open orders return their items to stock.">
            <button class="btn btn-outline-danger w-100" type="submit"><i class="bi bi-trash"></i> Delete order</button>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
