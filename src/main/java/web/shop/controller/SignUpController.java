package web.shop.controller;

import org.apache.log4j.Logger;
import web.shop.entity.Newsletter;
import web.shop.entity.User;
import web.shop.exception.DataAccessException;
import web.shop.service.UserService;
import web.shop.service.ValidatorService;
import web.shop.util.CaptchaGeneratorUtil;
import web.shop.util.Constant;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

import static web.shop.util.Constant.NEWSLETTERS;
import static web.shop.util.Constant.PROFILE_PICTURE;
import static web.shop.util.Constant.USER;
import static web.shop.util.Constant.USER_SERVICE;
import static web.shop.util.Constant.VALIDATOR_SERVICE;


@WebServlet(name = "signUp", urlPatterns = "/sign_up")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 100
)
public class SignUpController extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SignUpController.class);
    private ValidatorService validatorService;
    private UserService userService;

    public SignUpController() {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        validatorService = (ValidatorService) config.getServletContext().getAttribute(VALIDATOR_SERVICE);
        userService = (UserService) config.getServletContext().getAttribute(USER_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(NEWSLETTERS, Newsletter.values());
        CaptchaGeneratorUtil.generate(req, resp);
        req.getRequestDispatcher(Constant.PATH_TO_SIGN_UP_PAGE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = validatorService.registrationValidate(req);
            if (Objects.isNull(user)) {
                doGet(req, resp);
            } else {
                userService.create(user, req.getPart(PROFILE_PICTURE));
                HttpSession session = req.getSession();
                synchronized (session) {
                    session.setAttribute(USER, user);
                }
                resp.sendRedirect("/");
            }
        } catch (DataAccessException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            LOG.error(e.getMessage(), e);
        }
    }
}
