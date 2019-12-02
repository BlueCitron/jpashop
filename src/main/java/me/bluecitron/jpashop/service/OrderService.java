package me.bluecitron.jpashop.service;

import lombok.RequiredArgsConstructor;
import me.bluecitron.jpashop.domain.Delivery;
import me.bluecitron.jpashop.domain.Member;
import me.bluecitron.jpashop.domain.Order;
import me.bluecitron.jpashop.domain.OrderItem;
import me.bluecitron.jpashop.domain.item.Item;
import me.bluecitron.jpashop.repository.ItemRepository;
import me.bluecitron.jpashop.repository.MemberRepository;
import me.bluecitron.jpashop.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }
    // 검색
}
