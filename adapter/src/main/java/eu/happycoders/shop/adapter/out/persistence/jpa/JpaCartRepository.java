package eu.happycoders.shop.adapter.out.persistence.jpa;

import eu.happycoders.shop.application.port.out.persistence.CartRepository;
import eu.happycoders.shop.model.cart.Cart;
import eu.happycoders.shop.model.customer.CustomerId;
import io.quarkus.arc.lookup.LookupIfProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.Optional;

@LookupIfProperty(name = "persistence", stringValue = "mysql")
@ApplicationScoped
public class JpaCartRepository implements CartRepository {

  private final EntityManagerFactory entityManagerFactory;

  public JpaCartRepository(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }


  @Override
  public void save(Cart cart) {
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      entityManager.getTransaction().begin();
      entityManager.merge(CartMapper.toJpaEntity(cart));
      entityManager.getTransaction().commit();
    }
  }

  @Override
  public Optional<Cart> findByCustomerId(CustomerId customerId) {
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      var jpaEntity = entityManager.find(CartJpaEntity.class, customerId.value());
      return CartMapper.toModelEntityOptional(jpaEntity);
    }
  }

  @Override
  public void deleteByCustomerId(CustomerId customerId) {
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      entityManager.getTransaction().begin();

      var jpaEntity = entityManager.find(CartJpaEntity.class, customerId.value());

      if (jpaEntity != null) {
        entityManager.remove(jpaEntity);
      }

      entityManager.getTransaction().commit();
    }
  }
}
