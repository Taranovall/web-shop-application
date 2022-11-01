package web.shop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import web.shop.exception.DataAccessException;
import web.shop.service.CategoryService;
import web.shop.service.ProducerService;
import web.shop.service.ProductService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainPageControllerTest {

    @Mock
    private ServletContext context;
    @Mock
    private HttpServletRequest req;
    @Mock
    private HttpServletResponse resp;
    @Mock
    private HttpSession session;
    @Mock
    private ProductService productService;
    @Mock
    private ProducerService producerService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private RequestDispatcher requestDispatcher;
    @InjectMocks
    private MainPageController mainPageController;

    @BeforeEach
    void setUp() {
        when(req.getSession()).thenReturn(session);
    }

    @Test
    void shouldReturnPageWithProducts() throws ServletException, IOException {
        when(req.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        mainPageController.doGet(req, resp);
        verify(requestDispatcher, times(1)).forward(req, resp);
    }

    @Test
    void shouldHandleException() throws IOException, ServletException, DataAccessException {
        when(producerService.getAll()).thenThrow(DataAccessException.class);
        doNothing().when(resp).sendError(anyInt(), any());
        mainPageController.doGet(req, resp);

        verify(resp, times(1)).sendError(anyInt(), any());
    }
}