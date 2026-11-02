<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="New order"/></jsp:include>

<a class="small" href="${ctx}/admin/orders"><i class="bi bi-arrow-left"></i> Orders</a>
<h1 class="su-page-title mt-1">New order</h1>

<form method="post" action="${ctx}/admin/orders" class="row g-4" data-order-form>
    <div class="col-lg-7">
        <div class="su-panel">
            <h2 class="h5 mb-3">Items</h2>
            <c:if test="${empty products}"><p class="text-muted mb-0">No products in stock.</p></c:if>
            <ul class="list-unstyled su-pick-list mb-0">
                <c:forEach var="p" items="${products}">
                    <li class="su-pick">
                        <span class="su-pick-icon su-type-${fn:toLowerCase(p.type)}"><i class="bi ${p.type.icon}"></i></span>
                        <div class="flex-grow-1">
                            <label class="fw-semibold d-block" for="qty-${p.id}">${fn:escapeXml(p.name)}</label>
                            <span class="small text-muted">${fn:escapeXml(p.specs)} · ${p.stock} in stock</span>
                        </div>
                        <span class="su-price small text-nowrap">Rs. <fmt:formatNumber value="${p.unitPrice}" pattern="#,##0.00"/></span>
                        <input type="number" class="form-control su-qty" id="qty-${p.id}" name="quantities[${p.id}]"
                               min="0" max="${p.stock}" value="${empty orderForm.quantities[p.id] ? 0 : orderForm.quantities[p.id]}"
                               data-price="${p.unitPrice}">
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <div class="col-lg-5">
        <div class="su-panel su-sticky">
            <div class="mb-3">
                <label class="form-label" for="customerId">Customer</label>
                <select class="form-select" id="customerId" name="customerId" required>
                    <option value="">Choose a customer…</option>
                    <c:forEach var="cu" items="${customers}">
                        <option value="${cu.id}" ${orderForm.customerId == cu.id ? 'selected' : ''}>${fn:escapeXml(cu.fullName)} · ${fn:escapeXml(cu.phone)}</option>
                    </c:forEach>
                </select>
                <div class="form-text"><a href="${ctx}/admin/customers/new">Add a new customer</a> first if they're not listed.</div>
            </div>
            <div class="mb-3">
                <label class="form-label" for="pickupDate">Pickup date</label>
                <input class="form-control" type="date" id="pickupDate" name="pickupDate" min="${today}" value="${orderForm.pickupDate}">
            </div>
            <div class="mb-3">
                <label class="form-label" for="notes">Notes</label>
                <textarea class="form-control" id="notes" name="notes" rows="2" maxlength="500">${fn:escapeXml(orderForm.notes)}</textarea>
            </div>
            <div class="d-flex justify-content-between align-items-center border-top pt-3 mb-3">
                <span class="fw-semibold">Total</span>
                <span class="su-total">Rs. <span data-order-total>0.00</span></span>
            </div>
            <button class="btn btn-su w-100" type="submit"><i class="bi bi-check-lg"></i> Create order</button>
        </div>
    </div>
</form>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
