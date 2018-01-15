function addBBU(){
	$("#u8RackNO_tr").html(
			'<td>选择单板:</td>'+
			'<td class="blankTd"></td>'+
			'<td>'+
				'<div style="border:1px solid #C6D7E7;overflow:hidden;width:231px;">'+
				'<select  style="width:231px;border:1px solid #fff"  id="u8RackNO">	'+					
					'<option value="1">BBU</option>		'+				
				'</select></div>'+
			'</td>'+
			'<td id="u8RackNOError" class="error"></td>'
	);
}
function addRRU(){
	$("#u8RackNO_tr").html(
			'<td>选择单板:</td>'+
			'<td class="blankTd"></td>'+
			'<td>'+
				'<div style="border:1px solid #C6D7E7;overflow:hidden;width:231px;">'+
				'<select  style="width:231px;border:1px solid #fff"  id="u8RackNO">	'+					
					'<option value="2">RRU1</option>'+
					'<option value="3">RRU2</option>'+
					'<option value="4">RRU3</option>'+
				'</select></div>'+
			'</td>'	+
			'<td id="u8RackNOError" class="error"></td>'	
	);
}
function addU8FiberPort(){
	$("#u8FiberPort_tr").html(
			'<td>光口号:</td>'+
			'<td class="blankTd"></td>'+
			'<td>'+
				'<div style="border:1px solid #C6D7E7;overflow:hidden;width:231px;">'+
				'<select  style="width:231px;border:1px solid #fff"  id="u8FiberPort">	'+					
					'<option value="0">0</option>'+
					'<option value="1">1</option>'+
					'<option value="2">2</option>'+
				'</select></div>'+
			'</td>'+
			'<td id="u8FiberPortError" class="error"></td>'	
	);
}
function subU8FiberPort(){
	$("#u8FiberPort_tr").html("");
}