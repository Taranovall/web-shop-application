package web.shop.listener;


import web.shop.captcha.CaptchaHandler;
import web.shop.captcha.impl.CaptchaContextHandler;
import web.shop.captcha.impl.CaptchaSessionHandler;
import web.shop.entity.Constraint;
import web.shop.repository.impl.CategoryRepositoryImpl;
import web.shop.repository.impl.OrderRepositoryImpl;
import web.shop.repository.impl.ProducerRepositoryImpl;
import web.shop.repository.impl.ProductRepositoryImpl;
import web.shop.repository.impl.UserRepositoryImpl;
import web.shop.repository.template.JDBCTemplate;
import web.shop.service.CategoryService;
import web.shop.service.OrderService;
import web.shop.service.ProducerService;
import web.shop.service.ProductService;
import web.shop.service.UserService;
import web.shop.service.ValidatorService;
import web.shop.service.impl.CategoryServiceImpl;
import web.shop.service.impl.OrderServiceImpl;
import web.shop.service.impl.ProducerServiceImpl;
import web.shop.service.impl.ProductServiceImpl;
import web.shop.service.impl.UserServiceImpl;
import web.shop.service.impl.ValidatorServiceImpl;
import web.shop.service.manager.TransactionManager;
import web.shop.util.FailedAuthenticationUtil;
import web.shop.util.XmlSecurityParser;
import web.shop.util.locale.LocaleStorage;
import web.shop.util.locale.LocaleUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static web.shop.util.Constant.AVAILABLE_LOCALES;
import static web.shop.util.Constant.CAPTCHA_HANDLER;
import static web.shop.util.Constant.CATEGORY_SERVICE;
import static web.shop.util.Constant.CONSTRAINTS;
import static web.shop.util.Constant.LOCALE_STORAGE_UTIL;
import static web.shop.util.Constant.LOCALE_UTIL;
import static web.shop.util.Constant.ORDER_SERVICE;
import static web.shop.util.Constant.PRODUCER_SERVICE;
import static web.shop.util.Constant.PRODUCT_SERVICE;
import static web.shop.util.Constant.USER_SERVICE;
import static web.shop.util.Constant.VALIDATOR_SERVICE;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String handler = context.getInitParameter(CAPTCHA_HANDLER);

        CaptchaHandler captchaHandler = Map.of(
                "session", new CaptchaSessionHandler(),
                "context", new CaptchaContextHandler()
        ).get(handler);

        JDBCTemplate jdbcTemplate = new JDBCTemplate();
        TransactionManager transactionManager = new TransactionManager();
        UserService userService = new UserServiceImpl(new UserRepositoryImpl(jdbcTemplate), transactionManager);
        ProducerService producerService = new ProducerServiceImpl(new ProducerRepositoryImpl(jdbcTemplate), transactionManager);
        CategoryService categoryService = new CategoryServiceImpl(new CategoryRepositoryImpl(jdbcTemplate), transactionManager);
        ProductService productService = new ProductServiceImpl(new ProductRepositoryImpl(jdbcTemplate), transactionManager);
        OrderService orderService = new OrderServiceImpl(new OrderRepositoryImpl(jdbcTemplate), transactionManager);
        ValidatorService validatorService = new ValidatorServiceImpl(userService, new FailedAuthenticationUtil());
        LocaleUtil localeUtil = new LocaleUtil();
        LocaleStorage localeStorage = new LocaleStorage(localeUtil);
        Set<Locale> availableLocales = localeUtil.getAvailableLocales(context);
        List<Constraint> constraints = XmlSecurityParser.getXmlConstraintsAsList();

        context.setAttribute(USER_SERVICE, userService);
        context.setAttribute(PRODUCER_SERVICE, producerService);
        context.setAttribute(CATEGORY_SERVICE, categoryService);
        context.setAttribute(PRODUCT_SERVICE, productService);
        context.setAttribute(ORDER_SERVICE, orderService);
        context.setAttribute(VALIDATOR_SERVICE, validatorService);
        context.setAttribute(CAPTCHA_HANDLER, captchaHandler);
        context.setAttribute(LOCALE_STORAGE_UTIL, localeStorage);
        context.setAttribute(LOCALE_UTIL, localeUtil);
        context.setAttribute(AVAILABLE_LOCALES, availableLocales);
        context.setAttribute(CONSTRAINTS, constraints);
    }
}
