<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Customers"/></jsp:include>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
    <h1 class="su-page-title mb-0">Customers</h1>
    <a class="btn btn-su" href="${ctx}/admin/customers/new"><i class="bi bi-person-plus"></i> New customer</a>
</div>

<form class="row g-2 mb-3" method="get" action="${ctx}/admin/customers">
    <div class="col-sm-8 col-md-6">
        <input class="form-control" type="search" name="q" placeholder="Search by name or email" value="${fn:escapeXml(q)}" aria-label="Search customers">
    </div>
    <div class="col-sm-4 col-md-auto"><button class="btn btn-outline-su w-100" type="submit">Search</button></div>
</form>

<div class="su-panel p-0">
    <div class="table-responsive">
        <table class="table su-table align-middle mb-0">
            <thead><tr><th>Name</th><th>Email</th><th>Phone</th><th>Address</th><th>Since</th><th class="text-end">Actions</th></tr></thead>
            <tbody>
            <c:if test="${empty customers}">
                <tr><td colspan="6" class="text-center text-muted py-4">No customers found.</td></tr>
            </c:if>
            <c:forEach var="cu" items="${customers}">
                <tr>
                    <td class="fw-semibold">${fn:escapeXml(cu.fullName)}</td>
                    <td><a href="mailto:${fn:escapeXml(cu.email)}">${fn:escapeXml(cu.email)}</a></td>
                    <td class="text-nowrap">${fn:escapeXml(cu.phone)}</td>
                    <td class="small text-muted">${fn:escapeXml(cu.address)}</td>
                    <td class="small text-muted text-nowrap">${cu.createdAt.format(dateFmt)}</td>
                    <td class="text-end text-nowrap">
                        <a class="btn btn-sm btn-outline-su" href="${ctx}/admin/orders/new?customer=${cu.id}" title="New order for this customer"><i class="bi bi-bag-plus"></i></a>
                        <a class="btn btn-sm btn-outline-secondary" href="${ctx}/admin/customers/${cu.id}/edit" aria-label="Edit ${fn:escapeXml(cu.fullName)}"><i class="bi bi-pencil"></i></a>
                        <form class="d-inline" method="post" action="${ctx}/admin/customers/${cu.id}/delete"
                              data-confirm="Delete ${fn:escapeXml(cu.fullName)}?">
                            <button class="btn btn-sm btn-outline-danger" type="submit" aria-label="Delete ${fn:escapeXml(cu.fullName)}"><i class="bi bi-trash"></i></button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
