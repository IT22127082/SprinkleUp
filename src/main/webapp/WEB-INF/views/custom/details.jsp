<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- The request details, shared by the customer and admin pages. Expects "order" (a CustomOrder). --%>
<div class="su-panel">
    <h2>The request</h2>
    <dl class="su-dl mb-0">
        <dt>Occasion</dt><dd>${fn:escapeXml(order.occasion)}</dd>
        <dt>Flavour</dt><dd>${fn:escapeXml(order.flavour)}${order.eggless ? ' · Eggless' : ''}</dd>
        <dt>Size</dt><dd>${order.weightKg} kg · ${order.tiers} ${order.tiers == 1 ? 'tier' : 'tiers'}</dd>
        <dt>Needed by</dt><dd>${order.neededBy.format(dateFmt)}</dd>
        <dt>Design</dt><dd class="su-pre">${fn:escapeXml(order.design)}</dd>
        <c:if test="${not empty order.messageOnCake}"><dt>Message</dt><dd>"${fn:escapeXml(order.messageOnCake)}"</dd></c:if>
        <dt>Sent</dt><dd>${order.createdAt.format(dateTimeFmt)}</dd>
    </dl>
</div>
