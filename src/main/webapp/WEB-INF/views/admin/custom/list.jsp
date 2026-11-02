<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Custom cakes"/></jsp:include>

<h1 class="su-page-title mb-3">Custom cake requests</h1>

<nav class="su-filter mb-3" aria-label="Filter by status">
    <a class="${empty selectedStatus ? 'active' : ''}" href="${ctx}/admin/custom-orders">All</a>
    <c:forEach var="s" items="${statuses}">
        <a class="${selectedStatus == s ? 'active' : ''}" href="${ctx}/admin/custom-orders?status=${s}">${s.label}</a>
    </c:forEach>
</nav>

<div class="su-panel p-0">
    <div class="table-responsive">
        <table class="table su-table align-middle mb-0">
            <thead><tr><th>#</th><th>Customer</th><th>Cake</th><th>Needed by</th><th class="text-end">Budget</th><th class="text-end">Quote</th><th>Status</th><th class="text-end">Actions</th></tr></thead>
            <tbody>
            <c:if test="${empty requests}">
                <tr><td colspan="8" class="text-center text-muted py-4">No requests here.</td></tr>
            </c:if>
            <c:forEach var="r" items="${requests}">
                <tr>
                    <td><a class="fw-bold" href="${ctx}/admin/custom-orders/${r.id}">#${r.id}</a></td>
                    <td>${fn:escapeXml(r.customer.fullName)}</td>
                    <td class="small">${fn:escapeXml(r.summary)}</td>
                    <td class="text-nowrap">${r.neededBy.format(dateFmt)}</td>
                    <td class="text-end">${empty r.budget ? '—' : ''}<c:if test="${not empty r.budget}"><fmt:formatNumber value="${r.budget}" pattern="#,##0"/></c:if></td>
                    <td class="text-end">${empty r.quotedPrice ? '—' : ''}<c:if test="${not empty r.quotedPrice}"><fmt:formatNumber value="${r.quotedPrice}" pattern="#,##0.00"/></c:if></td>
                    <td><span class="badge text-bg-${r.statusBadge}">${r.statusLabel}</span></td>
                    <td class="text-end text-nowrap">
                        <a class="btn btn-sm btn-outline-secondary" href="${ctx}/admin/custom-orders/${r.id}" aria-label="Open request ${r.id}"><i class="bi bi-eye"></i></a>
                        <form class="d-inline" method="post" action="${ctx}/admin/custom-orders/${r.id}/delete" data-confirm="Delete custom request #${r.id}?">
                            <button class="btn btn-sm btn-outline-danger" type="submit" aria-label="Delete request ${r.id}"><i class="bi bi-trash"></i></button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
