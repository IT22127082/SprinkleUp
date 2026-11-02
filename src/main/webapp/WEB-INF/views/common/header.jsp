<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="isAdmin" value="${fn:startsWith(activePage, 'admin')}" scope="request"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${fn:escapeXml(param.title)} · SprinkleUp Bakery</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Patrick+Hand&family=Nunito:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="${ctx}/css/sprinkleup.css" rel="stylesheet">
    <link rel="icon" type="image/png" href="${ctx}/images/logo.png">
</head>
<body class="${isAdmin ? 'su-admin' : 'su-shop'}">

<nav class="navbar navbar-expand-md su-nav sticky-top">
    <div class="container">
        <a class="navbar-brand su-brand" href="${ctx}/">
            <img src="${ctx}/images/logo.png" alt=""><span>Sprinkle<b>Up</b></span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav"
                aria-controls="mainNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav ms-auto gap-md-1 align-items-md-center">
                <li class="nav-item"><a class="nav-link ${activePage == 'shop' ? 'active' : ''}" href="${ctx}/">Menu</a></li>
                <li class="nav-item"><a class="nav-link ${activePage == 'order' ? 'active' : ''}" href="${ctx}/order">Order</a></li>
                <c:if test="${empty currentUser or not currentUser.admin}">
                    <li class="nav-item"><a class="nav-link ${activePage == 'custom' ? 'active' : ''}" href="${ctx}/custom-orders/new">Custom cake</a></li>
                </c:if>
                <c:choose>
                    <c:when test="${empty currentUser}">
                        <li class="nav-item ms-md-2"><a class="btn btn-sm btn-su" href="${ctx}/login"><i class="bi bi-person me-1"></i>Sign in</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item dropdown ms-md-2">
                            <a class="nav-link dropdown-toggle su-user ${activePage == 'account' or isAdmin ? 'active' : ''}" href="#" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="bi ${currentUser.admin ? 'bi-shop-window' : 'bi-person-heart'} me-1"></i>${fn:escapeXml(currentUser.firstName)}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><span class="dropdown-item-text small text-muted">${fn:escapeXml(currentUser.email)}<br>${currentUser.role.label}</span></li>
                                <li><hr class="dropdown-divider"></li>
                                <c:choose>
                                    <c:when test="${currentUser.admin}">
                                        <li><a class="dropdown-item" href="${ctx}/admin"><i class="bi bi-speedometer2 me-2"></i>Bakery admin</a></li>
                                    </c:when>
                                    <c:otherwise>
                                        <li><a class="dropdown-item" href="${ctx}/account"><i class="bi bi-bag-heart me-2"></i>My orders</a></li>
                                        <li><a class="dropdown-item" href="${ctx}/account/edit"><i class="bi bi-gear me-2"></i>My details</a></li>
                                    </c:otherwise>
                                </c:choose>
                                <li>
                                    <form method="post" action="${ctx}/logout">
                                        <button class="dropdown-item" type="submit"><i class="bi bi-box-arrow-right me-2"></i>Sign out</button>
                                    </form>
                                </li>
                            </ul>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<c:if test="${isAdmin}">
    <div class="su-subnav">
        <div class="container">
            <nav class="nav nav-pills">
                <a class="nav-link ${activePage == 'admin-dashboard' ? 'active' : ''}" href="${ctx}/admin"><i class="bi bi-speedometer2"></i> Dashboard</a>
                <a class="nav-link ${activePage == 'admin-products' ? 'active' : ''}" href="${ctx}/admin/products"><i class="bi bi-basket2"></i> Products</a>
                <a class="nav-link ${activePage == 'admin-customers' ? 'active' : ''}" href="${ctx}/admin/customers"><i class="bi bi-people"></i> Customers</a>
                <a class="nav-link ${activePage == 'admin-orders' ? 'active' : ''}" href="${ctx}/admin/orders"><i class="bi bi-receipt"></i> Orders</a>
                <a class="nav-link ${activePage == 'admin-custom' ? 'active' : ''}" href="${ctx}/admin/custom-orders"><i class="bi bi-stars"></i> Custom cakes</a>
            </nav>
        </div>
    </div>
</c:if>

<main class="container py-4">
    <c:if test="${not empty success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle me-1"></i>${fn:escapeXml(success)}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle me-1"></i>${fn:escapeXml(error)}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
