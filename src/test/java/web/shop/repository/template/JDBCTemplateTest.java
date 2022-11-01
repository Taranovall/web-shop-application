package web.shop.repository.template;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import web.shop.connection.ConnectionPool;
import web.shop.entity.Category;
import web.shop.entity.Producer;
import web.shop.entity.Product;
import web.shop.exception.DataAccessException;
import web.shop.repository.UserRepository;
import web.shop.repository.impl.UserRepositoryImpl;
import web.shop.repository.mapper.ProducerMapper;
import web.shop.repository.mapper.ProductMapper;
import web.shop.repository.mapper.UserMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static web.shop.util.Constant.AMOUNT;
import static web.shop.util.Constant.DESCRIPTION;
import static web.shop.util.Constant.ID;
import static web.shop.util.Constant.IMG_PATH;
import static web.shop.util.Constant.NAME;
import static web.shop.util.Constant.PRICE;
import static web.shop.util.SQLQuery.GET_ALL_PRODUCTS;
import static web.shop.util.SQLQuery.GET_USER_BY_LOGIN;

@ExtendWith(MockitoExtension.class)
class JDBCTemplateTest {

    @Spy
    private final JDBCTemplate jdbcTemplate = new JDBCTemplate();
    private final UserRepository repository = new UserRepositoryImpl(jdbcTemplate);
    @Mock
    private PreparedStatement pstmt;
    @Mock
    private Statement stmt;
    @Mock
    private ResultSet rs;
    @Mock
    Connection c;
    private static ConnectionPool connectionPool;
    private static MockedStatic<ConnectionPool> mocked;

    @BeforeAll
    static void beforeAll() {
        connectionPool = mock(ConnectionPool.class);
        mocked = mockStatic(ConnectionPool.class);
        mocked.when(ConnectionPool::getInstance).thenReturn(connectionPool);
    }

    @AfterAll
    static void afterAll() {
        mocked.close();
    }

    @BeforeEach
    void setUp() {
        c = mock(Connection.class);
        when(connectionPool.getConnection()).thenReturn(c);
    }


    @Test
    void shouldGetProductByQuery() throws SQLException, DataAccessException {
        Long productId = 1L;
        Long categoryId = 29L;
        Long producerId = 33L;
        String name = "Samsung A20";
        Integer price = 21999;
        Integer amount = 5;
        String desc = "This is a phone";
        String category = "Phone";
        String producer = "Samsung";
        String imgPath = "/images/products/galaxyS%20A20.png";

        when(c.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getLong(ID)).thenReturn(productId);
        when(rs.getString(NAME)).thenReturn(name);
        when(rs.getInt(PRICE)).thenReturn(price);
        when(rs.getInt(AMOUNT)).thenReturn(amount);
        when(rs.getString(DESCRIPTION)).thenReturn(desc);
        when(rs.getString(IMG_PATH)).thenReturn(imgPath);
        when(rs.getLong("category_id")).thenReturn(categoryId);
        when(rs.getLong("producer_id")).thenReturn(producerId);
        when(rs.getString("category")).thenReturn(category);
        when(rs.getString("producer")).thenReturn(producer);

        Optional<Product> actualProduct = jdbcTemplate.queryForObject(c, GET_ALL_PRODUCTS, new ProductMapper());

        assertTrue(actualProduct.isPresent());

        Product expectedProduct = new Product()
                .setId(productId).setName(name).setPrice(price).setAmount(amount).setDescription(desc).setImgPath(imgPath)
                .setCategory(new Category().setId(categoryId).setName(category))
                .setProducer(new Producer().setId(producerId).setName(producer));

        assertEquals(expectedProduct, actualProduct.get());
    }

    @Test
    void shouldGetListOfProducersByQuery() throws SQLException, DataAccessException {
        List<Producer> expectedList = List.of(
                new Producer().setId(1L).setName("Samsung"),
                new Producer().setId(9L).setName("Xiaomi"),
                new Producer().setId(22L).setName("Asus")
        );

        when(c.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rs);
        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(rs.getLong(ID)).thenReturn(1L).thenReturn(9L).thenReturn(22L);
        when(rs.getString(NAME)).thenReturn("Samsung").thenReturn("Xiaomi").thenReturn("Asus");

        String sqlQuery = "query";
        List<Producer> actualList = jdbcTemplate.queryForList(c, sqlQuery, new ProducerMapper());

        assertEquals(expectedList, actualList);
    }

    @Test
    void shouldThrowDataAccessException() throws SQLException, DataAccessException {
        when(c.prepareStatement(anyString())).thenReturn(pstmt);
        DataAccessException thrown = assertThrows(DataAccessException.class, () ->
                jdbcTemplate.queryForObject(c, GET_USER_BY_LOGIN, new Object[]{"FirstParam", "SecondParam"}, new UserMapper())
        );
        String expectedMessage = "Amount of parameters wanted in sql query: 1, amount of passed parameters 2";
        String actualMessage = thrown.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}