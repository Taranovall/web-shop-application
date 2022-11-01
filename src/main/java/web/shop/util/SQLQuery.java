package web.shop.util;

public class SQLQuery {

    public static final String CREATE_USER = "INSERT INTO user (login, first_name, second_name, password, email, profile_pic_path) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String ADD_NEWSLETTER_TO_USER = "INSERT INTO user_newsletters (user_id, newsletter_id) VALUES (?, ?)";
    public static final String GET_USER_BY_LOGIN = "SELECT * FROM user WHERE login = ?";
    public static final String GET_NEWSLETTERS_OF_USER_BY_ID = "SELECT n.id FROM newsletter n, user_newsletters un WHERE ? = un.user_id AND un.newsletter_id = n.id";
    public static final String GET_LOGIN = "SELECT 1 FROM user WHERE login = ? LIMIT 1";
    public static final String GET_EMAIL = "SELECT 1 FROM user WHERE email = ? LIMIT 1";
    public static final String GET_ALL_PRODUCTS = "SELECT product.id,\n" +
            "       product.name AS name,\n" +
            "       price,\n" +
            "       amount,\n" +
            "       description,\n" +
            "       c.name AS category,\n" +
            "       p.name AS producer,\n" +
            "       category_id,\n" +
            "       producer_id,\n" +
            "       img_path\n" +
            "FROM product\n" +
            "         JOIN category c on c.id = product.category_id\n" +
            "         JOIN producer p on p.id = product.producer_id\n" +
            "ORDER BY product.id\n";
    public static final String GET_MAX_PRICE = "SELECT max(price) FROM  product";
    public static final String GET_MIN_PRICE = "SELECT min(price) FROM  product";

    public static final String GET_PRODUCTS = "SELECT product.id,\n" +
            "       product.name AS name,\n" +
            "       price,\n" +
            "       amount,\n" +
            "       description,\n" +
            "       c.name       AS category,\n" +
            "       p.name       AS producer,\n" +
            "       category_id,\n" +
            "       producer_id,\n" +
            "       img_path\n" +
            "FROM product\n" +
            "         JOIN category c on c.id = product.category_id\n" +
            "         JOIN producer p on p.id = product.producer_id\n" +
            "ORDER BY product.id\n" +
            "LIMIT ? OFFSET ?\n";

    public static final String GET_AMOUNT_OF_PRODUCTS = "SELECT count(*) FROM product";

    public static final String GET_ALL_PRODUCERS = "SELECT * FROM producer";
    public static final String GET_ALL_CATEGORIES = "SELECT * FROM category";

    public static final String CREATE_ORDER = "INSERT INTO `order` (status, status_detail, detail_time, user_id, payment_method, address)\n" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    public static final String CREATE_ORDERED_PRODUCT_INFO = "INSERT INTO ordered_products_info (product_id, price, amount)\n" +
            "VALUES (?, ?, ?)";
    public static final String LINK_ORDER_WITH_ORDERED_PRODUCT_INFO = "INSERT INTO order_product (order_id, ordered_product_id)\n" +
            "VALUES (?, ?)";
    public static final String GET_PRODUCT_BY_ID = "SELECT product.id,\n" +
            "       product.name AS name,\n" +
            "       price,\n" +
            "       amount,\n" +
            "       description,\n" +
            "       c.name AS category,\n" +
            "       p.name AS producer,\n" +
            "       category_id,\n" +
            "       producer_id,\n" +
            "       img_path\n" +
            "FROM product\n" +
            "         JOIN category c on c.id = product.category_id\n" +
            "         JOIN producer p on p.id = product.producer_id\n" +
            "WHERE product.id = ?";
    public static final String CREATE_CARD = "INSERT INTO order_card (order_id, cvv, number, expiration_date)\n" +
            "VALUES (?, ?, ?, ?)";
    public static final String CHANGE_AMOUNT_OF_PRODUCT_IN_SHOP = "UPDATE product SET amount = ? WHERE id = ?";
    public static final String GET_ROLE_BY_USER_ID = "SELECT r.name\n" +
            "FROM user u\n" +
            "         JOIN user_roles ur on u.id = ur.user_id\n" +
            "         JOIN role r on r.id = ur.role_id\n" +
            "WHERE u.login = ?";
}
