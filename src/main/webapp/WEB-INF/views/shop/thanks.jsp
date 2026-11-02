<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Thank you"/></jsp:include>

<div class="su-panel su-thanks mx-auto">
    <img class="su-thanks-logo" src="${ctx}/images/logo.png" alt="">
    <h1 class="su-page-title text-center">Thank you, ${fn:escapeXml(order.customer.fullName)}!</h1>
    <p class="text-center text-muted">Your order <strong>#${order.id}</strong> is in. We'll start baking soon.</p>

    <table class="table su-table mt-4">
        <thead><tr><th>Item</th><th class="text-end">Qty</th><th class="text-end">Amount</th></tr></thead>
        <tbody>
        <c:forEach var="i" items="${order.items}">
            <tr>
                <td>${fn:escapeXml(i.productName)}</td>
                <td class="text-end">${i.quantity}</td>
                <td class="text-end">Rs. <fmt:formatNumber value="${i.lineTotal}" pattern="#,##0.00"/></td>
            </tr>
        </c:forEach>
        </tbody>
        <tfoot>
        <tr><th colspan="2">Total</th><th class="text-end">Rs. <fmt:formatNumber value="${order.total}" pattern="#,##0.00"/></th></tr>
        </tfoot>
    </table>
    <c:if test="${not empty order.pickupDate}">
        <p class="mb-0"><i class="bi bi-calendar-event me-1"></i> Pickup on <strong>${order.pickupDate.format(dateFmt)}</strong></p>
    </c:if>
    <div class="text-center mt-4">
        <a class="btn btn-outline-su" href="${ctx}/">Back to the menu</a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
