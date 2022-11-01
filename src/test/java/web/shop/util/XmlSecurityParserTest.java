package web.shop.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import web.shop.entity.Constraint;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class XmlSecurityParserTest {

    @Test
    void shouldParseXmlAndReturnConstraintEntity() {
       List<Constraint> actualConstraints =  XmlSecurityParser.getXmlConstraintsAsList();
       List<Constraint> expectedConstraints = List.of(
               new Constraint("/admin/hello", List.of("ADMIN")),
               new Constraint("/user/.*", List.of("USER")),
               new Constraint("/all_roles/.*", List.of("ADMIN", "USER"))
       );

       assertEquals(expectedConstraints, actualConstraints);
    }
}