<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Menu"/></jsp:include>

<section class="su-hero mb-4">
    <div class="row align-items-center g-4">
        <div class="col-lg-7">
            <span class="su-eyebrow">Baked fresh every morning</span>
            <h1 class="su-hero-title">Life is short.<br>Add more <em>sprinkles</em> <i class="bi bi-heart-fill su-title-heart" aria-hidden="true"></i></h1>
            <p class="lead mb-4">Celebration cakes, cupcakes, warm breads and pastries from the SprinkleUp kitchen.
                Order online and pick up when it suits you.</p>
            <div class="d-flex flex-wrap gap-2">
                <a class="btn btn-su btn-lg" href="${ctx}/order"><i class="bi bi-bag-heart me-1"></i> Start an order</a>
                <a class="btn btn-outline-su btn-lg bg-white" href="${ctx}/custom-orders/new"><i class="bi bi-stars me-1"></i> Design a custom cake</a>
            </div>
        </div>
        <div class="col-lg-5 d-none d-lg-block">
            <div class="su-hero-art">
                <img src="${ctx}/images/logo.png" alt="Sprinkle up by Aish logo">
                <i class="bi bi-heart-fill su-heart su-heart-1" aria-hidden="true"></i>
                <i class="bi bi-heart-fill su-heart su-heart-2" aria-hidden="true"></i>
                <i class="bi bi-heart-fill su-heart su-heart-3" aria-hidden="true"></i>
                <i class="bi bi-heart-fill su-heart su-heart-4" aria-hidden="true"></i>
            </div>
        </div>
    </div>
</section>

<div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-3">
    <h2 class="su-section-title mb-0">Our menu</h2>
    <nav class="su-filter" aria-label="Filter by category">
        <a class="${empty selectedType ? 'active' : ''}" href="${ctx}/">All</a>
        <c:forEach var="t" items="${types}">
            <a class="${selectedType == t ? 'active' : ''}" href="${ctx}/?type=${t}"><i class="bi ${t.icon}"></i> ${t.label}</a>
        </c:forEach>
    </nav>
</div>

<c:choose>
    <c:when test="${empty products}">
        <div class="su-empty">Nothing in this category today. Check back tomorrow!</div>
    </c:when>
    <c:otherwise>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-lg-3 g-4">
            <c:forEach var="p" items="${products}">
                <div class="col">
                    <article class="su-card h-100">
                        <div class="su-card-art su-type-${fn:toLowerCase(p.type)}">
                            <i class="bi ${p.type.icon}"></i>
                            <span class="su-chip">${p.type.label}</span>
                        </div>
                        <div class="su-card-body">
                            <h3 class="h5 mb-1">${fn:escapeXml(p.name)}</h3>
                            <div class="su-specs mb-2">${fn:escapeXml(p.specs)}</div>
                            <p class="text-muted small mb-3">${fn:escapeXml(p.description)}</p>
                            <div class="d-flex align-items-center justify-content-between mt-auto">
                                <span class="su-price">Rs. <fmt:formatNumber value="${p.unitPrice}" pattern="#,##0.00"/></span>
                                <c:choose>
                                    <c:when test="${p.inStock}">
                                        <a class="btn btn-sm btn-su" href="${ctx}/order?product=${p.id}">Order</a>
                                    </c:when>
                                    <c:otherwise><span class="badge text-bg-light">Sold out</span></c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </article>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
