package web.shop.service.impl;

import web.shop.bean.FilterBean;
import web.shop.captcha.CaptchaHandler;
import web.shop.entity.Newsletter;
import web.shop.entity.User;
import web.shop.exception.DataAccessException;
import web.shop.exception.not.found.UserNotFoundException;
import web.shop.service.UserService;
import web.shop.service.ValidatorService;
import web.shop.util.Constant;
import web.shop.util.FailedAuthenticationUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static web.shop.util.Constant.CAPTCHA;
import static web.shop.util.Constant.CAPTCHA_HANDLER;
import static web.shop.util.Constant.CTG;
import static web.shop.util.Constant.EMAIL;
import static web.shop.util.Constant.ERROR;
import static web.shop.util.Constant.FIELD_TO_BE_HIGHLIGHTED;
import static web.shop.util.Constant.FIRST_NAME;
import static web.shop.util.Constant.LANGUAGE;
import static web.shop.util.Constant.LOGIN;
import static web.shop.util.Constant.NEWSLETTERS;
import static web.shop.util.Constant.PAGE;
import static web.shop.util.Constant.PASSWORD;
import static web.shop.util.Constant.PROD;
import static web.shop.util.Constant.RANGE_PRIMARY;
import static web.shop.util.Constant.SEARCH_QUERY;
import static web.shop.util.Constant.SECOND_NAME;
import static web.shop.util.Constant.SORT_METHOD;

public class ValidatorServiceImpl implements ValidatorService {

    private final UserService userService;
    private final FailedAuthenticationUtil failedAuthenticationUtil;

    public ValidatorServiceImpl(UserService userService, FailedAuthenticationUtil failedAuthenticationUtil) {
        this.userService = userService;
        this.failedAuthenticationUtil = failedAuthenticationUtil;
    }

    @Override
    public FilterBean filterValidate(Map<String, String[]> params, HttpServletResponse resp) throws IOException {
        params = new TreeMap<>(params);
        Map<String, String[]> validParams = new TreeMap<>();
        FilterBean filterBean = new FilterBean();

        String price = params.get(Constant.RANGE_PRIMARY)[0];
        Pattern pattern = Pattern.compile("^(\\d+);(\\d+)$");
        Matcher matcher = pattern.matcher(price);
        if (matcher.find()) {
            String min = matcher.group(1);
            String max = matcher.group(2);
            if (min.compareTo(max) < 0) {
                validParams.put(RANGE_PRIMARY, new String[]{price});
                filterBean.setMinPrice(Integer.valueOf(min));
                filterBean.setMaxPrice(Integer.valueOf(max));
            }
        }

        String[] query = params.get(SEARCH_QUERY);
        if (Objects.nonNull(query) && query[0].length() > 0) {
            validParams.put(SEARCH_QUERY, query);
            filterBean.setQuery(query[0]);
        }

        String[] categories = putParameterValuesIntoMap(params.get(CTG));
        if (categories.length > 0) {
            validParams.put(CTG, categories);
            filterBean.setCategories(categories);
        }

        String[] producers = putParameterValuesIntoMap(params.get(PROD));
        if (producers.length > 0) {
            validParams.put(PROD, producers);
            filterBean.setProducers(producers);
        }

        String[] sortMethod = params.get(SORT_METHOD);
        if (Objects.nonNull(sortMethod) && sortMethod[0].matches("^[a-z-]+$")) {
            validParams.put(SORT_METHOD, sortMethod);
            filterBean.setSortMethod(sortMethod[0]);
        }

        if (params.containsKey(LANGUAGE)) {
            validParams.put(LANGUAGE, params.get(LANGUAGE));
        }

        params.remove(PAGE);

        String validURI = generateURI(validParams);
        String actualURI = generateURI(params);
        if (!actualURI.equals(validURI)) {
            resp.sendRedirect(validURI);
            return null;
        }
        return filterBean;
    }

    private String generateURI(Map<String, String[]> validParams) {
        StringBuilder uri = new StringBuilder();
        uri.append("/product-filter?");
        validParams.entrySet().forEach(e -> {
            String parameterName = e.getKey();
            for (String parameterValue : e.getValue()) {
                uri.append(String.format("%s=%s&", parameterName, parameterValue));
            }
        });
        return uri.toString().replaceFirst(".$", "");
    }

    private String[] putParameterValuesIntoMap(String[] values) {
        if (Objects.nonNull(values)) {
            return Arrays.stream(values)
                    .filter(val -> val.matches("^\\d+$"))
                    .toArray(String[]::new);
        }
        return new String[0];
    }

    @Override
    public User registrationValidate(HttpServletRequest req) throws DataAccessException {
        boolean isThereAnyError = false;
        User userFromReq = getUserFromRequest(req);

        if (userService.isUserExists(userFromReq.getLogin())) {
            String errorMsg = String.format("User with login '%s' already exists", userFromReq.getLogin());
            req.setAttribute(ERROR, errorMsg);
            req.setAttribute(FIELD_TO_BE_HIGHLIGHTED, LOGIN);
            userFromReq.setLogin(null);
            isThereAnyError = true;
        }

        if (userService.isEmailTaken(userFromReq.getMail())) {
            String errorMsg = String.format("Email '%s' is already taken", userFromReq.getMail());
            req.setAttribute(ERROR, errorMsg);
            req.setAttribute(FIELD_TO_BE_HIGHLIGHTED, EMAIL);
            userFromReq.setMail(null);
            isThereAnyError = true;
        }

        if (!isCaptchaValid(req)) {
            isThereAnyError = true;
        }

        if (isThereAnyError) {
            userFromReq.setPassword(null);
            req.setAttribute("userBean", userFromReq);
            return null;
        }
        return userFromReq;
    }

    @Override
    public User loginValidate(HttpServletRequest req) throws DataAccessException {
        String login = req.getParameter(LOGIN);
        byte[] password = req.getParameter(PASSWORD).getBytes(StandardCharsets.UTF_8);
        try {
            if (!failedAuthenticationUtil.isUserBanned(login)) {
                return handleAuthentication(req, login, password, userService.getByLogin(login));
            }

            req.setAttribute(ERROR, String.format("You will be unbanned in %s minutes",
                    failedAuthenticationUtil.getMinutesRemainingToUnban(login)));
        } catch (UserNotFoundException e) {
            req.setAttribute(ERROR, e.getMessage());
            req.setAttribute(FIELD_TO_BE_HIGHLIGHTED, LOGIN);
        }
        return null;
    }

    private User handleAuthentication(HttpServletRequest req, String login, byte[] password, User user) {
        if (Arrays.equals(password, user.getPassword())) {
            user.setPassword(null);
            failedAuthenticationUtil.authenticate(login);
            return user;
        }
        failedAuthenticationUtil.handleFailedAuthentication(login);
        req.setAttribute(ERROR, "Incorrect password");
        req.setAttribute(FIELD_TO_BE_HIGHLIGHTED, PASSWORD);
        return null;
    }

    private boolean isCaptchaValid(HttpServletRequest req) {
        ServletContext servlet = req.getSession().getServletContext();
        CaptchaHandler handler = (CaptchaHandler) servlet.getAttribute(CAPTCHA_HANDLER);

        if (handler.isCaptchaUpToDate(req)) {
            String inputtedCaptcha = req.getParameter(CAPTCHA);
            String actualCaptcha = handler.getCaptcha(req);

            if (inputtedCaptcha.equals(actualCaptcha)) {
                return true;
            }
            req.setAttribute(ERROR, "Incorrect captcha");
            req.setAttribute(FIELD_TO_BE_HIGHLIGHTED, CAPTCHA);
            return false;
        }
        handler.removeCaptcha(req);
        req.setAttribute(ERROR, "Captcha is out of date");
        return false;
    }

    private User getUserFromRequest(HttpServletRequest req) {
        String login = req.getParameter(Constant.LOGIN);
        String firstName = req.getParameter(FIRST_NAME);
        String secondName = req.getParameter(SECOND_NAME);
        String password = req.getParameter(PASSWORD);
        String email = req.getParameter(EMAIL);
        List<Newsletter> newsletters = Optional.ofNullable(req.getParameterValues(NEWSLETTERS))
                .map(strings -> Arrays.stream(strings)
                        .map(Newsletter::valueOf)
                        .collect(Collectors.toList())).orElse(Collections.emptyList());
        return new User()
                .setLogin(login)
                .setFirstName(firstName)
                .setSecondName(secondName)
                .setPassword(password.getBytes(StandardCharsets.UTF_8))
                .setMail(email)
                .setNewsletterList(newsletters);
    }
}
