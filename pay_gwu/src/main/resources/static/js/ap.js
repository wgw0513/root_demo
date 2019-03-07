(function() {
	var b = {};
	var a = {};
	a.PADCHAR = "=";
	a.ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	a.makeDOMException = function() {
		var f, d;
		try {
			return new DOMException(DOMException.INVALID_CHARACTER_ERR)
		} catch (d) {
			var c = new Error("DOM Exception 5");
			c.code = c.number = 5;
			c.name = c.description = "INVALID_CHARACTER_ERR";
			c.toString = function() {
				return "Error: " + c.name + ": " + c.message
			};
			return c
		}
	};
	a.getbyte64 = function(e, d) {
		var c = a.ALPHA.indexOf(e.charAt(d));
		if (c === -1) {
			throw a.makeDOMException()
		}
		return c
	};
	a.decode = function(f) {
		f = "" + f;
		var j = a.getbyte64;
		var h, e, g;
		var d = f.length;
		if (d === 0) {
			return f
		}
		if (d % 4 !== 0) {
			throw a.makeDOMException()
		}
		h = 0;
		if (f.charAt(d - 1) === a.PADCHAR) {
			h = 1;
			if (f.charAt(d - 2) === a.PADCHAR) {
				h = 2
			}
			d -= 4
		}
		var c = [];
		for (e = 0; e < d; e += 4) {
			g = (j(f, e) << 18) | (j(f, e + 1) << 12) | (j(f, e + 2) << 6)
					| j(f, e + 3);
			c.push(String.fromCharCode(g >> 16, (g >> 8) & 255, g & 255))
		}
		switch (h) {
		case 1:
			g = (j(f, e) << 18) | (j(f, e + 1) << 12) | (j(f, e + 2) << 6);
			c.push(String.fromCharCode(g >> 16, (g >> 8) & 255));
			break;
		case 2:
			g = (j(f, e) << 18) | (j(f, e + 1) << 12);
			c.push(String.fromCharCode(g >> 16));
			break
		}
		return c.join("")
	};
	a.getbyte = function(e, d) {
		var c = e.charCodeAt(d);
		if (c > 255) {
			throw a.makeDOMException()
		}
		return c
	};
	a.encode = function(f) {
		if (arguments.length !== 1) {
			throw new SyntaxError("Not enough arguments")
		}
		var g = a.PADCHAR;
		var h = a.ALPHA;
		var k = a.getbyte;
		var e, j;
		var c = [];
		f = "" + f;
		var d = f.length - f.length % 3;
		if (f.length === 0) {
			return f
		}
		for (e = 0; e < d; e += 3) {
			j = (k(f, e) << 16) | (k(f, e + 1) << 8) | k(f, e + 2);
			c.push(h.charAt(j >> 18));
			c.push(h.charAt((j >> 12) & 63));
			c.push(h.charAt((j >> 6) & 63));
			c.push(h.charAt(j & 63))
		}
		switch (f.length - d) {
		case 1:
			j = k(f, e) << 16;
			c.push(h.charAt(j >> 18) + h.charAt((j >> 12) & 63) + g + g);
			break;
		case 2:
			j = (k(f, e) << 16) | (k(f, e + 1) << 8);
			c.push(h.charAt(j >> 18) + h.charAt((j >> 12) & 63)
					+ h.charAt((j >> 6) & 63) + g);
			break
		}
		return c.join("")
	};
	b.pay = function(d) {
		var c = encodeURIComponent(a.encode(d));
		location.href = "pay.htm?goto=" + c
	};
	b.decode = function(c) {
		return a.decode(decodeURIComponent(c))
	};
	window._AP = b
})();