package chen.eric.cats.service;

import org.springframework.util.ObjectUtils;

public class ImageResult {
	private String id;
	private String url;
	private int width;
	private int height;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public boolean equals(Object object) {
		return (object instanceof ImageResult
			&& equals((ImageResult)object));
	}

	public boolean equals(ImageResult imageResult) {
		return ObjectUtils.nullSafeEquals(this.url, imageResult.url)
			&& ObjectUtils.nullSafeEquals(this.height, imageResult.height)
			&& ObjectUtils.nullSafeEquals(this.width, imageResult.width);
	}

	@Override
	public int hashCode() {
		return ObjectUtils.nullSafeHashCode(url)
			^ height
			^ width;
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("{url: \"")
			.append(url)
			.append("\", height: ")
			.append(height)
			.append(", width: ")
			.append(width)
			.append("}")
			.toString();
	}
}
