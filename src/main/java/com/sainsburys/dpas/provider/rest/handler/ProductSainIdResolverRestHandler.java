package com.sainsburys.dpas.provider.rest.handler;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.BaseStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sainsburys.dpas.provider.dataaccess.dto.EanToSainID;
import com.sainsburys.dpas.provider.dataaccess.dto.Product;
import com.sainsburys.dpas.provider.dataaccess.dto.SkuToSainID;
import com.sainsburys.dpas.provider.dataaccess.enumeration.ChannelType;
import com.sainsburys.dpas.provider.rest.service.ICachResoloutionService;
import com.sainsburys.dpas.provider.rest.service.IEanResolutionService;
import com.sainsburys.dpas.provider.rest.service.IProductResolutionService;
import com.sainsburys.dpas.provider.rest.service.ISkuResolutionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ProductSainIdResolverRestHandler {

	@Autowired
	private IEanResolutionService eanResolutionService;
	@Autowired
	private ICachResoloutionService cachedResolutionService;
	@Autowired
	private IProductResolutionService productService;
	@Autowired
	private ISkuResolutionService skuResolutionService;
	private static final ResponseEntity<String> NOT_FOUND = ResponseEntity.notFound().build();

	

	@GetMapping(value="/v1/product/ean/{ean}/sainId", produces="application/json")
	public Mono<ResponseEntity<String>> getSainIdForEan(@PathVariable String ean){
		return validateEan(ean)
				.flatMap(validEan -> 
					cachedResolutionService.findByKey(validEan, EanToSainID.class, () -> eanResolutionService.getSainsId(validEan)))
				.filter(sainId -> sainId.getSainId() != null)
				.map(result -> ResponseEntity.ok(result.toJson()))
				.defaultIfEmpty(NOT_FOUND);
	}
	
	@GetMapping(value="/v1/product/sku/{sku}/channel/{channel}/sainId", produces="application/json")
	public Mono<ResponseEntity<String>> getSainsIdForSkuAndChannel(@PathVariable String sku, @PathVariable String channel){
		return cachedResolutionService.findByKey(SkuToSainID.generateCacheKey(sku,ChannelType.valueByIdentifier(channel)), SkuToSainID.class, () -> skuResolutionService.getSainsId(sku, ChannelType.valueByIdentifier(channel)))
				.filter(skuToSainsId -> skuToSainsId.getSainId() != null)
				.map(result -> ResponseEntity.ok(result.toJson()))
				.defaultIfEmpty(NOT_FOUND);
	}
	
	

	private Mono<String> validateEan(String anEan) {
		Mono<String> validEan = Mono.empty();
		if (anEan !=null && !anEan.isBlank() && anEan.matches("^\\d+$")) {
			validEan = Mono.just(anEan);
		}

		return validEan;
	}

	//TAD - this is all gumpf to temporarily load data up
	@GetMapping(value = "/")
	public String hello() {
		processFile();
		return "hello";
	}
	private void processFile() {
		readFromPath(Path.of("/Users/tim.adams2/Downloads/pis-dec2020.json"))
		.flatMap(this::processProd)
		.subscribe();
	}
	private Flux<String> processProd(String prod) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode json = mapper.readTree(prod);
			String sainId = json.get("sainWrapper").get("sain").asText();
			JsonNode identifiers = json.get("sainWrapper").get("identifiers");
			identifiers.forEach(val -> {
				switch (val.get("alias").asText()) {
					case "gtin":
					case "legacy-js-ownbrand-barcode":				
//There are potentially multiple gtin's for a sku... each needs to be recorded					
						JsonNode eans = val.get("value");
						eans.forEach(ean -> {
							EanToSainID eanToSainsID = new EanToSainID(ean.asText(),sainId);
							eanResolutionService.save(eanToSainsID);
						});
						break;
					case "sainsburys-sku":
					case "argos-sku":
						JsonNode skus = val.get("value");
						skus.forEach(sku -> {
							SkuToSainID skuToSainsId = new SkuToSainID(sku.asText(),ChannelType.valueByName(val.get("alias").asText()),sainId);

							skuResolutionService.save(skuToSainsId);
						});
						break;
				
				}
			});
			//now save the product against the sainId
			Product prodToSave = new Product(sainId, prod);
			productService.save(prodToSave);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Flux.just(prod);
	}
	 
	private Flux<String> readFromPath(Path path) {
		return Flux.using(() -> Files.lines(path),
				Flux::fromStream,
				BaseStream::close);
	}

	//Need to go to the db to get our values...
}
