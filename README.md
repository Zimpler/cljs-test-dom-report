# cljs-test-dom-report

A cljs.test report that outputs in your browser. Good on its own but
perfect in combination with figwheel.

## Demo

![Demo tests](https://raw.githubusercontent.com/Zimpler/cljs-test-dom-report/master/demo_tests.gif "Demo tests")

![Demo report compile error](https://raw.githubusercontent.com/Zimpler/cljs-test-dom-report/master/demo_report_compile_error.gif "Demo report compile error")

## Usage

If you start with a test runner that looks like this:

```clojure
(ns cljs-test-dom-report-demo.runner
  (:require [cljs.test :refer-macros [run-tests]]
            [cljs-test-dom-report-demo.core-test]))

(run-tests 'cljs-test-dom-report-demo.core-test)
```

You will need to wrap your `run-tests` in a function:

```clojure
(ns cljs-test-dom-report-demo.runner
  (:require [cljs.test :refer-macros [run-tests]]
            [cljs-test-dom-report-demo.core-test]))

(defn ^:export run []
  (run-tests 'cljs-test-dom-report-demo.core-test))
```

And add a new runner that will be used in combination with that where
you just have to require this library:

```clojure
(ns cljs-test-dom-report-demo.runner-live
    (:require [com.zimpler.cljs-test-dom-report]
              [cljs-test-dom-report-demo.runner :refer [run]]))
```

From there, to use with figwheel, you need to add a new figwheel build
for your test:

```clojure
{:id           "test-live"
 :source-paths ["src" "test"]
 :figwheel {:on-jsload "cljs-test-dom-report-demo.runner/run"
            :open-urls ["http://localhost:3449/tests.html"]}
 :compiler     {:main          cljs-test-dom-report-demo.runner-live
                :output-to     "resources/public/js/compiled/test-live/test.js"
                :output-dir    "resources/public/js/compiled/test-live/out"
                :asset-path    "js/compiled/test-live/out"
                :source-map-timestamp true}}
```

Along with an new HTML page that will load and render the tests:

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
  </head>
  <body>
    <script src="js/compiled/test-live/test.js" type="text/javascript"></script>
    <script>cljs_test_dom_report_demo.runner.run();</script>
  </body>
</html>
```

With all this setup you should be able to run:

```
lein figwheel test-live
```

And you're good to go!

There's a demo project here showing how to make it work (including
have tests runnable both in the browser and in PhantomJS):

https://github.com/Zimpler/cljs-test-dom-report-demo

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
