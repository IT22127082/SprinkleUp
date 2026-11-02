<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="My account"/></jsp:include>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
    <h1 class="su-page-title mb-0">Hi, ${fn:escapeXml(currentUser.firstName)}!</h1>
    <div class="d-flex gap-2">
        <a class="btn btn-outline-su" href="${ctx}/order"><i class="bi bi-basket"></i> Order from menu</a>
        <a class="btn btn-su" href="${ctx}/custom-orders/new"><i class="bi bi-stars"></i> Request a custom cake</a>
    </div>
</div>

<div class="row g-4">
    <div class="col-lg-8">
        <div class="su-panel p-0">
            <div class="px-4 pt-4 pb-2"><h2 class="mb-0">My orders</h2></div>
            <c:choose>
                <c:when test="${empty history}">
                    <p class="text-muted px-4 pb-4 mb-0">No orders yet. Your menu orders and custom cake requests will show up here.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table su-table align-middle mb-0">
                            <thead><tr><th>#</th><th>Type</th><th>What</th><th>Date</th><th class="text-end">Price</th><th>Status</th></tr></thead>
                            <tbody>
                            <%-- One list holds both kinds of order; each one supplies its own label, price and status. --%>
                            <c:forEach var="o" items="${history}">
                                <tr>
                                    <td><a class="fw-bold" href="${ctx}${o.customerPath}">#${o.id}</a></td>
                                    <td><span class="su-kind ${o.kindLabel == 'Custom cake' ? 'su-kind-custom' : ''}">${o.kindLabel}</span></td>
                                    <td class="small">${fn:escapeXml(o.summary)}</td>
                                    <td class="small text-nowrap">${empty o.dueDate ? '—' : o.dueDate.format(dateFmt)}</td>
                                    <td class="text-end text-nowrap">
                                        <c:choose>
                                            <c:when test="${empty o.total}"><span class="small text-muted">Quote pending</span></c:when>
                                            <c:otherwise>Rs. <fmt:formatNumber value="${o.total}" pattern="#,##0.00"/></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><span class="badge text-bg-${o.statusBadge}">${o.statusLabel}</span></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="col-lg-4">
        <div class="su-panel">
            <h2>My details</h2>
            <p class="mb-1 fw-bold">${fn:escapeXml(currentUser.fullName)}</p>
            <p class="mb-1"><i class="bi bi-envelope me-1"></i>${fn:escapeXml(currentUser.email)}</p>
            <p class="mb-1"><i class="bi bi-telephone me-1"></i>${fn:escapeXml(currentUser.phone)}</p>
            <c:if test="${not empty currentUser.address}"><p class="mb-1"><i class="bi bi-geo-alt me-1"></i>${fn:escapeXml(currentUser.address)}</p></c:if>
            <a class="btn btn-sm btn-outline-su mt-3" href="${ctx}/account/edit"><i class="bi bi-pencil"></i> Edit details</a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
