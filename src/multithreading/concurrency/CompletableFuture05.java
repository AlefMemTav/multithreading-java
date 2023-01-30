package multithreading.concurrency;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import multithreading.concurrency.dominio.Quote;
import multithreading.concurrency.service.StoreServiceWithDiscount;

public class CompletableFuture05 {

    public static void main(String[] args) {
        StoreServiceWithDiscount service = new StoreServiceWithDiscount();
        searchPricesWithDiscountAsync(service);
    }

    private static void searchPricesWithDiscountAsync(StoreServiceWithDiscount service) {
        long start = System.currentTimeMillis();
        List<String> stores = List.of("Store 1", "Store 2", "Store 3", "Store 4");

        List<CompletableFuture<String>> completableFutures = stores.stream()
                // Getting the price async storeName:price:discountCode
                .map(s -> CompletableFuture.supplyAsync(() -> service.getPriceSync(s)))
                // Instantiating a new Quote from the string generated by getPriceSync
                .map(cf -> cf.thenApply(Quote::newQuote))
                // Composing the first completable future
                .map(cf -> cf.thenCompose(quote -> CompletableFuture.supplyAsync(() -> service.applyDiscount(quote))))
                .collect(Collectors.toList());

        completableFutures.stream()
                .map(CompletableFuture::join)
                .forEach(System.out::println);

        long end = System.currentTimeMillis();
        System.out.printf("Time passed to searchPricesSync %dms%n", (end - start));
    }
}