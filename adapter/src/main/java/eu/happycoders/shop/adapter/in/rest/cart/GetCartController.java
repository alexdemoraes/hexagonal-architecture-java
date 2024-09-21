package eu.happycoders.shop.adapter.in.rest.cart;

import static eu.happycoders.shop.adapter.in.rest.common.CustomerIdParser.parseCustomerId;

import eu.happycoders.shop.application.port.in.cart.GetCartUseCase;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/carts")
@Produces(MediaType.APPLICATION_JSON)
public class GetCartController {

  private final GetCartUseCase getCartUseCase;

  public GetCartController(GetCartUseCase getCartUseCase) {
    this.getCartUseCase = getCartUseCase;
  }

  @GET
  @Path("/{customerId}")
  public CartWebModel getCart(@PathParam("customerId") String customerIdString) {
    var customerId = parseCustomerId(customerIdString);
    var cart = getCartUseCase.getCart(customerId);
    return CartWebModel.fromDomainModel(cart);
  }
}
