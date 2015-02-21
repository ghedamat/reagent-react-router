(ns reagent-react-router.core
   (:require [reagent.core :as reagent :refer [atom]]
             [clojure.walk :refer  [walk postwalk]]
             [react-router :as router])) ;// this should become [cljsjs.react-router]

(def Link (reagent/adapt-react-class js/ReactRouter.Link))
(def RouteHandler (reagent/adapt-react-class js/ReactRouter.RouteHandler))

(defn Route
  [params & args]
  (let [route (.createFactory js/React js/ReactRouter.Route)]
    (apply route
           (js-obj
             "name" (:name params)
             "path" (:path params)
             "handler" (reagent/reactify-component (:handler params)))
           args)))

(defn DefaultRoute
  [params & args]
  (let [default (.createFactory js/React js/ReactRouter.DefaultRoute)]
    (apply default
           (js-obj
             "handler" (reagent/reactify-component (:handler params)))
           args)))

(defn NotFound
  [params & args]
  (let [default (.createFactory js/React js/ReactRouter.NotFoundRoute)]
    (apply default
           (js-obj
             "handler" (reagent/reactify-component (:handler params)))
           args)))

(defn Redirect
  [params & args]
  (let [default (.createFactory js/React js/ReactRouter.Redirect)]
    (apply default
           (js-obj
             "from" (:from params)
             "to"   (:to params)
             )
           args)))

(defn defroutes
  [r]
  (postwalk
    (fn [node]
      (if (vector? node)
        (let [[t p h & f] node
              route-name (if (= p "/") "app" p)]
          (cond
            (= t :route)            (apply Route {:name route-name :path p :handler h} f)
            (= t :default-route)    (DefaultRoute {:handler p})
            (= t :not-found)  (NotFound {:handler p})
            (= t :redirect)   (Redirect {:from p :to h})))
        node))
    r))

(defn- router-cbk
  [elem handler state]
  (js/React.render (js/React.createElement handler (js-obj "params" (.-params state))) elem))

(defn run-router [elem routes]
  "receives the dom element to append the router tree to and the Routes component"
  (.run js/ReactRouter routes (partial router-cbk elem)))
