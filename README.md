# Wildcard

Converts an arbitrary date-range interval into the optimal list of wildcard patterns to represent that interval.

## Installation

```
git clone http://github.com/NarekA/wildcard
cd wildcard
lein uberjar
```


## Usage

    $ java -jar wildcard-0.1.0-standalone.jar start-date end-date

### Example:

From Command line:

```
$ java -jar target/uberjar/*-standalone.jar 20151212 20191201
20151212
20151213
20151214
20151215
20151216
20151217
20151218
20151219
2015122*
2015123*
2016*
2017*
2018*
201901*
201902*
201903*
201904*
201905*
201906*
201907*
201908*
201909*
201910*
201911*
```

In Clojure
```
(require '[wildcard.core :as wildcard])
=> nil
(wildcard/date-range->wildcards (t/interval (t/date-time 2016 10 3) (t/date-time 2016 11 17)))
=>
["20161003"
 "20161004"
 "20161005"
 "20161006"
 "20161007"
 "20161008"
 "20161009"
 "2016101*"
 "2016102*"
 "2016103*"
 "2016110*"
 "20161110"
 "20161111"
 "20161112"
 "20161113"
 "20161114"
 "20161115"
 "20161116"]

```

## Coming Soon

Support for more date formats


## License

Copyright © 2016

Distributed under the Eclipse Public License either version 1.0
