import cn.itcast.travel.domain.Order;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderTest {
    @Test
    public void odertest() {
        Order order = new Order();
        order.setType("oder");
        order.setA_d("asc");
        System.out.println(order.getA_d());

        List<Integer> routeList = new ArrayList<>();
        routeList.add(1);
        routeList.add(2);
        routeList.add(3);
        routeList.add(5);
        routeList.add(6);
        routeList.add(7);
        List<Integer> list = routeList.subList(0,6);
        System.out.println(list);

    }
}