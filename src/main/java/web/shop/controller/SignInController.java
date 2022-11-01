package web.shop.controller;

import org.apache.log4j.Logger;
import web.shop.entity.User;
import web.shop.exception.DataAccessException;
import web.shop.service.ValidatorService;
import web.shop.util.Constant;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static web.shop.util.Constant.CONSTRAINED_PAGE_ATTR;
import static web.shop.util.Constant.USER;
import static web.shop.util.Constant.VALIDATOR_SERVICE;

@WebServlet(name = "signInController", urlPatterns = "/sign_in")
public class SignInController extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(SignInController.class);
    private ValidatorService validatorService;

    public SignInController() {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        validatorService = (ValidatorService) config.getServletContext().getAttribute(VALIDATOR_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(Constant.PATH_TO_SIGN_IN_PAGE_JSP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = validatorService.loginValidate(req);
            if (Objects.nonNull(user)) {
                HttpSession session = req.getSession();
                synchronized (session) {
                    session.setAttribute(USER, user);
                }
                String previousPage = getPreviousUrl(session);
                resp.sendRedirect(previousPage);
            } else {
                doGet(req, resp);
            }
        } catch (DataAccessException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            LOG.error(e.getMessage(), e);
        }
    }

    private String getPreviousUrl(HttpSession session) {
        Optional<String> previousUrl = Optional.ofNullable((String) session.getAttribute(CONSTRAINED_PAGE_ATTR));
        session.removeAttribute(CONSTRAINED_PAGE_ATTR);
        return previousUrl.orElse("/");
    }
}
