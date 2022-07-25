package chen.eric.cats.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ImageResult} tests.
 */
@JsonTest
public class ImageResultTest {
	@Autowired
	private JacksonTester<ImageResult[]> jacksonTester;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Tests JSON deserialization.
	 *
	 * @throws IOException if an error occurs reading or parsing the object.
	 */
	@Test
	public void testDeserialization() throws IOException {
		final Resource jsonFile = getClassPathResource("ImageResultTest-testDeserialization-response.json");
		final ImageResult[] imageResults = jacksonTester.readObject(jsonFile);
		assertEquals(1, imageResults.length, "Unexpected number of image results");
		final ImageResult imageResult = imageResults[0];
		assertEquals("TM_kbeZSi", imageResult.getId(), "Unexpected id");
		assertEquals("https://cdn2.thecatapi.com/images/TM_kbeZSi.jpg", imageResult.getUrl(), "Unexpected Url");
		assertEquals(1168, imageResult.getWidth(), "Unexpected width");
		assertEquals(657, imageResult.getHeight(), "Unexpected height");
	}

	@Test
	public void testToString() {
		final String URL = "https://cdn.cats.com/12345.jpg";
		final int HEIGHT = 600;
		final int WIDTH = 800;
		final ImageResult imageResult = new ImageResult();
		imageResult.setUrl(URL);
		imageResult.setHeight(HEIGHT);
		imageResult.setWidth(WIDTH);
		assertEquals(
			"{url: \"" + URL + "\""
				+ ", height: " + HEIGHT
				+ ", width: " + WIDTH
				+ "}",
			imageResult.toString(),
			"Unexpected string representation");
	}

	protected ClassPathResource getClassPathResource(String fileName) {
		return new ClassPathResource(
			ImageResultTest.class.getPackageName()
				.replace('.', File.separatorChar)
				+ File.separatorChar
				+ "ImageResultTest-testDeserialization-response.json");
	}

}
