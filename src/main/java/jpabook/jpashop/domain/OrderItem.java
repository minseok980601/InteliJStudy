package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count;      // 주문 수량량

    // ==생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // 넘어 온 것 만큼 아이템의 재고를 깍음
        item.removeStock(count);
        return orderItem;
    }

    // ==비즈니스 로직==
    // 재고수량을 롬복해준다.
    public void cancel() {
        getItem().addStock(count);
    }

    // ==조회 로직==
    // 주문 상품 전체 가격 조회
    // 주문할 때 주문 가격과 수량이 있으므로 두개를 곱해야 한다.
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
