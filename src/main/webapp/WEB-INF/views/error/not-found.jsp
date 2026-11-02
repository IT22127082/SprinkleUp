<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<jsp:include page="/WEB-INF/views/common/header.jsp"><jsp:param name="title" value="Not found"/></jsp:include>

<div class="su-panel su-thanks mx-auto text-center">
    <div class="su-thanks-icon"><i class="bi bi-emoji-frown"></i></div>
    <h1 class="su-page-title">Crumbs! Nothing here.</h1>
    <p class="text-muted">${fn:escapeXml(message)}</p>
    <a class="btn btn-su" href="${ctx}/">Back to the menu</a>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
