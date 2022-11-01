package web.shop.repository.mapper;

import web.shop.entity.Category;
import web.shop.entity.Producer;
import web.shop.entity.Product;
import web.shop.exception.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;

import static web.shop.util.Constant.AMOUNT;
import static web.shop.util.Constant.DESCRIPTION;
import static web.shop.util.Constant.ID;
import static web.shop.util.Constant.IMG_PATH;
import static web.shop.util.Constant.NAME;
import static web.shop.util.Constant.PRICE;

public class ProductMapper implements RowMapper<Product> {

    private static final String CATEGORY_ID = "category_id";
    private static final String CATEGORY = "category";
    private static final String PRODUCER_ID = "producer_id";
    private static final String PRODUCER = "producer";

    @Override
    public Product mapRow(ResultSet rs) throws DataAccessException {
        try {
            Category category = new Category().
                    setId(rs.getLong(CATEGORY_ID))
                    .setName(rs.getString(CATEGORY));

            Producer producer = new Producer()
                    .setId(rs.getLong(PRODUCER_ID))
                    .setName(rs.getString(PRODUCER));

            return new Product()
                    .setId(rs.getLong(ID))
                    .setName(rs.getString(NAME))
                    .setPrice(rs.getInt(PRICE))
                    .setAmount(rs.getInt(AMOUNT))
                    .setDescription(rs.getString(DESCRIPTION))
                    .setCategory(category)
                    .setProducer(producer)
                    .setImgPath(rs.getString(IMG_PATH));
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }

    }
}
