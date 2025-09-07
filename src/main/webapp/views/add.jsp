<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm danh mục</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f5f5f5; margin:0; }
        .container { max-width: 600px; margin: 40px auto; background: #fff;
            border-radius: 10px; padding: 24px;
            box-shadow: 0 2px 8px rgba(0,0,0,.1); }
        h1 { margin-top:0; font-size: 24px; color: #1976d2; }
        .form-row { margin-bottom: 18px; }
        label { display:block; font-weight:600; margin-bottom:6px; }
        input[type="text"], input[type="file"] {
            width:100%; padding:10px; border:1px solid #ccc; border-radius:8px;
        }
        img.preview { width: 140px; height: 140px; border-radius: 50%;
            object-fit: cover; border: 1px solid #ddd; margin-top:10px; display:none; }
        .actions { display:flex; gap:10px; margin-top:20px; }
        button { padding:10px 20px; border:none; border-radius:8px; cursor:pointer; }
        .btn-primary { background:#1976d2; color:#fff; }
        .btn-secondary { background:#eee; }
    </style>
</head>
<body>
<div class="container">
    <h1>Thêm danh mục mới</h1>

    <c:url value="/category/add" var="addUrl"/>
    <form action="${addUrl}" method="post" enctype="multipart/form-data">

        <!-- tên danh mục -->
        <div class="form-row">
            <label for="name">Tên danh mục</label>
            <input type="text" id="name" name="name" placeholder="Nhập tên danh mục" required/>
        </div>

        <!-- upload ảnh -->
        <div class="form-row">
            <label for="icon">Ảnh sản phẩm</label>
            <input type="file" id="icon" name="icon" accept="image/*" onchange="previewImage(event)" required/>
            <img id="preview" class="preview" alt="Preview"/>
        </div>

        <div class="actions">
            <button type="submit" class="btn-primary">Thêm mới</button>
            <button type="reset" class="btn-secondary">Reset</button>
        </div>
    </form>
</div>

<script>
    function previewImage(e) {
        const file = e.target.files[0];
        if (!file) return;
        const url = URL.createObjectURL(file);
        const img = document.getElementById("preview");
        img.style.display = "block";
        img.src = url;
        img.onload = () => URL.revokeObjectURL(url);
    }
</script>
</body>
</html>
