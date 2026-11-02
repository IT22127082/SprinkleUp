<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Place an order"/></jsp:include>

<h1 class="su-page-title">Place an order</h1>
<p class="text-muted mb-4">Choose what you'd like, tell us who you are and when you'll pick it up.</p>

<form method="post" action="${ctx}/order" class="row g-4" data-order-form>
    <div class="col-lg-7">
        <div class="su-panel">
            <h2 class="h5 mb-3"><i class="bi bi-basket me-1"></i> Choose your treats</h2>
            <c:if test="${empty products}"><p class="text-muted mb-0">Everything is sold out right now. Sorry!</p></c:if>
            <ul class="list-unstyled su-pick-list mb-0">
                <c:forEach var="p" items="${products}">
                    <li class="su-pick">
                        <span class="su-pick-icon su-type-${fn:toLowerCase(p.type)}"><i class="bi ${p.type.icon}"></i></span>
                        <div class="flex-grow-1">
                            <label class="fw-semibold d-block" for="qty-${p.id}">${fn:escapeXml(p.name)}</label>
                            <span class="small text-muted">${fn:escapeXml(p.specs)} · ${p.stock} left</span>
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
            <h2 class="h5 mb-3"><i class="bi bi-person me-1"></i> Your details</h2>
            <div class="mb-3">
                <label class="form-label" for="fullName">Full name</label>
                <input class="form-control" id="fullName" name="fullName" required maxlength="100" value="${fn:escapeXml(orderForm.fullName)}">
            </div>
            <div class="row g-3 mb-3">
                <div class="col-sm-6">
                    <label class="form-label" for="email">Email</label>
                    <input class="form-control" type="email" id="email" name="email" required value="${fn:escapeXml(orderForm.email)}">
                </div>
                <div class="col-sm-6">
                    <label class="form-label" for="phone">Phone</label>
                    <input class="form-control" type="tel" id="phone" name="phone" required pattern="[0-9+ ]{9,15}" value="${fn:escapeXml(orderForm.phone)}">
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label" for="address">Address <span class="text-muted small">(optional)</span></label>
                <input class="form-control" id="address" name="address" maxlength="255" value="${fn:escapeXml(orderForm.address)}">
            </div>
            <div class="mb-3">
                <label class="form-label" for="pickupDate">Pickup date</label>
                <input class="form-control" type="date" id="pickupDate" name="pickupDate" min="${today}" value="${orderForm.pickupDate}">
            </div>
            <div class="mb-3">
                <label class="form-label" for="notes">Notes for the baker</label>
                <textarea class="form-control" id="notes" name="notes" rows="2" maxlength="500"
                          placeholder="e.g. Write 'Happy Birthday Amaya' on the cake">${fn:escapeXml(orderForm.notes)}</textarea>
            </div>
            <div class="d-flex justify-content-between align-items-center border-top pt-3 mb-3">
                <span class="fw-semibold">Estimated total</span>
                <span class="su-total">Rs. <span data-order-total>0.00</span></span>
            </div>
            <button class="btn btn-su w-100 btn-lg" type="submit"><i class="bi bi-bag-check me-1"></i> Place order</button>
        </div>
    </div>
</form>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
