$(function() {
	var fieldLevelString = $("#fieldLevelString").val();
	fieldLevelString = getVersionMatchFields(fieldLevelString);
	fieldLevelString.push("justForView");
	$(".McWillTable tr").each(function() {
		var fieldName = $(this).attr("class");
		if (!showField(fieldName, fieldLevelString)) {
			$(this).css("display", "none");
		}
	});
});
