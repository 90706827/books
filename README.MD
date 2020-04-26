![艾薇儿-拉维尼](install/img/Avril.jpg)

# Setup and Installation of GitBook

Getting GitBook installed and ready-to-go should only take a few minutes.

### legacy.gitbook.com

[legacy.gitbook.com](https://legacy.gitbook.com) is an easy to use solution to write, publish and host books. It is the easiest solution for publishing your content and collaborating on it.

It integrates well with the [GitBook Editor](https://legacy.gitbook.com/editor).

### Local Installation

##### Requirements

Installing GitBook is easy and straightforward. Your system just needs to meet these two requirements:

* NodeJS (v4.0.0 and above is recommended)
* Windows, Linux, Unix, or Mac OS X

##### Install with NPM

The best way to install GitBook is via **NPM**. At the terminal prompt, simply run the following command to install GitBook:

```
$ npm install -g gitbook-cli
```

`gitbook-cli` is an utility to install and use multiple versions of GitBook on the same system. It will automatically install the required version of GitBook to build a book.

##### Create a book

GitBook can setup a boilerplate book:

```
$ gitbook init
```

If you wish to create the book into a new directory, you can do so by running `gitbook init ./directory`

Preview and serve your book using:

```
$ gitbook serve
```

Or build the static website using:

```
$ gitbook build
```

##### Install pre-releases

`gitbook-cli` makes it easy to download and install other versions of GitBook to test with your book:

```
$ gitbook fetch beta
```

Use `gitbook ls-remote` to list remote versions available for install.

##### Debugging

You can use the options `--log=debug` and `--debug` to get better error messages (with stack trace). For example:

```
$ gitbook build ./ --log=debug --debug
```

#### book.json Config File

```
$ gitbook install
```

#### book config info

```json
{
    "title": "标题",
    "author": "作者",
    "generator": "site",
    "language": "zh-hans",
    "description": "book描述",
    "gitbook": "3.2.3",
    "-links": {
        "sidebar": {
            "主页": "https://wwww.baidu.com 暂时注销"
        }
    },
    "styles": {
        "website": "styles/website.css",
        "ebook": "styles/ebook.css",
        "pdf": "styles/pdf.css",
        "mobi": "styles/mobi.css",
        "epub": "styles/epub.css"
    },
    "plugins": [
        "github",
        "splitter",
        "-sharing",
        "tbfed-pagefooter",
        "donate",
        "hide-element",
        "back-to-top-button",
        "expandable-chapters",
        "code",
        "search-pro",
        "insert-logo",
        "favicon"
    ],
    "pluginsConfig": {
        "tbfed-pagefooter": {
            "copyright": "Copyright &copy Mr.Jangni 2011",
            "modify_label": "该文件修订时间：",
            "modify_format": "YYYY-MM-DD HH:mm:ss"
        },
        "github": {
            "url": "https://github.com/90706827/books"
        },
        "donate": {
            "wechat": "img/weixin.png",
            "alipay": "img/alipay.png",
            "title": "",
            "button": "打赏",
            "alipayText": "支付宝打赏",
            "wechatText": "微信打赏"
        },
        "hide-element": {
            "elements": [
                ".gitbook-link"
            ]
        },
        "insert-logo": {
            "url": "install/img/jm_logo.png",
            "style": "background: none; max-height: 30px; min-height: 30px"
        },
        "favicon": {
            "shortcut": "install/img/favicon.ico",
            "bookmark": "install/img/favicon.ico",
            "appleTouch": "install/img/favicon.ico",
            "appleTouchMore": {
                "120x120": "install/img/favicon.ico",
                "180x180": "install/img/favicon.ico"
            }
        }
    },
    "variables": {
        "phone": "18310768225",
        "email": "90706827@163.com"
    }
}
```

