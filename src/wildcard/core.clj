(ns wildcard.core
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.coerce :as c]
            [clojure.string :as string])
  (:gen-class))

(def eight-date (f/formatter "yyyyMMdd"))

(defn min-date [& args]
  (apply min-key c/to-long args))

(defn max-date [& args]
  (apply max-key c/to-long args))

(defn get-first-day-of-month [date]
  (-> date .dayOfMonth .withMinimumValue))

(defn get-last-day-of-month [date]
  (-> date .dayOfMonth .withMaximumValue))

(defn bite-off-calendar-year [date interval]
  (let [first-day (-> date .dayOfYear .withMinimumValue)
        last-day (-> date .dayOfYear .withMaximumValue)]
    (when (and (t/within? interval first-day)
               (t/within? interval last-day))
      [last-day (f/unparse (f/formatter "yyyy*") date)])))

(defn bite-off-calendar-month [date interval]
  (let [first-day (get-first-day-of-month date)
        last-day (get-last-day-of-month date)]
    (when (and (t/within? interval first-day)
               (t/within? interval last-day))
      [last-day (f/unparse (f/formatter "yyyyMM*") date)])))

(defn bite-off-ten-days [date interval]
  (let [days-to-subtract (-> date .getDayOfMonth (mod 10))
        days-to-add (- 9 days-to-subtract)
        first-day (max-date (t/minus date (t/days days-to-subtract)) (get-first-day-of-month date))
        last-day (min-date (t/plus date (t/days days-to-add)) (get-last-day-of-month date))]
    (when (and (t/within? interval first-day)
               (t/within? interval last-day))
      (let [pattern (->> date
                         (f/unparse eight-date)
                         drop-last
                         string/join
                         (#(str % "*")))]
        [last-day pattern]))))

(defn bite-off-day [date interval]
  (when (t/within? interval date)
    [date (f/unparse eight-date date)]))

(defn date-range->wildcards
  ([interval]
   (date-range->wildcards interval (.getStart interval) []))
  ([interval date patterns]
   (let [[next-date pattern] (some #(% date interval) [bite-off-calendar-year
                                                       bite-off-calendar-month
                                                       bite-off-ten-days
                                                       bite-off-day])]
     (if next-date
       (recur interval (t/plus next-date (t/days 1)) (conj patterns pattern))
       patterns))))


(defn -main
  "I don't do a whole lot ... yet."
  [start end]
  (run! println (date-range->wildcards (t/interval (f/parse eight-date start) (f/parse eight-date end)))))
