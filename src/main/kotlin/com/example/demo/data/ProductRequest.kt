import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

class ProductRequest {

    @NotEmpty(message = "Product name isn't provided.")
    var name: String? = null

    @NotNull
    @Min(value = 0, message = "Price should be greater or equal to 0.")
    var price: Int? = null
}