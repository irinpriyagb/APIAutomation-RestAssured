package practice;

import Models.Product;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiTests {
    @Test
    public void getCategories()
    {
        String endpoint = "http://localhost:8888/api_testing/category/read.php";
        var response =
                given().
                        when().get(endpoint).
                                then();
        response.log().body();
    }

    @Test
    public void getProducts()
    {
        String endpoint = "http://localhost:8888/api_testing/product/read.php";
         given().
         when().
            get(endpoint).
         then().
                 log().headers().
            assertThat().
            statusCode(200).
                 header("Content-Type",equalTo("application/json; charset=UTF-8")).
            body("records.size()", greaterThan(0)).
            body("records.id[0]", equalTo("23")).
            body("records.id",everyItem(notNullValue())).
            body("records.name", everyItem(notNullValue())).
            body("records.description",everyItem(notNullValue())).
            body("records.price",everyItem(notNullValue())).
            body("records.category_id",everyItem(notNullValue()));
            //body("category_name",everyItem(notNullValue()));

    }

    @Test
    public void createProduct()
    {
        String endpoint ="http://localhost:8888/api_testing/product/create.php";
        String body =
                """
                {
                "name": "Sweatband",
                "description": "Sweatband",
                "price": 5,
                "category_id": 3
                }
                """;
        var response =
                given().
                        body(body).
                when().
                        post(endpoint).
                then();
        response.log().body();
    }

    @Test
    public void updateProduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/update.php";
        String body =
                """
                {
                "id": 23,
                "price": 6
                }
                """;
        var response =
                given().
                        body(body).
                when().
                        put(endpoint).
                then();
        response.log().body();
    }

    @Test
    public void deleteProduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/delete.php";
        String body =
                """
                {
                "id": 23
                }
                """;
        var response =
                given().
                        body(body).
                when().
                        delete(endpoint).
                then();
        response.log().body();
    }

    @Test
    public void createSerializedProduct()
    {
        String endpoint ="http://localhost:8888/api_testing/product/create.php";
        Product product = new Product(
                "Resistance band",
                "Weighs 15 kg ; for Beginner",
                55,
                3
        );
        var response =
                given().
                        body(product).
                when().
                        post(endpoint).
                then();
        response.log().body();
    }

    @Test
    public void getDeserializedProduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        Product expectedProduct = new Product(12, "V-Neck Pullover", "This organic hemp jersey pullover is perfect in a pinch. Wear for casual days at the office, a game of hoops after work, or running your weekend errands.", 65.00, 1, "Active Wear - Men");
        Product actualProduct =
            given().
                queryParam("id","12").
            when().
                get(endpoint).
                    as(Product.class);
        assertThat(actualProduct, samePropertyValuesAs(expectedProduct));
    }

    @Test
    public void getMutivitaminProduct()
    {
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
            given().
                    queryParam("id","18").
            when().
                    get(endpoint).
            then().
                    log().body().
                    assertThat().
                        statusCode(200).
                        header("Content-Type", "application/json").
                        body("id", equalTo("18")).
                        body("name", equalTo("Multi-Vitamin (90 capsules)")).
                        body("description", equalTo("A daily dose of our Multi-Vitamins fulfills a day’s nutritional needs for over 12 vitamins and minerals.")).
                        body("price", equalTo("10.00")).
                        body("category_id", equalTo("4")).
                        body("category_name", equalTo("Supplements"));

    }
}
