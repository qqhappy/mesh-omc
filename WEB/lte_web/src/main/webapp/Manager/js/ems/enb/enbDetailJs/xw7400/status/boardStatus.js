$(function() {
	$(".boardStatusTable tr").each(function() {
		var rowClass = $(this).attr("class");
		if (rowClass == "value") {
			$(".statusValue td").each(function() {
				var columnClass = $(this).attr("class");
				if(columnClass == "boardFlag") {
					var rackNo = $("#rackNo").val();
					var shelfNo = $("#shelfNo").val();
					var slotNo = $("#slotNo").val();
					$(this).val(rackNo+"-"+shelfNo+"-"+slotNo);
				}
			});
		}
	});
});
