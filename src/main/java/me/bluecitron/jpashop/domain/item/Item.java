package me.bluecitron.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;
import me.bluecitron.jpashop.domain.Category;
import me.bluecitron.jpashop.exception.NotEnoughStockException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void subtractStock(int quantity) {
        if (this.stockQuantity - quantity >= 0) {
            this.stockQuantity -= quantity;
        } else {
            throw new NotEnoughStockException("Need more stock");
        }
    }
}
