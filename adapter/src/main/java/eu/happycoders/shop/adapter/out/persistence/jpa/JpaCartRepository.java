package eu.happycoders.shop.adapter.out.persistence.jpa;

import eu.happycoders.shop.application.port.out.persistence.CartRepository;
import eu.happycoders.shop.model.cart.Cart;
import eu.happycoders.shop.model.customer.CustomerId;
import io.quarkus.arc.lookup.LookupIfProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Optional;

@LookupIfProperty(name = "persistence", stringValue = "mysql")
@ApplicationScoped
public class JpaCartRepository implements CartRepository {

  private final JpaCartPanacheRepository panacheRepository;

  public JpaCartRepository(JpaCartPanacheRepository panacheRepository) {
    this.panacheRepository = panacheRepository;
  }

  @Override
  @Transactional
  public void save(Cart cart) {
    panacheRepository.getEntityManager().merge(CartMapper.toJpaEntity(cart));
  }

  @Override
  @Transactional
  public Optional<Cart> findByCustomerId(CustomerId customerId) {
    var jpaEntity = panacheRepository.findById(customerId.value());
    return CartMapper.toModelEntityOptional(jpaEntity);
  }

  @Override
  @Transactional
  public void deleteByCustomerId(CustomerId customerId) {
    panacheRepository.deleteById(customerId.value());
  }
}
