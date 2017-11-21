(ns com.zimpler.cljs-test-dom-report
  (:require [cljs.test :as t]))

(defn- create-dom-logs-div []
  (let [div (.createElement js/document "div")]
    (set! (.-id div) "dom-logs")
    (.appendChild (.-body js/document) div)
    div))

(defn- dom-log [text & {:keys [color]}]
  (let [log-div (or (.getElementById js/document "dom-logs")
                    (create-dom-logs-div))]
    (let [elem (.createElement js/document "pre")]
      (set! (.-innerHTML elem) text)
      (when color
        (set! (.-style.color elem) color))
      (set! (.-style.margin elem) "10px 20px")
      (.appendChild log-div elem)
      (.scrollIntoView elem))))

(defn- print-comparison [m]
  (let [formatter-fn (or (:formatter (t/get-current-env)) pr-str)]
    (dom-log (str "expected:" (formatter-fn (:expected m))))
    (dom-log (str "  actual:" (formatter-fn (:actual m))))))

(defmethod t/report [::t/default :begin-test-ns] [m]
  (dom-log (str "Testing " (name (:ns m)))))

(defmethod t/report [::t/default :fail] [m]
  (t/inc-report-counter! :fail)
  (dom-log (str "FAIL in " (t/testing-vars-str m))
           :color "red")
  (when (seq (:testing-contexts (t/get-current-env)))
    (dom-log (t/testing-contexts-str)))
  (when-let [message (:message m)] (dom-log message))
  (print-comparison m))

(defmethod t/report [::t/default :error] [m]
  (t/inc-report-counter! :error)
  (dom-log (str "ERROR in " (t/testing-vars-str m))
           :color "red")
  (when (seq (:testing-contexts (t/get-current-env)))
    (dom-log (t/testing-contexts-str)
             :color "red"))
  (when-let [message (:message m)]
    (dom-log message :color
             "red"))
  (print-comparison m))

(defmethod t/report [::t/default :begin-test-var] [m]
  (let [{:keys [ns name file line column]} (meta (:var m))]
    (dom-log
     (str "(" file ":" line ") " name))))

(defmethod t/report [::t/default :summary] [m]
  (let [color   (if (t/successful? m) "green" "red")]
    (dom-log (str "Ran " (:test m) " tests containing "
                  (+ (:pass m) (:fail m) (:error m)) " assertions.")
             :color color)
    (dom-log (str (:fail m) " failures, " (:error m) " errors.")
             :color color)
    (dom-log " ")))
