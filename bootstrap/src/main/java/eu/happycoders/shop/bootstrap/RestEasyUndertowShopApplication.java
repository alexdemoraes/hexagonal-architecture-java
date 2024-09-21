package eu.happycoders.shop.bootstrap;

import eu.happycoders.shop.adapter.in.rest.cart.AddToCartController;
import eu.happycoders.shop.adapter.in.rest.cart.EmptyCartController;
import eu.happycoders.shop.adapter.in.rest.cart.GetCartController;
import eu.happycoders.shop.adapter.in.rest.product.FindProductsController;
import eu.happycoders.shop.adapter.out.persistence.inmemory.InMemoryCartRepository;
import eu.happycoders.shop.adapter.out.persistence.inmemory.InMemoryProductRepository;
import eu.happycoders.shop.adapter.out.persistence.jpa.EntityManagerFactoryFactory;
import eu.happycoders.shop.adapter.out.persistence.jpa.JpaCartRepository;
import eu.happycoders.shop.adapter.out.persistence.jpa.JpaProductRepository;
import eu.happycoders.shop.application.port.out.persistence.CartRepository;
import eu.happycoders.shop.application.port.out.persistence.ProductRepository;
import eu.happycoders.shop.application.service.cart.AddToCartService;
import eu.happycoders.shop.application.service.cart.EmptyCartService;
import eu.happycoders.shop.application.service.cart.GetCartService;
import eu.happycoders.shop.application.service.product.FindProductsService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.ws.rs.core.Application;
import java.util.Set;

public class RestEasyUndertowShopApplication extends Application {

  private CartRepository cartRepository;
  private ProductRepository productRepository;

  @Override
  public Set<Object> getSingletons() {
    initPersistenceAdapters();

    return Set.of(
        addToCartController(),
        getCartController(),
        emptyCartController(),
        findProductsController());
  }

//  Indexer

  private void initPersistenceAdapters() {
    String persistence = System.getProperty("persistence", "inmemory");
    switch (persistence) {
      case "inmemory" -> initInMemoryAdapters();
      case "mysql" -> initMySqlAdapters();
      default -> throw new IllegalArgumentException(
          "Invalid 'persistence' property: '%s' (allowed: 'inmemory', 'mysql')"
              .formatted(persistence));
    }
  }

  private void initInMemoryAdapters() {
    cartRepository = new InMemoryCartRepository();
    productRepository = new InMemoryProductRepository();
  }

  private void initMySqlAdapters() {
    EntityManagerFactory entityManagerFactory =
        EntityManagerFactoryFactory.createMySqlEntityManagerFactory(
            "jdbc:mysql://localhost:3306/shop", "root", "test");

    cartRepository = new JpaCartRepository(entityManagerFactory);
    productRepository = new JpaProductRepository(entityManagerFactory);
  }

  private AddToCartController addToCartController() {
    return new AddToCartController(new AddToCartService(cartRepository, productRepository));
  }

  private GetCartController getCartController() {
    return new GetCartController(new GetCartService(cartRepository));
  }

  private EmptyCartController emptyCartController() {
    return new EmptyCartController(new EmptyCartService(cartRepository));
  }

  private FindProductsController findProductsController() {
    return new FindProductsController(new FindProductsService(productRepository));
  }
}
