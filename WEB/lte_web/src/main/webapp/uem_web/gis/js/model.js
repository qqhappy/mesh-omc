function Map() {
	var struct = function(key, value) {
		this.key = key;
		this.value = value;
	}

	var put = function(key, value) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				this.arr[i].value = value;
				return;
			}
		}
		this.arr[this.arr.length] = new struct(key, value);
	}
	var put1 = function(key, value) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				return;
			}
		}
		this.arr[this.arr.length] = new struct(key, value);
	}

	var get = function(key) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				return this.arr[i].value;
			}
		}
		return null;
	}

	var getIndex = function(i) {
		if (i < this.arr.length) {
			return this.arr[i];
		}
		return null;
	}

	var getIndexValue = function(i) {
		if (i < this.arr.length) {
			return this.arr[i].value;
		}
		return null;
	}

	var getIndexKey = function(i) {
		if (i < this.arr.length) {
			return this.arr[i].key;
		}
		return null;
	}

	var keySet = function() {
		var keys = new Array();
		for (var i = 0; i < this.arr.length; i++) {
			keys[i] = this.arr[i].key;
		}
		return keys;
	}

	var values = function() {
		var valueList = new Array();
		for (var i = 0; i < this.arr.length; i++) {
			valueList[i] = this.arr[i].value;
		}
		return valueList;
	}
	var entrySet = function() {
		var entrys = new Array();
		for (var i = 0; i < this.arr.length; i++) {
			entrys[i] = this.arr[i];
		}
		return entrys;
	}
	var remove = function(key) {
		var v;
		for (var i = 0; i < this.arr.length; i++) {
			v = this.arr.pop();
			if (v.key === key) {
				continue;
			}
			this.arr.unshift(v);
		}
	}

	var size = function() {
		return this.arr.length;
	}

	var isEmpty = function() {
		return this.arr.length <= 0;
	}

	var ascCompare = function() {
		this.arr.sort(compareX("key", true));
	}

	var descCompare = function() {
		this.arr.sort(compareX("key", false));
	}

	this.arr = new Array();
	this.get = get;
	this.put = put;
	this.remove = remove;
	this.size = size;
	this.isEmpty = isEmpty;
	this.values = values;
	this.keySet = keySet;
	this.entrySet = entrySet;
	this.getIndex = getIndex;
	this.getIndexValue = getIndexValue;
	this.getIndexKey = getIndexKey;
	this.ascCompare = ascCompare;
	this.descCompare = descCompare;
}

// -------------------------------------MyOverlay---------------------------------------
function MyOverlay() {
}
MyOverlay.prototype = new google.maps.OverlayView();
MyOverlay.prototype.constructor = MyOverlay;
MyOverlay.prototype.onAdd = function() {
}
MyOverlay.prototype.draw = function() {
}
MyOverlay.prototype.onRemove = function() {
}

// -------------------------------------XinWeilOverlayMapType---------------------------------------
function XinWeilOverlayMapType(imageFileDir, imageFileExtend) {
	this.imageFileDir = imageFileDir;
	this.imageFileExtend = imageFileExtend;
}
XinWeilOverlayMapType.prototype.tileSize = new google.maps.Size(256, 256);
XinWeilOverlayMapType.prototype.getTile = function(coord, zoom, ownerDocument) {

	// 卫星地图地名存放路径
	var strURL = this.imageFileDir + "/" + zoom + "/" + coord.x + "/" + coord.y + this.imageFileExtend;

	var img = ownerDocument.createElement("img");

	img.src = strURL;
	var errorImage = this.errorImageUrl;
	img.onerror = function() {
		// this.src = "images\\transparent.png"
		this.parentNode.removeChild(this);
	};

	var div = ownerDocument.createElement('DIV');
	div.style.width = this.tileSize.width + "px";
	div.style.height = this.tileSize.height + "px";
	div.appendChild(img);
	return div;
};
// -------------------------------------XinWeilMapType---------------------------------------
function XinWeilMapType(imageFileDir, imageFileExtend, errorImageUrl) {
	this.imageFileDir = imageFileDir;
	this.imageFileExtend = imageFileExtend;
	this.errorImageUrl = errorImageUrl;
}
XinWeilMapType.prototype.tileSize = new google.maps.Size(256, 256);
XinWeilMapType.prototype.setMaxZoom = function(maxZoom) {
	this.maxZoom = maxZoom;
}
XinWeilMapType.prototype.setMinZoom = function(minZoom) {
	this.minZoom = minZoom;
}
XinWeilMapType.prototype.setName = function(name) {
	this.name = name;
}
XinWeilMapType.prototype.setAlt = function(alt) {
	this.alt = alt;
}
XinWeilMapType.prototype.getTile = function(coord, zoom, ownerDocument) {
	var img = ownerDocument.createElement("img");
	img.style.width = this.tileSize.width + "px";
	img.style.height = this.tileSize.height + "px";

	var strURL = this.imageFileDir + "/" + zoom + "/" + coord.x + "/" + coord.y + this.imageFileExtend;

	img.src = strURL;
	var errorImage = this.errorImageUrl;
	img.onerror = function() {
		this.src = errorImage
	};
	return img;
}
// --------------------------util-------------------------------------
function compareX(propertyName, asc) {
	return function(object1, object2) {
		var value1 = object1[propertyName];
		var value2 = object2[propertyName];

		var v = 0;
		if (value2 < value1) {
			v = 1;
		} else if (value2 > value1) {
			v = -1;
		}
		if (!asc) {
			v = 0 - v;
		}
		return v;
	}
}

// 两点距离
function distance(latA, logA, latB, logB) {
	var earthR = 6371000;
	var x = Math.cos(latA * Math.PI / 180) * Math.cos(latB * Math.PI / 180) * Math.cos((logA - logB) * Math.PI / 180);
	var y = Math.sin(latA * Math.PI / 180) * Math.sin(latB * Math.PI / 180);
	var s = x + y;
	if (s > 1)
		s = 1;
	if (s < -1)
		s = -1;
	var alpha = Math.acos(s);
	var v = alpha * earthR;

	return v;
}

function getDate(s) {// yyyyMMddHHmmss
	var d = new Date();
	d.setFullYear(s.substr(0, 4));
	d.setMonth(s.substr(4, 2));
	d.setDate(s.substr(6, 2));
	d.setHours(s.substr(8, 2));
	d.setMinutes(s.substr(10, 2));
	d.setSeconds(s.substr(12, 2));
	d.setMilliseconds(0);
	return d;
}

// -------------------------------------String---------------------------------------
function getStringLength(s,e){
   e.innerHTML=s  
  
   return e.offsetWidth;
}