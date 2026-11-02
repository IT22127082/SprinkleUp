<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Products"/></jsp:include>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
    <h1 class="su-page-title mb-0">Products</h1>
    <div class="dropdown">
        <button class="btn btn-su dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
            <i class="bi bi-plus-lg"></i> New product
        </button>
        <ul class="dropdown-menu dropdown-menu-end">
            <c:forEach var="t" items="${types}">
                <li><a class="dropdown-item" href="${ctx}/admin/products/new?type=${t}"><i class="bi ${t.icon} me-2"></i>${t.label}</a></li>
            </c:forEach>
        </ul>
    </div>
</div>

<form class="row g-2 mb-3" method="get" action="${ctx}/admin/products">
    <div class="col-sm-6 col-md-5">
        <input class="form-control" type="search" name="q" placeholder="Search by name" value="${fn:escapeXml(q)}" aria-label="Search by name">
    </div>
    <div class="col-sm-4 col-md-3">
        <select class="form-select" name="type" aria-label="Filter by type">
            <option value="">All types</option>
            <c:forEach var="t" items="${types}">
                <option value="${t}" ${selectedType == t ? 'selected' : ''}>${t.label}</option>
            </c:forEach>
        </select>
    </div>
    <div class="col-sm-2 col-md-auto"><button class="btn btn-outline-su w-100" type="submit">Filter</button></div>
</form>

<div class="su-panel p-0">
    <div class="table-responsive">
        <table class="table su-table align-middle mb-0">
            <thead>
            <tr><th>Product</th><th>Type</th><th>Details</th><th class="text-end">Base</th><th class="text-end">Sells at</th>
                <th class="text-end">Stock</th><th>Status</th><th class="text-end">Actions</th></tr>
            </thead>
            <tbody>
            <c:if test="${empty products}">
                <tr><td colspan="8" class="text-center text-muted py-4">No products found.</td></tr>
            </c:if>
            <c:forEach var="p" items="${products}">
                <tr>
                    <td class="fw-semibold">${fn:escapeXml(p.name)}</td>
                    <td><span class="su-type-badge su-type-${fn:toLowerCase(p.type)}"><i class="bi ${p.type.icon}"></i> ${p.type.label}</span></td>
                    <td class="small text-muted">${fn:escapeXml(p.specs)}</td>
                    <td class="text-end"><fmt:formatNumber value="${p.basePrice}" pattern="#,##0.00"/></td>
                    <td class="text-end fw-semibold"><fmt:formatNumber value="${p.unitPrice}" pattern="#,##0.00"/></td>
                    <td class="text-end ${p.stock < lowStockLimit ? 'text-danger fw-bold' : ''}">${p.stock}</td>
                    <td>
                        <c:choose>
                            <c:when test="${p.available}"><span class="badge text-bg-success">On menu</span></c:when>
                            <c:otherwise><span class="badge text-bg-secondary">Hidden</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-end text-nowrap">
                        <a class="btn btn-sm btn-outline-secondary" href="${ctx}/admin/products/${p.id}/edit" aria-label="Edit ${fn:escapeXml(p.name)}"><i class="bi bi-pencil"></i></a>
                        <form class="d-inline" method="post" action="${ctx}/admin/products/${p.id}/delete"
                              data-confirm="Delete ${fn:escapeXml(p.name)}? Past orders keep their copy of it.">
                            <button class="btn btn-sm btn-outline-danger" type="submit" aria-label="Delete ${fn:escapeXml(p.name)}"><i class="bi bi-trash"></i></button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<p class="small text-muted mt-2">"Sells at" is worked out by each product type: cakes by weight, cupcakes per box, sliced bread adds a slicing charge.</p>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
