<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="editing" value="${not empty productForm.id}"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="${editing ? 'Edit product' : 'New product'}"/></jsp:include>

<a class="small" href="${ctx}/admin/products"><i class="bi bi-arrow-left"></i> Products</a>
<h1 class="su-page-title mt-1">${editing ? 'Edit product' : 'New product'}</h1>

<c:set var="action" value="${editing ? '/admin/products/' += productForm.id : '/admin/products'}"/>
<form:form modelAttribute="productForm" servletRelativeAction="${action}" method="post" cssClass="su-panel su-form">
    <div class="row g-3">
        <div class="col-md-4">
            <label class="form-label" for="type">Type</label>
            <c:choose>
                <c:when test="${editing}">
                    <form:hidden path="type"/>
                    <input class="form-control" value="${productForm.type.label}" disabled>
                </c:when>
                <c:otherwise>
                    <form:select path="type" cssClass="form-select">
                        <c:forEach var="t" items="${types}">
                            <form:option value="${t}">${t.label}</form:option>
                        </c:forEach>
                    </form:select>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="col-md-8">
            <label class="form-label" for="name">Name</label>
            <form:input path="name" cssClass="form-control" cssErrorClass="form-control is-invalid" maxlength="100"/>
            <form:errors path="name" cssClass="invalid-feedback"/>
        </div>
        <div class="col-12">
            <label class="form-label" for="description">Description</label>
            <form:textarea path="description" cssClass="form-control" rows="2" maxlength="500"/>
        </div>
        <div class="col-sm-6 col-md-4">
            <label class="form-label" for="basePrice">Base price (Rs.) <span class="small text-muted" data-price-hint></span></label>
            <form:input path="basePrice" type="number" step="0.01" min="0" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
            <form:errors path="basePrice" cssClass="invalid-feedback"/>
        </div>
        <div class="col-sm-6 col-md-4">
            <label class="form-label" for="stock">In stock</label>
            <form:input path="stock" type="number" min="0" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
            <form:errors path="stock" cssClass="invalid-feedback"/>
        </div>
        <div class="col-md-4 d-flex align-items-end">
            <div class="form-check form-switch mb-2">
                <form:checkbox path="available" cssClass="form-check-input" id="available"/>
                <label class="form-check-label" for="available">Show on the menu</label>
            </div>
        </div>
    </div>

    <%-- Fields for each product type; JavaScript shows only the one that matches the chosen type. --%>
    <fieldset class="su-specs-box mt-4" data-type="CAKE">
        <legend><i class="bi bi-cake2"></i> Cake details</legend>
        <div class="row g-3">
            <div class="col-sm-4">
                <label class="form-label" for="weightKg">Weight (kg)</label>
                <form:input path="weightKg" type="number" step="0.25" min="0.25" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                <form:errors path="weightKg" cssClass="invalid-feedback"/>
            </div>
            <div class="col-sm-5">
                <label class="form-label" for="flavour">Flavour</label>
                <form:input path="flavour" cssClass="form-control"/>
            </div>
            <div class="col-sm-3 d-flex align-items-end">
                <div class="form-check mb-2">
                    <form:checkbox path="eggless" cssClass="form-check-input" id="eggless"/>
                    <label class="form-check-label" for="eggless">Eggless (+10%)</label>
                </div>
            </div>
        </div>
    </fieldset>

    <fieldset class="su-specs-box mt-4" data-type="CUPCAKE">
        <legend><i class="bi bi-balloon-heart"></i> Cupcake details</legend>
        <div class="row g-3">
            <div class="col-sm-4">
                <label class="form-label" for="piecesPerBox">Pieces per box</label>
                <form:input path="piecesPerBox" type="number" min="1" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                <form:errors path="piecesPerBox" cssClass="invalid-feedback"/>
            </div>
            <div class="col-sm-8">
                <label class="form-label" for="frosting">Frosting</label>
                <form:input path="frosting" cssClass="form-control"/>
            </div>
        </div>
    </fieldset>

    <fieldset class="su-specs-box mt-4" data-type="BREAD">
        <legend><i class="bi bi-basket"></i> Bread details</legend>
        <div class="row g-3">
            <div class="col-sm-4">
                <label class="form-label" for="weightGrams">Loaf weight (g)</label>
                <form:input path="weightGrams" type="number" min="1" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
                <form:errors path="weightGrams" cssClass="invalid-feedback"/>
            </div>
            <div class="col-sm-8 d-flex align-items-end">
                <div class="form-check mb-2">
                    <form:checkbox path="sliced" cssClass="form-check-input" id="sliced"/>
                    <label class="form-check-label" for="sliced">Sliced (+Rs. 20)</label>
                </div>
            </div>
        </div>
    </fieldset>

    <fieldset class="su-specs-box mt-4" data-type="PASTRY">
        <legend><i class="bi bi-cup-hot"></i> Pastry details</legend>
        <div class="row g-3">
            <div class="col-sm-8">
                <label class="form-label" for="filling">Filling</label>
                <form:input path="filling" cssClass="form-control"/>
            </div>
            <div class="col-sm-4 d-flex align-items-end">
                <div class="form-check mb-2">
                    <form:checkbox path="servedWarm" cssClass="form-check-input" id="servedWarm"/>
                    <label class="form-check-label" for="servedWarm">Best served warm</label>
                </div>
            </div>
        </div>
    </fieldset>

    <div class="d-flex gap-2 mt-4">
        <button class="btn btn-su" type="submit"><i class="bi bi-check-lg"></i> ${editing ? 'Save changes' : 'Add product'}</button>
        <a class="btn btn-outline-secondary" href="${ctx}/admin/products">Cancel</a>
    </div>
</form:form>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
