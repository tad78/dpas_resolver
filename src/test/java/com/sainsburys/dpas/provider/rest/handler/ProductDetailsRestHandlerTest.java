package com.sainsburys.dpas.provider.rest.handler;


import static org.mockito.ArgumentMatchers.eq;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.sainsburys.dpas.provider.dataaccess.dto.EanToSainID;
import com.sainsburys.dpas.provider.dataaccess.dto.Product;
import com.sainsburys.dpas.provider.dataaccess.dto.SkuToSainID;
import com.sainsburys.dpas.provider.dataaccess.dynamo.IBarcodeResolutionRepository;
import com.sainsburys.dpas.provider.dataaccess.enumeration.ChannelType;
import com.sainsburys.dpas.provider.rest.service.ICachResoloutionService;
import com.sainsburys.dpas.provider.rest.service.IEanResolutionService;
import com.sainsburys.dpas.provider.rest.service.IProductResolutionService;
import com.sainsburys.dpas.provider.rest.service.ISkuResolutionService;

import reactor.core.publisher.Mono;

@WebFluxTest(ProductSainIdResolverRestHandler.class)
public class ProductDetailsRestHandlerTest {

	@MockBean
	private IEanResolutionService eanResolutionService;
	@MockBean
	private ICachResoloutionService cachedResolutionService;
	@MockBean
	private IProductResolutionService productService;
	@MockBean
	private ISkuResolutionService skuResolutionService;
	@MockBean
	private IBarcodeResolutionRepository barcodeResolutionRepository;
	@Autowired
    private WebTestClient webClient;
	
	
	@Test
	public void givenAKnownEANExpect200AndCorrectProduct() {
		
		EanToSainID eanToSainsId = new EanToSainID();
		eanToSainsId.setEan("1234");
		eanToSainsId.setSainId("5678");
		Mockito.when(eanResolutionService.getSainsId("1234"))
		.thenReturn(Mono.just(eanToSainsId));
		Product prod = new Product();
		prod.setSainId("5678");
		prod.setProductDescription("{\"sainId\":\"5678\"}");
		
		Mockito.when(
				cachedResolutionService.findByKey(
						eq("1234"), 
						eq(EanToSainID.class), 
						ArgumentMatchers.any()))
		.thenReturn(Mono.just(eanToSainsId));
		Mockito.when(
				cachedResolutionService.findByKey(
						eq("5678"), 
						eq(Product.class), 
						ArgumentMatchers.any()))
		.thenReturn(Mono.just(prod));
			
		webClient.get()
			.uri("/getByEan/1234")
			.header(HttpHeaders.ACCEPT, "application/json")
            .exchange()
            .expectStatus().isOk()
            .expectBody().json("{\"sainId\":\"5678\"}");
			
	}

	@Test
	public void givenAnUnknownEANExpect404() {
		EanToSainID eanToSainsId = new EanToSainID();
		eanToSainsId.setEan(null);
		eanToSainsId.setSainId(null);
		Mockito.when(barcodeResolutionRepository.findById("1234"))
		.thenReturn(new CompletableFuture<EanToSainID>().completeAsync(() -> null));
		Product prod = new Product();
		prod.setSainId("5678");
		prod.setProductDescription("{\"sainId\":\"5678\"}");
		
		Mockito.when(
				cachedResolutionService.findByKey(
						eq("1234"), 
						eq(EanToSainID.class), 
						ArgumentMatchers.any()))
		.thenReturn(Mono.just(eanToSainsId));

		webClient.get()
		.uri("/getByEan/1234")
		.header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus().isNotFound();
		
	}
	
	@Test
	public void givenAnInvalidEanExpectA400Response() {
		webClient.get()
		.uri("/getByEan/x1234")
		.header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus().is4xxClientError();
	}
	
	@Test
	public void givenAKnownSainsIdExpect200AndCorrectProduct() {

		Product prod = new Product();
		prod.setSainId("5678");
		prod.setProductDescription("{\"sainId\":\"5678\"}");

		Mockito.when(
				cachedResolutionService.findByKey(
						eq("5678"), 
						eq(Product.class), 
						ArgumentMatchers.any()))
		.thenReturn(Mono.just(prod));
		webClient.get()
		.uri("/getBySainId/5678")
		.header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus().isOk()
        .expectBody().json("{\"sainId\":\"5678\"}");
		
	}

	@Test
	public void givenAKnownSkuExpect200AndCorrectProduct() {

		
		SkuToSainID skuToSainId = new SkuToSainID();
		skuToSainId.setSku("1234");
		skuToSainId.setChannel(ChannelType.SAINSBURYS.getChannelDescriptor());
		skuToSainId.setSainId("5678");
		
		Product prod = new Product();
		prod.setSainId("5678");
		prod.setProductDescription("{\"sainId\":\"5678\"}");

		Mockito.when(
				cachedResolutionService.findByKey(
						eq("1234:S"), 
						eq(SkuToSainID.class), 
						ArgumentMatchers.any()))
		.thenReturn(Mono.just(skuToSainId));

		Mockito.when(
				cachedResolutionService.findByKey(
						eq("5678"), 
						eq(Product.class), 
						ArgumentMatchers.any()))
		.thenReturn(Mono.just(prod));
		webClient.get()
		.uri("/productBySku/1234/byChannel/S")
		.header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus().isOk()
        .expectBody().json("{\"sainId\":\"5678\"}");
		
	}


}
