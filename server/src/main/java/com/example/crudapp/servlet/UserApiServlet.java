package com.example.crudapp.servlet;

import com.example.crudapp.dao.UserDatabaseHandler;
import com.example.crudapp.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet({ "/api/users", "/api/users/*" })
public class UserApiServlet extends HttpServlet {

    UserDatabaseHandler database = new UserDatabaseHandler();
    ObjectMapper jsonMapper = new ObjectMapper();

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setStatus(200);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setContentType("application/json");

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<User> list = database.getAllUsers();
            String json = jsonMapper.writeValueAsString(list);
            response.getWriter().write(json);
        } else {
            try {
                String idStr = pathInfo.substring(1);
                Long id = Long.parseLong(idStr);
                User user = database.getOneUser(id);
                
                if (user != null) {
                    response.getWriter().write(jsonMapper.writeValueAsString(user));
                } else {
                    response.setStatus(404);
                }
            } catch (Exception e) {
                response.setStatus(400);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");

        User newUser = jsonMapper.readValue(req.getReader(), User.class);
        database.addUser(newUser);

        resp.setStatus(201);
        resp.getWriter().write(jsonMapper.writeValueAsString(newUser));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);
        resp.setContentType("application/json");

        String path = req.getPathInfo();
        if (path != null && path.length() > 1) {
            Long id = Long.parseLong(path.substring(1));

            User updatedUser = jsonMapper.readValue(req.getReader(), User.class);
            updatedUser.setId(id);
            
            database.updateUser(updatedUser);
            resp.getWriter().write(jsonMapper.writeValueAsString(updatedUser));
        } else {
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(resp);

        String path = req.getPathInfo();
        if (path != null && path.length() > 1) {
            Long id = Long.parseLong(path.substring(1));
            database.deleteUser(id);
            resp.setStatus(204);
        } else {
            resp.setStatus(400);
        }
    }
}
