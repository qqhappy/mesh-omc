var GPS = {
    PI : 3.14159265358979324,
 
    delta : function (lat, lon) {
        // Krasovsky 1940
        //
        // a = 6378245.0, 1/f = 298.3
        // b = a * (1 - f)
        // ee = (a^2 - b^2) / a^2;
        var a = 6378245.0; //  a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
        var ee = 0.00669342162296594323; //  ee: 椭球的偏心率。
        var dLat = this.transformLat(lon - 105.0, lat - 35.0);
        var dLon = this.transformLon(lon - 105.0, lat - 35.0);
        var radLat = lat / 180.0 * this.PI;
        var magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        var sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * this.PI);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * this.PI);
        return {'lat': dLat, 'lon': dLon};
    },
     
    //WGS-84 to GCJ-02
    transform2Mars : function (wgsLat, wgsLon) {
        if (this.outOfChina(wgsLat, wgsLon))
            return {'lat': wgsLat, 'lon': wgsLon};
 
        var d = this.delta(wgsLat, wgsLon);
        return {'lat' : wgsLat + d.lat,'lon' : wgsLon + d.lon};
    },
    // two point's distance
    distance : function (latA, lonA, latB, lonB) {
        var earthR = 6371000.;
        var x = Math.cos(latA * this.PI / 180.) * Math.cos(latB * this.PI / 180.) * Math.cos((lonA - lonB) * this.PI / 180);
        var y = Math.sin(latA * this.PI / 180.) * Math.sin(latB * this.PI / 180.);
        var s = x + y;
        if (s > 1) s = 1;
        if (s < -1) s = -1;
        var alpha = Math.acos(s);
        var distance = alpha * earthR;
        return distance;
    },
    outOfChina : function (lat, lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    },
    transformLat : function (x, y) {
        var ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * this.PI) + 20.0 * Math.sin(2.0 * x * this.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * this.PI) + 40.0 * Math.sin(y / 3.0 * this.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * this.PI) + 320 * Math.sin(y * this.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    },
    transformLon : function (x, y) {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * this.PI) + 20.0 * Math.sin(2.0 * x * this.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * this.PI) + 40.0 * Math.sin(x / 3.0 * this.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * this.PI) + 300.0 * Math.sin(x / 30.0 * this.PI)) * 2.0 / 3.0;
        return ret;
    }
};

var Douglas = {  
    
    compare : function (myPoint) {
       var  newPoint =[];
       var inferPOint = []; // 特征点集
		   
		   // 由起点和终点建立直线方程Ax+By+C=0
		   var startPoint = myPoint[0];
		   var endPoint = myPoint[myPoint.length - 1];
		   
		   newPoint.push(startPoint);
		   // 直线方程参数
		   var A = (startPoint.Y - endPoint.Y) / (Math.pow((endPoint.Y - startPoint.Y), 2) + Math.pow((endPoint.X - startPoint.X), 2));
		   var B = (endPoint.X - startPoint.X) / (Math.pow((endPoint.Y - startPoint.Y), 2) + Math.pow((endPoint.X - startPoint.X), 2));
		   var C = (startPoint.X * endPoint.Y - endPoint.X * startPoint.Y) / (Math.pow((endPoint.Y - startPoint.Y), 2) + Math.pow((endPoint.X - startPoint.X), 2));

      // 求曲线上各点到弦线的距离
		  var Q=false;
		  var maxdh = 0;
		  var maxPoint; // 记录距离最大点(中间特征点)
		  var maxPoint1; // 临时距离最大点
		 
		  for ( var i = 0; i < myPoint.length; i++){
		 	   var dh;
			   var temp = myPoint[i];
			   dh = Math.abs(A * temp.X + B * temp.Y + C);
			   if (maxdh < dh) {
				     maxdh = dh;
				     maxPoint1 = temp;
			   }
		  }

		  var ε=0.1
		  if (maxdh <= ε) {
			     Q = false;
		   } else {
			     Q = true;
			     maxPoint = maxPoint1;
			     endPoint = maxPoint;
		   }       

     if (Q) {
			while (true) {
				maxdh = 0;
				A = (startPoint.Y - endPoint.Y) / (Math.pow((endPoint.Y - startPoint.Y), 2) + Math.pow((endPoint.X - startPoint.X), 2));
				B = (endPoint.X - startPoint.X) / (Math.pow((endPoint.Y - startPoint.Y), 2) + Math.pow((endPoint.X - startPoint.X), 2));
				C = (startPoint.X * endPoint.Y - endPoint.X * startPoint.Y) / (Math.pow((endPoint.Y - startPoint.Y), 2) + Math.pow((endPoint.X - startPoint.X), 2));
				for (var i = startPoint.index; i <= endPoint.index; i++) {
					var dh;
					var temp = myPoint[i];
					dh = Math.abs(A * temp.X + B * temp.Y + C);
					if (maxdh < dh) {
						  maxdh = dh;
					  	maxPoint1 = temp;
					}
				}
				if (maxdh <= ε) {
					  Q = false;
				} else {
					  Q = true;
					  maxPoint = maxPoint1;
				}

				// 判断迭代终止
				if (endPoint.index == myPoint.length - 1 && Q == false) {
					  break;
				} // ...

				if (!Q) {
					   inferPOint.push(maxPoint); // 加入特征点
					   startPoint = maxPoint;
					   endPoint = myPoint[myPoint.length - 1];
				} else {
					   endPoint = maxPoint;
				}
			}			
		}

    // 形成新的数据文件
		var index = [];
		for (var i = 0; i < inferPOint.length; i++) {
			index[i] = inferPOint[i].index;
		}
		
		// 选择排序
		for (var k = 0; k < inferPOint.length; k++) {
			for (var m = k + 1; m < inferPOint.length; m++) {
				if (index[k] > index[m]) {
					var temp = 0;
					temp = index[k];
					index[k] = index[m];
					index[m] = temp;
				}
			}
		}

		for (var k = 0; k < inferPOint.length; k++) {
			newPoint.push(myPoint[index[k]]);
		}
		newPoint.push(myPoint[myPoint.length - 1]);

		return newPoint;   
  }     
   
};