package me.bluecitron.jpashop.service;

import me.bluecitron.jpashop.domain.Address;
import me.bluecitron.jpashop.domain.Member;
import me.bluecitron.jpashop.domain.Order;
import me.bluecitron.jpashop.domain.OrderStatus;
import me.bluecitron.jpashop.domain.item.Book;
import me.bluecitron.jpashop.domain.item.Item;
import me.bluecitron.jpashop.exception.NotEnoughStockException;
import me.bluecitron.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void 상품주문() {
        // given
        Member member = new Member();
        member.setName("회원");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus()
                ,"상품 주문시 상태는 ORDER");
        Assertions.assertEquals(1, getOrder.getOrderItems().size(),
                "주문한 상품 종류 수가 정확해야 한다.");
        Assertions.assertEquals(10000 * orderCount, getOrder.getTotalPrice(),
                "주문 가격은 가격 * 수량이다.");
        Assertions.assertEquals(8, book.getStockQuantity(),
                "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    void 주문취소() {
        // given
        Member member = createMember();
        Book book = (Book)createItem();
        em.persist(member);
        em.persist(book);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.CANCEL, getOrder.getStatus(),
                "주문 취소시 상태는 CANCEL이다.");
        Assertions.assertEquals(10, createItem().getStockQuantity(),
                "주문 취소된 상품은 그만큼 재고가 증가해야 한다.");
    }

    @Test
    void 상품주문_재고수량초과() {
        Assertions.assertThrows(NotEnoughStockException.class, () -> {
            // given
            Member member = createMember();
            em.persist(member);

            Book book = (Book)createItem();
            em.persist(book);

            int orderCount = 12;
            // when
            Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

            // then
            // Order getOrder = orderRepository.findOne(orderId);
        });
    }

    public Member createMember() {
        Member member = new Member();
        member.setName("회원");
        member.setAddress(new Address("서울", "강가", "123-123"));
        return member;
    }

    public Item createItem() {
        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        return book;
    }
}
