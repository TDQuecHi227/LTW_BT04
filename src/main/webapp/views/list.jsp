<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap (tùy ý) -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <style>
        body { background:#f7f8fa; }
        .page-header { padding:24px 0; }
        .card { border-radius:16px; box-shadow:0 6px 16px rgba(0,0,0,.06); }
        table img { object-fit:cover; border-radius:8px; }
        .action-link { text-decoration:none; }
        .action-link:hover { text-decoration:underline; }
        .logout-btn {
            padding: 10px 20px;
            background-color: #f44336;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .logout-btn:hover {
            background-color: #d32f2f;
        }
    </style>
    </style>
</head>
<body>
<div class="container my-4">
    <c:if test="${not empty alert}">
        <div class="alert alert-warning mb-0">
            ${alert}
        </div>
    </c:if>
    <!-- Header -->
    <div class="page-header d-flex justify-content-between align-items-center">
        <h1 class="h3 m-0">Bảng điều khiển ${nameRole}</h1>
        <form action="home" method="POST">
            <button type="submit" class="logout-btn">Đăng xuất</button>
        </form>
        <div>
            <c:url value="/category/add" var="addUrl"/>
            <a href="${addUrl}" class="btn btn-primary">+ Thêm danh mục</a>
        </div>
    </div>

    <!-- Danh sách danh mục -->
    <div class="card">
        <div class="card-body">
            <h2 class="h5 mb-3">Danh sách danh mục</h2>

            <!-- Nếu rỗng -->
            <c:if test="${empty categoryList}">
                <div class="alert alert-warning mb-0">
                    Chưa có danh mục nào.
                </div>
            </c:if>

            <!-- Bảng dữ liệu -->
            <c:if test="${not empty categoryList}">
                <div class="table-responsive">
                    <table class="table table-striped align-middle">
                        <thead>
                        <tr>
                            <th style="width:80px;">STT</th>
                            <th style="width:220px;">Ảnh</th>
                            <th>Tên danh mục</th>
                            <th>Thuộc quyền</th>
                            <th style="width:180px;" class="text-center">Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <!-- ĐOẠN CODE CỦA BẠN -->
                        <c:forEach items="${categoryList}" var="cate" varStatus="STT">
                            <tr class="odd gradeX">
                                <td>${STT.index + 1}</td>

                                <c:url value="/image/categories/${cate.cateIcon}" var="imgUrl"/>
                                <td>
                                    <img height="150" width="200" src="${imgUrl}" alt="icon ${cate.cateName}"/>
                                </td>

                                <td>${cate.cateName}</td>
                                <td>${cate.user.username}</td>
                                <td class="text-center">
                                    <a href="<c:url value='/category/edit?id=${cate.cateId}'/>"
                                       class="action-link me-2">Sửa</a>
                                    |
                                    <a href="<c:url value='/category/delete?id=${cate.cateId}'/>"
                                       class="action-link ms-2"
                                       onclick="return confirm('Bạn có chắc muốn xóa danh mục này?');">Xóa</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <!-- HẾT ĐOẠN CODE CỦA BẠN -->
                        </tbody>
                    </table>
                </div>
            </c:if>
        </div>
    </div>
</div>

<!-- JS (tùy ý) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
