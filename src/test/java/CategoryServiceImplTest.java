import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CategoryServiceImplTest {

    @Test
    public void findAll2() {
        CategoryServiceImpl service = new CategoryServiceImpl();
        String json = service.findAll2();
        System.out.println(json);
    }
}