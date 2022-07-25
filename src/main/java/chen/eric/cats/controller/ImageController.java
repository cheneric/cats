package chen.eric.cats.controller;


import chen.eric.cats.CatsRuntimeException;
import chen.eric.cats.service.ImageCategory;
import chen.eric.cats.service.ImageResult;
import chen.eric.cats.service.ImageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Controller
public class ImageController {
	private static final Log log = LogFactory.getLog(ImageController.class);
	private final ImageService imageService;

	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}

	@GetMapping("/")
	public String getIndex(Model model) {
		return getImage(model, null);
	}

	@GetMapping({"/images", "/images/{imageId}"})
	public String getImage(Model model, @PathVariable(required = false) String imageId) {
		final String METHOD_NAME_PREFIX = "getImage(): ";
		try {
			CompletableFuture<ImageResult> completableImageResult;
			if (imageId ==null) {
				log.debug(METHOD_NAME_PREFIX + "fetching image by random");
				completableImageResult = imageService.asyncFetchImageByRandom();
			}
			else {
				log.debug(METHOD_NAME_PREFIX + "fetching image by ID (" + imageId + ")");
				completableImageResult = imageService.asyncFetchImageById(imageId);
			}
			final ImageResult imageResult = completableImageResult.get();
			log.debug(METHOD_NAME_PREFIX + "getImage() result = " + imageResult);
			model.addAttribute("image", imageResult);
			return "viewImage";
		}
		catch (Exception exception) {
			throw new CatsRuntimeException((exception));
		}
	}

	@GetMapping("/images/search")
	public String getImageSearch(
		Model model,
		@RequestParam(required = false) Integer[] categoryIds)
	{
		final String METHOD_NAME_PREFIX = "getImageSearch(): ";
		try {
			log.debug(METHOD_NAME_PREFIX + "categoryIds = " + Arrays.toString(categoryIds));
			final CompletableFuture<ImageCategory[]> completableImageCategories =
				imageService.asyncFetchImageCategories();
			final CompletableFuture<ImageResult[]> completableImageResults =
				imageService.asyncSearchImages(
					6,
					ImageService.Sort.ascending,
					0,
					categoryIds
				);
			CompletableFuture.allOf(completableImageCategories, completableImageResults).join();
			// CompletableFuture.get() throws InterruptedException, ExecutionException, CancellationException
			final ImageCategory[] imageCategories = completableImageCategories.get();
			final ImageResult[] imageResults = completableImageResults.get();
			log.debug(METHOD_NAME_PREFIX + "imageCategories = " + Arrays.toString(imageCategories));
			log.debug(METHOD_NAME_PREFIX + "imageResults = " + Arrays.toString(imageResults));
			final Map<String,String> selectedCategoryIds = new HashMap<>();
			if (categoryIds != null) {
				for (final Integer categoryId : categoryIds) {
					selectedCategoryIds.put(categoryId.toString(), categoryId.toString());
				}
			}
			model.addAttribute("categories", imageCategories);
			model.addAttribute("selectedCategoryIds", selectedCategoryIds);
			model.addAttribute("images", imageResults);
			return "searchImages";
		}
		catch (Exception exception) {
			throw new CatsRuntimeException((exception));
		}
	}
}