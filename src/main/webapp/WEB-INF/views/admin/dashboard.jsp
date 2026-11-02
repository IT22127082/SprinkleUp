<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Dashboard"/></jsp:include>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
    <h1 class="su-page-title mb-0">Good day, baker!</h1>
    <div class="d-flex gap-2">
        <a class="btn btn-outline-su" href="${ctx}/admin/products/new"><i class="bi bi-plus-lg"></i> Product</a>
        <a class="btn btn-su" href="${ctx}/admin/orders/new"><i class="bi bi-plus-lg"></i> Order</a>
    </div>
</div>

<div class="row row-cols-2 row-cols-lg-4 g-3 mb-4">
    <div class="col"><div class="su-stat">
        <span class="su-stat-label">Revenue</span>
        <span class="su-stat-value">Rs. <fmt:formatNumber value="${revenue}" pattern="#,##0"/></span>
        <span class="su-stat-note">excluding cancelled orders</span>
    </div></div>
    <div class="col"><div class="su-stat">
        <span class="su-stat-label">Open orders</span>
        <span class="su-stat-value">${openOrders}</span>
        <span class="su-stat-note">out of ${orderCount} placed</span>
    </div></div>
    <div class="col"><div class="su-stat">
        <span class="su-stat-label">Products</span>
        <span class="su-stat-value">${productCount}</span>
        <span class="su-stat-note ${lowStockCount > 0 ? 'text-danger' : ''}">${lowStockCount} running low</span>
    </div></div>
    <div class="col"><div class="su-stat">
        <span class="su-stat-label">Customers</span>
        <span class="su-stat-value">${customerCount}</span>
        <span class="su-stat-note">registered</span>
    </div></div>
</div>

<div class="su-panel">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="h5 mb-0">Latest orders</h2>
        <a href="${ctx}/admin/orders" class="small">See all</a>
    </div>
    <c:choose>
        <c:when test="${empty recentOrders}"><p class="text-muted mb-0">No orders yet.</p></c:when>
        <c:otherwise>
            <div class="table-responsive">
                <table class="table su-table align-middle mb-0">
                    <thead><tr><th>#</th><th>Customer</th><th>Placed</th><th class="text-end">Total</th><th>Status</th></tr></thead>
                    <tbody>
                    <c:forEach var="o" items="${recentOrders}">
                        <tr>
                            <td><a href="${ctx}/admin/orders/${o.id}">#${o.id}</a></td>
                            <td>${fn:escapeXml(o.customer.fullName)}</td>
                            <td class="text-muted small">${o.createdAt.format(dateTimeFmt)}</td>
                            <td class="text-end">Rs. <fmt:formatNumber value="${o.total}" pattern="#,##0.00"/></td>
                            <td><span class="badge text-bg-${o.status.badge}">${o.status.label}</span></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
