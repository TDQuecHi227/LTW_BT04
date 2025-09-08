package controller;

import entity.Category;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;
import service.CategoryService;
import service.impl.CategoryServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet({"/category/add", "/category/delete", "/category/edit"})
public class CategoryController extends HttpServlet {
    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path  = req.getServletPath();
        if (path.equals("/category/add")) {
            req.getRequestDispatcher("/views/add.jsp").forward(req,resp);
        }
        String id = req.getParameter("id");
        User user = (User)req.getSession().getAttribute("account");
        int role = user.getRole();
        Category category = categoryService.findById(Integer.parseInt(id));
        if (category == null) { resp.sendError(404, "Category not found"); return; }
        else if (path.equals("/category/delete")) {
            if(role == category.getUser().getRole()) {
                categoryService.delete(Integer.parseInt(id));
                resp.sendRedirect(req.getContextPath()+ "/waiting");
            }
            else{
                req.setAttribute("alert", "Không có quyền xóa");
                req.setAttribute("nameRole", user.getUsername());
                if(user.getUserId() != 2){
                    req.setAttribute("categoryList", categoryService.findAll());
                }
                else{
                    req.setAttribute("categoryList", categoryService.findByUserId(user.getUserId()));
                }
                req.getRequestDispatcher("/views/list.jsp").forward(req, resp);
            }
        }
        else if (path.equals("/category/edit")) {
            if(role == category.getUser().getRole()) {
                req.setAttribute("category", category);
                req.getRequestDispatcher("/views/edit.jsp").forward(req, resp);
            }
            else{
                req.setAttribute("nameRole", user.getUsername());
                req.setAttribute("alert", "Không có quyền sửa");
                if(user.getUserId() != 2){
                    req.setAttribute("categoryList", categoryService.findAll());
                }
                else{
                    req.setAttribute("categoryList", categoryService.findByUserId(user.getUserId()));
                }
                req.getRequestDispatcher("/views/list.jsp").forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        User user = (User)req.getSession().getAttribute("account");
        String path = req.getServletPath();
        int roleID = user.getRole();
        switch (path){
            case "/category/add":
                insert(req, resp, user, roleID);
                break;
            case "/category/edit":
                edit(req, resp, user, roleID);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void insert(HttpServletRequest req, HttpServletResponse resp, User user, int roleID) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        Category category = new Category();

        // ---- Setup Commons FileUpload (Jakarta) ----
        DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
        JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);

        try {
            // Parse multipart
            List<FileItem> items = upload.parseRequest(req);

            // Thư mục đích: /image/category trong webapp
            String uploadPath = getServletContext().getRealPath("/image/categories");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            for (FileItem item : items) {
                if (item.isFormField()) {
                    // -------- Text fields --------
                    if ("name".equals(item.getFieldName())) {
                        category.setCateName(item.getString(StandardCharsets.UTF_8));
                    }
                } else {
                    // -------- File field --------
                    if ("icon".equals(item.getFieldName()) && item.getSize() > 0) {
                        // Tên file gốc & làm sạch
                        String original = item.getName();
                        int slash = Math.max(original.lastIndexOf('/'), original.lastIndexOf('\\'));
                        String base = (slash >= 0) ? original.substring(slash + 1) : original;
                        String safeBase = base.replaceAll("[^A-Za-z0-9._-]", "_");
                        if (safeBase.isBlank()) safeBase = "upload.jpg";

                        // Nếu trùng tên -> thêm _1, _2, ...
                        File saved = new File(uploadDir, safeBase);
                        if (saved.exists()) {
                            int dot = safeBase.lastIndexOf('.');
                            String namePart = (dot >= 0) ? safeBase.substring(0, dot) : safeBase;
                            String extPart  = (dot >= 0) ? safeBase.substring(dot)     : "";
                            int i = 1;
                            while (saved.exists()) {
                                saved = new File(uploadDir, namePart + "_" + i + extPart);
                                i++;
                            }
                        }

                        // Ghi file
                        item.write(saved.toPath());

                        // Lưu path tương đối để hiển thị trong JSP
                        category.setCateIcon(saved.getName());
                    }
                }
            }

            // Validate tối thiểu
            if (category.getCateName() == null || category.getCateName().isBlank()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Chưa nhập tên");
                return;
            }

            // Gán chủ sở hữu
            category.setUser(user);

            // INSERT
            categoryService.create(category);
            resp.sendRedirect(req.getContextPath()+ "/waiting");

        } catch (FileUploadException e) {
            throw new ServletException("Tải file thất bại", e);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    private void edit(HttpServletRequest req, HttpServletResponse resp, User user, int roleID) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
        JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);

        // Sẽ gán vào bản ghi có sẵn
        Integer id = null;
        String name = null;
        String newIconRel = null;
        String oldIcon = null;

        try {
            List<FileItem> items = upload.parseRequest(req);

            // Thư mục tĩnh /image/category trong webapp
            String uploadPath = getServletContext().getRealPath("/image/categories");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            for (FileItem item : items) {
                if (item.isFormField()) {
                    String field = item.getFieldName();
                    String val = item.getString(StandardCharsets.UTF_8); // v2: dùng Charset

                    switch (field) {
                        case "id":      if (!val.isBlank()) id = Integer.parseInt(val.trim()); break;
                        case "name":    name = val; break;
                        case "oldIcon": oldIcon = val; break;
                    }
                } else if ("icon".equals(item.getFieldName()) && item.getSize() > 0) {
                    String original = item.getName();
                    int slash = Math.max(original.lastIndexOf('/'), original.lastIndexOf('\\'));
                    String base = (slash >= 0) ? original.substring(slash + 1) : original;

                    String safeBase = base.replaceAll("[^A-Za-z0-9._-]", "_");
                    if (safeBase.isBlank()) safeBase = "upload";

                    // tách đuôi
                    int dot = safeBase.lastIndexOf('.');
                    String namePart = (dot >= 0) ? safeBase.substring(0, dot) : safeBase;
                    String extPart  = (dot >= 0) ? safeBase.substring(dot)     : "";

                    String fileName = namePart + extPart;

                    File saved = new File(uploadDir, fileName);
                    item.write(saved.toPath());

                    newIconRel = fileName;
                }

            }

            if (id == null) { resp.sendError(400, "Missing id"); return; }
            Category db = categoryService.findById(id);
            if (db == null) { resp.sendError(404, "Category not found"); return; }
            if (name == null || name.isBlank()) { resp.sendError(400, "Name is required"); return; }

            db.setCateName(name);
            if (newIconRel != null) db.setCateIcon(newIconRel);
            else if (oldIcon != null && !oldIcon.isBlank()) db.setCateIcon(oldIcon);
            // else: giữ icon hiện tại trong db

            categoryService.update(db); // <-- UPDATE thay vì insert
            resp.sendRedirect(req.getContextPath() + "/waiting");

        } catch (FileUploadException e) {
            throw new ServletException("Upload failed", e);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
