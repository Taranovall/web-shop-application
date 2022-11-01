package web.shop.service;


import web.shop.bean.FilterBean;
import web.shop.entity.User;
import web.shop.exception.DataAccessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface ValidatorService {

    User registrationValidate(HttpServletRequest req) throws DataAccessException;

    User loginValidate(HttpServletRequest req) throws DataAccessException;

    FilterBean filterValidate(Map<String, String[]> req, HttpServletResponse resp) throws IOException;
}