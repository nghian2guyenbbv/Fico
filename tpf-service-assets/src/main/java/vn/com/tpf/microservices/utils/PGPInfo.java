package vn.com.tpf.microservices.utils;

import java.util.Map;

public class PGPInfo {

	public static final String preshareKey = "123456";

	public static final String privateKey = "-----BEGIN PGP PRIVATE KEY BLOCK-----\n"
			+ "Version: Keybase OpenPGP v1.0.0\n" + "Comment: https://keybase.io/crypto\n" + "\n"
			+ "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\n" + "=V+wD\n" + "-----END PGP PRIVATE KEY BLOCK-----\n"
			+ "";

	public static final String publicKey = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" + "Version: Keybase OpenPGP v1.0.0\n"
			+ "Comment: https://keybase.io/crypto\n" + "\n"
			+ "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\n" + "6lPNcDqPG4QnwQ==\n" + "=kBUb\n"
			+ "-----END PGP PUBLIC KEY BLOCK-----\n" + "";

	public static final Map<String, String> publicKey3P = Map.of("momo",
			"-----BEGIN PGP PUBLIC KEY BLOCK-----\n" + "Version: Keybase OpenPGP v1.0.0\n"
					+ "Comment: https://keybase.io/crypto\n" + "\n"
					+ "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\n" + "CyDrKmFnscz5mqEx/zYpCUo=\n"
					+ "=/jPH\n" + "-----END PGP PUBLIC KEY BLOCK-----\n" + "",
			"fpt",
			"-----BEGIN PGP PUBLIC KEY BLOCK-----\n" + "Version: Keybase OpenPGP v1.0.0\n"
					+ "Comment: https://keybase.io/crypto\n" + "\n"
					+ "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\n" + "=o+JZ\n"
					+ "-----END PGP PUBLIC KEY BLOCK-----\n" + "",
			"digitex",
			"-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
					"Version: BCPG C# v1.8.5.0\n" +
					"\n" +
					"mIsEXhRrJgEEAMWIqTn8lcPtjJT15Hn+NfEQUq/HCvoFlRy5LHu5Uv0pwJQp6dlO\n" +
					"fbN1DNvpnzC1Ys/zrK5BnMVVbyNkDA3ABQB5pi2ChFK2Rq3SP/K4hK2QBIfmKORY\n" +
					"vZAlbMIu6mDzYn8gyXfpquy2yd5HReUMzTzAkFlqjdZEm8m6LdakXZKtAAUTtBFU\n" +
					"UEZpY28tUHJvZHVjdGlvboicBBABAgAGBQJeFB3OAAoJEMwgfRsCNmiWY/QEAKxi\n" +
					"dh6tn/UiF4RD6zNskEqGEsJjPIQeIOGHjJFo+IY7lQHWV/rG/nKpvyokqcDM63x6\n" +
					"vPgSjMPOj4/aUGO5xuIlWgc+h1C11YDhzBcufyGTMirI+IB8wuhcuq5CJJw1kaZ7\n" +
					"0iNY/ieUK6NFeEXXCW1q5rot5VHULNQu7gNvtBEN\n" +
					"=+Ygn\n" +
					"-----END PGP PUBLIC KEY BLOCK-----" + "",
			"sgbpo",
			"-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
					"Version: OpenPGP.js v2.6.2\n" +
					"Comment: https://openpgpjs.org\n" +
					"\n" +
					"xsBNBF5nEugBCACzP7uaTjpJSH8JjBuT+197yOLak/8NW9bDRupK744xibHU\n" +
					"6BJuWCxB4pccKhEcgrcBrOYayLA0BQhQr/Z0nExPLuolnrpH2cEd3yjlkIPo\n" +
					"vUOo/EV1p9Xc8fDvyUmLiPueXlsynwbw/4wIBVULfxbqEMKh+YUUUehjO7a7\n" +
					"wdPbRB/so+G6XD6Qz4U4Y4jP4JzV3iak649oKYOUsjI80xt7aVYSLXUgNCJf\n" +
					"lDs+LMUH0fz9jHfyKn/qMAfWmlyr//BDZLckdC2d1Zn4kZtxzsH3n8FGw6yF\n" +
					"++3fXFgQIFHq5BNFg130XLEoMVQzUDdheVsuuUw6jFmsHrvBiGfkcTydABEB\n" +
					"AAHNInNhaWdvbiBicG8gPG5vcmVwbHlAc2FpZ29uYnBvLmNvbT7CwHsEEAEI\n" +
					"AC8FAl5nEukFCVexLAAGCwkHCAMCCRB1Q1a7IWn1/QQVCAoCAxYCAQIZAQIb\n" +
					"AwIeAQAAsVIH/Azdx+Xj8g2Sp6wnTXhMxo8BVVV7mJFx0KW9jqOUzf0OYQrD\n" +
					"UbieNmI4NleiuN29W67rY36Ajy5o70O75RxpAHF6wTTba6rXgt5OrTW6XLoP\n" +
					"/yl4kzUGRmz3rSIEDsQMZZtXfoFRkNZgI896Lt7o62tNd87xjWiKfC1Ci7Wb\n" +
					"wQ1SkovBV18p9OzKM+XElgA2/c1YXozQc30FIe0kpCVoPTyGt/qE5Ec3YIpC\n" +
					"d75l2tWr0i6ESANOYRlJrntfE0UPm1hxduVPbZofY+8Bov/vbg7fD2nYp34o\n" +
					"DUYRybzHtYnT2iUUjA5t68DTfI2MKcGZBb2cjWyJtg0EwXIuA39yikjOwE0E\n" +
					"XmcS6AEIAL9xP/pNKjZZ8DBO23PTcVcqc9BEBGCDi6/mARFEn0FRV9gVSHZg\n" +
					"l4A1ZKG3/u5Z3FXNKyM/dZeujIUHYy1kqih8woZIGsCwAn+BY2gHuEi2ZGPR\n" +
					"tf+LGIDM/eLh3Kcb0ndlvcwqr3yrX1ttodMlEO6JxpCsnynvqSuFr4aiff4j\n" +
					"+D9VG6+7xWiTv21pCIvywxorPOSh2JbHbjkPhFI6oaQ/PuIw26+mhuDYYCKm\n" +
					"OAhcMuvao/uH23cDYOjTnjPabidbOhQJPe4thAbsy0+73CU4W4l8kodVfxDd\n" +
					"iaLscrYi8zOZd+zSSfxEtlQ115/rYq8EEInbKHzDkneNjqAWvv0AEQEAAcLA\n" +
					"ZQQYAQgAGQUCXmcS6QUJV7EsAAkQdUNWuyFp9f0CGwwAAHd2CACeTDqJIWDL\n" +
					"jHgW8BxlWe5lb0ZtjSz2UYed8LQT6zXSQ9JJTVTwASfyeAx7i/OL6YxdV+rM\n" +
					"YxxaR9o2XJY6p8VlfAY3tSLZakwEw7Z+otHgiOQZhK3uU9QSSEPh/PQ5Gb1R\n" +
					"U6VrsN7nTSIuAuGqie58n/BQpEepsaUdKqmaD9Ojbj/Rlxo8+XMotGYPhMy7\n" +
					"219BTu8iWqILX9fHy+egdoCUARBmo/r9PMFkfyLfCDC1mXl0Vw1O7Si5Vq0c\n" +
					"jdyPq3902uBwk2M4lI7QtdHKj3ApcDiM41qX3cflGG3WbMTWeKpbQ7gKWYLY\n" +
					"YLNM2TIT53LlKmRauaxPvbom4Xlk1z5E\n" +
					"=tX3s\n" +
					"-----END PGP PUBLIC KEY BLOCK-----" +"",
			"smartnet",
			"-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
					"\n" +
					"mQENBF6QIFsBCADu0wZYFs2ljqnex0tHSRAF9T/6ThdRct1nVQqIrM2AA40fMRQs\n" +
					"Wh9vU43iPJXrXQ0JBd31d8nYox5/XKzkJhEswpDPVHCE1MsjpYbrb1h4O8XhSWir\n" +
					"kJQEeRjqpgKP36BbjGdRehL7WEn4hFmsFxHfAl1rd91s1Ltp/eJRVmUpymCfXfg9\n" +
					"G0FJ4BC2UyfGscOWNY8m1Y0D7uTePwpOBDpMitJcJehVz+s3kMc4BtSOfg4EFwNQ\n" +
					"Cclldm4wnsYRm4TXd1mZSz+XzWaCr3lG4YM3yvVWeKjl1Uq2+v7IJxd3/3e9Ot0h\n" +
					"k943d+MgkU2NVMApxTaW7x6RdJEhgkLDyelDABEBAAG0K0F1dG9nZW5lcmF0ZWQg\n" +
					"S2V5IDxhZG1pbkBzbWFydG5ldHZuLmNvbS52bj6JAU4EEwEIADgWIQSqJtB3CKO/\n" +
					"Jr5MpVmZhsLTCOoJJQUCXpAgWwIbLwULCQgHAgYVCgkICwIEFgIDAQIeAQIXgAAK\n" +
					"CRCZhsLTCOoJJb7NB/4mKOMzkWzlyhpkD/JwBg97nio1h29GHjFhnVf7Oll79jbK\n" +
					"QQIpFSqyihPSM/DEWnPsQ78slPypfwSLjIo6JaVc6EOwkQgon7gHWayCwz5EqpBw\n" +
					"71fTtfpsP7O7yFKKwzS5Y42t5XdLAyLIUirVSV5soPRK36fQN88Vx1sAX3fGjx9O\n" +
					"+RrA6CizQ4dk/odZM9PuhVyUga+YU0hwRZh3kPMIVAbT5R6wJv1F9NMr1Y/plldU\n" +
					"2m3y86UPnyMhyxbR5fH2xN25IW9XG44gNU8KXYWKs1OVfwFlk0w5vnbxd4I0oVL4\n" +
					"kpoyFEw8jejotM4vYKd/4n26xTjcbVRO/tNuQany\n" +
					"=4dIR\n" +
					"-----END PGP PUBLIC KEY BLOCK-----",
			"test",
			"-----BEGIN PGP PUBLIC KEY BLOCK-----\r\n" + "Version: Keybase OpenPGP v1.0.0\r\n"
					+ "Comment: https://keybase.io/crypto\r\n" + "\r\n"
					+ "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\r\n" + "=0COC\r\n"
					+ "-----END PGP PUBLIC KEY BLOCK-----\r\n" + "");

}