<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Orders"/></jsp:include>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
    <h1 class="su-page-title mb-0">Orders</h1>
    <a class="btn btn-su" href="${ctx}/admin/orders/new"><i class="bi bi-plus-lg"></i> New order</a>
</div>

<nav class="su-filter mb-3" aria-label="Filter by status">
    <a class="${empty selectedStatus ? 'active' : ''}" href="${ctx}/admin/orders">All</a>
    <c:forEach var="s" items="${statuses}">
        <a class="${selectedStatus == s ? 'active' : ''}" href="${ctx}/admin/orders?status=${s}">${s.label}</a>
    </c:forEach>
</nav>

<div class="su-panel p-0">
    <div class="table-responsive">
        <table class="table su-table align-middle mb-0">
            <thead><tr><th>#</th><th>Customer</th><th class="text-end">Items</th><th class="text-end">Total</th>
                <th>Pickup</th><th>Status</th><th>Placed</th><th class="text-end">Actions</th></tr></thead>
            <tbody>
            <c:if test="${empty orders}">
                <tr><td colspan="8" class="text-center text-muted py-4">No orders here.</td></tr>
            </c:if>
            <c:forEach var="o" items="${orders}">
                <tr>
                    <td><a class="fw-semibold" href="${ctx}/admin/orders/${o.id}">#${o.id}</a></td>
                    <td>${fn:escapeXml(o.customer.fullName)}</td>
                    <td class="text-end">${o.itemCount}</td>
                    <td class="text-end">Rs. <fmt:formatNumber value="${o.total}" pattern="#,##0.00"/></td>
                    <td class="text-nowrap">${empty o.pickupDate ? '—' : o.pickupDate.format(dateFmt)}</td>
                    <td><span class="badge text-bg-${o.status.badge}">${o.status.label}</span></td>
                    <td class="small text-muted text-nowrap">${o.createdAt.format(dateTimeFmt)}</td>
                    <td class="text-end text-nowrap">
                        <a class="btn btn-sm btn-outline-secondary" href="${ctx}/admin/orders/${o.id}" aria-label="Open order ${o.id}"><i class="bi bi-eye"></i></a>
                        <form class="d-inline" method="post" action="${ctx}/admin/orders/${o.id}/delete"
                              data-confirm="Delete order #${o.id}? Open orders return their items to stock.">
                            <button class="btn btn-sm btn-outline-danger" type="submit" aria-label="Delete order ${o.id}"><i class="bi bi-trash"></i></button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
