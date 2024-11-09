package io.teamchallenge.mentality.product;

import io.hypersistence.utils.hibernate.type.money.MonetaryAmountType;
import io.teamchallenge.mentality.customer.CustomerCart;
import io.teamchallenge.mentality.customer.CustomerWishlist;
import io.teamchallenge.mentality.order.OrderItem;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.money.MonetaryAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CompositeType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Transient
  @CompositeType(MonetaryAmountType.class)
  @AttributeOverride(name = "amount", column = @Column(name = "price_amount"))
  @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
  private MonetaryAmount price;

  @Enumerated(EnumType.STRING)
  private ProductStatus status;

  @Builder.Default
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Category> categories = new HashSet<>();

  @Builder.Default
  @ManyToMany(mappedBy = "products")
  private Set<CustomerWishlist> customerWishlists = new HashSet<>();

  @Builder.Default
  @ManyToMany(mappedBy = "products")
  private Set<CustomerCart> customerCarts = new HashSet<>();

  public Product addCategory(Category category) {
    categories.add(category);
    category.setProduct(this);
    return this;
  }

  public Product removeCategory(Category category) {
    categories.remove(category);
    category.setProduct(null);
    return this;
  }
}