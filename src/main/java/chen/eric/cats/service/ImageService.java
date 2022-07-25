package chen.eric.cats.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class ImageService {
	public enum Sort {
		ascending("ASC"),
		descending("DESC"),
		random("RANDOM");

		private Sort(String parameterValue) {
			this.parameterValue = parameterValue;
		}

		private final String parameterValue;

		public String getParameterValue() {
			return parameterValue;
		}
	}

	protected static final String CATEGORY_IDS_PARAMETER_NAME = "category_ids";
	protected static final String LIMIT_PARAMETER_NAME = "limit";
	protected static final String MIME_TYPES_PARAMETER_NAME = "mime_types";
	protected static final String PAGE_NUMBER_PARAMETER_NAME = "page";
	protected static final String SIZE_PARAMETER_NAME = "size";
	protected static final String SIZE_FULL = "full";
	protected static final String SORT_PARAMETER_NAME ="order";

	private final Log log;
	private final int requestTimeoutMillis;

	private final WebClient webClient;

	public ImageService(WebClient.Builder webClientBuilder,
						Log log,
						@Value("${cats.imageService.baseUrl}") String baseUrl,
						@Value("${cats.imageService.requestTimeoutMillis}") int requestTimeoutMillis)
	{
		this.webClient =
			webClientBuilder.baseUrl(baseUrl)
				.build();
		this.log = log;
		this.requestTimeoutMillis = requestTimeoutMillis;
	}

	/**
	 * Asynchronously fetches an {@link ImageResult} by ID.
	 *
	 * @param imageId the image ID to fetch.
	 * @return the image result.
	 */
	public CompletableFuture<ImageResult> asyncFetchImageById(final String imageId) {
		return CompletableFuture.completedFuture(
			fetchImageById(imageId));
	}


	/**
	 * Synchronously fetches an {@link ImageResult} by ID.
	 *
	 * @param imageId the image ID fetch.
	 * @return the image result.
	 */
	public ImageResult fetchImageById(final String imageId) {
		return this.webClient.get()
			.uri("/images/{imageId}?size={size}", imageId, SIZE_FULL)
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToMono(ImageResult.class)
			.block(Duration.ofMillis(requestTimeoutMillis));
	}

	/**
	 * Asynchronously fetches a random image.
	 *
	 * @return a {@link CompletableFuture} of a {@link ImageResult}.
	 */
	@Async
	public CompletableFuture<ImageResult> asyncFetchImageByRandom() {
		return CompletableFuture.completedFuture(
			fetchImageByRandom());
	}

	/**
	 * Synchronously fetches a random image.
	 *
	 * @return a cat {@link ImageResult}.
	 */
	public ImageResult fetchImageByRandom() {
		return searchImages(null, null, null, null)[0];
	}

	/**
	 * Asynchronously fetches all image categories.
	 *
	 * @return all image categories.
	 */
	@Async
	public CompletableFuture<ImageCategory[]> asyncFetchImageCategories() {
		return CompletableFuture.completedFuture(
			fetchImageCategories());
	}

	/**
	 * Synchronously fetches all image categories.
	 *
	 * @return all image categories.
	 */
	public ImageCategory[] fetchImageCategories() {
		return this.webClient.get()
			.uri("/categories")
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToMono(ImageCategory[].class)
			.block(Duration.ofMillis(requestTimeoutMillis));
	}

	/**
	 * Asynchronously search for images.
	 *
	 * @param limit
	 * @param sort
	 * @param pageNumber
	 * @param categoryIds
	 * @return a {@link CompletableFuture} of an array of matching images.
	 */
	@Async
	public CompletableFuture<ImageResult[]> asyncSearchImages(
		final Integer limit,
		final Sort sort,
		final Integer pageNumber,
		final Integer[] categoryIds)
	{
		return CompletableFuture.completedFuture(
			searchImages(limit, sort, pageNumber, categoryIds));
	}

	/**
	 * Synchronously search for images.
	 *
	 * @param limit
	 * @param sort
	 * @param pageNumber
	 * @param categoryIds
	 * @return an array of matching images.
	 */
	public ImageResult[] searchImages(
		final Integer limit,
		final Sort sort,
		final Integer pageNumber,
		final Integer[] categoryIds)
	{
		final String METHOD_NAME_PREFIX = "searchImages(): ";
		return this.webClient.get()
			.uri(uriBuilder -> {
					uriBuilder.path("/images/search");
					uriBuilder.queryParam(SIZE_PARAMETER_NAME, SIZE_FULL);
					if (categoryIds != null && categoryIds.length > 0) {
						final String categoryIdsString = StringUtils.arrayToCommaDelimitedString(categoryIds);
						log.debug("categoryIds = " + categoryIdsString);
						uriBuilder.queryParam(
							CATEGORY_IDS_PARAMETER_NAME,
							categoryIdsString);
					}
					if (limit != null) {
						uriBuilder.queryParam(LIMIT_PARAMETER_NAME, limit);
					}
					if (pageNumber != null) {
						uriBuilder.queryParam(PAGE_NUMBER_PARAMETER_NAME, pageNumber);
					}
					if (sort != null) {
						uriBuilder.queryParam(SORT_PARAMETER_NAME, sort.getParameterValue());
					}
					return uriBuilder.build(limit, sort, pageNumber, categoryIds);
				})
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToMono(ImageResult[].class)
			.block(Duration.ofMillis(requestTimeoutMillis));
	}
}
