package chen.eric.cats.service;

import org.springframework.util.ObjectUtils;

public class ImageCategory {
	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object object) {
		return (object instanceof ImageResult
			&& equals((ImageResult)object));
	}

	public boolean equals(ImageCategory imageCategory) {
		return ObjectUtils.nullSafeEquals(this.id, imageCategory.id)
			&& ObjectUtils.nullSafeEquals(this.name, imageCategory.name);
	}

	@Override
	public int hashCode() {
		return id
			^ ObjectUtils.nullSafeHashCode(name);
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("{id: \"")
			.append(id)
			.append("\", name: ")
			.append(name)
			.append("}")
			.toString();
	}
}
