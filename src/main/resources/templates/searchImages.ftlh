<!DOCTYPE HTML>
<html>
	<head>
		 <link rel="stylesheet" href="/style.css"/>
	</head>
	<body id="searchImages">
		<div class="header">
			<h1>Search for cats</h1>
			<div>
				or <a href="/images">view a random cat</a>!
			</div>
		</div>
		<div class="searchBar">
			<form method="get">
				In:
				<select id="categoryIds" name="categoryIds">
					<option value="">anything</option>
					<#list categories as category>
						<option value="${category.id}"
							<#if selectedCategoryIds[category.id?string.computer]??>selected="true"</#if>
							/>
							${category.name}
						</option>
					</#list>
				</select>
				<input type="submit" value="Search!"/>
			</form>
		</div>
		<div class="imageBox">
			<#list images as image>
				<div class="imageCell">
					<a href="/images/${image.id}"><img class="boundedCover" src="${image.url}"/></a>
				</div>
			</#list>
		</div>
	</body>
</html>